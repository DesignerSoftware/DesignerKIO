package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioNovedadesSolici;
import javax.ejb.Stateless;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioNovedadesSolici;
import java.math.BigDecimal;
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
        System.out.println("modificarNovedadSolici-novedadSolici: "+novedadSolici);
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

    @Override
    public KioNovedadesSolici recargarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws NoResultException, NonUniqueResultException, IllegalStateException {
        System.out.println(this.getClass().getName() + ".recargarNovedadSolici()");
        em.clear();
        String consulta = "select kns from KioNovedadesSolici kns "
                + "where kns.empleado.secuencia = :secEmpleado "
                + "and kns.fechaInicialDisfrute = :dtInicialDis "
                + "and kns.fechaSistema between CURRENT_DATE and :dtSistema "
                + "and kns.vacacion.rfVacacion = :vacacion "
                + "and kns.dias = :dias "
                + "and kns.subtipo = :subtipo "
                + "and kns.adelantaPagoHasta = :dtPagoHasta "
                + "order by kns.fechaInicialDisfrute ";
        List lista = new ArrayList();
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", novedadSolici.getEmpleado().getSecuencia());
            query.setParameter("dtInicialDis", novedadSolici.getFechaInicialDisfrute());
            query.setParameter("dtSistema", novedadSolici.getFechaSistema(), TemporalType.TIMESTAMP);
            query.setParameter("vacacion", novedadSolici.getVacacion().getRfVacacion());
            query.setParameter("dias", novedadSolici.getDias());
            query.setParameter("subtipo", novedadSolici.getSubtipo());
            query.setParameter("dtPagoHasta", novedadSolici.getAdelantaPagoHasta());
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
                if ( !em.contains(novedadSolici) ){
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
            System.out.println("Resultado consulta traslapamiento: "+contTras);
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
