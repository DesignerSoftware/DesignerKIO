package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin Hastamorir
 */
@Local
public interface IAdministrarProcesarTiempos {

    /**
     * M�todo para obtener la conexi�n a la base de datos, la cu�l debi� ser 
     * creada al ingresar al sistema. 
     * @param idSesion Identificador de la sesi�n.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Consulta de los estados con solicitudes que tiene determinado empleado.
     * @param empleado
     * @return Lista de estados que contienen las solicitudes.
     */
    public List<KioEstadosLocalizaSolici> obtenerKioEstadosLocalizaSolici(Empleados empleado);

    
}
