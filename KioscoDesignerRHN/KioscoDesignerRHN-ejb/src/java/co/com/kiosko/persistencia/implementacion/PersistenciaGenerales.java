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
//            eManager.getTransaction().begin();
            String sqlQuery = "SELECT g FROM Generales g";
            Query query = eManager.createQuery(sqlQuery);
            Generales g = (Generales) query.getResultList().get(0);
//            eManager.getTransaction().commit();
            return g;
        } catch (Exception e) {
            System.out.println("Error PersistenciaGenerales.consultarRutasGenerales: " + e);
//            try {
//                eManager.getTransaction().rollback();
//            } catch (NullPointerException npe) {
//                System.out.println("error de nulo en la transacción.");
//            }
            return null;
        }
    }
}
