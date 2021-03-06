package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarCrearSolicitud;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioNovedadesSolici;
import co.com.kiosko.entidades.KioSoliciVacas;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.entidades.VwVacaPendientesEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioNovedadesSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioSoliciVacas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaVwVacaPendientesEmpleados;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.math.BigInteger;
import java.util.Date;
//import java.math.BigDecimal;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarCrearSolicitud implements IAdministrarCrearSolicitud, Serializable {

    //private EntityManager em;
    private transient EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaVwVacaPendientesEmpleados persistenciaVwVacaPendEmpl;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaKioNovedadesSolici persistenciaNovedadSolici;
    @EJB
    private IPersistenciaKioSoliciVacas persistenciaSolicitud;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaEstadoSolicitud;
    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List consultarPeriodosVacacionesEmpl(Empleados empleado) throws Exception {
        EntityManager em = emf.createEntityManager();
        List<VwVacaPendientesEmpleados> lista;
        try {
            lista = persistenciaVwVacaPendEmpl.consultarPeriodosPendientesEmpleado(em, empleado.getSecuencia());
            for (int i=0; i < lista.size(); i++){
                lista.get(i).setDiasreales(
                        persistenciaVwVacaPendEmpl.consultarDiasRealPendPeriodo(em,lista.get(i).getRfVacacion()));
            }
        } catch (Exception ex) {
            throw ex;
        }
        em.close();
        return lista;
    }

    @Override
    public VwVacaPendientesEmpleados consultarPeriodoMasAntiguo(Empleados empleado) throws Exception{
        EntityManager em = emf.createEntityManager();
        VwVacaPendientesEmpleados periodo;
        try {
            periodo = persistenciaVwVacaPendEmpl.consultarPeriodoMasAntiguo(em, empleado.getSecuencia(), consultarFechaContrato(empleado));
            periodo.setDiasreales(persistenciaVwVacaPendEmpl.consultarDiasRealPendPeriodo(em, periodo.getRfVacacion()) );
        } catch (Exception ex) {
//            Logger.getLogger(AdministrarCrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
        em.close();
        return periodo;
    }

    @Override
    public Date consultarFechaContrato(Empleados empleado) {
        EntityManager em = emf.createEntityManager();
        Date fecha = persistenciaEmpleados.fechaContratoEmpl(em, empleado.getSecuencia());
        em.close();
        return fecha;
    }

    @Override
    public BigDecimal consulDiasPendientes(Empleados empleado) {
        EntityManager em = emf.createEntityManager();
        BigDecimal diasPendientes = null;
        try {
            diasPendientes = persistenciaVwVacaPendEmpl.consultaDiasPendientes(em, empleado.getSecuencia());
            em.close();
            System.out.println("dias pendientes: " + diasPendientes);
        } catch (Exception ex) {
            System.out.println("AdministrarCrearSolicitud. Error consultando los d�as pendientes.");
        }
        return diasPendientes;
    }

    @Override
    public BigDecimal consultarCodigoJornada(Empleados empleado, Date fechaDisfrute) {
        EntityManager em = emf.createEntityManager();
        BigDecimal codJornada = null;
        try {
            codJornada = persistenciaVwVacaPendEmpl.consultarCodigoJornada(em, empleado.getSecuencia(), fechaDisfrute);
            em.close();
            System.out.println("Codigo de Jornada: " + codJornada);
        } catch (Exception e) {
            System.out.println("AdministrarCrearSolicitud. Error consultando el c�digo de jornada.");
        }
        return codJornada;
    }

    @Override
    public boolean verificarDiaLaboral(Date fechaDisfrute, BigDecimal codJornada) {
        EntityManager em = emf.createEntityManager();
        boolean res = false;
        try {
            res = persistenciaVwVacaPendEmpl.verificarDiaLaboral(em, fechaDisfrute, codJornada);
            em.close();
            if (res) {
                System.out.println(fechaDisfrute + " es dia laboral. ");
            } else {
                System.out.println(fechaDisfrute + " NO es dia laboral. ");
            }
        } catch (Exception ex) {
            System.out.println("AdministrarCrearSolicitud. Error verificando el dia laboral.");
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean verificarDiaFestivo(Date fechaDisfrute, BigDecimal codJornada) {
        EntityManager em = emf.createEntityManager();
        boolean res = false;
        try {
            res = persistenciaVwVacaPendEmpl.verificarFestivo(em, fechaDisfrute);
            em.close();
            if (res) {
                System.out.println(fechaDisfrute + " es dia festivo. ");
            } else {
                System.out.println(fechaDisfrute + " NO es dia festivo. ");
            }
        } catch (Exception ex) {
            System.out.println("AdministrarCrearSolicitud. Error verificando el dia festio.");
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean existeSolicitudFecha(KioSoliciVacas solicitud) throws Exception {
        boolean res = false;
        BigDecimal conteo = BigDecimal.ZERO;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            conteo = persistenciaSolicitud.verificaExistenciaSolicitud(em, solicitud.getEmpleado().getSecuencia(), solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
            res = (conteo.compareTo(new BigDecimal("0")) == 1);
        } catch (TransactionRolledbackLocalException trle) {
            res = false;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
//                em.joinTransaction();
                em.close();
            }
        }
        return res;
    }

    @Override
    public Date fechaPago(Empleados empleado) {
        Date fechaUltPago = null;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            fechaUltPago = persistenciaVwVacaPendEmpl.consultaFechaUltimoPago(em, empleado.getSecuencia());
            System.out.println("fechaUltimoPago: " + fechaUltPago);
        } catch (Exception exi) {
            System.out.println("AdministrarCrearSolicitud. Error consultando la fecha de �ltimo pago.");
            exi.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return fechaUltPago;
    }

//    private void otrasFunciones(){
//        try {
//            EntityManager em = emf.createEntityManager();
//            BigDecimal diasNoContinuos = persistenciaVwVacaPendEmpl.consultarDiasNoContinuos(em, empleado.getSecuencia(), consultarFechaContrato(empleado), fechaUltPago);
//            em.close();
//            System.out.println("diasNoContinuos: " + diasNoContinuos);
//        } catch (Exception exi) {
//            System.out.println("AdministrarCrearSolicitud. Error consultando los d�as no continuos.");
//            exi.printStackTrace();
//        }
//        try {
//            EntityManager em = emf.createEntityManager();
//            Date fechaMaxPago = persistenciaVwVacaPendEmpl.consultarVacaMaxPago(em, empleado.getSecuencia());
//            em.close();
//            System.out.println("fecha Pago ultima vacacion: " + fechaMaxPago);
//        } catch (Exception exi) {
//            System.out.println("AdministrarCrearSolicitud. Error consultando la fecha de la �ltima vacaci�n.");
//            exi.printStackTrace();
//        }
//        return null;
//    }
    @Override
    public void calcularFechasFin(KioSoliciVacas solicitud) throws Exception {
        Date fechaRegreso = null;
        EntityManager em = emf.createEntityManager();
        try {
            fechaRegreso = persistenciaVwVacaPendEmpl.calculaFechaRegreso(em, solicitud.getKioNovedadesSolici().getEmpleado().getSecuencia(),
                    solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(),
                    solicitud.getKioNovedadesSolici().getDias());
            solicitud.getKioNovedadesSolici().setFechaSiguienteFinVaca(fechaRegreso);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        Date fechaFinVaca = null;
        em = emf.createEntityManager();
        if (fechaRegreso != null) {
            try {
                fechaFinVaca = persistenciaVwVacaPendEmpl.calculaFechaFinVaca(
                        em, solicitud.getKioNovedadesSolici().getEmpleado().getSecuencia(),
                        solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(),
                        fechaRegreso);
                solicitud.getKioNovedadesSolici().setAdelantaPagoHasta(fechaFinVaca);
            } catch (Exception e) {
                throw e;
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        } else {
            throw new Exception("La fecha de regreso de las vacaciones no puede ser calculada.");
        }
    }
    
    @Override
    public void calcularFechasFin(KioSoliciVacas solicitud, int opcion) throws Exception {
        Date fechaRegreso = null;
        EntityManager em = emf.createEntityManager();
        try {
            BigInteger diasSolicitados = solicitud.getKioNovedadesSolici().getDias().add(solicitud.getKioNovedadesSolici().getDiasAnticipo());
            fechaRegreso = persistenciaVwVacaPendEmpl.calculaFechaRegreso(em, solicitud.getKioNovedadesSolici().getEmpleado().getSecuencia(),
                    solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(),diasSolicitados
                    );
            solicitud.getKioNovedadesSolici().setFechaSiguienteFinVaca(fechaRegreso);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        Date fechaFinVaca = null;
        em = emf.createEntityManager();
        if (fechaRegreso != null) {
            try {
                fechaFinVaca = persistenciaVwVacaPendEmpl.calculaFechaFinVaca(
                        em, solicitud.getKioNovedadesSolici().getEmpleado().getSecuencia(),
                        solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(),
                        fechaRegreso);
                solicitud.getKioNovedadesSolici().setAdelantaPagoHasta(fechaFinVaca);
            } catch (Exception e) {
                throw e;
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        } else {
            throw new Exception("La fecha de regreso de las vacaciones no puede ser calculada.");
        }
    }

    @Override
    public Empleados consultarEmpleadoJefe(Empleados empleado) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaEmpleados.consutarJefe(em, empleado.getSecuencia(), empleado.getEmpresa().getSecuencia());
        } catch (Exception e) {
            //throw e;
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public KioSoliciVacas guardarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception {
        System.out.println(this.getClass().getName() + ".guardarSolicitud()");
        EntityManager em = emf.createEntityManager();
        try {
            persistenciaNovedadSolici.crearNovedadSolici(em, solicitud.getKioNovedadesSolici());
            System.out.println("1) novedad: " + solicitud.toString());
            solicitud.setKioNovedadesSolici(persistenciaNovedadSolici.recargarNovedadSolici(em, solicitud.getKioNovedadesSolici()));
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        System.out.println("2) novedad: " + solicitud.toString());
        em = emf.createEntityManager();
        try {
            persistenciaSolicitud.crearSolicitud(em, solicitud);
            System.out.println("3) novedad: " + solicitud.toString());
            solicitud = persistenciaSolicitud.recargarSolicitud(em, solicitud);
        } catch (NoResultException nre) {
            System.out.println("No encontr� la solicitud que presuntamente se creo.");
            throw nre;
        } catch (Exception e) {
            System.out.println("Excepcion Creando la solicitud");
            System.out.println("mensaje: " + e.getMessage());
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        System.out.println("4) novedad: " + solicitud.toString());
        em = emf.createEntityManager();
        try {
            persistenciaEstadoSolicitud.crearEstadoSolicitud(em, solicitud, emplEjecuta.getSecuencia(), "GUARDADO", "");
            return solicitud;
        } catch (Exception e) {
//            persistenciaNovedadSolici.removerNovedadSolici(em, solicitud.getKioNovedadesSolici());
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public KioSoliciVacas enviarSolicitud(KioSoliciVacas solicitud, Empleados emplEjecuta) throws Exception {
        EntityManager em = emf.createEntityManager();
        System.out.println(this.getClass().getName() + ".enviarSolicitud()");
        try {
            solicitud = this.guardarSolicitud(solicitud, emplEjecuta);
            System.out.println("enviarSolicitud: guardando");
//            return solicitud;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        System.out.println("enviarSolicitud: guardado");
        em = emf.createEntityManager();
        try {
            persistenciaEstadoSolicitud.crearEstadoSolicitud(em, solicitud, emplEjecuta.getSecuencia(), "ENVIADO", "");
            System.out.println("enviarSolicitud: enviando");
            return solicitud;
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
//        System.out.println("enviarSolicitud: enviado");
    }
    @Override
    public BigDecimal consultarTraslapamientos(KioNovedadesSolici novedad) throws Exception{
        System.out.println(this.getClass().getName()+".consultarTraslapamientos()");
        EntityManager em = null;
        BigDecimal cuentTras=null;
        try {
            em = emf.createEntityManager();
            cuentTras = persistenciaNovedadSolici.consultaTraslapamientos(em, novedad.getEmpleado().getSecuencia(), novedad.getFechaInicialDisfrute(),
                    novedad.getAdelantaPagoHasta());
            System.out.println("cuentTras: "+cuentTras);
            return cuentTras;
        } catch (Exception e) {
            System.out.println("Excepcion en consultarTraslapamientos: "+e);
            throw new Exception(e.fillInStackTrace());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    @Override
    public Personas consultarAutorizador(Empleados empleado) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaEmpleados.consutarAutorizador(em, empleado.getSecuencia(), empleado.getEmpresa().getSecuencia());
        } catch (Exception e) {
            //throw e;
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    } 
    
    @Override
    public BigDecimal consultarDiasPendientesTotal(Empleados empleado) throws Exception{
        System.out.println(this.getClass().getName() + ".consultarDiasPendientesTotal()");
        EntityManager em = emf.createEntityManager();
        BigDecimal res ;
        try {
            res = persistenciaVwVacaPendEmpl.consultarDiasProvisiMenosDisfruta(em, empleado.getSecuencia());
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return res;
    }
}
