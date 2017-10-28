package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.ConexionesKioskos;
/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaConexionesKioskos {

    public boolean registrarConexion(javax.persistence.EntityManager eManager, co.com.kiosko.entidades.ConexionesKioskos cnk);

    public ConexionesKioskos consultarConexionEmpleado(javax.persistence.EntityManager eManager, java.lang.String codigoEmpleado, long nitEmpresa);
    
}
