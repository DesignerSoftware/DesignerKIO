package co.com.kiosko.utilidadesUI;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Edwin
 */
@ManagedBean
@ViewScoped
public class ControladorArchivoReporte implements Serializable{

    private ExternalContext externalContext;
    private String userAgent;
    private String reporte;
    private String tipo;
    private Empleados empleado;
    private String pathReporteGenerado;
    private FileInputStream fis;
    private StreamedContent reporteGenerado;
    @EJB
    private IAdministrarGenerarReporte administrarGenerarReporte;

    public ControladorArchivoReporte() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println(this.getClass().getName() + "." + "inicializarAdministrador" + "()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            administrarGenerarReporte.obtenerConexion(ses.getId());

        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    public void validar() {
        FacesContext y = FacesContext.getCurrentInstance();
        Map<String, String> params = y.getExternalContext().getRequestParameterMap();
        String nombreArchivo = params.get("nombreArchivo");
        String tipoReporte = params.get("tipo");
        System.out.println(this.getClass().getName() + "." + "validar" + "()");
        System.out.println("validar:nombreArchivo: " + nombreArchivo);
        System.out.println("validar:tipo: " + tipoReporte);
        this.reporte = nombreArchivo;
        this.tipo = tipoReporte;
        PrimefacesContextUI.ejecutar("PF('generandoReporte').show();");
        PrimefacesContextUI.ejecutar("generarReporte();");
    }

    public void generarReporte() {
        System.out.println(this.getClass().getName() + "." + "generarReporte" + "()");
        Map parametros = new HashMap();
        parametros.put("secuenciaempleado", empleado.getSecuencia());
        pathReporteGenerado = administrarGenerarReporte.generarReporte(reporte, tipo, parametros);
        if (pathReporteGenerado != null) {
            PrimefacesContextUI.ejecutar("validarDescargaReporte();");
        } else {
            PrimefacesContextUI.ejecutar("PF('generandoReporte').hide();");
            MensajesUI.error("El reporte no se pudo generar.");
        }
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + "." + "validarDescargaReporte" + "()");
        String nombrearchivo = "reporte." + tipo;
        String[] arregloruta = new String[1];
        RequestContext context = RequestContext.getCurrentInstance();
        String contentType = "";
        context.execute("PF('generandoReporte').hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("ruta: " + pathReporteGenerado);
            try {
                arregloruta = pathReporteGenerado.split(Pattern.quote("\\"));
            } catch (PatternSyntaxException pse) {
                System.out.println("Error en la fragmentación de la ruta.");
                System.out.println("Causa: " + pse);
            }
            nombrearchivo = arregloruta[arregloruta.length - 1];
            System.out.println("nombre de archivo: " + nombrearchivo);
            try {
                System.out.println("pathReporteGenerado: " + pathReporteGenerado);
                File archivo = new File(pathReporteGenerado);
                fis = new FileInputStream(archivo);
                if ("PDF".equalsIgnoreCase(this.tipo)) {
                    contentType = "application/pdf";
                } else if ("XLS".equalsIgnoreCase(this.tipo)) {
                    contentType = "application/vnd.ms-excel";
                }
                reporteGenerado = new DefaultStreamedContent(fis, contentType, nombrearchivo);
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getCause());
                reporteGenerado = null;
            }
            if (reporteGenerado != null) {
                System.out.println("userAgent: " + userAgent);
                if (contentType.equalsIgnoreCase("application/vnd.ms-excel")
                        || userAgent.toUpperCase().contains("Mobile".toUpperCase())
                        || userAgent.toUpperCase().contains("Tablet".toUpperCase())
                        || userAgent.toUpperCase().contains("Android".toUpperCase())
                        || userAgent.toUpperCase().contains("IOS".toUpperCase())) {
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
        } else {
            context.update("principalForm:errorGenerandoReporte");
            context.execute("PF('errorGenerandoReporte').show();");
        }
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + "reiniciarStreamedContent" + "()");
        reporteGenerado = null;
        pathReporteGenerado = null;
    }

    public StreamedContent getReporteGenerado() {
        return reporteGenerado;
    }

    public void setReporteGenerado(StreamedContent reporteGenerado) {
        this.reporteGenerado = reporteGenerado;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

}
