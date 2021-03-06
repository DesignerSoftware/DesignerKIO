package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin
 */
@Local
public interface IAdministrarVerSolicitudesEmpl {
    public void obtenerConexion(String idSesion);
    public List<KioEstadosSolici> consultarEstaSolici(Empleados empleado, String estado);
    public Date consultarFechaContrato(Empleados empleado);
    public List<KioEstadosSolici> consultarEstaSoliciGuar(Empleados empleado);
}
