package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConexionesKioskos implements IPersistenciaConexionesKioskos {

    @Override
    public boolean registrarConexion(EntityManager eManager, ConexionesKioskos cnk) {
        cnk.setUltimaconexion(new Date());
        eManager.clear();
        EntityTransaction tx = eManager.getTransaction();
        try {
            tx.begin();
            eManager.merge(cnk);
            tx.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionesKioskos.registrarConexion: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    @Override
    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String codigoEmpleado, long nitEmpresa) {
        System.out.println("eManager: "+eManager);
        System.out.println("codigoEmpleado: "+codigoEmpleado);
        System.out.println("nitEmpresa: "+nitEmpresa);
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT ck FROM ConexionesKioskos ck WHERE ck.empleado.codigoempleado = :codigoEmpleado and ck.empleado.empresa.nit = :nitEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", new BigInteger(codigoEmpleado));
            query.setParameter("nitEmpresa", nitEmpresa);
            eManager.getTransaction().commit();
            return (ConexionesKioskos) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionesKioskos.consultarConexionEmpleado: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
