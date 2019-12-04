package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Generales;
import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import java.io.Serializable;
import java.math.BigInteger;
import javax.ejb.EJB;
//import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
//@Stateful
@Stateless
public class AdministrarInicioKiosko implements IAdministrarInicioKiosko, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
//    private transient EntityManagerFactory emf;

    @Override
    public EntityManagerFactory obtenerConexion(String idSesion) {
        return administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Empleados consultarEmpleado(String idSesion, BigInteger codigoEmpleado, long nit) {
//        EntityManager em = emf.createEntityManager();
        EntityManager em = obtenerConexion(idSesion).createEntityManager();
        Empleados empl = persistenciaEmpleados.consultarEmpleado(em, codigoEmpleado, nit);
        em.close();
        return empl;
    }

    @Override
    public String fotoEmpleado(String idSesion) {
        String rutaFoto = null;
//        EntityManager em = emf.createEntityManager();
        EntityManager em = obtenerConexion(idSesion).createEntityManager();
        Generales general = persistenciaGenerales.consultarRutasGenerales(em);
        if (general != null) {
//            return general.getPathfoto();
            rutaFoto = general.getPathfoto();
        } else {
//            return null;
            rutaFoto = null;
        }
        return rutaFoto;
    }
    @Override
    public String consultarLogoEmpresa(String idSesion, long nit){
//        EntityManager em = emf.createEntityManager();
        EntityManager em = obtenerConexion(idSesion).createEntityManager();
        String logo = persistenciaEmpleados.consultarEmpresa(em, nit).getLogo();
        em.close();
        return logo;
    }
}
