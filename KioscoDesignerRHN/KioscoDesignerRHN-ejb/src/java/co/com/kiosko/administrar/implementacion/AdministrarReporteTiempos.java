package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarReporteTiempos;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizaciones;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosLocalizaSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioLocalizaciones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioLocalizacionesEmpl;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioSolicisLocaliza;
import co.com.kiosko.persistencia.interfaz.IPersistenciaUtilidadesBD;
import java.io.Serializable;
//import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
//import java.math.BigDecimal;
//import java.math.BigInteger;
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
public class AdministrarReporteTiempos implements IAdministrarReporteTiempos, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaKioLocalizaciones persistenciaKioLocalizaciones;
    @EJB
    private IPersistenciaKioSolicisLocaliza persistenciaKioSolicisLocaliza;
    @EJB
    private IPersistenciaKioEstadosLocalizaSolici persistenciaKioEstadosLocalizaSolici;
    @EJB
    private IPersistenciaKioLocalizacionesEmpl persistenciaKioLocalizacionesEmpl;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;

    private String idSesion;
    private transient EntityManagerFactory emf;

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
    public List<KioLocalizaciones> obtenerKioLocalizaciones(Empleados empleado) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaKioLocalizaciones.consultarKioLocalizaciones(em, empleado.getEmpresa().getSecuencia());

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerKioLocalizaciones: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public String consultarUsuario() {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaUtilidadesBD.consultaUsuario(em);
        } catch (Exception e) {
//            throw e;
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Empleados consultarEmpleadoJefe(Empleados empleado) {
//        System.out.println(this.getClass().getName() + ".consultarEmpleadoJefe()");
//        System.out.println(this.getClass().getName() + ".empl: "+empleado);
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEmpleados.consutarJefe(em, empleado.getSecuencia(), empleado.getEmpresa().getSecuencia());
        } catch (Exception e) {
//            throw e;
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public KioSolicisLocaliza guardarSolicitud(KioSolicisLocaliza solicitud, Personas emplEjecuta) throws Exception {
//        System.out.println(this.getClass().getName() + ".guardarSolicitud()");
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            persistenciaKioSolicisLocaliza.crearSolicitud(em, solicitud);
            solicitud = persistenciaKioSolicisLocaliza.recargarSolicitud(em, solicitud);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return solicitud;
    }

    @Override
    public void enviarReporteTiemposLab(KioSolicisLocaliza solicitud, List<KioLocalizacionesEmpl> listaLocalizaEmpl, Empleados empleado,
            String motivo) throws Exception {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            persistenciaKioSolicisLocaliza.crearSolicitud(em, solicitud);
            solicitud = persistenciaKioSolicisLocaliza.recargarSolicitud(em, solicitud);

            persistenciaKioEstadosLocalizaSolici.crearEstadoSolicitud(em, solicitud, empleado.getSecuencia(),
                    "GUARDADO", motivo, empleado.getPersona().getSecuencia());
            persistenciaKioEstadosLocalizaSolici.crearEstadoSolicitud(em, solicitud, empleado.getSecuencia(),
                    "ENVIADO", motivo, empleado.getPersona().getSecuencia());
            listaLocalizaEmpl = persistenciaKioLocalizacionesEmpl.crearLocalizacionEmpleado(em, solicitud, listaLocalizaEmpl);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<KioEstadosLocalizaSolici> obtenerEstadoLocalizacion(Empleados empleado, Calendar fechaReporte) throws Exception{
        EntityManager em = null;
        List<KioEstadosLocalizaSolici> lista = new ArrayList();
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            lista = persistenciaKioLocalizacionesEmpl.consultarEstadosLocalizaXEmpleado(em, empleado.getSecuencia(), fechaReporte);
            return lista;
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
