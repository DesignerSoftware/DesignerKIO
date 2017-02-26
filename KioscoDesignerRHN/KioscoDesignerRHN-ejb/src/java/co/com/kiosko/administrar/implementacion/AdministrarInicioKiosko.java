package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Generales;
import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import java.math.BigInteger;
import javax.ejb.EJB;
//import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
//@Stateful
@Stateless
public class AdministrarInicioKiosko implements IAdministrarInicioKiosko {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Empleados consultarEmpleado(BigInteger codigoEmpleado, long nit) {
        EntityManager em = emf.createEntityManager();
        Empleados empl = persistenciaEmpleados.consultarEmpleado(em, codigoEmpleado, nit);
        em.close();
        return empl;
    }

    @Override
    public String fotoEmpleado() {
        String rutaFoto = null;
        EntityManager em = emf.createEntityManager();
        Generales general = persistenciaGenerales.consultarRutasGenerales(em);
        if (general != null) {
//            return general.getPathfoto();
            rutaFoto = general.getPathfoto();
        } else {
//            return null;
            rutaFoto = null;
        }
        return rutaFoto;
    }
}
