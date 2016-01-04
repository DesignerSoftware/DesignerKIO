package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import java.math.BigInteger;
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
    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String codigoEmpleado) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT ck FROM ConexionesKioskos ck WHERE ck.empleado.codigoempleado = :codigoEmpleado";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", new BigInteger(codigoEmpleado));
            eManager.getTransaction().commit();
            return (ConexionesKioskos) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionesKioskos.consultarConexionEmpleado: " + e);
            eManager.getTransaction().rollback();
            return null;
        }
    }
}
