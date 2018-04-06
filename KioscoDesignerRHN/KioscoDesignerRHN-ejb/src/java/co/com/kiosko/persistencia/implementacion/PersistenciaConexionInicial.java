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
        System.out.println(this.getClass().getName() + "." + "setearKiosko" + "()");
        setearKiosko(eManager, null);
    }
    
    @Override
    public void setearKiosko(EntityManager eManager, String esquema) {
        System.out.println(this.getClass().getName()+"."+"setearKiosko"+"()-2");
        try {
            String rol = "ROLKIOSKO";
            if (esquema != null && !esquema.isEmpty()) {
                rol = rol+esquema.toUpperCase();
            }
            //String sqlQuery = "SET ROLE ROLKIOSKO IDENTIFIED BY RLKSK ";
            String sqlQuery = "SET ROLE "+ rol + " IDENTIFIED BY RLKSK ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.executeUpdate();
        } catch (NullPointerException npe) {
            System.out.println("PersistenciaConexionInicial.setearKiosko()-2");
            System.out.println("Error de nulo");
        } catch (Exception e){
            System.out.println("PersistenciaConexionInicial.setearKiosko()-2 "+e.getMessage());
        }
    }

    @Override
    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa) {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM EMPLEADOS e, Empresas em "
                    + "WHERE e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND em.nit = ? "
                    + "AND EMPLEADOCURRENT_PKG.TipoTrabajadorCorte(e.secuencia, SYSDATE)='ACTIVO' ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioyEmpresa: " + e);
            resultado = false;
        }
        return resultado;
    }

    @Override
    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario, String nitEmpresa) {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "AND e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND em.nit = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioRegistrado: " + e);
            resultado = false;
        }
        return resultado;
    }

    @Override
    public boolean validarEstadoUsuario(EntityManager eManager, String usuario, String nitEmpresa) {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "AND e.empresa = em.secuencia "
                    + "AND ck.activo = 'N' "
                    + "AND e.codigoempleado = ? "
                    + "AND em.nit = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = !(instancia > 0);
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarEstadoUsuario: " + e);
            resultado = false;
        }
        return resultado;
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa) {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "AND e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND ck.PWD = GENERALES_PKG.ENCRYPT(?) "
                    + "AND em.nit = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, clave);
            query.setParameter(3, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuarioRegistrado: " + e);
            resultado = false;
        }
        return resultado;
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
            try {
                emf.close();
            } catch (NullPointerException npe) {
                System.out.println("error de nulo en el entity manager.");
            }
        }
        return null;
    }
}
