package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
//import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConfiguracionCorreo implements IPersistenciaConfiguracionCorreo {

    @Override
    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(EntityManager eManager, BigInteger secuenciaEmpresa) {
        ConfiguracionCorreo cc;
        try {
            if (eManager.isOpen()) {
                eManager.getTransaction().begin();
                String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
                cc = (ConfiguracionCorreo) query.getSingleResult();
                eManager.getTransaction().commit();
            } else {
                cc = null;
                System.out.println("error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo.");
                System.out.println("entityManager cerrado.");
            }
            return cc;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
            try {
                eManager.getTransaction().rollback();
            } catch (NullPointerException npe) {
                System.out.println("error de nulo en la transacción.");
            }
            return null;
        }
    }
}
