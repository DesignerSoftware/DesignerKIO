package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.administrar.interfaz.IAdministrarVerSolicitudesEmpl;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarVerSolicitudesEmpl implements IAdministrarVerSolicitudesEmpl {

    private EntityManagerFactory emf;
    
    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaKioEstadosSolici;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    
    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<KioEstadosSolici> consultarEstaSolici(Empleados empleado, String estado){
        EntityManager em = emf.createEntityManager();
        List<KioEstadosSolici> lista = null;
        try {
//            lista = persistenciaKioEstadosSolici.consultarEstadosXEmpl(em, empleado.getSecuencia());
            lista = persistenciaKioEstadosSolici.consultarEstadosXEmplEsta(em, empleado.getSecuencia(), estado);
        } catch (Exception ex) {
            Logger.getLogger(AdministrarVerSolicitudesEmpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.close();
        return lista;
    }
    @Override
    public List<KioEstadosSolici> consultarEstaSoliciGuar(Empleados empleado){
        EntityManager em = emf.createEntityManager();
        List<KioEstadosSolici> lista = persistenciaKioEstadosSolici.consultarEstadosXEmplEsta(em, empleado.getSecuencia(), "GUARDADO");
        em.close();
        return lista;
    }
    
    @Override
    public Date consultarFechaContrato(Empleados empleado){
        EntityManager em = emf.createEntityManager();
        Date fecha = persistenciaEmpleados.fechaContratoEmpl(em, empleado.getSecuencia());
        em.close();
        return fecha;
    }
}
