/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.VwVacaPendientesEmpleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 *
 * @author Edwin
 */
@Local
public interface IPersistenciaVwVacaPendientesEmpleados {
    public List<VwVacaPendientesEmpleados> consultarPeriodosPendientesEmpleado(EntityManager em, BigDecimal secEmpleado);
    public VwVacaPendientesEmpleados consultarPeriodoMasAntiguo(EntityManager em, BigDecimal secEmpleado, Date fechaContratos);
    public BigDecimal consultarCodigoJornada(EntityManager em, BigDecimal secEmpleado, Date fechaDisfrute);
    public boolean verificarFestivo(EntityManager em, Date fechaDisfrute );
    public boolean verificarDiaLaboral(EntityManager em, Date fechaDisfrute, BigDecimal codigoJornada) ;
    public BigDecimal consultaDiasPendientes(EntityManager em, BigDecimal secEmpleado );
    /**
     * M�todo que calcula la fecha de regreso a laborar de la novedad de vacaciones
     * que se esta creando.
     * @param em
     * @param secEmpleado
     * @param fechaIniVaca
     * @param dias
     * @return
     * @throws PersistenceException
     * @throws NullPointerException
     * @throws Exception 
     */
    public Date calculaFechaRegreso(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, BigInteger dias) throws PersistenceException, NullPointerException, Exception;
    /**
     * M�todo que calcula la fecha de finalizaci�n de la novedad de vacaciones 
     * que se esta creando.
     * @param em
     * @param secEmpleado
     * @param fechaIniVaca
     * @param fechaRegreso
     * @return
     * @throws PersistenceException
     * @throws NullPointerException
     * @throws Exception 
     */
    public Date calculaFechaFinVaca(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaRegreso) throws PersistenceException, NullPointerException, Exception;
    /**
     * Calcula la fecha de pago de la novedad de vacaciones a crear, seg�n los 
     * criterios de la base de datos.
     * @param em
     * @param secEmpleado
     * @param fechaIniVaca
     * @param fechaRegreso
     * @param procDifNom
     * @return
     * @throws PersistenceException
     * @throws NullPointerException
     * @throws Exception 
     */
    public Date calculaFechaPago(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaRegreso, String procDifNom) throws PersistenceException, NullPointerException, Exception;
    /**
     * M�todo que consulta la fecha de �ltimo pago de la n�mina o de las vacaciones.
     * @param em
     * @param secEmpleado
     * @return 
     */
    public Date consultaFechaUltimoPago(EntityManager em, BigDecimal secEmpleado );
    public BigDecimal consultarDiasNoContinuos(EntityManager em, BigDecimal secEmpleado, Date fechaIngreso, Date fechaUltPago);
    /**
     * M�todo que consulta la fecha de pago la �ltima vacaci�n.s
     * @param em
     * @param secEmpleado
     * @return 
     */
    public Date consultarVacaMaxPago(EntityManager em, BigDecimal secEmpleado);
    /**
     * M�todo que consulta la fecha siguiente al final de las vacaciones, 
     * es decir, la fecha de regreso a laborar.
     * @param em
     * @param secEmpleado
     * @return 
     */
    public Date consultarVacaSigFinVaca(EntityManager em, BigDecimal secEmpleado);
}
