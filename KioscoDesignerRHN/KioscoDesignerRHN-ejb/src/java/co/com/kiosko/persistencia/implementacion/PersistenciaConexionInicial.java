package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Stateless
public class PersistenciaConexionInicial implements IPersistenciaConexionInicial {

    @Override
    public void setearKiosko(EntityManager eManager) {
        eManager.getTransaction().begin();
        String sqlQuery = "SET ROLE ROLKIOSKO IDENTIFIED BY RLKSK";
        Query query = eManager.createNativeQuery(sqlQuery);
        query.executeUpdate();
        eManager.getTransaction().commit();
    }

    @Override
    public boolean validarIngresoUsuario(EntityManager eManager, String usuario, String clave) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e WHERE ck.EMPLEADO = e.SECUENCIA AND e.codigoempleado = ? AND ck.PWD = GENERALES_PKG.ENCRYPT(?)";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, clave);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            eManager.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario y clave son correctos.");
                return true;
            } else {
                System.out.println("El usuario o clave son incorrectos.");
                eManager.getEntityManagerFactory().close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuario: " + e);
            return false;
        }
    }

    @Override
    public EntityManager validarConexionUsuario(EntityManagerFactory emf) {
        try {
            EntityManager eManager = emf.createEntityManager();
            if (eManager.isOpen()) {
                return eManager;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarConexionUsuario : " + e);
            emf.close();
        }
        return null;
    }
}
