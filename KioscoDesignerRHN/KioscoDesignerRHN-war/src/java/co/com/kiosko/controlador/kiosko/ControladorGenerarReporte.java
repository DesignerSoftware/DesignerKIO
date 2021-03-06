package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Felipe Trivi�o
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
    private LeerArchivoXML leerArchivoXML;
    
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
            leerArchivoXML = new LeerArchivoXML();
            leerArchivoXML.leerArchivoConfigModulos();
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
                /* generar reporte de auditoria */
//                generarAuditoria(pathReporteGenerado);
                PrimefacesContextUI.ejecutar("validarDescargaReporte();");
            } else {
                PrimefacesContextUI.ejecutar("PF('generandoReporte').hide();");
                MensajesUI.error("El reporte no se pudo generar.");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('generandoReporte').hide();");
            MensajesUI.error("Se gener� un error registrando la conexi�n.");
        }
    }
    
    private void generarAuditoria(String rutaReporteGen) {
        Calendar dtGen = Calendar.getInstance();
        System.out.println(dtGen.get(Calendar.YEAR) + "/" + (dtGen.get(Calendar.MONTH) + 1) + "/" + dtGen.get(Calendar.DAY_OF_MONTH) + " " + dtGen.get(Calendar.HOUR_OF_DAY) + ":" + dtGen.get(Calendar.MINUTE));
        System.out.println(conexionEmpleado.getEmpleado().getCodigoempleado() + " " + conexionEmpleado.getEmpleado().getEmpresa().getNit());
        System.out.println(reporte.getCodigo() + " " + reporte.getDescripcion());
        System.out.println(rutaReporteGen);
        System.out.println("destinatarios");
        List<String> cuentasAud = leerArchivoXML.getCuentasAudOp("reportes", conexionEmpleado.getEmpleado().getEmpresa().getNit(), reporte.getCodigo());
        System.out.println("cuentas: " + cuentasAud);
        System.out.println("Enviando mensaje de auditoria");
        if (cuentasAud != null && !cuentasAud.isEmpty()) {
            for (String cuentaAud : cuentasAud) {
                String mensaje = "Apreciado usuario(a): \n\n"
                        + "Nos permitimos informar que el "
                        + dtGen.get(Calendar.DAY_OF_MONTH) + "/" + (dtGen.get(Calendar.MONTH) + 1) + "/" + dtGen.get(Calendar.YEAR) + " a las " + dtGen.get(Calendar.HOUR_OF_DAY) + ":" + dtGen.get(Calendar.MINUTE)
                        + " se gener� el " + reporte.getDescripcion()
                        + " en el m�dulo de Kiosco N�mina Designer. "
                        + "La persona que GENER� el reporte es: "
                        + conexionEmpleado.getEmpleado().getPersona().getNombreCompleto() + ". \n\n";
                mensaje = mensaje + "Le recordamos que esta direcci�n de correo es utilizada solamente para env�os "
                        + "autom�ticos de la informaci�n solicitada. Por favor no responda este correo, "
                        + "ya que no podr� ser atendido. Si desea contactarse con nosotros, env�e un correo "
                        + "o comun�quese telef�nicamente con Talento Humano de "
                        + conexionEmpleado.getEmpleado().getEmpresa().getNombre() + " \n\n"
                        + "Cordial saludo. ";
                if (administrarGenerarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), cuentaAud,
                        "Auditoria: Reporte Kiosko - " + reporte.getDescripcion(),
                        mensaje,
                        pathReporteGenerado)) {
                    MensajesUI.info("El reporte de auditoria ha sido enviado exitosamente.");
                    PrimefacesContextUI.actualizar("principalForm:growl");
                } else {
                    MensajesUI.error("No fue posible enviar el correo de auditoria, por favor comun�quese con soporte.");
                    PrimefacesContextUI.actualizar("principalForm:growl");
                }
            }
        }
//        estaAuditado(reporte.getCodigo(), conexionEmpleado.getEmpleado().getEmpresa().getNit());
    }

//    private boolean estaAuditado(String codigo, long nit){
//        boolean resultado = false;
//        String modulo = "reportes";
//        System.out.println(modulo+" "+codigo+" "+nit);
//        
//        return resultado;
//    }
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
//        context.execute("PF('generandoReporte').hide();");
        PrimeFaces.current().executeScript("PF('generandoReporte').hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            //System.out.println("ruta: "+pathReporteGenerado);
            try {
                arregloruta = pathReporteGenerado.split(Pattern.quote("\\"));
            } catch (PatternSyntaxException pse) {
                System.out.println("Error en la fragmentaci�n de la ruta.");
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
                System.out.println("userAgent: " + userAgent);
                if (userAgent.toUpperCase().contains("Mobile".toUpperCase())
                        || userAgent.toUpperCase().contains("Tablet".toUpperCase())
                        || userAgent.toUpperCase().contains("Android".toUpperCase())
                        || userAgent.toUpperCase().contains("IOS".toUpperCase())) {
                    //System.out.println("Acceso por mobiles.");
//                    context.update("principalForm:dwlReportePDF");
//                    context.execute("PF('dwlReportePDF').show();");
                    PrimeFaces.current().ajax().update("principalForm:dwlReportePDF");
                    PrimeFaces.current().executeScript("PF('dwlReportePDF').show();");
                } else {
//                    context.update("principalForm:verReportePDF");
//                    context.execute("PF('verReportePDF').show();");
                    PrimeFaces.current().ajax().update("principalForm:verReportePDF");
                    PrimeFaces.current().executeScript("PF('verReportePDF').show();");
                }
//                context.execute("validarEnvioCorreo();");
                PrimeFaces.current().executeScript("validarEnvioCorreo();");
            } else {
//                context.update("principalForm:errorGenerandoReporte");
//                context.execute("PF('errorGenerandoReporte').show();");
                PrimeFaces.current().ajax().update("principalForm:errorGenerandoReporte");
                PrimeFaces.current().executeScript("PF('errorGenerandoReporte').show();");
            }
            //pathReporteGenerado = null;
        } else {
//            context.update("principalForm:errorGenerandoReporte");
//            context.execute("PF('errorGenerandoReporte').show();");
            PrimeFaces.current().ajax().update("principalForm:errorGenerandoReporte");
            PrimeFaces.current().executeScript("PF('errorGenerandoReporte').show();");
        }
        generarAuditoria(pathReporteGenerado);
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
                MensajesUI.error("Correo inv�lido, por favor verifique.");
            }
        } else {
            return true;
        }
        return false;
    }
    
    public void validarEnviaCorreo() {
        System.out.println(this.getClass().getName() + "." + "validarEnviaCorreo" + "()");
        if (conexionEmpleado.isEnvioCorreo()) {
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de generar el " + reporte.getDescripcion()
                    + "en el m�dulo de Kiosco N�mina Designer. \n\n";
            mensaje = mensaje + "Le recordamos que esta direcci�n de correo es utilizada solamente para env�os "
                    + "autom�ticos de la informaci�n solicitada. Por favor no responda este correo, "
                    + "ya que no podr� ser atendido. Si desea contactarse con nosotros, env�e un correo "
                    + "o comun�quese telef�nicamente con Talento Humano de "
                    + conexionEmpleado.getEmpleado().getEmpresa().getNombre() + " \n\n"
                    + "Cordial saludo. ";
            if (administrarGenerarReporte.enviarCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia(), email,
                    "Reporte Kiosko - " + reporte.getDescripcion(), mensaje,
                    pathReporteGenerado)) {
                MensajesUI.info("El reporte ha sido enviado exitosamente.");
                PrimefacesContextUI.actualizar("principalForm:growl");
            } else {
                MensajesUI.error("No fue posible enviar el correo, por favor comun�quese con soporte.");
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
        boolean resultado = false;
        try {
            resultado = administrarGenerarReporte.comprobarConfigCorreo(conexionEmpleado.getEmpleado().getEmpresa().getSecuencia());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println(this.getClass().getName() + "." + "validarConfigSMTP" + "()");
            System.out.println("La transacci�n se ha deshecho.");
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
                System.out.println("Error en la fragmentaci�n de la ruta.");
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
                MensajesUI.error("Configuraci�n de Servidor SMTP inv�lida.");
            }
        } else {
            conexionEmpleado.setEnvioCorreo(enviocorreo);
            this.enviocorreo = conexionEmpleado.isEnvioCorreo();
        }
    }
}
