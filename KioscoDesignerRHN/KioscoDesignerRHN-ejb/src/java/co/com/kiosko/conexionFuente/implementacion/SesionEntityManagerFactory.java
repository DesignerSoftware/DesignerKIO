package co.com.kiosko.conexionFuente.implementacion;

import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import java.io.Serializable;
//import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
@Stateless
//@Stateful
public class SesionEntityManagerFactory implements ISesionEntityManagerFactory, Serializable {

    @Override
    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia) {
        try {
            return Persistence.createEntityManagerFactory(unidadPersistencia);
        } catch (Exception e) {
            System.out.println("Error SesionEntityManagerFactory.crearConexionUsuario: " + e);
            return null;
        }
    }

}
