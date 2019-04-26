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
    public void setearKiosko(EntityManager eManager) throws Exception {
        System.out.println(this.getClass().getName() + "." + "setearKiosko" + "()");
        try {
            setearKiosko(eManager, null);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    @Override
    public void setearKiosko(EntityManager eManager, String esquema) throws Exception {
        System.out.println(this.getClass().getName() + "." + "setearKiosko" + "()-2");
        try {
            String rol = "ROLKIOSKO";
            if (esquema != null && !esquema.isEmpty()) {
                rol = rol + esquema.toUpperCase();
            }
            //String sqlQuery = "SET ROLE ROLKIOSKO IDENTIFIED BY RLKSK ";
            String sqlQuery = "SET ROLE " + rol + " IDENTIFIED BY RLKSK ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.executeUpdate();
        } catch (NullPointerException npe) {
            System.out.println("PersistenciaConexionInicial.setearKiosko()-2");
            System.out.println("Error de nulo");
//            throw new Exception(npe);
            throw npe;
        } catch (Exception e) {
            System.out.println("PersistenciaConexionInicial.setearKiosko()-2 " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM EMPLEADOS e, Empresas em "
                    + "WHERE e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND em.nit = ? "
                    + "AND (EMPLEADOCURRENT_PKG.TipoTrabajadorCorte(e.secuencia, SYSDATE) = 'ACTIVO' "
                    + "OR EMPLEADOCURRENT_PKG.TipoTrabajadorCorte(e.secuencia, SYSDATE) = 'PENSIONADO')";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioyEmpresa: " + e);
            resultado = false;
//            throw new Exception(e);
            throw e;
        }
        return resultado;
    }

    @Override
    public boolean validarAutorizador(EntityManager eManager, String usuario) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) \n"
                    + "FROM KIOAUTORIZADORES KA, PERSONAS PER \n"
                    + "WHERE PER.SECUENCIA = KA.PERSONA \n"
                    + "AND PER.NUMERODOCUMENTO = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioyEmpresa: " + e);
            resultado = false;
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario, String nitEmpresa) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) "
                    + "FROM CONEXIONESKIOSKOS ck, PERSONAS per, EMPLEADOS e, EMPRESAS em "
                    //                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND e.persona = per.secuencia "
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
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) "
                    + "FROM CONEXIONESKIOSKOS ck, PERSONAS per "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND per.NUMERODOCUMENTO = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarUsuarioRegistrado-2: " + e);
            resultado = false;
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarEstadoUsuario(EntityManager eManager, String usuario, String nitEmpresa) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, PERSONAS per, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND per.secuencia = e.persona "
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
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarEstadoUsuario(EntityManager eManager, String usuario) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, PERSONAS per "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND ck.activo = 'N' "
                    + "AND per.numerodocumento = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = !(instancia > 0);
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarEstadoUsuario-2: " + e);
            resultado = false;
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) "
                    + "FROM CONEXIONESKIOSKOS ck, Personas per, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND per.secuencia = e.persona "
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
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave) throws Exception {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) "
                    + "FROM CONEXIONESKIOSKOS ck, Personas per "
                    + "WHERE ck.PERSONA = per.SECUENCIA "
                    + "AND per.numerodocumento = ? "
                    + "AND ck.PWD = GENERALES_PKG.ENCRYPT(?) ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, clave);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            resultado = instancia > 0;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuarioRegistrado: " + e);
            resultado = false;
            throw new Exception(e);
        }
        return resultado;
    }

    @Override
    public EntityManager validarConexionUsuario(EntityManagerFactory emf) throws Exception {
        try {
            EntityManager eManager = emf.createEntityManager();
            if (eManager.isOpen()) {
                return eManager;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarConexionUsuario : " + e);
            throw new Exception(e);
        } finally {
            try {
                emf.close();
            } catch (NullPointerException npe) {
                System.out.println("error de nulo en el entity manager.");
                throw new Exception(npe);
            }
        }
        return null;
    }
}
