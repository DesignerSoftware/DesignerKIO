/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public List<KioEstadosSolici> consultarEstadoSoliciEmpl(Empleados empl) throws Exception;
    /**
     * Método para consultar las solicitudes de los empleados asociados a determinada empresa
     * sin tener en cuenta el estado en el que se encuentran.
     * @param empresa Objeto de tipo Empresas para que sea extraída la información necesaria.
     * @return Lista de estados de las solicitudes
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa) throws Exception;
}
