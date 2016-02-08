package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import co.com.kiosko.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso {

    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    private IAdministrarSesiones administrarSessiones;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    private EntityManager em;
    private final SesionEntityManagerFactory sessionEMF;

    public AdministrarIngreso() {
        sessionEMF = new SesionEntityManagerFactory();
    }

    @Override
    public boolean conexionIngreso(String unidadPersistencia) {
        try {
            if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
                sessionEMF.getEmf().close();
            }
            if (sessionEMF.crearConexionUsuario(unidadPersistencia)) {
                em = sessionEMF.getEmf().createEntityManager();
                return true;
            } else {
                System.out.println("Error AdministrarIngreso.conexionIngreso (La unidad de persistencia no existe, revisar el archivo de conexiones.)");
                em = null;
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.conexionIngreso" + e);
            return false;
        }
    }

    @Override
    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa) {
        try {
            if (em != null) {
                persistenciaConexionInicial.setearKiosko(em);
                return persistenciaConexionInicial.validarUsuarioyEmpresa(em, usuario, nitEmpresa);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarUsuarioyEmpresa" + e);
            return false;
        }
    }

    @Override
    public boolean validarUsuarioRegistrado(String usuario) {
        try {
            if (em != null) {
                return persistenciaConexionInicial.validarUsuarioRegistrado(em, usuario);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarUsuarioRegistrado" + e);
            return false;
        }
    }

    @Override
    public boolean validarEstadoUsuario(String usuario) {
        try {
            if (em != null) {
                return persistenciaConexionInicial.validarEstadoUsuario(em, usuario);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarEstadoUsuario" + e);
            return false;
        }
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave) {
        try {
            if (em != null) {
                return persistenciaConexionInicial.validarIngresoUsuarioRegistrado(em, usuario, clave);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarIngresoUsuarioRegistrado" + e);
            return false;
        }
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion) {
        try {
            if (em != null) {
                if (em.isOpen()) {
                    SessionEntityManager sem = new SessionEntityManager(idSesion, sessionEMF.getEmf());
                    administrarSessiones.adicionarSesion(sem);

                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.adicionarConexionUsuario: " + e);
            return false;
        }
    }

    @Override
    public ConexionesKioskos obtenerConexionEmpelado(String codigoEmpleado, String nitEmpresa) {
        return persistenciaConexionesKioskos.consultarConexionEmpleado(em, codigoEmpleado, Long.parseLong(nitEmpresa));
    }

    @Override
    public boolean bloquearUsuario(String codigoEmpleado, String nitEmpresa) {
        ConexionesKioskos cnx = obtenerConexionEmpelado(codigoEmpleado, nitEmpresa);
        cnx.setActivo("N");
        return persistenciaConexionesKioskos.registrarConexion(em, cnx);
    }

    @Override
    public void modificarUltimaConexion(ConexionesKioskos cnx) {
        cnx.setUltimaconexion(new Date());
        persistenciaConexionesKioskos.registrarConexion(em, cnx);
    }

    @Override
    public void cerrarSession(String idSesion) {
        if (em.isOpen()) {
            em.getEntityManagerFactory().close();
            administrarSessiones.borrarSesion(idSesion);
        }
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
