package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.Empleados;
import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarInicioKiosko implements IAdministrarInicioKiosko {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Empleados consultarEmpleado(BigInteger codigoEmpleado) {
        return persistenciaEmpleados.consultarEmpleado(em, codigoEmpleado);
    }
}
