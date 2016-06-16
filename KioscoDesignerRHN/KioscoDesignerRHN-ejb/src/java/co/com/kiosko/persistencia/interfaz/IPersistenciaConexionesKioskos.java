package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaConexionesKioskos {

    public boolean registrarConexion(javax.persistence.EntityManager eManager, co.com.kiosko.entidades.ConexionesKioskos cnk);

    public co.com.kiosko.entidades.ConexionesKioskos consultarConexionEmpleado(javax.persistence.EntityManager eManager, java.lang.String codigoEmpleado, long nitEmpresa);
    
}
