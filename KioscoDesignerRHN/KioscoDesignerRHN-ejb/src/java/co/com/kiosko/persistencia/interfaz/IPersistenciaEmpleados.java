package co.com.kiosko.persistencia.interfaz;

import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaEmpleados {

    public co.com.kiosko.entidades.Empleados consultarEmpleado(EntityManager eManager, BigInteger codigoEmpleado, long nit);
    
}
