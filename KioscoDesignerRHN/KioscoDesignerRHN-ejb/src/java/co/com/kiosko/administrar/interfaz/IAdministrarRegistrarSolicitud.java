/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
