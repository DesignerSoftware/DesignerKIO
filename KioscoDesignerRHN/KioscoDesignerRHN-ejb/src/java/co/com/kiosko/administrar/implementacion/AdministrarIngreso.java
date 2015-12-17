package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import co.com.kiosko.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso{

    @EJB
    IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    IAdministrarSesiones administrarSessiones;
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
    public boolean validarDatosIngreso(String usuario, String clave, String nitEmpresa) {
        try {
            if (em != null) {
                persistenciaConexionInicial.setearKiosko(em);
                if (persistenciaConexionInicial.validarIngresoUsuario(em, usuario, clave, nitEmpresa)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.validarDatosIngreso" + e);
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
    public void cerrarSession(String idSesion) {
        if (em.isOpen()) {
            em.getEntityManagerFactory().close();
            administrarSessiones.borrarSesion(idSesion);
        }
    }
}
