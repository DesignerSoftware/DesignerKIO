package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import co.com.kiosko.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
//import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements IAdministrarSesiones {

    private List<SessionEntityManager> sessionesActivas;
    private final ISesionEntityManagerFactory sessionEMF;

    public AdministrarSesiones() {
        sessionesActivas = new ArrayList<SessionEntityManager>();
        sessionEMF = new SesionEntityManagerFactory();
    }
    
    @Override
    public void adicionarSesion(SessionEntityManager session) {
        System.out.println(this.getClass().getName() + "." + "adicionarSesion" + "()");
        sessionesActivas.add(session);
    }

    @Override
    public void consultarSessionesActivas() {
        System.out.println(this.getClass().getName() + "." + "consultarSessionesActivas" + "()");
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                System.out.println("Id Sesion: " + sessionesActivas.get(i).getIdSession() + " - Unidad de Persistencia " + sessionesActivas.get(i).getUnidadPersistencia());
            }
            System.out.println("TOTAL SESIONES: " + sessionesActivas.size());
        }
    }

    @Override
    public EntityManagerFactory obtenerConexionSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "obtenerConexionSesion" + "()");
        SessionEntityManager sesionActual = null;
//        EntityManager eManager = null;
        
        try {
            if (!sessionesActivas.isEmpty()) {
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                        sesionActual = sessionesActivas.get(i);
                        i = sessionesActivas.size();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error en " + "obtenerConexionSesion");
            System.out.println("Causa: " + e);
            sesionActual = null;
        }
        EntityManagerFactory emf = null;
        if (sesionActual != null) {
            emf = sessionEMF.crearConexionUsuario(sesionActual.getUnidadPersistencia());
//            eManager = emf.createEntityManager();
            System.out.println("Se creó entityManagerFactory.");
            System.out.println("eManager" + emf.toString());
        }
        return emf;
    }

    @Override
    public void borrarSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "borrarSesion" + "()");
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                    sessionesActivas.get(i).setIdSession("");
                    sessionesActivas.remove(sessionesActivas.get(i));
                    i = sessionesActivas.size();
                }
            }
        }
    }

    @Override
    public boolean borrarSesiones() {
        System.out.println(this.getClass().getName() + "." + "borrarSesiones" + "()");
        try {
            if (!sessionesActivas.isEmpty()) {
//                for (int i = 0; i < sessionesActivas.size(); i++) {
//                    if (sessionesActivas.get(i).getEm().isOpen() && sessionesActivas.get(i).getEm().getEntityManagerFactory().isOpen()) {
//                        sessionesActivas.get(i).getEm().getEntityManagerFactory().close();
//                    }
//                }
                sessionesActivas.removeAll(sessionesActivas);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error AdministrarSesiones.borrarSesiones: (BORRADO DIARIO): " + e);
            return false;
        }
    }

//    @PreDestroy
//    public void destruct() {
//        System.out.println(this.getClass().getName() + "." + "destruct" + "()");
//        for (int i = 0; i < sessionesActivas.size(); i++) {
//            sessionesActivas.get(i).destruct();
//        }
//    }
}
