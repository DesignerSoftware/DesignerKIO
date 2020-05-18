package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarHistoRepoTiempos;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosLocalizaSolici;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarHistoRepoTiempos implements IAdministrarHistoRepoTiempos {

    private transient EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaKioEstadosLocalizaSolici persistenciaKioEstadosLocalizaSolici;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                return emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerConexion - 2: " + e);
        }
        return null;
    }

    @Override
    public List<KioEstadosLocalizaSolici> obtenerEstadosLocalizaSoliciXJefe(Empleados empleadoJefe, String filtroEstado) {
        EntityManager em = null;
        String estado = "";
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            if ("SIN PROCESAR".equalsIgnoreCase(filtroEstado)) {
                estado = "ENVIADO";
            }
            return persistenciaKioEstadosLocalizaSolici.consultarEstadosLocalizaXJefe(em, empleadoJefe.getSecuencia(), estado);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerEstadosLocalizaSoliciXJefe: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<KioEstadosLocalizaSolici> obtenerEstadosLocalizaSoliciXEmpleado(Empleados empleado, String filtroEstado) {
        EntityManager em = null;
        String estado = "";
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            if ("SIN PROCESAR".equalsIgnoreCase(filtroEstado)) {
                estado = "ENVIADO";
            }else{
                estado = filtroEstado;
            }
            return persistenciaKioEstadosLocalizaSolici.consultarEstadosLocalizaXEmpleado(em, empleado.getSecuencia(), estado);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerEstadosLocalizaSoliciXJefe: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void cambiarEstadoSolicitud(KioSolicisLocaliza solicitud, Empleados emplEjecuta, String estado, String motivo, Personas perEjecuta) throws Exception {
        System.out.println(this.getClass().getName() + ".cambiarEstadoSolicitud()");
        EntityManager em = null;
//        System.out.println("cambiarEstadoSolicitud: guardado");
        BigInteger secPersona = null;
        if (perEjecuta != null) {
            secPersona = perEjecuta.getSecuencia();
        }
        BigDecimal secEmpleado = null;
        if (emplEjecuta != null) {
            secEmpleado = emplEjecuta.getSecuencia();

        }
        try {
            em = emf.createEntityManager();
            persistenciaKioEstadosLocalizaSolici.crearEstadoSolicitud(em, solicitud, secEmpleado, estado, motivo, secPersona);
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
    public List<KioLocalizacionesEmpl> obtenerLocalizacionesXSolicitud(KioSolicisLocaliza solicitud) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaKioEstadosLocalizaSolici.consultarLocalizacionesXSolicitud(em, solicitud.getSecuencia());
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerLocalizacionesXSolicitud: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
