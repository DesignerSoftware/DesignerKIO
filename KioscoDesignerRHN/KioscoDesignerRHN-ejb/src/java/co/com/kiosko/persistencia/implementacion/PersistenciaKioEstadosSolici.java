package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin
 */
@Stateless
public class PersistenciaKioEstadosSolici implements IPersistenciaKioEstadosSolici {

    @EJB
    IPersistenciaEmpleados persistenciaEmpleados;

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpl(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpl()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.secuencia = :rfEmpleado "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpleado", secEmpleado);
            listaEstaSolici = query.getResultList();
            System.out.println("consultarEstadosXEmpl: resultado consul: " + listaEstaSolici.size());
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
//            throw e;
            throw new Exception(e);
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmplEsta(EntityManager em, BigDecimal secEmpleado, String estado) {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmplEsta()");
        System.out.println("consultarEstadosXEmplEsta-estado: " + estado);
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.secuencia = :rfEmpleado "
                    + "and e.estado = :estado "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpleado", secEmpleado);
            query.setParameter("estado", estado);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            return null;
        }
    }

    @Override
    public KioEstadosSolici consultarEstadosXSolici(EntityManager em, KioSoliciVacas solicitud) {
        System.out.println(this.getClass().getName() + ".consultarEstadosXSolici()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e from KioEstadosSolici e where e.kioSoliciVaca.secuencia = :rfSolicitud ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfSolicitud", solicitud.getSecuencia());
            listaEstaSolici = query.getResultList();
            return listaEstaSolici.get(0);
        } catch (Exception e) {
            System.out.println("error consultarEstadosXSolici: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void crearEstadoSolicitud(EntityManager em, KioSoliciVacas solicitud, BigDecimal secEmplEjecuta, String estado, String motivo) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        try {
            crearEstadoSolicitud(em, solicitud, secEmplEjecuta, estado, motivo, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void crearEstadoSolicitud(EntityManager em, KioSoliciVacas solicitud,
            BigDecimal secEmplEjecuta, String estado,
            String motivo, BigInteger secPersona) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearEstadoSolicitud()");
        KioEstadosSolici estadoSoli = new KioEstadosSolici(solicitud);
        System.out.println("crearEstadoSolicitud-estadoSoli: " + estadoSoli.getSecuencia());
        estadoSoli.setEstado(estado);
        if (secPersona != null && secPersona.compareTo(BigInteger.ZERO) > 0) {
            estadoSoli.setPersonaEjecuta(persistenciaEmpleados.consultaPersonaxSec(em,secPersona));
        } else {
            estadoSoli.setEmpleadoEjecuta(persistenciaEmpleados.consultaEmpleadoxSec(em, secEmplEjecuta));
        }
        estadoSoli.setMotivoProcesa(motivo);
        em.clear();
        try {
            System.out.println("guardando el nuevo estado");
            em.persist(estadoSoli);
            em.flush();
        } catch (EntityExistsException eee) {
            System.out.println("Error crearEstadoSolicitud: " + eee);
//            throw eee;
            throw new Exception(eee.toString());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearEstadoSolicitud: " + trle);
//            throw trle;
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error crearEstadoSolicitud: " + e);
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public KioEstadosSolici recargarEstadoSolicitud(EntityManager em, KioEstadosSolici estado) throws NoResultException, NonUniqueResultException, IllegalStateException, Exception {
        System.out.println(this.getClass().getName() + ".recargarEstadoSolicitud()");
        List lista = null;
        em.clear();
        estado.getEstado();
        String consulta = "select kns from KioEstadosSolici kns "
                + "where kns.empleadoEjecuta = :secEmpleado "
                + "and kns.fechaProcesamiento between CURRENT_DATE and :dtProcesamiento "
                + "and kns.estado = :estado "
                + "and kns.kioSoliciVaca.secuencia = :solicitud "
                + "order by kns.fechaProcesamiento ";
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", estado.getEmpleadoEjecuta());
            query.setParameter("dtProcesamiento", estado.getFechaProcesamiento(), TemporalType.TIMESTAMP);
            query.setParameter("estado", estado.getEstado());
            query.setParameter("solicitud", estado.getKioSoliciVaca().getSecuencia());
            lista = query.getResultList();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(estado.getFechaProcesamiento());
            Calendar c2 = Calendar.getInstance();
            int cont1 = 0;
            int cont2 = 0;
            for (Object elemento : lista) {
                if (elemento instanceof KioEstadosSolici) {
                    c2.setTime(((KioEstadosSolici) elemento).getFechaProcesamiento());
                    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                            && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                            && c1.get(Calendar.HOUR) == c2.get(Calendar.HOUR)
                            && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                            && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND)) {
                        estado = (KioEstadosSolici) elemento;
                    }
                } else {
                    cont1++;
                }
            }
            String msg = "";
            if (cont1 == lista.size()) {
                msg = "La lista obtenida no contiene los tipos de estados requeridos";
            }
            if (cont2 == lista.size()) {
                msg = "En la lista de estados no esta el estado requerido";
            }
            if (!"".equalsIgnoreCase(msg)) {
                throw new NoResultException(msg);
            }
            try {
                //Pregunta si la entidad esta en el contexto de persistencia.
                if (!em.contains(estado)) {
                    //Si no esta en el contexto de persistencia hace la consulta para obtenerla.
                    em.find(estado.getClass(), estado.getSecuencia());
                }
            } catch (IllegalArgumentException iae) {
                //Si es una entidad, hace la consulta para obtenerla.
                em.find(estado.getClass(), estado.getSecuencia());
            }
            return estado;
        } catch (NoResultException nre) {
            System.out.println("NoResultException");
            nre.printStackTrace();
            throw nre;
        } catch (NonUniqueResultException nure) {
            System.out.println("NonUniqueResultException");
            nure.printStackTrace();
            throw nure;
        } catch (IllegalStateException ise) {
            System.out.println("IllegalStateException");
            ise.printStackTrace();
            throw ise;
        }
    }

    public List consultarEstadoSolicitudes(EntityManager em) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSolicitudes()");
        List lista = null;
        em.clear();
        String consulta = "SELECT e "
                + "FROM KioEstadosSolici e "
                + "where e.secuencia = (select max(ei.secuencia) "
                + "from KIOESTADOSSOLICI ei "
                + "where ei.kiosolicivaca.secuencia = e.kiosolicivaca.secuencia ) "
                + "order by e.fechaProcesamiento ";
        try {
            Query query = em.createQuery(consulta);
            lista = query.getResultList();
//            lista = consultarEmpleadoEjecuta(em, lista);
            return lista;
        } catch (Exception e) {
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpre()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.empresa.secuencia = :rfEmpresa "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where e.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpresa", secEmpresa);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (RuntimeException re) {
//            throw re;
            throw new Exception(re.toString());
        } catch (Exception e) {
            System.out.println("consultarEstadosXEmpre:Excepcion " + e.getMessage());
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa, String estado) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpre()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.empresa.secuencia = :rfEmpresa "
                    + "and e.estado = :estado "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpresa", secEmpresa);
            query.setParameter("estado", estado);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("consultarEstadosXEmpre:Excepcion " + e.getMessage());
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa, String estado,
            BigDecimal secJefe) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpre()-2s");
        System.out.println("consultarEstadosXEmpre-secEmpresa: " + secEmpresa);
        System.out.println("consultarEstadosXEmpre-estado: " + estado);
        System.out.println("consultarEstadosXEmpre-secJefe: " + secJefe);
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.empresa.secuencia = :rfEmpresa "
                    + "and e.estado = :estado "
                    + "and e.kioSoliciVaca.empleadoJefe.secuencia = :rfJefe "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpresa", secEmpresa);
            query.setParameter("estado", estado);
            query.setParameter("rfJefe", secJefe);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("consultarEstadosXEmpre:Excepcion " + e.getMessage());
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa, String estado,
            BigInteger secPersona) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpre()-3");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e, Empresas em "
                    + "where em.secuencia = :rfEmpresa "
                    + "and e.kioSoliciVaca.empleado.empresa.secuencia = em.secuencia "
                    + "and e.estado = :estado "
                    + "and e.kioSoliciVaca.autorizador.secuencia = :rfautorizador "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) "
                    + "order by e.fechaProcesamiento ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpresa", secEmpresa);
            query.setParameter("estado", estado);
            query.setParameter("rfautorizador", secPersona);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("consultarEstadosXEmpre-3:Excepcion " + e.getMessage());
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public String registrarNovedad(EntityManager em, KioSoliciVacas solicitud) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".registrarNovedad()");
        System.out.println("registrarNovedad-solicitud: " + solicitud);
        em.clear();
        String resp = "";
        boolean res = false;
        try {
            System.out.println("solicitud: " + solicitud);
            StoredProcedureQuery query = em.createStoredProcedureQuery("KIOVACACIONES_PKG.REGISTRARNOVEDADVACACION");
            query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            query.setParameter(1, solicitud.getSecuencia());
            query.setParameter(2, resp);
            res = query.execute();
            query.hasMoreResults();
            resp = (String) query.getOutputParameterValue(2);
            return resp;
        } catch (EntityExistsException eee) {
            System.out.println("Error registrarNovedad: " + eee);
//            throw eee;
            throw new Exception(eee.toString());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error registrarNovedad: " + trle);
//            throw trle;
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error registrarNovedad: " + e);
//            throw e;
            throw new Exception(e.toString());
        }
    }
}
