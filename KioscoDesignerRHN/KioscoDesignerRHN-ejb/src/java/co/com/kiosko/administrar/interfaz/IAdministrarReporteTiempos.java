/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizaciones;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.entidades.Personas;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author usuario
 */
@Local
public interface IAdministrarReporteTiempos {

    public void obtenerConexion(String idSesion);

    public List<KioLocalizaciones> obtenerKioLocalizaciones(Empleados empleado);

    public Empleados consultarEmpleadoJefe(Empleados empleado);

    public KioSolicisLocaliza guardarSolicitud(KioSolicisLocaliza solicitud, Personas emplEjecuta) throws Exception;

    public void enviarReporteTiemposLab(KioSolicisLocaliza solicitud, List<KioLocalizacionesEmpl> listaLocalizaEmpl, Empleados empleado, 
            String motivo) throws Exception;

    public String consultarUsuario();
    
    public List<KioEstadosLocalizaSolici> obtenerEstadoLocalizacion(Empleados empleado, Calendar fechaReporte) throws Exception;

    public List<KioLocalizacionesEmpl> obtenerLocalizacionesXSolicitud(KioSolicisLocaliza solicitud);
}
