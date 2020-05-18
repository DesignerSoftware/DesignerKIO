package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioNovedadesSolici;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.entidades.Personas;
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
     * M�todo que obtiene la conexi�n creada para la sesi�n actual del usuario.
     * @param idSesion Identificador de la sesi�n que se esta utilizando.
     */
    public void obtenerConexion(String idSesion);
    /**
     * M�todo que consulta los periodos pendientes de vacaciones por empleado.
     * @param empleado Empleado al que se le van a consultar los periodos de vacaciones.
     * @return List de VwVacaPendientesEmpleados (periodos) asociados al empleado.
     * @throws java.lang.Exception
     */
    public List consultarPeriodosVacacionesEmpl(Empleados empleado) throws Exception;
    /**
     * M�todo que consulta el periodo m�s antiguo que tenga pendiente el empleado.
     * @param empleado Empleado al que se le van a consultar los periodos de vacaciones.
     * @return VwVacaPendientesEmpleados Periodo m�s antiguo autorizado al empleado.
     * @throws java.lang.Exception
     */
    public VwVacaPendientesEmpleados consultarPeriodoMasAntiguo(Empleados empleado) throws Exception;
    /**
     * M�todo para consultar la fecha de inicio del contrato del empleado.
     * @param empleado
     * @return 
     */
    public Date consultarFechaContrato(Empleados empleado);
    /**
     * M�todo que permite consultar el n�mero de d�as pendientes de un empleado.
     * @param empleado
     * @return 
     */
    public BigDecimal consulDiasPendientes(Empleados empleado);
    /**
     * M�todo que permite verificar si una fecha para determinada jornada es 
     * laboral
     * @param fechaDisfrute
     * @param codJornada
     * @return 
     */
    public boolean verificarDiaLaboral(Date fechaDisfrute, BigDecimal codJornada);
    /**
     * M�todo que permite verificar si una fecha corresponde a un festivo, 
     * seg�n la jornada.
     * @param fechaDisfrute
     * @param codJornada
     * @return 
     */
    public boolean verificarDiaFestivo(Date fechaDisfrute, BigDecimal codJornada);
    /**
     * M�todo que facilita el c�digo de jornada de un empleado, seg�n la fecha
     * en la que inicia las vacaciones.
     * @param empleado
     * @param fechaDisfrute
     * @return 
     */
    public BigDecimal consultarCodigoJornada(Empleados empleado, Date fechaDisfrute);
    /**
     * M�todo que permite consultar la �ltima fecha de pago del empleado, 
     * preguntando espec�ficamente los procesos de n�mina y de vacaciones.
     * @param empleado
     * @return 
     */
    public Date fechaPago(Empleados empleado);
    /**
     * M�todo que solo guarda la solicitud de vacaciones.
     * @param solicitud Solicitud a guardar
     * @param emplEjecuta Empleado que ejecuta el evento
     * @return solicitud con los cambios aplicados
     * @throws Exception 
     */
    public KioSoliciVacas guardarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception ;
    /**
     * M�todo que guarda y env�a la solicitud de vacaciones para su aprobaci�n.
     * @param solicitud Solicitud a enviar
     * @param emplEjecuta Empleado que ejecuta el evento
     * @return solicitud con los cambios aplicados
     * @throws Exception 
     */
    public KioSoliciVacas enviarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception ;
    /**
     * M�todo que permite obtener la fecha de finalizaci�n de las vacaciones.
     * @param solicitud
     * @throws Exception 
     */
    public void calcularFechasFin(KioSoliciVacas solicitud) throws Exception;
    /**
     * M�todo que consulta el empleado jefe asociado a un empleado en VigenciasCargos.
     * @param empleado 
     * @return Empleados Jefe del empleado 
     * @throws Exception 
     */
    public Empleados consultarEmpleadoJefe(Empleados empleado) throws Exception;
    /**
     * M�todo que consulta si existe una solicitud de vacaciones con una fecha 
     * inicial de disfrute igual a la solicitud que se esta creando. 
     * El procedimiento va vinculado con la base de datos.
     * @param solicitud
     * @return
     * @throws Exception 
     */
    public boolean existeSolicitudFecha(KioSoliciVacas solicitud) throws Exception;
    public BigDecimal consultarTraslapamientos(KioNovedadesSolici novedad) throws Exception;
    public Personas consultarAutorizador(Empleados empleado) throws Exception;

    public void calcularFechasFin(KioSoliciVacas solicitud, int opcion) throws Exception;

    public BigDecimal consultarDiasPendientesTotal(Empleados empleado) throws Exception;
}
