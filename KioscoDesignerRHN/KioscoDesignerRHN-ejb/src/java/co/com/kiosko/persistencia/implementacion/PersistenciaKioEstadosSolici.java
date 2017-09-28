package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
//import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin
 */
@Stateless
public class PersistenciaKioEstadosSolici implements IPersistenciaKioEstadosSolici {

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
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpleado", secEmpleado);
            listaEstaSolici = query.getResultList();
            System.out.println("consultarEstadosXEmpl: resultado consul: "+ listaEstaSolici.size());
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadosXEmplEsta(EntityManager em, BigDecimal secEmpleado, String estado) {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpl()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            //em.getTransaction().begin();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.secuencia = :rfEmpleado "
                    + "and e.estado = :estado "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpleado", secEmpleado);
            query.setParameter("estado", estado);
            listaEstaSolici = query.getResultList();
            //em.getTransaction().commit();
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
            //em.getTransaction().begin();
            String consulta = "select e from KioEstadosSolici e where e.kioSoliciVaca.secuencia = :rfSolicitud ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfSolicitud", solicitud.getSecuencia());
            listaEstaSolici = query.getResultList();
            //em.getTransaction().commit();
            return listaEstaSolici.get(0);
        } catch (Exception e) {
            System.out.println("error consultarEstadosXSolici: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void crearEstadoSolicitud(EntityManager em, KioSoliciVacas solicitud, BigDecimal secEmplEjecuta, String estado) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearSolicitud()");
        KioEstadosSolici estadoSoli = new KioEstadosSolici(solicitud);
        estadoSoli.setEstado(estado);
        estadoSoli.setEmpleadoEjecuta(secEmplEjecuta);
        em.clear();
        try {
//            em.persist(estadoSoli);
            em.merge(estadoSoli);
            em.flush();
        } catch (EntityExistsException eee) {
            System.out.println("Error crearEstadoSolicitud: " + eee);
            throw eee;
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearEstadoSolicitud: " + trle);
            throw trle;
        } catch (Exception e) {
            System.out.println("Error crearEstadoSolicitud: " + e);
            throw e;
        }
    }

    public KioEstadosSolici recargarEstadoSolicitud(EntityManager em, KioEstadosSolici estado) throws NoResultException, NonUniqueResultException, IllegalStateException {
        System.out.println(this.getClass().getName() + ".recargarEstadoSolicitud()");
        List lista = new ArrayList();
        em.clear();
        estado.getEstado();
        String consulta = "select kns from KioEstadosSolici kns "
                + "where kns.empleadoEjecuta = :secEmpleado "
                + "and kns.fechaProcesamiento between CURRENT_DATE and :dtProcesamiento "
                + "and kns.estado = :estado "
                + "and kns.kioSoliciVaca.secuencia = :solicitud ";
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", estado.getEmpleadoEjecuta());
            query.setParameter("dtProcesamiento", estado.getFechaProcesamiento(), TemporalType.TIMESTAMP);
            query.setParameter("estado", estado.getEstado());
            query.setParameter("solicitud", estado.getKioSoliciVaca().getSecuencia());
//            solicitud = (KioSoliciVacas) query.getSingleResult();
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
        List lista = new ArrayList();
        em.clear();
        String consulta = "SELECT e "
                + "FROM KioEstadosSolici e "
                + "where e.secuencia = (select max(ei.secuencia) "
                + "from KIOESTADOSSOLICI ei "
                + "where ei.kiosolicivaca.secuencia = e.kiosolicivaca.secuencia ) ";
        try {
            Query query = em.createQuery(consulta);
            lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            throw e;
        }
    }
    @Override
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa ) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadosXEmpre()");
        List<KioEstadosSolici> listaEstaSolici;
        try {
            em.clear();
            String consulta = "select e "
                    + "from KioEstadosSolici e "
                    + "where e.kioSoliciVaca.empleado.empresa.secuencia = :rfEmpresa "
                    + "and e.secuencia = (select max(ei.secuencia) "
                    + "from KioEstadosSolici ei "
                    + "where ei.kioSoliciVaca.secuencia = e.kioSoliciVaca.secuencia) ";
            Query query = em.createQuery(consulta);
            query.setParameter("rfEmpresa", secEmpresa);
            listaEstaSolici = query.getResultList();
            return listaEstaSolici;
        } catch (Exception e) {
            System.out.println("consultarEstadosXEmpre:Excepcion " + e.getMessage());
            throw e;
        }
    }
}
