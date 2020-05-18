package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarProcesarTiempos;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosLocalizaSolici;
import java.io.Serializable;
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
public class AdministrarProcesarTiempos implements IAdministrarProcesarTiempos, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaKioEstadosLocalizaSolici persistenciaKioEstadosLocalizaSolici;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
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
    public List<KioEstadosLocalizaSolici> obtenerKioEstadosLocalizaSolici(Empleados empleado) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            /*adicionar*/
            return null;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerKioEstadosLocalizaSolici: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
