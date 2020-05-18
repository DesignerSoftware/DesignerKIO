package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioSolicisLocaliza;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateless
public class PersistenciaKioSolicisLocaliza implements IPersistenciaKioSolicisLocaliza {

    @Override
    public void crearSolicitud(EntityManager em, KioSolicisLocaliza solicitud) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearSolicitud()");
        em.clear();
        try {
            em.merge(solicitud);
            em.flush();
            System.out.println(solicitud.toString());
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Excepcion: " + e.getMessage());
            throw new Exception(e.toString());
        }
    }
    
    @Override
    public KioSolicisLocaliza recargarSolicitud(EntityManager em, KioSolicisLocaliza solicitud) throws NoResultException, NonUniqueResultException, IllegalStateException {
        List lista = new ArrayList();
        em.clear();
        String consulta = "select ksl from KioSolicisLocaliza ksl "
                + "where ksl.empleado.secuencia = :secEmpleado "
                + "and ksl.fechaGeneracion between CURRENT_DATE and :dtGeneracion "
                + "and ksl.activa = :activa "
                + "and ksl.usuario = :usuario "
                ;
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", solicitud.getEmpleado().getSecuencia());
            query.setParameter("dtGeneracion", solicitud.getFechaGeneracion(), TemporalType.TIMESTAMP);
            query.setParameter("activa", solicitud.getActiva());
            query.setParameter("usuario", solicitud.getUsuario());
            lista = query.getResultList();
            
            System.out.println("Tamagno de la lista de solicitudes de localizaciones: " + lista.size());
            Calendar c1 = Calendar.getInstance();
            c1.setTime(solicitud.getFechaGeneracion());
            Calendar c2 = Calendar.getInstance();
            int cont1 = 0;
            int cont2 = 0;
            System.out.println("recibido: " + c1.get(Calendar.YEAR) + "/"
                    + c1.get(Calendar.MONTH) + "/"
                    + c1.get(Calendar.DAY_OF_MONTH) + " "
                    + c1.get(Calendar.HOUR) + ":"
                    + c1.get(Calendar.MINUTE) + ":"
                    + c1.get(Calendar.SECOND));

            for (int i = 0; i < lista.size(); i++) {
                System.out.println("Abrio el for");
                if (lista.get(i) instanceof KioSolicisLocaliza) {
                    c2.setTime(((KioSolicisLocaliza) lista.get(i)).getFechaGeneracion());
                    System.out.println("extraido: " + c2.get(Calendar.YEAR) + "/"
                            + c2.get(Calendar.MONTH) + "/"
                            + c2.get(Calendar.DAY_OF_MONTH) + " "
                            + c2.get(Calendar.HOUR) + ":"
                            + c2.get(Calendar.MINUTE) + ":"
                            + c2.get(Calendar.SECOND));
                    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                            && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                            && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                            && c1.get(Calendar.HOUR) == c2.get(Calendar.HOUR)
                            && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                            && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND)) {
                        System.out.println("La encontro");
                        solicitud = (KioSolicisLocaliza) lista.get(i);
                        System.out.println("secuencia: " + solicitud.getSecuencia());
                    } else {
                        cont2++;
                    }
                } else {
                    cont1++;
                }
            }
            String msg = "";
            if (cont1 == lista.size()) {
                msg = "La lista obtenida no contiene los tipos de solicitudes de localizaciones requeridas";
            }
            if (cont2 == lista.size()) {
                msg = "En la lista de solicitudes no esta la solicitud de localizaciones requerida";
            }
            if (!"".equalsIgnoreCase(msg)) {
                throw new NoResultException(msg);
            }
            try {
                //Pregunta si la entidad esta en el contexto de persistencia.
                if (!em.contains(solicitud)) {
                    //Si no esta en el contexto de persistencia hace la consulta para obtenerla.
                    System.out.println("Solicitud de localizaciones: No esta en el contexto de persistencia");
                    em.find(solicitud.getClass(), solicitud.getSecuencia());
                } else {
                    System.out.println("Solicitud de localizaciones: Si esta en el contexto de persistencia");
                }
            } catch (IllegalArgumentException iae) {
                //Si es una entidad, hace la consulta para obtenerla.
                System.out.println("Solicitud de localizaciones: Tuvo que buscarla de nuevo.");
                em.find(solicitud.getClass(), solicitud.getSecuencia());
            }
            return solicitud;
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
}
