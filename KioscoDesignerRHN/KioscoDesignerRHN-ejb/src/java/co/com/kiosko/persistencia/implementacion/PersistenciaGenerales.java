package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Generales;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaGenerales implements IPersistenciaGenerales {

    @Override
    public Generales consultarRutasGenerales(EntityManager eManager) {
        try {
            String sqlQuery = "SELECT g FROM Generales g";
            Query query = eManager.createQuery(sqlQuery);
            Generales g = (Generales) query.getResultList().get(0);
            return g;
        } catch (Exception e) {
            System.out.println("Error PersistenciaGenerales.consultarRutasGenerales: " + e);
            return null;
        }
    }
}
