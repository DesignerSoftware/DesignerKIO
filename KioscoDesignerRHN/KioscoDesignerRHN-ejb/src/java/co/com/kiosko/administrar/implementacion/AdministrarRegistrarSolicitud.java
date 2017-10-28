package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarRegistrarSolicitud;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioNovedadesSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaVwVacaPendientesEmpleados;
import java.util.Date;
import javax.ejb.EJB;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarRegistrarSolicitud implements IAdministrarRegistrarSolicitud, Serializable {

    
    private EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaEstadoSolicitud;
    @EJB
    private IPersistenciaKioNovedadesSolici persistenciaNovedadSolicitud;
    @EJB
    private IPersistenciaVwVacaPendientesEmpleados persistenciaVwVacaPendEmpl;

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
        System.out.println("registrarNovedad-solicitud: "+solicitud);
        EntityManager em = emf.createEntityManager();
        try {
            persistenciaNovedadSolicitud.modificarNovedadSolici(em, solicitud.getKioNovedadesSolici());
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        em = emf.createEntityManager();
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
    @Override
    public Date fechaPago(Empleados empleado) {
        Date fechaUltPago = null;
        EntityManager em=null;
        try {
            em = emf.createEntityManager();
            fechaUltPago = persistenciaVwVacaPendEmpl.consultaFechaUltimoPago(em, empleado.getSecuencia());
            System.out.println("fechaUltimoPago: " + fechaUltPago);
        } catch (Exception exi) {
            System.out.println("AdministrarRegistrarSolicitud. Error consultando la fecha de último pago.");
            exi.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return fechaUltPago;
    }
    
    @Override
    public Date calcularFechaPago(Empleados empleado, Date fini, Date ffin, String proceso){
        Date fechaUltPago = null;
        EntityManager em=null;
        try {
            em = emf.createEntityManager();
            fechaUltPago = persistenciaVwVacaPendEmpl.calculaFechaPago(em, empleado.getSecuencia(), fini, ffin, proceso);
            System.out.println("fechaPago: " + fechaUltPago);
        } catch (Exception exi) {
            System.out.println("AdministrarRegistrarSolicitud. Error calculandos la fecha de último pago.");
            exi.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return fechaUltPago;
    }
}
