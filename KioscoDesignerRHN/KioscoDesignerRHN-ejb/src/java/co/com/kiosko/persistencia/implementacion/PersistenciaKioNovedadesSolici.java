package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioNovedadesSolici;
import javax.ejb.Stateless;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioNovedadesSolici;
import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TransactionRequiredException;

/**
 *
 * @author Edwin
 */
@Stateless
public class PersistenciaKioNovedadesSolici implements IPersistenciaKioNovedadesSolici {

    @Override
    public void crearNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearNovedadSolici()");
        em.clear();
        try {
            em.persist(novedadSolici);
            em.flush();
        } catch (EntityExistsException eee) {
            System.out.println("Error crearNovedadSolici: " + eee);
//            throw eee;
            throw new Exception(eee.toString());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearNovedadSolici: " + trle);
//            throw trle;
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error crearNovedadSolici: " + e);
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public void modificarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".modificarNovedadSolici()");
        System.out.println("modificarNovedadSolici-novedadSolici: " + novedadSolici);
        em.clear();
        try {
            em.merge(novedadSolici);
            em.flush();
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error modificarNovedadSolici: " + trle);
//            throw trle;
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error modificarNovedadSolici: " + e);
            throw new Exception(e.toString());
        }
    }

    private void contarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) {
        String consulta = "select count(*) \n"
                + "from KioNovedadesSolici kns \n"
                + "where kns.empleado = ? \n";
        try {
            Query query = em.createNativeQuery(consulta);
            query.setParameter(1, novedadSolici.getEmpleado().getSecuencia());
            Object res = query.getSingleResult();
            System.out.println("Resultado del conteo: " + res);
            System.out.println("tipo: " + res.getClass().getName());
        } catch (Exception ex) {
            System.out.println("contarNovedadSolici.ex: " + ex);
        }
    }

    @Override
    public KioNovedadesSolici recargarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws NoResultException, NonUniqueResultException, IllegalStateException {
        System.out.println(this.getClass().getName() + ".recargarNovedadSolici()");
        em.clear();
        contarNovedadSolici(em, novedadSolici);
        String consulta;
        if (novedadSolici.getVacacion() == null) {
            consulta = "select kns from KioNovedadesSolici kns "
                    + "where kns.empleado.secuencia = :secEmpleado "
                    + "and kns.fechaInicialDisfrute = :dtInicialDis "
                    + "and kns.fechaSistema between CURRENT_DATE and :dtSistema "
                    + "and kns.dias = :dias "
                    + "and kns.subtipo = :subtipo "
                    + "and kns.adelantaPagoHasta = :dtPagoHasta "
                    + "order by kns.fechaInicialDisfrute ";
        } else {
            consulta = "select kns from KioNovedadesSolici kns "
                    + "where kns.empleado.secuencia = :secEmpleado "
                    + "and kns.fechaInicialDisfrute = :dtInicialDis "
                    + "and kns.fechaSistema between CURRENT_DATE and :dtSistema "
                    + "and kns.vacacion.rfVacacion = :vacacion "
                    + "and kns.dias = :dias "
                    + "and kns.subtipo = :subtipo "
                    + "and kns.adelantaPagoHasta = :dtPagoHasta "
                    + "order by kns.fechaInicialDisfrute ";
        }
        /*SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat formato2 = new SimpleDateFormat("ddMMyyyy HH:mm:ss.SSS");
        String txtFechaIniDisf = formato.format(novedadSolici.getFechaInicialDisfrute());
        String txtAdelantaPagoHasta = formato.format(novedadSolici.getAdelantaPagoHasta());
        String txtFechaSistema = formato2.format(novedadSolici.getFechaSistema());
        String consulta = "select kns.* \n"
                + "from KioNovedadesSolici kns \n"
                + "where kns.empleado = ? \n"
                + "and kns.fechaInicialDisfrute = to_date( ? ,'DDMMYYYY') \n"
                + "and kns.fechaSistema between trunc(SYSTIMESTAMP,'HH') and TO_TIMESTAMP( ? , 'DDMMYYYY HH24:MI:SS.FF') \n";
        if (novedadSolici.getVacacion() == null) {
            consulta = consulta + "and kns.vacacion is null \n";
        } else {
            consulta = consulta + "and kns.vacacion = ? \n";
        }
        consulta = consulta + "and kns.dias = ? \n"
                + "and kns.subtipo = ? \n"
                + "and kns.adelantaPagoHasta = to_date( ? ,'DDMMYYYY') \n"
                + "order by kns.fechaInicialDisfrute \n";*/
        List lista = new ArrayList();
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", novedadSolici.getEmpleado().getSecuencia());
            query.setParameter("dtInicialDis", novedadSolici.getFechaInicialDisfrute());
            query.setParameter("dtSistema", novedadSolici.getFechaSistema(), TemporalType.TIMESTAMP);
            if (novedadSolici.getVacacion() != null) {
                query.setParameter("vacacion", novedadSolici.getVacacion().getRfVacacion());
            }
            query.setParameter("dias", novedadSolici.getDias());
            query.setParameter("subtipo", novedadSolici.getSubtipo());
            query.setParameter("dtPagoHasta", novedadSolici.getAdelantaPagoHasta());
            /*Query query = em.createNativeQuery(consulta);
            query.setParameter(1, novedadSolici.getEmpleado().getSecuencia());
            query.setParameter(2, txtFechaIniDisf);
            query.setParameter(3, txtFechaSistema);
            if (novedadSolici.getVacacion() != null) {
                query.setParameter(4, novedadSolici.getVacacion().getRfVacacion());
                query.setParameter(5, novedadSolici.getDias());
                query.setParameter(6, novedadSolici.getSubtipo());
                query.setParameter(7, txtAdelantaPagoHasta);
            } else {
                query.setParameter(4, novedadSolici.getDias());
                query.setParameter(5, novedadSolici.getSubtipo());
                query.setParameter(6, txtAdelantaPagoHasta);
            }*/
            lista = query.getResultList();
            Calendar cl1 = Calendar.getInstance();
            cl1.setTime(novedadSolici.getFechaSistema());
            int cont1 = 0;
            int cont2 = 0;
            for (Object novedad : lista) {
                if (novedad instanceof KioNovedadesSolici) {
                    Calendar cl2 = Calendar.getInstance();
                    cl2.setTime(((KioNovedadesSolici) novedad).getFechaSistema());
                    System.out.println("recibido: " + cl1.get(Calendar.YEAR) + "/"
                            + cl1.get(Calendar.MONTH) + "/"
                            + cl1.get(Calendar.DAY_OF_MONTH) + " "
                            + cl1.get(Calendar.HOUR) + ":"
                            + cl1.get(Calendar.MINUTE) + ":"
                            + cl1.get(Calendar.SECOND));
                    System.out.println("extraido: " + cl2.get(Calendar.YEAR) + "/"
                            + cl2.get(Calendar.MONTH) + "/"
                            + cl2.get(Calendar.DAY_OF_MONTH) + " "
                            + cl2.get(Calendar.HOUR) + ":"
                            + cl2.get(Calendar.MINUTE) + ":"
                            + cl2.get(Calendar.SECOND));
                    if (cl1.get(Calendar.YEAR) == cl2.get(Calendar.YEAR)
                            && cl1.get(Calendar.MONTH) == cl2.get(Calendar.MONTH)
                            && cl1.get(Calendar.DAY_OF_MONTH) == cl2.get(Calendar.DAY_OF_MONTH)
                            && cl1.get(Calendar.HOUR) == cl2.get(Calendar.HOUR)
                            && cl1.get(Calendar.MINUTE) == cl2.get(Calendar.MINUTE)
                            && cl1.get(Calendar.SECOND) == cl2.get(Calendar.SECOND)) {
                        novedadSolici = (KioNovedadesSolici) novedad;
                    } else {
                        cont2++;
                    }
                } else {
                    cont1++;
                }
            }
            String msg = "";
            if (cont1 == lista.size()) {
                msg = "La lista obtenida no contiene los tipos de novedades requeridas";
            }
            if (cont2 == lista.size()) {
                msg = "En la lista de novedades no esta la novedad requerida";
            }
            if (!"".equalsIgnoreCase(msg)) {
                throw new NoResultException(msg);
            }
            try {
                //Pregunta si la entidad esta en el contexto de persistencia.
                if (!em.contains(novedadSolici)) {
                    //Si no esta en el contexto de persistencia hace la consulta para obtenerla.
                    em.find(novedadSolici.getClass(), novedadSolici.getSecuencia());
                }
            } catch (IllegalArgumentException iae) {
                //Si es una entidad, hace la consulta para obtenerla.
                em.find(novedadSolici.getClass(), novedadSolici.getSecuencia());
            }
            return novedadSolici;
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

    @Override
    public void removerNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws IllegalArgumentException, TransactionRequiredException, Exception {
        System.out.println(this.getClass().getName() + ".removerNovedadSolici()");
        em.clear();
        try {
            em.remove(novedadSolici);
        } catch (IllegalArgumentException iae) {
            throw new Exception(iae.toString());
        } catch (TransactionRequiredException tre) {
            throw new Exception(tre.toString());
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public BigDecimal consultaTraslapamientos(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaFinVaca) throws PersistenceException, NullPointerException, Exception {
        System.out.println(this.getClass().getName() + "." + "consultaTraslapamientos" + "()");
        String consulta = "SELECT "
                + "KIOVACACIONES_PKG.VERIFICARTRASLAPAMIENTO(?, ? , ? ) "
                + "FROM DUAL ";
        Query query = null;
        BigDecimal contTras = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaIniVaca, TemporalType.DATE);
            query.setParameter(3, fechaFinVaca, TemporalType.DATE);
            contTras = (BigDecimal) (query.getSingleResult());
            System.out.println("Resultado consulta traslapamiento: " + contTras);
            return contTras;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia en consultaTraslapamientos.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general en consultaTraslapamientos");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general en consultaTraslapamientos. " + e);
            throw new Exception(e.toString());
        }
    }
}
