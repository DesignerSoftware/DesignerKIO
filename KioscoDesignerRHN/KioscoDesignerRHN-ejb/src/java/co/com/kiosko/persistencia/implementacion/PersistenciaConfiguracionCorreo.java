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
        System.out.println(this.getClass().getName()+"."+"consultarConfiguracionServidorCorreo"+"()");
        ConfiguracionCorreo cc = null;
        try {
//            if (eManager.isOpen()) {
//            if (eManager != null) {
            if (eManager != null && eManager.isOpen()) {
//                eManager.getTransaction().begin();
                String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.secuencia = :secuenciaEmpresa";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("secuenciaEmpresa", secuenciaEmpresa);
                cc = (ConfiguracionCorreo) query.getSingleResult();
//                eManager.getTransaction().commit();
            } else {
                cc = null;
                System.out.println("entityManager nulo.");
            }
//            return cc;
        } catch (IllegalStateException ise){
            System.out.println("ERROR: "+ ise.getMessage());
        } catch (Exception e) {
            System.out.println("Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
//            try {
//                if (eManager.getTransaction().isActive()) {
//                    eManager.getTransaction().rollback();
//                    cc = null;
//                }
//            } catch (NullPointerException npe) {
//                System.out.println("error de nulo en la transacción.");
//            }
//            return null;
        }
        return cc;
    }
}
