package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConexionesKioskos implements IPersistenciaConexionesKioskos {

    @Override
    public boolean registrarConexion(EntityManager eManager, ConexionesKioskos cnk) {
        boolean resp;
        System.out.println(this.getClass().getName()+".registrarConexion()");
        System.out.println("Se creó entityManager.");
        System.out.println("eManager: "+eManager.toString());
        Map<String,Object> propiedades = eManager.getProperties();
        cnk.setUltimaconexion(new Date());
        try {
            eManager.merge(cnk);
            resp = true;
        } catch (PersistenceException re){
            System.out.println("PersistenciaConexionesKioskos.registrarConexion");
            System.out.println("Error PersistenceException: " + re);
            resp = false;
        } catch (Exception e) {
            System.out.println("PersistenciaConexionesKioskos.registrarConexion");
            System.out.println("Error generico: " + e);
            resp = false;
        }
        return resp;
    }

    @Override
    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String codigoEmpleado, long nitEmpresa) {
        System.out.println(this.getClass().getName()+".consultarConexionEmpleado()");
        System.out.println("eManager: " + eManager);
        System.out.println("codigoEmpleado: " + codigoEmpleado);
        System.out.println("nitEmpresa: " + nitEmpresa);
        try {
            String sqlQuery = "SELECT ck FROM ConexionesKioskos ck WHERE ck.empleado.codigoempleado = :codigoEmpleado and ck.empleado.empresa.nit = :nitEmpresa";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", new BigInteger(codigoEmpleado));
            query.setParameter("nitEmpresa", nitEmpresa);
            return (ConexionesKioskos) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionesKioskos.consultarConexionEmpleado: " + e);
            try {
            } catch (NullPointerException npe) {
                System.out.println("La transacción es nula.");
            }
            return null;
        }
    }
    @Override
    public ConexionesKioskos consultarConexionEmpleado(EntityManager eManager, String numerodocumento) {
        System.out.println(this.getClass().getName()+".consultarConexionEmpleado()");
        System.out.println("eManager: " + eManager);
        System.out.println("codigoEmpleado: " + numerodocumento);
        try {
            String sqlQuery = "SELECT ck FROM ConexionesKioskos ck WHERE ck.persona.numerodocumento = :numeroDocumento";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("numeroDocumento", new BigInteger(numerodocumento));
            return (ConexionesKioskos) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionesKioskos.consultarConexionEmpleado: " + e);
            try {
            } catch (NullPointerException npe) {
                System.out.println("La transacción es nula.");
            }
            return null;
        }
    }
}
