package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.Empleados;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
        sessionesActivas.add(session);
    }

    @Override
    public void consultarSessionesActivas() {
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                System.out.println("Id Sesion: " + sessionesActivas.get(i).getIdSession() + " - Entity Manager " + sessionesActivas.get(i).getEm().toString());
            }
            System.out.println("TOTAL SESIONES: " + sessionesActivas.size());
        }
    }

    @Override
    public EntityManager obtenerConexionSesion(String idSesion) {
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                    return sessionesActivas.get(i).getEm();
                }
            }
        }
        return null;
    }

    @Override
    public void borrarSesion(String idSesion) {
        if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
                if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                    sessionesActivas.remove(sessionesActivas.get(i));
                    break;
                }
            }
        }
    }
}
