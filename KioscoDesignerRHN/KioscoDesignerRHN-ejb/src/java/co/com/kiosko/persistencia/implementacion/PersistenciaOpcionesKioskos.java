package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaOpcionesKioskos;
//import java.math.BigDecimal;
import java.math.BigInteger;
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
    public List<OpcionesKioskos> consultarOpcionesPorPadre(EntityManager eManager, BigInteger secuenciaPadre, BigInteger secuenciaEmpresa) {
        try {
//            eManager.getTransaction().begin();
            String sqlQuery = "SELECT ok FROM OpcionesKioskos ok ";
            Query query;
            if (secuenciaPadre == null) {
                sqlQuery += "WHERE ok.opcionkioskopadre IS NULL AND ok.empresa.secuencia = :secuenciaEmpresa";
                query = eManager.createQuery(sqlQuery);
            } else {
                sqlQuery += "WHERE ok.opcionkioskopadre.secuencia = :secuenciaPadre AND ok.empresa.secuencia = :secuenciaEmpresa";
                query = eManager.createQuery(sqlQuery);
                query.setParameter("secuenciaPadre", secuenciaPadre);
            }
            query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
            List<OpcionesKioskos> lok = query.getResultList();
//            eManager.getTransaction().commit();
            return lok;
        } catch (Exception e) {
            System.out.println("Error PersistenciaOpcionesKioskos.consultarOpcionesPorPadre: " + e);
            try {
//                eManager.getTransaction().rollback();
            } catch (NullPointerException npe) {
                System.out.println("Error de nulo en la transacción.");
            }
            return null;
        }
    }
}
