package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.persistencia.interfaz.IPersistenciaVwVacaPendientesEmpleados;
import co.com.kiosko.entidades.VwVacaPendientesEmpleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;

/**
 *
 * @author Edwin
 */
@Stateless
public class PersistenciaVwVacaPendientesEmpleados implements IPersistenciaVwVacaPendientesEmpleados {

    @Override
    public List<VwVacaPendientesEmpleados> consultarPeriodosPendientesEmpleado(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarPeriodosPendientesEmpleado" + "()");
        List<VwVacaPendientesEmpleados> periodosPendientes = null;
        Query query = null;
        String consulta = "select vw from VwVacaPendientesEmpleados vw where vw.diasPendientes > 0 and vw.empleado.secuencia = :secEmpleado ";
        try {
            query = em.createQuery(consulta);
            query.setParameter("secEmpleado", secEmpleado);
            periodosPendientes = query.getResultList();
            return periodosPendientes;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
//            throw new Exception(npee.toString());
            return null;
        } catch (Exception e) {
            System.out.println("Error general." + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public VwVacaPendientesEmpleados consultarPeriodoMasAntiguo(EntityManager em, BigDecimal secEmpleado, Date fechaContrato) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarPeriodoMasAntiguo" + "()");
        VwVacaPendientesEmpleados periodo = null;
        Object retorno;
        Query query = null;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaContratoStr = formatoFecha.format(fechaContrato);
        System.out.println("Fecha de contratacion: " + fechaContratoStr);
        /*String consulta = "select vw "
                + "from VwVacaPendientesEmpleados vw "
                + "where vw.diasPendientes > 0 "
                + "and vw.empleado.secuencia = :secEmpleado "
                + "and vw.inicialCausacion = ( select min( vwi.inicialCausacion )"
                + "from VwVacaPendientesEmpleados vwi "
                + "where vwi.empleado.secuencia = vw.empleado.secuencia "
                + "and vwi.diasPendientes > 0 "
                + "and vwi.inicialCausacion >= :fechaContrato ) ";*/
        String consulta = "select vw.* "
                + "from VwVacaPendientesEmpleados vw "
                + "where vw.empleado =? "
                + "and vw.inicialCausacion = ( select min( vwi.inicialCausacion ) "
                + "from VwVacaPendientesEmpleados vwi "
                + "where vwi.empleado = vw.empleado "
                + "and KIOVACACIONES_PKG.DIASDISPOPER(vwi.rfVacacion) > 0 "
                + "and vwi.inicialCausacion >=? ) ";
        try {
            query = em.createNativeQuery(consulta,VwVacaPendientesEmpleados.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaContrato,TemporalType.DATE);
            retorno = query.getSingleResult();
            if (retorno instanceof VwVacaPendientesEmpleados) {
                periodo = (VwVacaPendientesEmpleados) retorno;
                System.out.println("Casteo exitoso a VwVacaPendientesEmpleados");
            } else {
                System.out.println("El retorno no es de tipo VwVacaPendientesEmpleados");
                System.out.println(retorno);
            }
            return periodo;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
//            throw new Exception(npee.toString());
            return null;
        } catch (Exception e) {
            System.out.println("Error general." + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public BigDecimal consultarCodigoJornada(EntityManager em, BigDecimal secEmpleado, Date fechaDisfrute) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarCodigoJornada" + "()");
        String consulta = "select nvl(j.codigo, 1) "
                + "from vigenciasjornadas v, jornadaslaborales j "
                + "where v.empleado = ? "
                + "and j.secuencia = v.jornadatrabajo "
                + "and v.fechavigencia = (select max(vi.fechavigencia) "
                + "from vigenciasjornadas vi "
                + "where vi.empleado = v.empleado "
                + "and vi.fechavigencia <= to_date( ? , 'ddmmyyyy') ) ";
        Query query = null;
        BigDecimal codigoJornada;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");
        String strFechaDisfrute = formatoFecha.format(fechaDisfrute);
        System.out.println("secuencia: " + secEmpleado);
        System.out.println("fecha en txt: " + strFechaDisfrute);
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, strFechaDisfrute);
            codigoJornada = new BigDecimal(query.getSingleResult().toString());
            return codigoJornada;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
//            throw new Exception(npee.toString());
            return null;
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public boolean verificarFestivo(EntityManager em, Date fechaDisfrute) throws Exception {
        System.out.println(this.getClass().getName() + "." + "verificarFestivo" + "()");
        String consulta = "select COUNT(*) "
                + "FROM FESTIVOS F, PAISES P "
                + "WHERE P.SECUENCIA = F.PAIS "
                + "AND P.NOMBRE = ? "
                + "AND F.DIA = TO_DATE( ? , 'DDMMYYYY') ";
        Query query = null;
        BigDecimal conteoDiaFestivo;
        boolean esDiaFestivo;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");
        String strFechaDisfrute = formatoFecha.format(fechaDisfrute);
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, "COLOMBIA");
            query.setParameter(2, strFechaDisfrute);
            conteoDiaFestivo = new BigDecimal(query.getSingleResult().toString());
            esDiaFestivo = !conteoDiaFestivo.equals(BigDecimal.ZERO);
            return esDiaFestivo;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
//        return false;
    }

    @Override
    public boolean verificarDiaLaboral(EntityManager em, Date fechaDisfrute, BigDecimal codigoJornada) throws Exception {
        System.out.println(this.getClass().getName() + "." + "verificarDiaLaboral" + "()");
        System.out.println("fechaDisfrute: " + fechaDisfrute);
        System.out.println("codigoJornada: " + codigoJornada);
        String consulta = "select COUNT(*) "
                + "FROM JORNADASSEMANALES JS, JORNADASLABORALES JL "
                + "WHERE JL.SECUENCIA = JS.JORNADALABORAL "
                + "AND JL.CODIGO = TO_number( ? ) "
                + "AND JS.DIA = ? ";
        Query query = null;
        BigDecimal conteoDiaLaboral;
        boolean esDiaLaboral;
        int diaSemana;
        String strFechaDisfrute = "";
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(fechaDisfrute);
        diaSemana = c.get(Calendar.DAY_OF_WEEK);
        strFechaDisfrute = nombreDia(diaSemana);
        System.out.println("strFechaDisfrute: " + strFechaDisfrute);
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, codigoJornada);
            query.setParameter(2, strFechaDisfrute);
            conteoDiaLaboral = new BigDecimal(query.getSingleResult().toString());
            esDiaLaboral = !conteoDiaLaboral.equals(BigDecimal.ZERO);
            return esDiaLaboral;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    private String nombreDia(int dia) {
        String retorno = "";
        switch (dia) {
            case 1:
                retorno = "DOM";
                break;
            case 2:
                retorno = "LUN";
                break;
            case 3:
                retorno = "MAR";
                break;
            case 4:
                retorno = "MIE";
                break;
            case 5:
                retorno = "JUE";
                break;
            case 6:
                retorno = "VIE";
                break;
            case 7:
                retorno = "SAB";
                break;
            default:
                retorno = "";
                break;
        }
        return retorno;
    }

    @Override
    public BigDecimal consultaDiasPendientes(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultaDiasPendientes" + "()");
        /*String consulta = "SELECT "
                + "NVL(SUM(V.DIASPENDIENTES), 0) "
                + "FROM VACACIONES V, NOVEDADES N "
                + "WHERE N.EMPLEADO = ? "
                + "AND N.SECUENCIA = V.NOVEDAD "
                + "AND N.TIPO = 'VACACION PENDIENTE' "
                + "AND V.INICIALCAUSACION >= EMPLEADOCURRENT_PKG.FECHATIPOCONTRATO( ? , TO_DATE( ? , 'DDMMYYYY') ) ";*/
        String consulta = "select sum(KIOVACACIONES_PKG.DIASDISPOPER(vw.rfvacacion)) "
                + "from VwVacaPendientesEmpleados vw "
                + "where vw.empleado = ? "
                + "and vw.inicialCausacion >= ( select min( vwi.inicialCausacion ) "
                + "from VwVacaPendientesEmpleados vwi "
                + "where vwi.empleado = vw.empleado "
                + "AND KIOVACACIONES_PKG.DIASDISPOPER(vwi.rfvacacion) > 0 "
                + "and vwi.inicialCausacion >= EMPLEADOCURRENT_PKG.FECHATIPOCONTRATO( ? , TO_DATE( ? , 'DDMMYYYY') ) ";
        Query query = null;
        BigDecimal diasPendientes = null;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");
        String strFechaDisfrute = formatoFecha.format(new Date());
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, secEmpleado);
            query.setParameter(3, strFechaDisfrute);
            diasPendientes = new BigDecimal(query.getSingleResult().toString());
            return diasPendientes;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date calculaFechaRegreso(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, BigInteger dias) throws PersistenceException, NullPointerException, Exception {
        System.out.println(this.getClass().getName() + "." + "calculaFechaRegreso" + "()");
        String consulta = "SELECT "
                + "KIOVACACIONES_PKG.CALCULARFECHAREGRESO( ? , ? , ? ) "
                + "FROM DUAL ";
        Query query = null;
        Date fechaRegreso = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaIniVaca, TemporalType.DATE);
            query.setParameter(3, dias);
            fechaRegreso = (Date) (query.getSingleResult());
            return fechaRegreso;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia en calculaFechaRegreso.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general en calculaFechaRegreso");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general en calculaFechaRegreso. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date calculaFechaFinVaca(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaRegreso) throws PersistenceException, NullPointerException, Exception {
        System.out.println(this.getClass().getName() + "." + "calculaFechaFinVaca" + "()");
        String consulta = "SELECT "
                + "KIOVACACIONES_PKG.CALCULARFECHAFINVACA( ? , ? , ? , ? ) "
                + "FROM DUAL ";
        Query query = null;
        Date fechaFinVaca = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaIniVaca, TemporalType.DATE);
            query.setParameter(3, fechaRegreso, TemporalType.DATE);
            query.setParameter(4, 'S');
            fechaFinVaca = (Date) (query.getSingleResult());
            return fechaFinVaca;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia en calculaFechaFinVaca.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general en calculaFechaFinVaca");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general en calculaFechaFinVaca. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date calculaFechaPago(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaRegreso, String procDifNom) throws PersistenceException, NullPointerException, Exception {
        System.out.println(this.getClass().getName() + "." + "calculaFechaPago" + "()");
        String consulta = "SELECT "
                + "KIOVACACIONES_PKG.CALCULARFECHAPAGO( ? , ? , ? , ? ) "
                + "FROM DUAL ";
        Query query = null;
        Date fechaPago = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaIniVaca, TemporalType.DATE);
            query.setParameter(3, fechaRegreso, TemporalType.DATE);
            query.setParameter(4, procDifNom);
            fechaPago = (Date) (query.getSingleResult());
            return fechaPago;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia en calculaFechaPago.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general en calculaFechaPago");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general en calculaFechaPago. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date consultaFechaUltimoPago(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultaFechaUltimoPago" + "()");
        String consulta = "SELECT GREATEST("
                + "CORTESPROCESOS_PKG.CAPTURARCORTEPROCESO(?, 1), "
                + "NVL( CORTESPROCESOS_PKG.CAPTURARCORTEPROCESO(?, 80), CORTESPROCESOS_PKG.CAPTURARCORTEPROCESO(?, 1)"
                + ")) "
                + "FROM DUAL ";
        Query query = null;
        Date fechaUltimoPago = null;
        try {
            //em.getTransaction().begin();
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, secEmpleado);
            query.setParameter(3, secEmpleado);
            fechaUltimoPago = (Date) (query.getSingleResult());
            return fechaUltimoPago;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public BigDecimal consultarDiasNoContinuos(EntityManager em, BigDecimal secEmpleado, Date fechaIngreso, Date fechaUltPago) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarDiasNoContinuos" + "()");
        String consulta = "SELECT "
                + "NOVEDADESSISTEMA_PKG.DIASDESCUENTOTIEMPOCONTINUO( ? , ? , ? ) "
                + "FROM DUAL ";
        Query query = null;
        BigDecimal diasNoContinuos = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaIngreso);
            query.setParameter(3, fechaUltPago);
            diasNoContinuos = new BigDecimal(query.getSingleResult().toString());
            return diasNoContinuos;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date consultarVacaMaxPago(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarVacaMaxPago" + "()");
        String consulta = "SELECT MAX(N.FECHAPAGO) "
                + "FROM NOVEDADESSISTEMA N "
                + "WHERE N.EMPLEADO = ? "
                + "AND N.TIPO = 'VACACION' "
                + "AND N.FECHAPAGO = (SELECT MAX(NI.FECHAPAGO) "
                + "FROM NOVEDADESSISTEMA NI "
                + "WHERE NI.EMPLEADO = N.EMPLEADO "
                + "AND NI.TIPO = 'VACACION') ";
        Query query = null;
        Date fechaMaxPago = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            fechaMaxPago = (Date) (query.getSingleResult());
            return fechaMaxPago;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }

    @Override
    public Date consultarVacaSigFinVaca(EntityManager em, BigDecimal secEmpleado) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarVacaSigFinVaca" + "()");
        String consulta = "SELECT MAX(N.FECHASIGUIENTEFINVACA) "
                + "FROM NOVEDADESSISTEMA N "
                + "WHERE N.EMPLEADO = ? "
                + "AND N.TIPO = 'VACACION' "
                + "AND N.FECHAPAGO = (SELECT MAX(NI.FECHAPAGO) "
                + "FROM NOVEDADESSISTEMA NI "
                + "WHERE NI.EMPLEADO = N.EMPLEADO "
                + "AND NI.TIPO = 'VACACION') ";
        Query query = null;
        Date fechaRegreUltiVaca = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, secEmpleado);
            fechaRegreUltiVaca = (Date) (query.getSingleResult());
            return fechaRegreUltiVaca;
        } catch (PersistenceException pe) {
            System.out.println("Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("Error general. " + e);
            throw new Exception(e.toString());
        }
    }
    @Override
    public BigDecimal consultarDiasRealPendPeriodo(EntityManager em, BigDecimal rfVacacion) throws Exception{
        System.out.println(this.getClass().getName() + "." + "consultarDiasRealPendPeriodo" + "()");
        String consulta = "select KIOVACACIONES_PKG.DIASDISPOPER(?) from dual ";
        Query query = null;
        BigDecimal diasPendientes = null;
        try {
            query = em.createNativeQuery(consulta);
            query.setParameter(1, rfVacacion);
//            diasPendientes = new BigDecimal(query.getSingleResult().toString());
            Object res = query.getSingleResult();
            if (res instanceof BigDecimal){
                System.out.println("Los DIASDISPOPER es BigDecimal");
                diasPendientes = (BigDecimal) res;
                System.out.println("Los DIASDISPOPER es: "+diasPendientes);
            }
            return diasPendientes;
        } catch (PersistenceException pe) {
            System.out.println("consultarDiasRealPendPeriodo-Error de persistencia.");
            throw new Exception(pe.toString());
        } catch (NullPointerException npee) {
            System.out.println("consultarDiasRealPendPeriodo-Nulo general");
            throw new Exception(npee.toString());
        } catch (Exception e) {
            System.out.println("consultarDiasRealPendPeriodo-Error general. " + e);
            throw new Exception(e.toString());
        }
    }
}
