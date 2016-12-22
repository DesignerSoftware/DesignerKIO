package co.com.kiosko.reportes;

import java.sql.Connection;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IniciarReporteInterface {

    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Map parametros, EntityManager em);

    /*public void cerrarConexion();*/

    //public Connection inicarC(EntityManager em);
}
