package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.entidades.ConfiguracionCorreo;
import co.com.kiosko.administrar.entidades.Generales;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import co.com.kiosko.reportes.Correo.EnvioCorreo;
import co.com.kiosko.reportes.IniciarReporteInterface;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarGenerarReporte implements IAdministrarGenerarReporte {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaConfiguracionCorreo persistenciaConfiguracionCorreo;
    @EJB
    private IniciarReporteInterface reporte;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros) {
        try {
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            String pathReporteGenerado = null;
            if (general != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "KSK_" + nombreReporte + "_" + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                } else if (tipoReporte.equals("XLSX")) {
                    nombreArchivo = nombreArchivo + ".xlsx";
                } else if (tipoReporte.equals("XLS")) {
                    nombreArchivo = nombreArchivo + ".xls";
                } else if (tipoReporte.equals("CSV")) {
                    nombreArchivo = nombreArchivo + ".csv";
                } else if (tipoReporte.equals("HTML")) {
                    nombreArchivo = nombreArchivo + ".html";
                } else if (tipoReporte.equals("DOCX")) {
                    nombreArchivo = nombreArchivo + ".rtf";
                }

                parametros.put("pathImagenes", rutaReporte);
                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, parametros, em);
                return pathReporteGenerado;
            }
            return pathReporteGenerado;
        } catch (Exception ex) {
            System.out.println("Error AdministrarGenerarReporte.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public boolean modificarConexionKisko(ConexionesKioskos cnx) {
        return persistenciaConexionesKioskos.registrarConexion(em, cnx);
    }

    @Override
    public boolean enviarCorreo(BigDecimal secuenciaEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
        EnvioCorreo enviarCorreo = new EnvioCorreo();
        return enviarCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
    }
}
