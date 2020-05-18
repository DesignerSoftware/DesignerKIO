package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarDescargarReporte;
import co.com.kiosko.clasesAyuda.ReporteGenerado;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.inject.Named;
//import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
//@Named(value = "controladorDescargarReporte")
//@ViewScoped
@ManagedBean
@SessionScoped
public class ControladorDescargarReporte {

    @EJB
    IAdministrarDescargarReporte administrarDescargarReporte;
    private OpcionesKioskos reporte;
    private ControladorIngreso controladorIngreso;
    private ConexionesKioskos conexionEmpleado;
    private String email;
    private List<ReporteGenerado> reportes;
    private List<ReporteGenerado> reportesEnviar;
    private StreamedContent reporteGenerado;
    private FileInputStream fis;
    private String userAgent;
    private String pathReporteSeleccionado;
    private String areaDe;
    private boolean enviocorreo;

    public ControladorDescargarReporte() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            ExternalContext externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administrarDescargarReporte.obtenerConexion(ses.getId());
            controladorIngreso = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class));
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            reporte = ((ControladorOpcionesKiosko) x.getApplication().evaluateExpressionGet(x, "#{controladorOpcionesKiosko}", ControladorOpcionesKiosko.class)).getOpcionReporte();
            email = conexionEmpleado.getEmpleado().getPersona().getEmail();
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e.getMessage());
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void consultarReporte() {
        reportes = null;
        reportesEnviar = null;
        if (administrarDescargarReporte.modificarConexionKisko(conexionEmpleado)) {
            controladorIngreso.actualizarConexionEmpleado();
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            reportes = administrarDescargarReporte.consultarReporte(reporte.getNombrearchivo(), controladorIngreso.getUsuario(), conexionEmpleado.getFechadesde(), conexionEmpleado.getFechahasta());
            if (reportes != null && !reportes.isEmpty()) {
                PrimefacesContextUI.actualizar("principalForm:tblReporte");
            } else {
                MensajesUI.info("La consulta no arrojo ningun resultado.");
            }
        } else {
            MensajesUI.error("Se generó un error registrando la conexión.");
        }
    }

    public void validar() {
        if (validarCampos()) {
            if ((reporte.getNombrearchivo().equalsIgnoreCase("kio_certificadoIngresos") || reporte.getDescripcion().toUpperCase().contains("INGRESOS"))
                    ? (conexionEmpleado.getFechadesde() != null && conexionEmpleado.getFechahasta() != null) ? validarFechasCertificadoIngresosRetenciones() : true : true) {
                PrimefacesContextUI.ejecutar("consultarReporte();");
            } else {
                PrimefacesContextUI.ejecutar("PF('dlgVerificarFechas').show();");
            }
        }
    }

    public void verReporte(String rutaReporte) {
        RequestContext context = RequestContext.getCurrentInstance();
        pathReporteSeleccionado = rutaReporte;

        try {
            File archivo = new File(pathReporteSeleccionado);
            fis = new FileInputStream(archivo);
            reporteGenerado = new DefaultStreamedContent(fis, "application/xlsx");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getCause());
            reporteGenerado = null;
        }
        if (reporteGenerado != null) {
            if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase())) {
//                context.update("principalForm:dwlReportePDF");
//                context.execute("PF('dwlReportePDF').show();");
                PrimeFaces.current().ajax().update("principalForm:dwlReportePDF");
                PrimeFaces.current().executeScript("PF('dwlReportePDF').show();");
            } else {
//                context.update("principalForm:verReportePDF");
//                context.execute("PF('verReportePDF').show();");
                PrimeFaces.current().ajax().update("principalForm:verReportePDF");
                PrimeFaces.current().executeScript("PF('verReportePDF').show();");
            }
            //context.execute("validarEnvioCorreo();");
        } else {
//            context.update("principalForm:errorGenerandoReporte");
//            context.execute("PF('errorGenerandoReporte').show();");
            PrimeFaces.current().ajax().update("principalForm:errorGenerandoReporte");
            PrimeFaces.current().executeScript("PF('errorGenerandoReporte').show();");
        }
    }

    public boolean validarCampos() {
        boolean retorno = false;
        if (conexionEmpleado.getFechadesde() != null && conexionEmpleado.getFechahasta() != null) {
            if (conexionEmpleado.getFechadesde().compareTo(conexionEmpleado.getFechahasta()) < 0) {
                retorno = true;
            } else {
                MensajesUI.error("La fecha hasta debe ser mayor a la fecha desde.");
                retorno = false;
            }
        } else {
            MensajesUI.error("Es necesario ingresar las fechas.");
            retorno = false;
        }
        /*if (retorno) {
            retorno = validarCorreo();
        }*/
        return retorno;
    }
    
    public boolean visualizarReporte(){
        return false;
    }

    public boolean validarCorreo() {
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            MensajesUI.error("Correo inv?lido, por favor verifique.");
        }
        return false;
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

    public StreamedContent descargarArchivo(String nombre, String rutaReporte) {
        StreamedContent archivoDescarga = null;
        if (rutaReporte != null && !rutaReporte.isEmpty()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            ec.setResponseHeader("Pragma", "no-cache");
            ec.setResponseHeader("Expires", "0");
            ec.setResponseHeader("Expires", "Mon, 8 Aug 1980 10:00:00 GMT");
            try {
                File archivo = new File(rutaReporte);
                fis = new FileInputStream(archivo);
                archivoDescarga = new DefaultStreamedContent(fis, "application/xlsx", nombre);
            } catch (Exception e) {
                e.printStackTrace();
                archivoDescarga = null;
            }
        }
        return archivoDescarga;
    }

    public void enviarCorreo() {
        if (conexionEmpleado.isEnvioCorreo() && validarCorreo()) {
            if (reportesEnviar != null && !reportesEnviar.isEmpty()) {
                if (administrarDescargarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), email,
                        "Reporte Kiosko - " + reporte.getDescripcion(), "Mensaje enviado autom?ticamente, por favor no responda a este correo.",
                        reportesEnviar)) {
                    MensajesUI.info("El reporte ha sido enviado exitosamente.");
                    PrimefacesContextUI.actualizar("principalForm:growl");
                } else {
                    MensajesUI.error("No fue posible enviar el correo, por favor comun?quese con soporte.");
                    PrimefacesContextUI.actualizar("principalForm:growl");
                }
            } else {
                MensajesUI.error("Por favor seleccione al menos un elemento de la lista.");
                PrimefacesContextUI.actualizar("principalForm:growl");
            }
        }
    }

    public boolean validarConfigSMTP() {
        return administrarDescargarReporte.comprobarConfigCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia());
    }

    public void reiniciarStreamedContent() {
        reporteGenerado = null;
        pathReporteSeleccionado = null;
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

    public StreamedContent getReporteGenerado() {
        if (pathReporteSeleccionado != null && !pathReporteSeleccionado.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Pragma", "no-cache");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "0");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "Mon, 8 Aug 1980 10:00:00 GMT");
            try {
                File archivo = new File(pathReporteSeleccionado);
                fis = new FileInputStream(archivo);
                reporteGenerado = new DefaultStreamedContent(fis, "application/xlsx");
            } catch (Exception e) {
                e.printStackTrace();
                reporteGenerado = null;
            }
        }
        return reporteGenerado;
    }

    public boolean isEnviocorreo() {
        boolean retorno;
        retorno = validarConfigSMTP() && conexionEmpleado.isEnvioCorreo();
        return retorno;
    }

    public void setEnviocorreo(boolean enviocorreo) {
        if (enviocorreo) {
            if (validarConfigSMTP()) {
                conexionEmpleado.setEnvioCorreo(enviocorreo);
                this.enviocorreo = conexionEmpleado.isEnvioCorreo();
            } else {
                MensajesUI.error("Configuración de Servidor SMTP inválida.");
            }
        } else {
            conexionEmpleado.setEnvioCorreo(enviocorreo);
            this.enviocorreo = conexionEmpleado.isEnvioCorreo();
        }
    }

    public List<ReporteGenerado> getReportes() {
        return reportes;
    }

    public List<ReporteGenerado> getReportesEnviar() {
        return reportesEnviar;
    }

    public void setReportesEnviar(List<ReporteGenerado> reportesEnviar) {
        this.reportesEnviar = reportesEnviar;
    }
}
