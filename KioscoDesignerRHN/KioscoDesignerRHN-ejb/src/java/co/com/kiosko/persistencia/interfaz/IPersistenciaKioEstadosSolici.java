/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.entidades.KioSoliciVacas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin
 */
@Local
public interface IPersistenciaKioEstadosSolici {
    /**
     * Método que consulta los estados de las solicitudes (KioEstadosSolici) por empleado.
     * @param em EntityManager ya inicializado.
     * @param secEmpleado Secuencia del empleado
     * @return Lista de KioEstadosSolici asociados al empleado.s
     * @throws Exception
     */
    public List<KioEstadosSolici> consultarEstadosXEmpl(EntityManager em, BigDecimal secEmpleado) throws Exception ;
    /**
     * 
     * @param em EntityManager
     * @param solicitud
     * @return Estado de la solicitud
     */
    public KioEstadosSolici consultarEstadosXSolici(EntityManager em, KioSoliciVacas solicitud);
    /**
     * Método que crea el estado de la solicitud. Se utiliza al momento de guardar la solicitud.
     * @param em EntityManager
     * @param solicitud
     * @param secEmplEjecuta Secuencia del empleado que ejecuta la creación de la solicitud.
     * @param estado Puede ser: GUARDADO, ENVIADO, AUTORIZADO, LIQUIDADO, RECHAZADO, CANCELADO
     * @throws EntityExistsException Si la entidad ya existe en la base de datos.
     * @throws TransactionRolledbackLocalException Si la transacción fue abortada.
     * @throws Exception Si hay error en general.s
     */
    public void crearEstadoSolicitud(EntityManager em, KioSoliciVacas solicitud, BigDecimal secEmplEjecuta, String estado) throws EntityExistsException, TransactionRolledbackLocalException, Exception ;
    /**
     * Método que consulta los estados determinados de las solicitudes que tiene asociadas un empleado.
     * @param em EntityManager
     * @param secEmpleado Secuencia del empleado objetivo
     * @param estado Puede ser: GUARDADO, ENVIADO, AUTORIZADO, LIQUIDADO, RECHAZADO, CANCELADO
     * @return 
     */
    public List<KioEstadosSolici> consultarEstadosXEmplEsta(EntityManager em, BigDecimal secEmpleado, String estado);
    /**
     * 
     * @param em entityManager
     * @param secEmpresa secuencia de la empresa
     * @return
     * @throws Exception 
     */
    public List<KioEstadosSolici> consultarEstadosXEmpre(EntityManager em, BigInteger secEmpresa ) throws Exception;
}
