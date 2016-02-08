package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.entidades.ParametrizaClave;
import co.com.kiosko.administrar.interfaz.IAdministrarOlvidoClave;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import co.com.kiosko.persistencia.interfaz.IPersistenciaUtilidadesBD;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarOlvidoClave implements IAdministrarOlvidoClave {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaParametrizaClave persistenciaParametrizaClave;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public ConexionesKioskos obtenerConexionEmpleado(String codigoEmpleado, String nitEmpresa) {
        return persistenciaConexionesKioskos.consultarConexionEmpleado(em, codigoEmpleado, Long.parseLong(nitEmpresa));
    }

    @Override
    public boolean validarRespuestas(String respuesta1, String respuesta2, byte[] respuestaC1, byte[] respuestaC2) {
        if (respuesta1.toUpperCase().equals(persistenciaUtilidadesBD.desencriptar(em, respuestaC1))
                && respuesta2.toUpperCase().equals(persistenciaUtilidadesBD.desencriptar(em, respuestaC2))) {
            return true;
        }
        return false;
    }

    @Override
    public byte[] encriptar(String valor) {
        return persistenciaUtilidadesBD.encriptar(em, valor);
    }

    @Override
    public String desEncriptar(byte[] valor) {
        return persistenciaUtilidadesBD.desencriptar(em, valor);
    }

    @Override
    public boolean cambiarClave(ConexionesKioskos ck) {
        return persistenciaConexionesKioskos.registrarConexion(em, ck);
    }

    @Override
    public ParametrizaClave obtenerFormatoClave(long nitEmpresa) {
        return persistenciaParametrizaClave.obtenerFormatoClave(em, nitEmpresa);
    }
}
