package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Empresas;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaEmpleados implements IPersistenciaEmpleados {

    @Override
    public Empleados consultarEmpleado(EntityManager eManager, BigInteger codigoEmpleado, long nit) {
        //System.out.println(this.getClass().getName() + ".consultarEmpleado()");
        //System.out.println("codigoEmpleado: " + codigoEmpleado);
        //System.out.println("nit: " + nit);
        try {
            String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", codigoEmpleado);
            Empleados emp = (Empleados) query.getSingleResult();
            return emp;
        } catch (NonUniqueResultException nure) {
            System.out.println("Hay más de un empleado con el mismo código.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + nure);
            try {
                String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado AND e.empresa.nit = :nit ";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("codigoEmpleado", codigoEmpleado);
                query.setParameter("nit", nit);
                Empleados emp = (Empleados) query.getSingleResult();
                return emp;
            } catch (Exception e) {
                System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + e);
                try {
                } catch (NullPointerException npe) {
                    System.out.println("Error de nulo en la transacción.");
                }
                return null;
            }
        } catch (NullPointerException npe) {
            System.out.println("No hay empleado con el código dado.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + npe);
            try {
            } catch (NullPointerException npe2) {
                System.out.println("Error 2 de nulo en la transacción");
            }
            return null;
        }
    }

    @Override
    public Date fechaContratoEmpl(EntityManager em, BigDecimal secEmpleado) {
        System.out.println(this.getClass().getName() + ".fechaContratoEmpl()");
        Date fechaContrato;
        String sqlQuery = "SELECT empleadocurrent_pkg.fechatipocontrato(e.secuencia, sysdate ) FROM Empleados e WHERE e.secuencia = ? ";
        try {
            em.clear();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpleado);
            fechaContrato = (Date) query.getSingleResult();
            return fechaContrato;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean esJefe(EntityManager em, BigDecimal secEmpleado, BigInteger secEmpresa) {
        System.out.println(this.getClass().getName() + ".esJefe()");
        System.out.println("Parametro empleado: " + secEmpleado);
        System.out.println("Parametro empresa: " + secEmpresa);
        String sqlQuery = "select count(*) \n"
                + "from vigenciascargos vc, vigenciastipostrabajadores vtt, "
                + "tipostrabajadores tt, estructuras es, organigramas o, empresas em \n"
                + "where \n"
                + "vc.estructura = es.secuencia \n"
                + "and es.organigrama = o.secuencia \n"
                + "and o.empresa = em.secuencia \n"
                + "and vc.empleado = vtt.empleado \n"
                + "and vtt.tipotrabajador = tt.secuencia \n"
                + "and tt.tipo IN ('ACTIVO', 'PENSIONADO') \n"
                + "and em.secuencia = ? \n"
                + "and vc.empleadojefe = ? \n"
                + "and vtt.fechavigencia = (select max(vtti.fechavigencia) \n"
                + "                        from vigenciastipostrabajadores vtti \n"
                + "                        where vtti.empleado = vtt.empleado \n"
                + "                        and vtti.fechavigencia <= sysdate) \n"
                + "and vc.fechavigencia = (select max(vci.fechavigencia) \n"
                + "                        from vigenciascargos vci \n"
                + "                        where vci.empleado = vc.empleado \n"
                + "                        and vci.fechavigencia <= sysdate) ";
        BigDecimal conteo = BigDecimal.ZERO;
        try {
            em.clear();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpresa);
            query.setParameter(2, secEmpleado);
            conteo = (BigDecimal) query.getSingleResult();
            System.out.println("Conteo de jefe: " + conteo);
            return conteo.compareTo(BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public Empleados consutarJefe(EntityManager em, BigDecimal secEmpleado, BigInteger secEmpresa) throws Exception {
        String consulta = "select empj.secuencia "
                + "from empleados empl, empresas em, vigenciascargos vc, estructuras es, organigramas o, empleados empj, personas pj "
                + "where em.secuencia = empl.empresa "
                + "and vc.empleado = empl.secuencia "
                + "and es.secuencia = vc.estructura "
                + "and o.secuencia = es.organigrama "
                + "and em.secuencia = o.empresa "
                + "and vc.empleadojefe = empj.secuencia "
                + "and pj.secuencia = empj.persona "
                + "and empl.secuencia = ? "
                + "and vc.fechavigencia = (select max(vci.fechavigencia) "
                + "                        from vigenciascargos vci "
                + "                        where vci.empleado = vc.empleado "
                + "                        and vci.fechavigencia <= sysdate) ";
        String consulta2 = "select e from Empleados e "
                + "where e.secuencia = :secEmplJefe ";
        BigDecimal secEmplJefe;
        Empleados jefe;
        try {
            em.clear();
            Query query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            secEmplJefe = (BigDecimal) query.getSingleResult();
            try {
                Query query2 = em.createQuery(consulta2);
                query2.setParameter("secEmplJefe", secEmplJefe);
                jefe = (Empleados) query2.getSingleResult();
                return jefe;
            } catch (Exception e2) {
                System.out.println("Error consutarJefe 2: " + e2);
                throw e2;
            }
        } catch (Exception e) {
            System.out.println("Error consutarJefe: " + e);
            throw e;
        }
    }

    private List<BigDecimal> consultarSecEmplEmpre(EntityManager em, long nit) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarSecEmplEmpre()");
        String sqlQuery = "select e.secuencia "
                + "from Empleados e "
                + "where e.empresa = (select secuencia from empresas where nit = ? ) "
                + "and exists (select vtt.secuencia "
                + "  from vigenciastipostrabajadores vtt, tipostrabajadores tt "
                + "  where vtt.tipotrabajador = tt.secuencia "
                + "  and tt.tipo in ('ACTIVO', 'PENSIONADO') "
                + "  and vtt.fechavigencia = (select max(vtti.fechavigencia) "
                + "    from vigenciastipostrabajadores vtti "
                + "    where vtti.empleado = vtt.empleado "
                + "    and vtti.fechavigencia <= sysdate) "
                + "  and vtt.empleado = e.secuencia )";
        try {
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, nit);
            List<BigDecimal> secsEmpleados = query.getResultList();
            return secsEmpleados;
        } catch (PersistenceException pe) {
            throw pe;
        } catch (IllegalStateException ise) {
            throw ise;
        }
    }

    @Override
    public List consultarEmpleadosEmpresa(EntityManager em, long nit) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEmpleadosEmpresa()");
        String sqlQuery = "select e.SECUENCIA, e.CODIGOEMPLEADO, "
                + "e.FECHACREACION, e.CODIGOALTERNATIVO, e.USUARIOBD, e.PERSONA, "
                + "e.EMPRESA "
                + "from Empleados e "
                + "where e.empresa = (select secuencia from empresas where nit = ? ) "
                + "and exists (select vtt.secuencia "
                + "  from vigenciastipostrabajadores vtt, tipostrabajadores tt "
                + "  where vtt.tipotrabajador = tt.secuencia "
                + "  and tt.tipo in ('ACTIVO', 'PENSIONADO') "
                + "  and vtt.fechavigencia = (select max(vtti.fechavigencia) "
                + "    from vigenciastipostrabajadores vtti "
                + "    where vtti.empleado = vtt.empleado "
                + "    and vtti.fechavigencia <= sysdate) "
                + "  and vtt.empleado = e.secuencia )";
        List<Empleados> empleados = new ArrayList();
        try {
            Query query = em.createNativeQuery(sqlQuery, Empleados.class);
            query.setParameter(1, nit);
            empleados = query.getResultList();
            return empleados;
        } catch (PersistenceException pe) {
            throw pe;
        } catch (IllegalStateException ise) {
            throw ise;
        }
    }

    @Override
    public List consultarEmpleadosXJefe(EntityManager em, long nit, BigDecimal secJefe) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEmpleadosXJefe()");
        String sqlQuery = "select empl.SECUENCIA, empl.CODIGOEMPLEADO, "
                + "empl.FECHACREACION, empl.CODIGOALTERNATIVO, empl.USUARIOBD, empl.PERSONA, "
                + "empl.EMPRESA "
                + "from empleados empl, "
                + "vigenciascargos vc, estructuras es, organigramas o, empresas em, "
                + "empleados jefe "
                + "where "
                + "empl.secuencia = vc.empleado "
                + "and vc.estructura = es.secuencia "
                + "and es.organigrama = o.secuencia "
                + "and o.empresa = em.secuencia "
                + "and vc.empleadojefe = jefe.secuencia "
                + "and vc.fechavigencia = (select max(vci.fechavigencia) "
                + "from vigenciascargos vci "
                + "where vci.empleado = vc.empleado "
                + "and vci.fechavigencia <= sysdate) "
                + "and exists (select vtt.secuencia "
                + "from vigenciastipostrabajadores vtt, tipostrabajadores tt "
                + "where vtt.tipotrabajador = tt.secuencia "
                + "and tt.tipo in ('ACTIVO', 'PENSIONADO') "
                + "and vtt.fechavigencia = (select max(vtti.fechavigencia) "
                + "from vigenciastipostrabajadores vtti "
                + "where vtti.empleado = vtt.empleado "
                + "and vtti.fechavigencia <= sysdate) "
                + "and vtt.empleado = empl.secuencia )"
                + "and em.nit = ? "
                + "and jefe.secuencia = ? ";
        List<Empleados> empleados = new ArrayList<Empleados>();
        try {
            Query query = em.createNativeQuery(sqlQuery, Empleados.class);
            query.setParameter(1, nit);
            query.setParameter(2, secJefe);
            empleados = query.getResultList();
            return empleados;
        } catch (PersistenceException pe) {
            System.out.println("consultarEmpleadosXJefe-PersistenceException");
            throw pe;
        } catch (IllegalStateException ise) {
            System.out.println("consultarEmpleadosXJefe-IllegalStateException");
            throw ise;
        } catch (Exception e) {
            System.out.println("consultarEmpleadosXJefe-Exception");
            throw e;
        }
    }

    @Override
    public Empleados consultaEmpleadoxSec(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + ".consultaEmpleadoxSec()");
        String sqlQuery = "select e "
                + "from Empleados e "
                + "where e.secuencia = :secEmpleado ";
        Empleados empleado = null;
        try {
            Query query = em.createQuery(sqlQuery);
            query.setParameter("secEmpleado", secEmpleado);
            Object res = query.getSingleResult();
            if (res instanceof Empleados) {
                empleado = (Empleados) res;
            } else {
                throw new Exception("El resultado obtenido no es un empleado sec: " + secEmpleado);
            }
            return empleado;
        } catch (PersistenceException pe) {
            throw pe;
        } catch (NullPointerException npe) {
            throw npe;
        } catch (IllegalStateException ise) {
            throw ise;
        }
    }

    @Override
    public Personas consultarPersona(EntityManager eManager, BigInteger numeroDocumento) {
        try {
            String sqlQuery = "SELECT per FROM Personas per WHERE per.numeroDocumento = :numeroDocumento";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("numeroDocumento", numeroDocumento);
            Personas per = (Personas) query.getSingleResult();
            return per;
        } catch (NonUniqueResultException nure) {
            System.out.println("Hay más de una persona con el mismo numero de documento.");
            System.out.println("Error PersistenciaEmpleados.consultarPersona: " + nure);
            try {
                String sqlQuery = "SELECT per FROM Personas per WHERE per.numeroDocumento = :numeroDocumento ";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("numeroDocumento", numeroDocumento);
                List personas = query.getResultList();
                return (Personas) personas.get(0);
            } catch (Exception e) {
                System.out.println("Error PersistenciaEmpleados.consultarPersona: " + e);
                try {
                } catch (NullPointerException npe) {
                    System.out.println("consultarPersona: Error de nulo en la transacción.");
                }
                return null;
            }
        } catch (NullPointerException npe) {
            System.out.println("consultarPersona: No hay persona con el código dado.");
            System.out.println("Error PersistenciaEmpleados.consultarPersona: " + npe);
            try {
            } catch (NullPointerException npe2) {
                System.out.println("consultarPersona: Error 2 de nulo en la transacción");
            }
            return null;
        }
    }

    @Override
    public Empleados consultarEmpleadoXPersoEmpre(EntityManager em, BigInteger numeroDocumento, long nit) {
        String consulta = "select empl.* "
                + "from personas per, empleados empl, empresas em, vigenciascargos vc, estructuras es, organigramas o "
                + "where per.secuencia = empl.persona "
                + "and em.secuencia = empl.empresa "
                + "and vc.empleado = empl.secuencia "
                + "and es.secuencia = vc.estructura "
                + "and o.secuencia = es.organigrama "
                + "and em.secuencia = o.empresa "
                + "and per.numerodocumento = ? "
                + "and em.nit = ? "
                + "and vc.fechavigencia = (select max(vci.fechavigencia) "
                + "                        from vigenciascargos vci "
                + "                        where vci.empleado = vc.empleado "
                + "                        and vci.fechavigencia <= sysdate) ";
        Empleados empleado = null;
        try {
            Query query = em.createNativeQuery(consulta, Empleados.class);
            query.setParameter(1, numeroDocumento);
            query.setParameter(2, nit);
            empleado = (Empleados) query.getSingleResult();
        } catch (PersistenceException pe) {
            throw pe;
        } catch (IllegalStateException ise) {
            throw ise;
        }
        return empleado;
    }

    public boolean esAutorizador(EntityManager em, BigDecimal rfPersona) {
        String sqlQuery = "select count(*) "
                + "from kioautorizadores ka "
                + "where ka.persona = ? ";
        BigDecimal conteo = BigDecimal.ZERO;
        try {
            em.clear();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, rfPersona);
            conteo = (BigDecimal) query.getSingleResult();
            System.out.println("Conteo de autorizador: " + conteo);
            return conteo.compareTo(BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            System.out.println("esAutorizador:Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public BigInteger consultarEmpresaXNit(EntityManager em, long nit) {
        String sqlQuery = "select secuencia "
                + "from empresas e "
                + "where e.nit = ? ";
        BigInteger secuencia = BigInteger.ZERO;
        try {
            em.clear();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, nit);
            Object resul = query.getSingleResult();
            if (resul instanceof BigInteger) {
                secuencia = (BigInteger) resul;
            } else if (resul instanceof BigDecimal) {
                secuencia = ((BigDecimal) resul).toBigInteger();
            }
            System.out.println("secuencia empresa: " + secuencia);
            return secuencia;
        } catch (Exception e) {
            System.out.println("consultarEmpresaXNit:Error " + e.getMessage());
            return secuencia;
        }
    }

    @Override
    public Empresas consultarEmpresa(EntityManager em, long nit) {
        String sqlQuery = "select e.* "
                + "from empresas e "
                + "where e.nit = ? ";
        Empresas empresa = null;
        try {
            em.clear();
            Query query = em.createNativeQuery(sqlQuery, Empresas.class);
            query.setParameter(1, nit);
            Object resul = query.getSingleResult();
            if (resul instanceof Empresas) {
                empresa = (Empresas) resul;
            } else {
                empresa = null;
            }
        } catch (Exception e) {
            System.out.println("consultarEmpresa:excepcion: " + e);
        }
        return empresa;
    }

    @Override
    public List consultarEmpleadosXAutorizador(EntityManager em, long nit, BigInteger secPersona) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEmpleadosXAutorizador()");
        String sqlQuery = "select empl.* "
                + "from empleados empl, "
                + "vigenciascargos vc, estructuras es, organigramas o, empresas em, "
                + "kioautorizadores ka, KIOAUTORIZASOLICIVACAS KASV "
                + "where "
                + "empl.secuencia = vc.empleado "
                + "and vc.estructura = es.secuencia "
                + "and es.organigrama = o.secuencia "
                + "and o.empresa = em.secuencia "
                + "and kasv.empleado = empl.secuencia "
                + "and kasv.kioautorizador = ka.secuencia "
                + "and vc.fechavigencia = (select max(vci.fechavigencia) "
                + "from vigenciascargos vci "
                + "where vci.empleado = vc.empleado "
                + "and vci.fechavigencia <= sysdate) "
                + "and exists (select vtt.secuencia "
                + "from vigenciastipostrabajadores vtt, tipostrabajadores tt "
                + "where vtt.tipotrabajador = tt.secuencia "
                + "and tt.tipo in ('ACTIVO', 'PENSIONADO') "
                + "and vtt.fechavigencia = (select max(vtti.fechavigencia) "
                + "from vigenciastipostrabajadores vtti "
                + "where vtti.empleado = vtt.empleado "
                + "and vtti.fechavigencia <= sysdate) "
                + "and vtt.empleado = empl.secuencia ) "
                + "and em.nit = ? "
                + "and ka.persona = ? ";
        List<Empleados> empleados = new ArrayList();
        try {
            Query query = em.createNativeQuery(sqlQuery, Empleados.class);
            query.setParameter(1, nit);
            query.setParameter(2, secPersona);
            empleados = query.getResultList();
            return empleados;
        } catch (PersistenceException pe) {
            System.out.println("consultarEmpleadosXAutorizador-PersistenceException");
            throw pe;
        } catch (IllegalStateException ise) {
            System.out.println("consultarEmpleadosXAutorizador-IllegalStateException");
            throw ise;
        } catch (Exception e) {
            System.out.println("consultarEmpleadosXAutorizador-Exception");
            throw e;
        }
    }

    @Override
    public Personas consultaPersonaxSec(EntityManager em, BigInteger secPersona) throws Exception {
        System.out.println(this.getClass().getName() + ".consultaPersonaxSec()");
        String sqlQuery = "select e "
                + "from Personas e "
                + "where e.secuencia = :secPersona ";
        Personas persona = null;
        try {
            Query query = em.createQuery(sqlQuery);
            query.setParameter("secPersona", secPersona);
            Object res = query.getSingleResult();
            if (res instanceof Personas) {
                persona = (Personas) res;
            } else {
                throw new Exception("El resultado obtenido no es una persona sec: " + secPersona);
            }
            return persona;
        } catch (PersistenceException pe) {
            throw pe;
        } catch (NullPointerException npe) {
            throw npe;
        } catch (IllegalStateException ise) {
            throw ise;
        }
    }

    @Override
    public Personas consutarAutorizador(EntityManager em, BigDecimal secEmpleado, BigInteger secEmpresa) throws Exception {
        String consulta = "select per.* "
                + "from empleados empl, kioautorizadores ka, kioautorizasolicivacas kasv, personas per "
                + "where empl.secuencia = kasv.empleado "
                + "and kasv.kioautorizador = ka.secuencia "
                + "and per.secuencia = ka.persona "
                + "and empl.secuencia = ? ";
        try {
            em.clear();
            Query query = em.createNativeQuery(consulta, Personas.class);
            query.setParameter(1, secEmpleado);
            Personas persona = (Personas) query.getSingleResult();
            return persona;
        } catch (Exception e) {
            System.out.println("Error consutarAutorizador: " + e);
//            throw e;
            throw new Exception(e);
        }
    }
}
