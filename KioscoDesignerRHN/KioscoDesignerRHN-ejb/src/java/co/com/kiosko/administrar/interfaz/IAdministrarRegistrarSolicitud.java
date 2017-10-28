package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Edwin
 */
@Local
public interface IAdministrarRegistrarSolicitud {
    public void obtenerConexion(String idSesion);
    public void cambiarEstadoSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta, String estado, String motivo) throws Exception;
    public void registrarNovedad(KioSoliciVacas solicitud) throws Exception;
    public Date fechaPago(Empleados empleado);

    public Date calcularFechaPago(Empleados empleado, Date fini, Date ffin, String proceso);
}
