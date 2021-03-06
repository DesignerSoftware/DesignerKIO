package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.ParametrizaClave;
import co.com.kiosko.administrar.interfaz.IAdministrarOlvidoClave;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import co.com.kiosko.persistencia.interfaz.IPersistenciaUtilidadesBD;
import javax.ejb.EJB;
import java.io.Serializable;
import javax.ejb.Stateful;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Trivi�o
 */
//@Stateless
@Stateful
public class AdministrarOlvidoClave implements IAdministrarOlvidoClave, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaParametrizaClave persistenciaParametrizaClave;
    private transient EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public ConexionesKioskos obtenerConexionEmpleado(String codigoEmpleado, String nitEmpresa) {
        EntityManager em = emf.createEntityManager();
        ConexionesKioskos ck = persistenciaConexionesKioskos.consultarConexionEmpleado(em, codigoEmpleado, Long.parseLong(nitEmpresa));
        em.close();
        return ck;
    }

    @Override
    public ConexionesKioskos obtenerConexionEmpleado(String numeroDocumento) {
        EntityManager em = emf.createEntityManager();
        ConexionesKioskos ck = persistenciaConexionesKioskos.consultarConexionEmpleado(em, numeroDocumento);
        em.close();
        return ck;
    }
    
    @Override
    public ConexionesKioskos obtenerConexionPersona(String numeroDocumento, long nitEmpresa) {
        EntityManager em = emf.createEntityManager();
        ConexionesKioskos ck = persistenciaConexionesKioskos.consultarConexionPersona(em, numeroDocumento, nitEmpresa);
        em.close();
        return ck;
    }

    @Override
    public boolean validarRespuestas(String respuesta1, String respuesta2, byte[] respuestaC1, byte[] respuestaC2) {
        boolean respuesta;
        EntityManager em = emf.createEntityManager();
//        System.out.println("respuesta1Nueva: "+respuesta1);
//        System.out.println("respuesta2Nueva: "+respuesta2);
        respuesta = (respuesta1.toUpperCase().equals(persistenciaUtilidadesBD.desencriptar(em, respuestaC1).toUpperCase())
                && respuesta2.toUpperCase().equals(persistenciaUtilidadesBD.desencriptar(em, respuestaC2).toUpperCase()));
        em.close();
        return respuesta;
    }

    @Override
    public byte[] encriptar(String valor) {
        EntityManager em = emf.createEntityManager();
        byte[] datos = persistenciaUtilidadesBD.encriptar(em, valor);
        em.close();
        return datos;
    }

    @Override
    public String desEncriptar(byte[] valor) {
        EntityManager em = emf.createEntityManager();
        String resul = persistenciaUtilidadesBD.desencriptar(em, valor);
        em.close();
        return resul;
    }

    @Override
    public boolean cambiarClave(ConexionesKioskos ck) {
        EntityManager em = emf.createEntityManager();
        boolean resul = persistenciaConexionesKioskos.registrarConexion(em, ck);
        em.close();
        return resul;
    }

    @Override
    public ParametrizaClave obtenerFormatoClave(long nitEmpresa) {
//        System.out.println(this.getClass().getName()+".obtenerFormatoClave");
//        System.out.println("nitEmpresa: " + nitEmpresa);
        ParametrizaClave pc = null;
        try {
            EntityManager em = emf.createEntityManager();
            pc = persistenciaParametrizaClave.obtenerFormatoClave(em, nitEmpresa);
            em.close();
        } catch (Exception ex) {
            System.out.println("exception: " + ex.toString());
        }
        return pc;
    }
}
