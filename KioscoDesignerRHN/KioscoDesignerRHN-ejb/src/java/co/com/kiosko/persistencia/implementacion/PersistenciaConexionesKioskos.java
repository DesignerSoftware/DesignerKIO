package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConexionesKioskos implements IPersistenciaConexionesKioskos {

    @Override
    public boolean registrarConexion(EntityManager eManager, ConexionesKioskos cnk) {
        boolean resp;
//        EntityManagerFactory eManagerFact = Persistence.createEntityManagerFactory("DEFAULT1");
//        eManager = eManagerFact.createEntityManager();
        System.out.println(this.getClass().getName()+".registrarConexion()");
        System.out.println("Se creó entityManager.");
        System.out.println("eManager"+eManager.toString());
        cnk.setUltimaconexion(new Date());
//        eManager.clear();
        EntityTransaction tx = eManager.getTransaction();
        try {
            tx.begin();
            eManager.merge(cnk);
            tx.commit();
//            eManager.close();
            resp = true;
        } catch (RollbackException re){
            System.out.println("PersistenciaConexionesKioskos.registrarConexion");
            System.out.println("Error RollbackException: " + re);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (NullPointerException npe) {
                System.out.println("Transacción nula 1.");
            }
            resp = false;
        }
        catch (Exception e) {
            System.out.println("PersistenciaConexionesKioskos.registrarConexion");
            System.out.println("Error generico: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (NullPointerException npe) {
                System.out.println("Transacción nula 2.");
            }
            resp = false;
        }
        return resp;
    }

    @Override
    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String codigoEmpleado, long nitEmpresa) {
        System.out.println("eManager: " + eManager);
        System.out.println("codigoEmpleado: " + codigoEmpleado);
        System.out.println("nitEmpresa: " + nitEmpresa);
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
            try {
                eManager.getTransaction().rollback();
            } catch (NullPointerException npe) {
                System.out.println("La transacción es nula.");
            }
            return null;
        }
    }
}
