package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.administrar.entidades.OpcionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaOpcionesKioskos;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaOpcionesKioskos implements IPersistenciaOpcionesKioskos {

    @Override
    public List<OpcionesKioskos> consultarOpcionesPorPadre(EntityManager eManager, BigDecimal secuenciaPadre) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT ok FROM OpcionesKioskos ok ";
            Query query;
            if (secuenciaPadre == null) {
                sqlQuery += "WHERE ok.opcionkioskopadre IS NULL";
                query = eManager.createQuery(sqlQuery);
            } else {
                sqlQuery += "WHERE ok.opcionkioskopadre.secuencia = :secuenciaPadre";
                query = eManager.createQuery(sqlQuery);
                query.setParameter("secuenciaPadre", secuenciaPadre);
            }
            List<OpcionesKioskos> lok = query.getResultList();
            eManager.getTransaction().commit();
            return lok;
        } catch (Exception e) {
            System.out.println("Error PersistenciaOpcionesKioskos.consultarOpcionesPorPadre: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
