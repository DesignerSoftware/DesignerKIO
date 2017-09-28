package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Empleados;
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
//import javax.persistence.QueryTimeoutException;

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
//            eManager.getTransaction().begin();
            //System.out.println("Primera consulta");
            String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", codigoEmpleado);
            Empleados emp = (Empleados) query.getSingleResult();
//            eManager.getTransaction().commit();
            return emp;
        } catch (NonUniqueResultException nure) {
            System.out.println("Hay más de un empleado con el mismo código.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + nure);
//            eManager.getTransaction().rollback();
            try {
//                eManager.getTransaction().begin();
                //System.out.println("Segunda consulta");
                String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado AND e.empresa.nit = :nit ";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("codigoEmpleado", codigoEmpleado);
                query.setParameter("nit", nit);
                Empleados emp = (Empleados) query.getSingleResult();
//                eManager.getTransaction().commit();
                return emp;
            } catch (Exception e) {
                System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + e);
                try {
//                    eManager.getTransaction().rollback();
                } catch (NullPointerException npe) {
                    System.out.println("Error de nulo en la transacción.");
                }
                return null;
            }
        } catch (NullPointerException npe) {
            System.out.println("No hay empleado con el código dado.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + npe);
            try {
//                eManager.getTransaction().rollback();
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
            //em.getTransaction().begin();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpleado);
            fechaContrato = (Date) query.getSingleResult();
            //em.getTransaction().commit();
            return fechaContrato;
        } catch (Exception e) {
            /*if (em != null && em.getTransaction() != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }*/
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
            //em.getTransaction().begin();
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpresa);
            query.setParameter(2, secEmpleado);
            conteo = (BigDecimal) query.getSingleResult();
            System.out.println("Conteo de jefe: " + conteo);
            //em.getTransaction().commit();
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
//                + "and e.empresa.secuencia = :secEmpresa ";
        BigDecimal secEmplJefe;
        Empleados jefe;
        try {
            em.clear();
            Query query = em.createNativeQuery(consulta);
//            query.setParameter(1, secEmpresa);
            query.setParameter(1, secEmpleado);
            secEmplJefe = (BigDecimal) query.getSingleResult();
            try {
                Query query2 = em.createQuery(consulta2);
                query2.setParameter("secEmplJefe", secEmplJefe);
//                query2.setParameter("secEmpresa", secEmpresa);
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
        System.out.println(this.getClass().getName()+".consultarSecEmplEmpre()");
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
        System.out.println(this.getClass().getName()+".consultarEmpleadosEmpresa()");
        String sqlQuery = "select e "
                + "from Empleados e "
                + "where e.secuencia = :secEmpleado ";
        List<Empleados> empleados = new ArrayList<Empleados>();
        List<BigDecimal> secsEmpleados;
        try {
            secsEmpleados = consultarSecEmplEmpre(em, nit);
            for (int i = 0; i < secsEmpleados.size(); i++) {
                Query query = em.createQuery(sqlQuery);
                query.setParameter("secEmpleado", secsEmpleados.get(i));
                Object res = query.getSingleResult();
                if (res instanceof Empleados) {
                    empleados.add( (Empleados) res);
                } else {
                    throw new Exception("El resultado obtenido no es un empleado sec: " + secsEmpleados.get(i) );
                }
            }
            return empleados;
        } catch (PersistenceException pe) {
            throw pe;
        } catch (NullPointerException npe) {
            throw npe;
        } catch (IllegalStateException ise) {
            throw ise;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
