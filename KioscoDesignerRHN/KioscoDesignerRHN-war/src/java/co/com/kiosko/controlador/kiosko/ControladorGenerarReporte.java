package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorGenerarReporte implements Serializable {

    @EJB
    private IAdministrarGenerarReporte administrarGenerarReporte;
    private OpcionesKioskos reporte;
    private ConexionesKioskos conexionEmpleado;
    private String email, areaDe;
    private ControladorIngreso controladorIngreso;
    private String pathReporteGenerado;
    private StreamedContent reporteGenerado;
    private FileInputStream fis;

    public ControladorGenerarReporte() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGenerarReporte.obtenerConexion(ses.getId());
            controladorIngreso = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class));
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            reporte = ((ControladorOpcionesKiosko) x.getApplication().evaluateExpressionGet(x, "#{controladorOpcionesKiosko}", ControladorOpcionesKiosko.class)).getOpcionReporte();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void generarReporte() {
        if (administrarGenerarReporte.modificarConexionKisko(conexionEmpleado)) {
            controladorIngreso.actualizarConexionEmpleado();
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            Map parametros = new HashMap();
            parametros.put("secuenciaempleado", conexionEmpleado.getEmpleado().getSecuencia());
            pathReporteGenerado = administrarGenerarReporte.generarReporte(reporte.getNombrearchivo(), "PDF", parametros);
            if (pathReporteGenerado != null) {
                PrimefacesContextUI.ejecutar("validarDescargaReporte();");
            }
        }
    }

    public void validar() {
        if (validarCampos()) {
            if ((reporte.getNombrearchivo().equalsIgnoreCase("kio_certificadoIngresos") || reporte.getDescripcion().toUpperCase().contains("RETEN"))
                    ? validarFechasCertificadoIngresosRetenciones() : true) {
                PrimefacesContextUI.ejecutar("PF('generandoReporte').show();");
                PrimefacesContextUI.ejecutar("generarReporte();");
            } else {
                PrimefacesContextUI.ejecutar("PF('dlgVerificarFechas').show();");
            }
        }
    }

    public void validarDescargaReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('generandoReporte').hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            try {
                //System.out.println("pathReporteGenerado: " + pathReporteGenerado);
                File archivo = new File(pathReporteGenerado);
                fis = new FileInputStream(archivo);
                reporteGenerado = new DefaultStreamedContent(fis, "application/pdf");
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getCause());
                reporteGenerado = null;
            }
            if (reporteGenerado != null) {
                context.update("principalForm:verReportePDF");
                context.execute("PF('verReportePDF').show();");
                context.execute("validarEnvioCorreo();");
            }
            //pathReporteGenerado = null;
        } else {
            context.update("principalForm:errorGenerandoReporte");
            context.execute("PF('errorGenerandoReporte').show();");
        }
    }

    public boolean validarCampos() {
        if (conexionEmpleado.getFechadesde() != null && conexionEmpleado.getFechahasta() != null) {
            if (conexionEmpleado.getFechadesde().compareTo(conexionEmpleado.getFechahasta()) < 0) {
                return validarCorreo();
            } else {
                MensajesUI.error("La fecha hasta debe ser mayor a la fecha desde.");
            }
        } else {
            return validarCorreo();
        }
        return false;
    }

    public boolean validarCorreo() {
        if (conexionEmpleado.isEnvioCorreo()) {
            String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            } else {
                MensajesUI.error("Correo invalido, por favor verifique.");
            }
        } else {
            return true;
        }
        return false;
    }

    public void validarEnviaCorreo() {
        if (conexionEmpleado.isEnvioCorreo()) {
            if (administrarGenerarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), email,
                    "Reporte Kiosko - " + reporte.getDescripcion(), "Mensaje enviado automaticamente, por favor no responda a este correo.",
                    pathReporteGenerado)) {
                MensajesUI.info("El reporte a sido enviado exitosamente.");
            } else {
                MensajesUI.error("No fue posible enviar el correo, por favor comuniquese con soporte.");
            }
        }
    }

    public boolean validarFechasCertificadoIngresosRetenciones() {
        SimpleDateFormat formatoDia, formatoMes, formatoAnio;
        String dia, mes, anio;
        formatoDia = new SimpleDateFormat("dd");
        formatoMes = new SimpleDateFormat("MM");
        formatoAnio = new SimpleDateFormat("yyyy");
        dia = formatoDia.format(conexionEmpleado.getFechadesde());
        mes = formatoMes.format(conexionEmpleado.getFechadesde());
        anio = formatoAnio.format(conexionEmpleado.getFechadesde());

        if (dia.equals("01") && mes.equals("01")) {
            dia = formatoDia.format(conexionEmpleado.getFechahasta());
            mes = formatoMes.format(conexionEmpleado.getFechahasta());
            if (dia.equals("31") && mes.equals("12") && anio.equals(formatoAnio.format(conexionEmpleado.getFechahasta()))) {
                return true;
            }
        }
        return false;
    }

    public void reiniciarStreamedContent() {
        reporteGenerado = null;
        pathReporteGenerado = null;
    }

    public void cerrarControlador() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("controladorGenerarReporte");
    }
    //GETTER AND SETTER

    public OpcionesKioskos getReporte() {
        return reporte;
    }

    public ConexionesKioskos getConexionEmpleado() {
        return conexionEmpleado;
    }

    public void setConexionEmpleado(ConexionesKioskos conexionEmpleado) {
        this.conexionEmpleado = conexionEmpleado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaDe() {
        return areaDe;
    }

    public void setAreaDe(String areaDe) {
        this.areaDe = areaDe;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public StreamedContent getReporteGenerado() {
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Pragma", "no-cache");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "0");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "Mon, 8 Aug 1980 10:00:00 GMT");
        try {
            File archivo = new File(pathReporteGenerado);
            fis = new FileInputStream(archivo);
            reporteGenerado = new DefaultStreamedContent(fis, "application/pdf");
        } catch (Exception e) {
            reporteGenerado = null;
        }

        return reporteGenerado;
    }
}
