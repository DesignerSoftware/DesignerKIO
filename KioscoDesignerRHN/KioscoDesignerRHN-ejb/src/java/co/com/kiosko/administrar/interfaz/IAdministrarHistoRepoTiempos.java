package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.entidades.Personas;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin Hastamorir
 */
@Local
public interface IAdministrarHistoRepoTiempos {

    public void obtenerConexion(String idSesion);

    public List<KioEstadosLocalizaSolici> obtenerEstadosLocalizaSoliciXJefe(Empleados empleadoJefe, String filtroEstado);
    
    public List<KioEstadosLocalizaSolici> obtenerEstadosLocalizaSoliciXEmpleado(Empleados empleado, String filtroEstado);

    public void cambiarEstadoSolicitud(KioSolicisLocaliza solicitud, Empleados emplEjecuta, String estado, String motivo, Personas perEjecuta) throws Exception;

    public List<KioLocalizacionesEmpl> obtenerLocalizacionesXSolicitud(KioSolicisLocaliza solicitud);
    
}
