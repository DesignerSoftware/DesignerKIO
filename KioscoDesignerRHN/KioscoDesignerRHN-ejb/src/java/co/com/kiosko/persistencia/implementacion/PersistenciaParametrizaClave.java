package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.administrar.entidades.ParametrizaClave;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaParametrizaClave implements IPersistenciaParametrizaClave {

    @Override
    public ParametrizaClave obtenerFormatoClave(EntityManager eManager, long nitEmpresa) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT pc FROM ParametrizaClave pc WHERE pc.empresa.nit = :nitEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("nitEmpresa", nitEmpresa);
            eManager.getTransaction().commit();
            return (ParametrizaClave) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrizaClave.obtenerFormatoClave: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
