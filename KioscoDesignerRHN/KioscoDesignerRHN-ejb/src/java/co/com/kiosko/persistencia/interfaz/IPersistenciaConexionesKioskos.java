package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.ConexionesKioskos;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaConexionesKioskos {

    public boolean registrarConexion(EntityManager eManager, co.com.kiosko.entidades.ConexionesKioskos cnk);

    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, java.lang.String codigoEmpleado, long nitEmpresa);

    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String numerodocumento);
}
