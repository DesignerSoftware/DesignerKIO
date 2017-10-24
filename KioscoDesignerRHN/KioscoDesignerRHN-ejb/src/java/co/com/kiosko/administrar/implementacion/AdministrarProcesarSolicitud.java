package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import co.com.kiosko.administrar.interfaz.IAdministrarProcesarSolicitud;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarProcesarSolicitud implements IAdministrarProcesarSolicitud {

    private EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaEstadoSolicitud;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void cambiarEstadoSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta, String estado, String motivo) throws Exception {
        System.out.println(this.getClass().getName() + ".cambiarEstadoSolicitud()");
        EntityManager em = emf.createEntityManager();
        System.out.println("cambiarEstadoSolicitud: guardado");
        try {
            persistenciaEstadoSolicitud.crearEstadoSolicitud(em, solicitud, emplEjecuta.getSecuencia(), estado, motivo);
            System.out.println("cambiarEstadoSolicitud: enviando");
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void registrarNovedad(KioSoliciVacas solicitud) throws Exception {
        System.out.println(this.getClass().getName() + ".registrarNovedad()");
        EntityManager em = emf.createEntityManager();
        try {
            persistenciaEstadoSolicitud.registrarNovedad(em, solicitud);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
