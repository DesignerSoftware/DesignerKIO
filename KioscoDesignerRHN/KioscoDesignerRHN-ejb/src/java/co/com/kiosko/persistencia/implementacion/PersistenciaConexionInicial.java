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
    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT COUNT(*) FROM EMPLEADOS e, Empresas em WHERE e.empresa = em.secuencia AND e.codigoempleado = ? AND em.nit = ?";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            eManager.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario existe y corresponde a la empresa seleccionada.");
                return true;
            } else {
                System.out.println("El usuario no existe ó no corresponde a la empresa seleccionada.");
                eManager.getEntityManagerFactory().close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioyEmpresa: " + e);
            return false;
        }
    }

    @Override
    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e WHERE ck.EMPLEADO = e.SECUENCIA AND e.codigoempleado = ?";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            eManager.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario está registrado.");
                return true;
            } else {
                System.out.println("El usuario no esta registrado");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioRegistrado: " + e);
            return false;
        }
    }

    @Override
    public boolean validarEstadoUsuario(EntityManager eManager, String usuario) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e WHERE ck.EMPLEADO = e.SECUENCIA AND e.codigoempleado = ? AND ck.activo = 'N'";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            eManager.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario esta bloqueado.");
                return false;
            } else {
                System.out.println("El usuario esta activo");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarEstadoUsuario: " + e);
            return false;
        }
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave) {
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
                System.out.println("El usuario o clave son incorrectos");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuarioRegistrado: " + e);
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
