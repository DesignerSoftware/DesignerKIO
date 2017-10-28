package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.PreguntasKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPreguntasKioskos;
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
public class PersistenciaPreguntasKioskos implements IPersistenciaPreguntasKioskos {

    @Override
    public List<PreguntasKioskos> obtenerPreguntasSeguridad(EntityManager eManager) {
        try {
            String sqlQuery = "SELECT pk FROM PreguntasKioskos pk";
            Query query = eManager.createQuery(sqlQuery);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPreguntasKioskos.obtenerPreguntasSeguridad: " + e);
            return null;
        }
    }

    @Override
    public PreguntasKioskos consultarPreguntaSeguridad(EntityManager eManager, BigDecimal secuencia) {
        try {
            String sqlQuery = "SELECT pk FROM PreguntasKioskos pk WHERE pk.secuencia = :secuencia";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("secuencia", secuencia);
            return (PreguntasKioskos) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPreguntasKioskos.consultarPreguntaSeguridad: " + e);
            try {
            } catch (NullPointerException npe) {
                System.out.println("Error de nulo en la transacción.");
            }
            return null;
        }
    }
}
