package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Empresas;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.entidades.Personas;
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
     * M�todo para consultar los empleados activos que estan relacionados con un empleado jefe.
     * @param nit
     * @param emplJefe
     * @return
     * @throws Exception 
     */
    public List<Empleados> consultarEmpleadosJefe(long nit, Empleados emplJefe) throws Exception;
    
    public List<KioEstadosSolici> consultarEstadoSoliciEmpl(Empleados empl) throws Exception;
    /**
     * M�todo para consultar las solicitudes de los empleados asociados a determinada empresa
     * sin tener en cuenta el estado en el que se encuentran.
     * @param empresa Objeto de tipo Empresas para que sea extra�da la informaci�n necesaria.
     * @return Lista de estados de las solicitudes
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa) throws Exception;
    /**
     * M�todo para consultar las solicitudes de los empleados asociados a determinada empresa
     * teniendo en cuenta el estado que se env�a por par�metro.
     * @param empresa
     * @param estado
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado) throws Exception;
    /**
     * M�todo para consultar las solicitudes de los empleados asociados a determinada empresa 
     * teniendo en cuenta el estado que se env�a por par�metro y el empleado jefe que 
     * consulta.
     * @param empresa
     * @param estado
     * @param emplJefe
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado, Empleados emplJefe) throws Exception;
    /**
     * M�todo para consultar las solicitudes de los empleados asociados a determinada empresa 
     * teniendo en cuenta el estado que se env�a por par�metro y el autorizador que 
     * consulta.
     * @param estado de las solicitudes
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(long nit, String estado, Personas autorizador) throws Exception;
    public Empresas consultarInfoEmpresa(long nit);
    public List<Empleados> consultarEmpleadosAutorizador(long nit, Personas per) throws Exception;
}
