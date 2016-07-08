package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import java.math.BigDecimal;
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
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
            ConfiguracionCorreo cc = (ConfiguracionCorreo) query.getSingleResult();
            eManager.getTransaction().commit();
            return cc;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
