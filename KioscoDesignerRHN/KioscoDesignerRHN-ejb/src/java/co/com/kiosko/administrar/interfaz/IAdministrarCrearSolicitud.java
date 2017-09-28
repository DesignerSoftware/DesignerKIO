/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.entidades.VwVacaPendientesEmpleados;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin
 */
@Local
public interface IAdministrarCrearSolicitud {
    /**
     * Método que obtiene la conexión creada para la sesión actual del usuario.
     * @param idSesion Identificador de la sesión que se esta utilizando.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método que consulta los periodos pendientes de vacaciones por empleado.
     * @param empleado Empleado al que se le van a consultar los periodos de vacaciones.
     * @return List de VwVacaPendientesEmpleados (periodos) asociados al empleado.
     */
    public List consultarPeriodosVacacionesEmpl(Empleados empleado);
    /**
     * Método que consulta el periodo más antiguo que tenga pendiente el empleado.
     * @param empleado Empleado al que se le van a consultar los periodos de vacaciones.
     * @return VwVacaPendientesEmpleados Periodo más antiguo autorizado al empleado.
     */
    public VwVacaPendientesEmpleados consultarPeriodoMasAntiguo(Empleados empleado);
    public Date consultarFechaContrato(Empleados empleado);
    public BigDecimal consulDiasPendientes(Empleados empleado);
    public boolean verificarDiaLaboral(Date fechaDisfrute, BigDecimal codJornada);
    public boolean verificarDiaFestivo(Date fechaDisfrute, BigDecimal codJornada);
    public BigDecimal consultarCodigoJornada(Empleados empleado, Date fechaDisfrute);
    public Date fechaPago(Empleados empleado);
    /**
     * Método que solo guarda la solicitud de vacaciones.
     * @param solicitud Solicitud a guardar
     * @param emplEjecuta Empleado que ejecuta el evento
     * @return solicitud con los cambios aplicados
     * @throws Exception 
     */
    public KioSoliciVacas guardarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception ;
    /**
     * Método que guarda y envía la solicitud de vacaciones para su aprobación.
     * @param solicitud Solicitud a enviar
     * @param emplEjecuta Empleado que ejecuta el evento
     * @return solicitud con los cambios aplicados
     * @throws Exception 
     */
    public KioSoliciVacas enviarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception ;
    public void calcularFechasFin(KioSoliciVacas solicitud) throws Exception;
    /**
     * Método que consulta el empleado jefe asociado a un empleado en VigenciasCargos.
     * @param empleado 
     * @return Empleados Jefe del empleado 
     * @throws Exception 
     */
    public Empleados consultarEmpleadoJefe(Empleados empleado) throws Exception;
}
