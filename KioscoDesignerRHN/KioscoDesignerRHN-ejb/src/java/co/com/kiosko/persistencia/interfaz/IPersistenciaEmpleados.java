package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaEmpleados {

    public co.com.kiosko.entidades.Empleados consultarEmpleado(javax.persistence.EntityManager eManager, java.math.BigInteger codigoEmpleado);
    
}
