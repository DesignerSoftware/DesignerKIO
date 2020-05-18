package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioLocalizacionesEmpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
//import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateless
public class PersistenciaKioLocalizacionesEmpl implements IPersistenciaKioLocalizacionesEmpl {

    @Override
    public List<KioLocalizacionesEmpl> crearLocalizacionEmpleado(EntityManager em, KioSolicisLocaliza solicitud, List<KioLocalizacionesEmpl> listaLocalizaEmpl) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearLocalizacionEmpleado()");
        for (int i = 0; i < listaLocalizaEmpl.size(); i++) {
            listaLocalizaEmpl.get(i).setKioSolicisLocaliza(solicitud);
        }
        em.clear();
        try {
            for (int i = 0; i < listaLocalizaEmpl.size(); i++) {
                em.persist(listaLocalizaEmpl.get(i));
            }
            em.flush();
//            listaLocalizaEmpl = consultarLocalizacionesXSolicitud(em, solicitud.getSecuencia());
        } catch (EntityExistsException eee) {
            System.out.println("Error crearLocalizacionEmpleado: " + eee);
            eee.printStackTrace();
            throw new Exception(eee.toString());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearLocalizacionEmpleado: " + trle);
            trle.printStackTrace();
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error crearLocalizacionEmpleado: " + e);
            e.printStackTrace();
            throw new Exception(e.toString());
        }
        return listaLocalizaEmpl;
    }

    private List<KioLocalizacionesEmpl> consultarLocalizacionesXSolicitud(EntityManager em, BigDecimal secSolicitud) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarLocalizacionesXSolicitud" + "()");
        List<KioLocalizacionesEmpl> listaLocalizacionesXSolici = new ArrayList();
        String consulta = "select l "
                + "from KioLocalizacionesEmpl l "
                + "where l.kioSolicisLocaliza.secuencia = :solicitud ";
        Query query = null;
        try {
            query = em.createQuery(consulta);
            query.setParameter("solicitud", secSolicitud);
            listaLocalizacionesXSolici = query.getResultList();
            return listaLocalizacionesXSolici;
        } catch (PersistenceException pe) {
            System.out.println(this.getClass().getName() + ".consultarLocalizacionesXSolicitud Error de persistencia. " + pe);
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println(this.getClass().getName() + ".consultarLocalizacionesXSolicitud Nulo general " + npee);
            return null;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".consultarLocalizacionesXSolicitud Error general." + e);
            throw new Exception(e.toString());
        }
    }
    
    @Override
    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXEmpleado(EntityManager em, BigDecimal secEmpleado, Calendar fechaEnvio) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarEstadosLocalizaXEmpleado" + "()");
        List<KioEstadosLocalizaSolici> listaEstadosXEmpl = new ArrayList();
        String consulta = "select e.*  \n"
                + "from KIOESTADOSLOCALIZASOLICI e, KIOSOLICISLOCALIZA s \n"
                + "where s.secuencia = e.kiosolicilocaliza \n"
                + "and e.fechaprocesamiento = (select max(ei.fechaprocesamiento) \n"
                + "                              from KIOESTADOSLOCALIZASOLICI ei \n"
                + "                              where ei.kiosolicilocaliza = e.kiosolicilocaliza) \n"
                + "and s.empleado = ? \n"
                + "and exists (select 'x' \n"
                + "            from KIOLOCALIZACIONESEMPL l \n"
                + "            where l.kiosolicilocaliza = s.secuencia \n"
//                + "            and l.fecha between trunc(to_date('15012020', 'ddmmyyyy'), 'mm') and last_day(to_date('15012020', 'ddmmyyyy')) \n"
                + "            and l.fecha between trunc( ? , 'mm') and last_day( ? ) \n"
                + "           )";
        Query query = null;
        try {
            query = em.createNativeQuery(consulta, KioEstadosLocalizaSolici.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaEnvio.getTime(), TemporalType.DATE);
            query.setParameter(3, fechaEnvio.getTime(), TemporalType.DATE);
            listaEstadosXEmpl = query.getResultList();
            return listaEstadosXEmpl;
        } catch (PersistenceException pe) {
            System.out.println(this.getClass().getName() + " Error de persistencia. " + pe);
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println(this.getClass().getName() + " Nulo general " + npee);
            return null;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error general." + e);
            throw new Exception(e.toString());
        }
    }
}
