package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosLocalizaSolici;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
//import javax.persistence.TemporalType;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateless
public class PersistenciaKioEstadosLocalizaSolici implements IPersistenciaKioEstadosLocalizaSolici {

    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;

    @Override
    public void crearEstadoSolicitud(EntityManager em, KioSolicisLocaliza solicitud,
            BigDecimal secEmplEjecuta, String estado,
            String motivo, BigInteger secPersona) throws EntityExistsException, TransactionRolledbackLocalException, Exception {
        System.out.println(this.getClass().getName() + ".crearEstadoSolicitud()");
        KioEstadosLocalizaSolici estadoSoli = new KioEstadosLocalizaSolici();
        estadoSoli.setEstado(estado);
        System.out.println("crearEstadoSolicitud-estadoSoli: " + estadoSoli.getSecuencia());
        if (secPersona != null && secPersona.compareTo(BigInteger.ZERO) > 0) {
            estadoSoli.setPersonaEjecuta(persistenciaEmpleados.consultaPersonaxSec(em, secPersona));
        }
        estadoSoli.setMotivoProcesa(motivo);
        estadoSoli.setKioSoliciLocaliza(solicitud);
        System.out.println("estadoSoli: " + estadoSoli);
        System.out.println("estadoSoli: " + estadoSoli.getSecuencia());
        System.out.println("estadoSoli: " + estadoSoli.getFechaProcesamiento());
        System.out.println("estadoSoli: " + estadoSoli.getEstado());
        System.out.println("estadoSoli: " + estadoSoli.getMotivoProcesa());
        System.out.println("estadoSoli: " + estadoSoli.getUsuarioBD());
        em.clear();
        try {
            System.out.println("guardando el nuevo estado");
            em.persist(estadoSoli);
            em.flush();
        } catch (EntityExistsException eee) {
            System.out.println("Error crearEstadoSolicitud: " + eee);
            eee.printStackTrace();
            throw new Exception(eee.toString());
        } catch (TransactionRolledbackLocalException trle) {
            System.out.println("Error crearEstadoSolicitud: " + trle);
            trle.printStackTrace();
            throw new Exception(trle.toString());
        } catch (Exception e) {
            System.out.println("Error crearEstadoSolicitud: " + e);
            e.printStackTrace();
            throw new Exception(e.toString());
        }
    }

    @Override
    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXJefe(EntityManager em, BigDecimal secEmpleado, String estado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarEstadosLocalizaXJefe" + "()");
        List<KioEstadosLocalizaSolici> listaEstadosXJefe = new ArrayList();
        String consulta = "select e.* \n"
                + "from KIOESTADOSLOCALIZASOLICI e, KIOSOLICISLOCALIZA s, empleados empl, vigenciascargos vc \n"
                + "where s.secuencia = e.kiosolicilocaliza \n"
                + "and empl.secuencia = s.empleado \n"
                + "and empl.secuencia = vc.empleado \n"
                + "and e.fechaprocesamiento = (select max(ei.fechaprocesamiento) \n"
                + "                              from KIOESTADOSLOCALIZASOLICI ei \n"
                + "                              where ei.kiosolicilocaliza = e.kiosolicilocaliza) \n"
                + "and vc.fechavigencia = (select max(vci.fechavigencia) \n"
                + "                        from vigenciascargos vci \n"
                + "                        where vci.empleado = vc.empleado) \n"
                + "and vc.empleadojefe = ? "
                + "and e.estado = ? ";
        Query query = null;
        try {
            query = em.createNativeQuery(consulta, KioEstadosLocalizaSolici.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, estado);
            listaEstadosXJefe = query.getResultList();
            return listaEstadosXJefe;
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
    
    @Override
    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXEmpleado(EntityManager em, BigDecimal secEmpleado, String estado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarEstadosLocalizaXEmpleado" + "()");
        List<KioEstadosLocalizaSolici> listaEstadosXEmpleado = new ArrayList();
        String consulta = "select e.* \n"
                + "from KIOESTADOSLOCALIZASOLICI e, KIOSOLICISLOCALIZA s, empleados empl \n"
                + "where s.secuencia = e.kiosolicilocaliza \n"
                + "and empl.secuencia = s.empleado \n"
                + "and e.fechaprocesamiento = (select max(ei.fechaprocesamiento) \n"
                + "                              from KIOESTADOSLOCALIZASOLICI ei \n"
                + "                              where ei.kiosolicilocaliza = e.kiosolicilocaliza) \n"
                + "and empl.secuencia = ? "
                + "and e.estado = ? ";
        Query query = null;
        try {
            query = em.createNativeQuery(consulta, KioEstadosLocalizaSolici.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, estado);
            listaEstadosXEmpleado = query.getResultList();
            return listaEstadosXEmpleado;
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

    @Override
    public List<KioLocalizacionesEmpl> consultarLocalizacionesXSolicitud(EntityManager em, BigDecimal secSolicitud) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarLocalizacionesXSolicitud" + "()");
        List<KioLocalizacionesEmpl> listaLocalizacionesXSolici = new ArrayList();
        String consulta = "select * \n"
                + "from KioLocalizacionesEmpl\n"
                + "where kiosolicilocaliza = ? ";
        Query query = null;
        try {
            query = em.createNativeQuery(consulta, KioLocalizacionesEmpl.class);
            query.setParameter(1, secSolicitud);
            listaLocalizacionesXSolici = query.getResultList();
            return listaLocalizacionesXSolici;
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
