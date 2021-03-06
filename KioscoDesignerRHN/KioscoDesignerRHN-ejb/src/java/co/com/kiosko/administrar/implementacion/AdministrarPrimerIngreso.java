package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.ParametrizaClave;
import co.com.kiosko.entidades.PreguntasKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarPrimerIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPreguntasKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaUtilidadesBD;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import java.io.Serializable;
//import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Trivi�o
 */
//@Stateful
@Stateless
public class AdministrarPrimerIngreso implements IAdministrarPrimerIngreso, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaPreguntasKioskos persistenciaPreguntasKioskos;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
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
    public List<PreguntasKioskos> obtenerPreguntasSeguridad() {
        EntityManager em = emf.createEntityManager();
        List<PreguntasKioskos> datos = persistenciaPreguntasKioskos.obtenerPreguntasSeguridad(em);
        em.close();
        return datos;
    }

    @Override
    public PreguntasKioskos consultarPreguntaSeguridad(BigDecimal secuencia) {
        EntityManager em = emf.createEntityManager();
        PreguntasKioskos pk = persistenciaPreguntasKioskos.consultarPreguntaSeguridad(em, secuencia);
        em.close();
        return pk;
    }

    @Override
    public boolean registrarConexionKiosko(ConexionesKioskos cnk) {
        EntityManager em = emf.createEntityManager();
        boolean resul = persistenciaConexionesKioskos.registrarConexion(em, cnk);
        em.close();
        return resul;
    }

    @Override
    public Empleados consultarEmpleado(BigInteger codigoEmpleado, long nit) {
        //System.out.println(this.getClass().getName()+".consultarEmpleado");
        //System.out.println("Codigo de empleado: "+codigoEmpleado);
        //System.out.println("Nit de empresa: "+nit);
        EntityManager em = emf.createEntityManager();
        Empleados empl = persistenciaEmpleados.consultarEmpleado(em, codigoEmpleado, nit);
        em.close();
        return empl;
    }

    @Override
    public byte[] encriptar(String valor) {
        EntityManager em = emf.createEntityManager();
        byte[] datos = persistenciaUtilidadesBD.encriptar(em, valor);
        em.close();
        return datos;
    }

    @Override
    public String desencriptar(byte[] valor) {
        EntityManager em = emf.createEntityManager();
        String dato = persistenciaUtilidadesBD.desencriptar(em, valor);
        em.close();
        return dato;
    }

    @Override
    public ParametrizaClave obtenerFormatoClave(long nitEmpresa) {
        EntityManager em = emf.createEntityManager();
        ParametrizaClave pc = persistenciaParametrizaClave.obtenerFormatoClave(em, nitEmpresa);
        em.close();
        return pc;
    }

    @Override
    public Personas consultarPersona(BigInteger numeroDocumento) {
        EntityManager em;
        Personas per = null;
        try {
            em = emf.createEntityManager();
            per = persistenciaEmpleados.consultarPersona(em, numeroDocumento);
            em.close();
        } catch (Exception ex) {
            System.out.println("consultarPersona;excepcion: " + ex);
        }
        boolean resul = false;
        if (per != null) {
            try {
                em = emf.createEntityManager();
                resul = persistenciaEmpleados.esAutorizador(em, new BigDecimal(per.getSecuencia()));
                em.close();
            } catch (Exception ex2) {
                System.out.println("consultarPersona;excepcionAutorizador: " + ex2);
            }
        }
        return per;
    }
}
