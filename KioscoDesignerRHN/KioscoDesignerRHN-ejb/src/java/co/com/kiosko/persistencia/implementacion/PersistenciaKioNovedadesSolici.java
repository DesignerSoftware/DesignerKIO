/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioNovedadesSolici;
import javax.ejb.Stateless;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioNovedadesSolici;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
            throw eee;
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearNovedadSolici: " + trle);
            throw trle;
        } catch (Exception e) {
            System.out.println("Error crearNovedadSolici: " + e);
            throw e;
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
                + "and kns.adelantaPagoHasta = :dtPagoHasta ";
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
//            novedadSolici = (KioNovedadesSolici) query.getSingleResult();
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

    public void removerNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws IllegalArgumentException, TransactionRequiredException, Exception {
        System.out.println(this.getClass().getName() + ".removerNovedadSolici()");
        em.clear();
        try {
            em.remove(novedadSolici);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (TransactionRequiredException tre) {
            throw tre;
        } catch (Exception e) {
            throw e;
        }
    }
}
