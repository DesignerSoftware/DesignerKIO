package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
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
        System.out.println(this.getClass().getName()+"."+"consultarConfiguracionServidorCorreo"+"()");
        ConfiguracionCorreo cc = null;
        try {
            if (eManager != null && eManager.isOpen()) {
                String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
                cc = (ConfiguracionCorreo) query.getSingleResult();
            } else {
                cc = null;
                System.out.println("entityManager nulo.");
            }
        } catch (IllegalStateException ise){
            System.out.println("ERROR: "+ ise.getMessage());
        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
        }
        return cc;
    }
}
