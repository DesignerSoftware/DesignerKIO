package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ParametrizaClave;
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
        System.out.println(this.getClass().getName() + ".obtenerFormatoClave()");
        System.out.println("nit " + nitEmpresa);
        Object objeto;
        try {
            String sqlQuery = "SELECT pc FROM ParametrizaClave pc WHERE pc.empresa.nit = :nitEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("nitEmpresa", nitEmpresa);
            objeto = query.getSingleResult();
            System.out.println(objeto.getClass());
            System.out.println(objeto.toString());
            if (objeto instanceof ParametrizaClave) {
                System.out.println("si es");
            } else {
                System.out.println("no es");
            }
            return (ParametrizaClave) objeto;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrizaClave.obtenerFormatoClave: " + e);
            try {
            } catch (NullPointerException npe) {
                System.out.println("Error de nulo en la transacción.");
            }
            return null;
        }
    }
}
