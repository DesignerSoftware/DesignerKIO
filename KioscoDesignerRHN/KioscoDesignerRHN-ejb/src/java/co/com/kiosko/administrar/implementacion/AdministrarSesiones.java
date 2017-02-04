package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements IAdministrarSesiones {

    private List<SessionEntityManager> sessionesActivas = new ArrayList<SessionEntityManager>();

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
                System.out.println("Id Sesion: " + sessionesActivas.get(i).getIdSession() + " - Entity Manager " + sessionesActivas.get(i).getEm().toString());
            }
            System.out.println("TOTAL SESIONES: " + sessionesActivas.size());
        }
    }

    @Override
    public EntityManager obtenerConexionSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "obtenerConexionSesion" + "()");
        try {
            if (!sessionesActivas.isEmpty()) {
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                        return sessionesActivas.get(i).getEm();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error en " + "obtenerConexionSesion");
            System.out.println("Causa: " + e);
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public void borrarSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "borrarSesion" + "()");
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                    //sessionesActivas.get(i).cerrarEMF();
                    sessionesActivas.get(i).setIdSession("");
                    sessionesActivas.remove(sessionesActivas.get(i));
                    //break;
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
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getEm().isOpen() && sessionesActivas.get(i).getEm().getEntityManagerFactory().isOpen()) {
                        sessionesActivas.get(i).getEm().getEntityManagerFactory().close();
                    }
                }
                sessionesActivas.removeAll(sessionesActivas);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error AdministrarSesiones.borrarSesiones: (BORRADO DIARIO): " + e);
            return false;
        }
    }

    @PreDestroy
    public void destruct() {
        System.out.println(this.getClass().getName() + "." + "destruct" + "()");
        for (int i = 0; i < sessionesActivas.size(); i++) {
            sessionesActivas.get(i).destruct();
        }
    }
}
