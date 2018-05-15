package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.entidades.Personas;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Edwin
 */
@Local
public interface IAdministrarProcesarSolicitud {

    public void obtenerConexion(String idSesion);

    public void cambiarEstadoSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta, String estado, String motivo, Personas perEjecuta) throws Exception;
    public void registrarNovedad(KioSoliciVacas solicitud) throws Exception;
    public boolean existeSolicitudFecha(KioSoliciVacas solicitud) throws Exception;
    public Date fechaUltimoPago(Empleados empleado);
}
