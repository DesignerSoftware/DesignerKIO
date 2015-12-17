package co.com.kiosko.conexionFuente.implementacion;

import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Felipe Triviño
 */
//@Singleton
@Stateful
public class SesionEntityManagerFactory implements ISesionEntityManagerFactory, Serializable {

    private EntityManagerFactory emf;

    @Override
    public boolean crearConexionUsuario(String unidadPersistencia) {
        try {
            emf = Persistence.createEntityManagerFactory(unidadPersistencia);
            return true;
        } catch (Exception e) {
            System.out.println("Error SesionEntityManagerFactory.crearConexionUsuario: " + e);
            return false;
        }
    }

    @Override
    public EntityManagerFactory getEmf() {
        return emf;
    }

    @Override
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
