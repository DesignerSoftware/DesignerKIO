package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.OpcionesKioskos;
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
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionRolledbackLocalException;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpServletRequest;
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
    private boolean enviocorreo;
    private ExternalContext externalContext;
    private String userAgent;

    public ControladorGenerarReporte() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println(this.getClass().getName() + "." + "inicializarAdministrador" + "()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administrarGenerarReporte.obtenerConexion(ses.getId());
            controladorIngreso = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class));
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            reporte = ((ControladorOpcionesKiosko) x.getApplication().evaluateExpressionGet(x, "#{controladorOpcionesKiosko}", ControladorOpcionesKiosko.class)).getOpcionReporte();
            email = conexionEmpleado.getEmpleado().getPersona().getEmail();
            //System.out.println("contexto: " + userAgent);
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e.getMessage());
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void generarReporte() {
        System.out.println(this.getClass().getName() + "." + "generarReporte" + "()");
        if (administrarGenerarReporte.modificarConexionKisko(conexionEmpleado)) {
            controladorIngreso.actualizarConexionEmpleado();
            conexionEmpleado = controladorIngreso.getConexionEmpleado();
            Map parametros = new HashMap();
            parametros.put("secuenciaempleado", conexionEmpleado.getEmpleado().getSecuencia());
            pathReporteGenerado = administrarGenerarReporte.generarReporte(reporte.getNombrearchivo(), "PDF", parametros);
            if (pathReporteGenerado != null) {
                PrimefacesContextUI.ejecutar("validarDescargaReporte();");
            } else {
                PrimefacesContextUI.ejecutar("PF('generandoReporte').hide();");
                MensajesUI.error("El reporte no se pudo generar.");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('generandoReporte').hide();");
            MensajesUI.error("Se generó un error registrando la conexión.");
        }
    }

    public void validar() {
        System.out.println(this.getClass().getName() + "." + "validar" + "()");
        if (validarCampos()) {
            if ((reporte.getNombrearchivo().equalsIgnoreCase("kio_certificadoIngresos") || reporte.getDescripcion().toUpperCase().contains("RETEN"))
                    ? (conexionEmpleado.getFechadesde() != null && conexionEmpleado.getFechahasta() != null) ? validarFechasCertificadoIngresosRetenciones() : true : true) {
                PrimefacesContextUI.ejecutar("PF('generandoReporte').show();");
                PrimefacesContextUI.ejecutar("generarReporte();");
            } else {
                PrimefacesContextUI.ejecutar("PF('dlgVerificarFechas').show();");
            }
        }
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + "." + "validarDescargaReporte" + "()");
        String nombrearchivo = "reporte.pdf";
        String[] arregloruta = new String[1];
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('generandoReporte').hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            //System.out.println("ruta: "+pathReporteGenerado);
            try {
                arregloruta = pathReporteGenerado.split(Pattern.quote("\\"));
            } catch (PatternSyntaxException pse) {
                System.out.println("Error en la fragmentación de la ruta.");
                System.out.println("Causa: " + pse);
            }
            /*for (int i = 0; i < arregloruta.length; i++) {
             System.out.println("pos" + i + ": " + arregloruta[i]);
             }*/
            nombrearchivo = arregloruta[arregloruta.length - 1];
            //System.out.println("nombre de archivo: " + nombrearchivo);
            try {
                //System.out.println("pathReporteGenerado: " + pathReporteGenerado);
                File archivo = new File(pathReporteGenerado);
                fis = new FileInputStream(archivo);
                //reporteGenerado = new DefaultStreamedContent(fis, "application/pdf");
                reporteGenerado = new DefaultStreamedContent(fis, "application/pdf", nombrearchivo);
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getCause());
                reporteGenerado = null;
            }
            if (reporteGenerado != null) {
                System.out.println("userAgent: "+userAgent);
                if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) 
                        || userAgent.toUpperCase().contains("Tablet".toUpperCase())
                        || userAgent.toUpperCase().contains("Android".toUpperCase())
                        || userAgent.toUpperCase().contains("IOS".toUpperCase())
                   ) {
                    //System.out.println("Acceso por mobiles.");
                    context.update("principalForm:dwlReportePDF");
                    context.execute("PF('dwlReportePDF').show();");
                } else {
                    context.update("principalForm:verReportePDF");
                    context.execute("PF('verReportePDF').show();");
                }
                context.execute("validarEnvioCorreo();");
            } else {
                context.update("principalForm:errorGenerandoReporte");
                context.execute("PF('errorGenerandoReporte').show();");
            }
            //pathReporteGenerado = null;
        } else {
            context.update("principalForm:errorGenerandoReporte");
            context.execute("PF('errorGenerandoReporte').show();");
        }
    }

    public boolean validarCampos() {
        System.out.println(this.getClass().getName() + "." + "validarCampos" + "()");
        boolean retorno = false;
        if (conexionEmpleado.getFechadesde() != null && conexionEmpleado.getFechahasta() != null) {
            if (conexionEmpleado.getFechadesde().compareTo(conexionEmpleado.getFechahasta()) < 0) {
                retorno = true;
            } else {
                MensajesUI.error("La fecha hasta debe ser mayor a la fecha desde.");
                retorno = false;
            }
        } else {
            MensajesUI.error("Es necesario colocar las fechas.");
            retorno = false;
        }
        if (retorno) {
            retorno = validarCorreo();
        }
        return retorno;
    }

    public boolean validarCorreo() {
        System.out.println(this.getClass().getName() + "." + "validarCorreo" + "()");
        if (conexionEmpleado.isEnvioCorreo()) {
            String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(PATTERN_EMAIL);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            } else {
                MensajesUI.error("Correo inválido, por favor verifique.");
            }
        } else {
            return true;
        }
        return false;
    }

    public void validarEnviaCorreo() {
        System.out.println(this.getClass().getName() + "." + "validarEnviaCorreo" + "()");
        if (conexionEmpleado.isEnvioCorreo()) {
            if (administrarGenerarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), email,
                    "Reporte Kiosko - " + reporte.getDescripcion(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
                    pathReporteGenerado)) {
                MensajesUI.info("El reporte ha sido enviado exitosamente.");
                PrimefacesContextUI.actualizar("principalForm:growl");
            } else {
                MensajesUI.error("No fue posible enviar el correo, por favor comuníquese con soporte.");
                PrimefacesContextUI.actualizar("principalForm:growl");
            }
        }
    }

    public boolean validarFechasCertificadoIngresosRetenciones() {
        System.out.println(this.getClass().getName() + "." + "validarFechasCertificadoIngresosRetenciones" + "()");
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

    public boolean validarConfigSMTP() {
        System.out.println(this.getClass().getName() + "." + "validarConfigSMTP" + "()");
        boolean resultado=false;
        try {
            resultado = administrarGenerarReporte.comprobarConfigCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println(this.getClass().getName() + "." + "validarConfigSMTP" + "()");
            System.out.println("La transacción se ha deshecho.");
            System.out.println(trle);
        }
        return resultado;
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + "reiniciarStreamedContent" + "()");
        reporteGenerado = null;
        pathReporteGenerado = null;
    }

    public void cerrarControlador() {
        System.out.println(this.getClass().getName() + "." + "cerrarControlador" + "()");
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(context.getExternalContext().getSessionMap().get("controladorGenerarReporte"));
        context.getExternalContext().getSessionMap().remove("controladorGenerarReporte");
    }

    //GETTER AND SETTER
    public OpcionesKioskos getReporte() {
        System.out.println(this.getClass().getName() + "." + "getReporte" + "()");
        return reporte;
    }

    public ConexionesKioskos getConexionEmpleado() {
        System.out.println(this.getClass().getName() + "." + "getConexionEmpleado" + "()");
        return conexionEmpleado;
    }

    public void setConexionEmpleado(ConexionesKioskos conexionEmpleado) {
        System.out.println(this.getClass().getName() + "." + "setConexionEmpleado" + "()");
        this.conexionEmpleado = conexionEmpleado;
    }

    public String getEmail() {
        System.out.println(this.getClass().getName() + "." + "getEmail" + "()");
        return email;
    }

    public void setEmail(String email) {
        System.out.println(this.getClass().getName() + "." + "setEmail" + "()");
        this.email = email;
    }

    public String getAreaDe() {
        System.out.println(this.getClass().getName() + "." + "getAreaDe" + "()");
        return areaDe;
    }

    public void setAreaDe(String areaDe) {
        System.out.println(this.getClass().getName() + "." + "setAreaDe" + "()");
        this.areaDe = areaDe;
    }

    public String getPathReporteGenerado() {
        System.out.println(this.getClass().getName() + "." + "getPathReporteGenerado" + "()");
        return pathReporteGenerado;
    }

    public StreamedContent getReporteGenerado() {
        System.out.println(this.getClass().getName() + "." + "getReporteGenerado" + "()");
        String nombrearchivo = "reporte.pdf";
        String[] arregloruta = new String[1];
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Pragma", "no-cache");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "0");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expires", "Mon, 8 Aug 1980 10:00:00 GMT");
        if (pathReporteGenerado != null) {
            try {
                arregloruta = pathReporteGenerado.split(Pattern.quote("\\"));
            } catch (PatternSyntaxException pse) {
                System.out.println("Error en la fragmentación de la ruta.");
                pse.printStackTrace();
            }
            /*for (int i = 0; i < arregloruta.length; i++) {
             System.out.println("pos" + i + ": " + arregloruta[i]);
             }*/
            nombrearchivo = arregloruta[arregloruta.length - 1];
            //System.out.println("nombre de archivo: " + nombrearchivo);
        }
        try {
            File archivo = new File(pathReporteGenerado);
            fis = new FileInputStream(archivo);
            if (pathReporteGenerado != null) {
                reporteGenerado = new DefaultStreamedContent(fis, "application/pdf", nombrearchivo);
            } else {
                reporteGenerado = new DefaultStreamedContent(fis, "application/pdf");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            System.out.println("reporte nulo");
            reporteGenerado = null;
        }

        return reporteGenerado;
    }

    public boolean isEnviocorreo() {
        System.out.println(this.getClass().getName() + "." + "isEnviocorreo" + "()");
        boolean retorno = false;
//        retorno = validarConfigSMTP() && conexionEmpleado.isEnvioCorreo();
        if (conexionEmpleado.isEnvioCorreo()) {
            retorno = validarConfigSMTP();
        }
        return retorno;
    }

    public void setEnviocorreo(boolean enviocorreo) {
        System.out.println(this.getClass().getName() + "." + "setEnviocorreo" + "()");
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
}
