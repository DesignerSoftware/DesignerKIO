package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import javax.ejb.EJB;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import co.com.kiosko.administrar.interfaz.IAdministrarProcesarSolicitud;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaVwVacaPendientesEmpleados;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.TransactionRolledbackLocalException;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarProcesarSolicitud implements IAdministrarProcesarSolicitud, Serializable {

    private EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaEstadoSolicitud;
    @EJB
    private IPersistenciaKioSoliciVacas persistenciaSolicitud;
    @EJB
    private IPersistenciaVwVacaPendientesEmpleados persistenciaVwVacaPendEmpl;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void cambiarEstadoSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta, String estado, String motivo) throws Exception {
        System.out.println(this.getClass().getName() + ".cambiarEstadoSolicitud()");
        EntityManager em = null;
        System.out.println("cambiarEstadoSolicitud: guardado");
        try {
            em = emf.createEntityManager();
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

    @Override
    public boolean existeSolicitudFecha(KioSoliciVacas solicitud) throws Exception {
        boolean res = false;
        BigDecimal conteo = BigDecimal.ZERO;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            conteo = persistenciaSolicitud.verificaExistenciaSolicitud(em, solicitud.getEmpleado().getSecuencia(), solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
            res = (conteo.compareTo(new BigDecimal("1")) == 1);
        } catch (TransactionRolledbackLocalException trle) {
            res = false;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
//                em.joinTransaction();
                em.close();
            }
        }
        return res;
    }

    @Override
    public Date fechaUltimoPago(Empleados empleado) {
        Date fechaUltPago = null;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            fechaUltPago = persistenciaVwVacaPendEmpl.consultaFechaUltimoPago(em, empleado.getSecuencia());
            System.out.println("fechaUltimoPago: " + fechaUltPago);
        } catch (Exception exi) {
            System.out.println("AdministrarProcesarSolicitud. Error consultando la fecha de último pago.");
            exi.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return fechaUltPago;
    }

}
