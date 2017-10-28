package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioSoliciVacas;
import javax.ejb.Stateless;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioSoliciVacas;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin
 */
@Stateless
public class PersistenciaKioSoliciVacas implements IPersistenciaKioSoliciVacas {

    @Override
    public void crearSolicitud(EntityManager em, KioSoliciVacas solicitud) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearSolicitud()");
        em.clear();
        try {
            em.merge(solicitud);
            em.flush();
            System.out.println("Creacion de la solicitud completada.");
            System.out.println(solicitud.toString());
        } catch (Exception e) {
            System.out.println("crearSolicitud Excepcion: " + e.getMessage());
            throw new Exception(e.toString());
        }
    }

    @Override
    public KioSoliciVacas recargarSolicitud(EntityManager em, KioSoliciVacas solicitud) throws NoResultException, NonUniqueResultException, IllegalStateException {
        System.out.println(this.getClass().getName() + ".recargarSolicitud()");
        System.out.println("recibido");
        System.out.println("secEmpleado: " + solicitud.getEmpleado().getSecuencia());
        System.out.println("dtGeneracion: " + solicitud.getFechaGeneracion());
        System.out.println("novedad: " + solicitud.getKioNovedadesSolici().getSecuencia());
        System.out.println("activa: " + solicitud.getActiva());
        List lista = new ArrayList();
        em.clear();
        String consulta = "select kns from KioSoliciVacas kns "
                + "where kns.empleado.secuencia = :secEmpleado "
                + "and kns.fechaGeneracion between CURRENT_DATE and :dtGeneracion "
                + "and kns.kioNovedadSolici.secuencia = :novedad "
                + "and kns.activa = :activa ";
        try {
            Query query = em.createQuery(consulta);
            query.setParameter("secEmpleado", solicitud.getEmpleado().getSecuencia());
            query.setParameter("dtGeneracion", solicitud.getFechaGeneracion(), TemporalType.TIMESTAMP);
            query.setParameter("novedad", solicitud.getKioNovedadesSolici().getSecuencia());
            query.setParameter("activa", solicitud.getActiva());
            lista = query.getResultList();
            System.out.println("Tamagno de la lista de solicitudes: " + lista.size());
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
                if (lista.get(i) instanceof KioSoliciVacas) {
                    c2.setTime(((KioSoliciVacas) lista.get(i)).getFechaGeneracion());
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
                        solicitud = (KioSoliciVacas) lista.get(i);
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
                msg = "La lista obtenida no contiene los tipos de solicitudes requeridas";
            }
            if (cont2 == lista.size()) {
                msg = "En la lista de solicitudes no esta la solicitud requerida";
            }
            if (!"".equalsIgnoreCase(msg)) {
                throw new NoResultException(msg);
            }
            try {
                //Pregunta si la entidad esta en el contexto de persistencia.
                if (!em.contains(solicitud)) {
                    //Si no esta en el contexto de persistencia hace la consulta para obtenerla.
                    System.out.println("No esta en el contexto de persistencia");
                    em.find(solicitud.getClass(), solicitud.getSecuencia());
                } else {
                    System.out.println("Si esta en el contexto de persistencia");
                }
            } catch (IllegalArgumentException iae) {
                //Si es una entidad, hace la consulta para obtenerla.
                System.out.println("Tuvo que buscarla de nuevo.");
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

    @Override
    public void modificarSolicitud(EntityManager em, KioSoliciVacas solicitud) throws Exception {
        System.out.println(this.getClass().getName() + ".modificarSolicitud()");
        em.clear();
        try {
            em.merge(solicitud);
        } catch (Exception e) {
            System.out.println("modificarSolicitud Excepcion: " + e.getMessage());
            throw new Exception(e.toString());
        }
    }

    @Override
    public List consultaSolicitudesEnviadas(EntityManager em) throws Exception {
        System.out.println(this.getClass().getName() + ".consultaSolicitudesEnviadas()");
        String consulta = "select s from KioSoliciVacas s "
                + "where s.activa = 'S' "
                + "and (s.fechaVencimiento is null or s.fechaVencimiento >= CURRENT_DATE "
                + "and exists (select ei from KioEstadosSolici ei where ei.estado = 'ENVIADO' "
                + "and ei.kioSoliciVaca.secuencia = s.secuencia ) ";
        List resultado;
        try {
            em.clear();
            Query query = em.createQuery(consulta);
            resultado = query.getResultList();
            return resultado;
        } catch (Exception e) {
//            throw e;
            throw new Exception(e.toString());
        }
    }

    @Override
    public BigDecimal verificaExistenciaSolicitud(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca) throws Exception {
        System.out.println(this.getClass().getName() + ".verificaExistenciaSolicitud()");
        System.out.println("verificaExistenciaSolicitud-secEmpleado: " + secEmpleado);
        System.out.println("verificaExistenciaSolicitud-fechaIniVaca: " + fechaIniVaca);
        SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
        String txtFecha = formato.format(fechaIniVaca);
        String consulta = "SELECT "
                + "KIOVACACIONES_PKG.VERIFICAEXISTESOLICITUD(?, to_date(?,'DDMMYYYY') ) "
                + "FROM DUAL ";
        System.out.println("verificaExistenciaSolicitud-consulta: " + consulta);
        Query query = null;
        BigDecimal conteo = null;
        try {
            try {
                query = em.createNativeQuery(consulta);
                query.setParameter(1, secEmpleado);
                query.setParameter(2, txtFecha);
            } catch (NullPointerException npe) {
                throw new Exception("verificaExistenciaSolicitud: EntiyManager, query o consulta nulos.");
            }
            Object res = query.getSingleResult();
            System.out.println("verificaExistenciaSolicitud-res: " + res);
            if (res instanceof BigDecimal) {
                conteo = (BigDecimal) res;
                System.out.println("verificaExistenciaSolicitud-conteo: " + conteo);
            } else {
                throw new Exception("El conteo de la solicitud no es BigDecimal. " + res + " tipo: " + res.getClass().getName());
            }
        } catch (Exception e) {
            System.out.println("verificaExistenciaSolicitud-excepcion: " + e);
//            throw e;
            throw new Exception("Error verificando si la solicitud ya existe " + e);
        }
        System.out.println("verificaExistenciaSolicitud-conteo: " + conteo);
        return conteo;
    }

}
