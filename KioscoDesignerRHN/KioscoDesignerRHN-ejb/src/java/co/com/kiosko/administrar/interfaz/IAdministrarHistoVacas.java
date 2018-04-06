package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Empresas;
import co.com.kiosko.entidades.KioEstadosSolici;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin
 */
@Local
public interface IAdministrarHistoVacas {

    public void obtenerConexion(String idSesion);

    public List<Empleados> consultarEmpleadosEmpresa(long nit) throws Exception;
    /**
     * Método para consultar los empleados activos que estan relacionados con un empleado jefe.
     * @param nit
     * @param emplJefe
     * @return
     * @throws Exception 
     */
    public List<Empleados> consultarEmpleadosJefe(long nit, Empleados emplJefe) throws Exception;
    
    public List<KioEstadosSolici> consultarEstadoSoliciEmpl(Empleados empl) throws Exception;
    /**
     * Método para consultar las solicitudes de los empleados asociados a determinada empresa
     * sin tener en cuenta el estado en el que se encuentran.
     * @param empresa Objeto de tipo Empresas para que sea extraída la información necesaria.
     * @return Lista de estados de las solicitudes
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa) throws Exception;
    /**
     * Método para consultar las solicitudes de los empleados asociados a determinada empresa
     * teniendo en cuenta el estado que se envía por parámetro.
     * @param empresa
     * @param estado
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado) throws Exception;
    /**
     * Método para consultar las solicitudes de los empleados asociados a determinada empresa 
     * teniendo en cuenta el estado que se envía por parámetro y el empleado jefe que 
     * consulta.
     * @param empresa
     * @param estado
     * @param emplJefe
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado, Empleados emplJefe) throws Exception;
}
