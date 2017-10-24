package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarCrearSolicitud;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
//import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.*;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigInteger;
//import java.text.SimpleDateFormat;
//import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
//import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Edwin
 */
@ManagedBean
@SessionScoped
public class ControladorKio_CrearSolicitud implements Serializable {

    @EJB
    IAdministrarCrearSolicitud administrarCrearSolicitud;
    @EJB
    private IAdministrarGenerarReporte administrarGenerarReporte;

    private Empleados empleado;

    private String urlMenuNavegation;
    private List<Integer> dias;
    private Integer diaSelecto;
    private Integer topeDias;
    private List<String> tiposVaca;
    private String tipoSelecto;
    private KioSoliciVacas solicitud;
    private List<VwVacaPendientesEmpleados> listaVaca;
    private VwVacaPendientesEmpleados perVacaSelecto;
    private String mensajeCreacion;
    private String grupoEmpre;
//    private Date fechaContratacion;

    public ControladorKio_CrearSolicitud() {
        System.out.println(this.getClass().getName());
        System.out.println("Llamada al constructor");
        dias = new ArrayList();
        tiposVaca = new ArrayList();
        topeDias = 0;
    }

    @PostConstruct
    public void inicializar() {
        System.out.println(this.getClass().getName() + ".inicializar()");
        tiposVaca.add("TIEMPO");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            grupoEmpre = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getGrupoSeleccionado();
            crearSolicitud();
            consultaIniciales(ses);
            //System.out.println("cantidad periodos: " + listaVaca.size());
            System.out.println("fecha periodo vaca: " + perVacaSelecto.getInicialCausacion());
//            System.out.println("fecha de contratacion: " + fechaContratacion);
            HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println("URL: " + origRequest.getRequestURL());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    private void crearSolicitud() {
        solicitud = new KioSoliciVacas();
        solicitud.setEmpleado(empleado);
        solicitud.getKioNovedadesSolici().setEmpleado(empleado);
        solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
//        solicitud.setUsuario("KIOSKO");
//        solicitud.setEmpleadoJefe(empleado);
        setTipoSelecto("TIEMPO");
    }

    private void consultaIniciales(HttpSession ses) {
        administrarCrearSolicitud.obtenerConexion(ses.getId());
        administrarGenerarReporte.obtenerConexion(ses.getId());
        setPerVacaSelecto(administrarCrearSolicitud.consultarPeriodoMasAntiguo(empleado));
        try {
            solicitud.setEmpleadoJefe(administrarCrearSolicitud.consultarEmpleadoJefe(empleado));
            if (solicitud.getEmpleadoJefe() != null) {
                System.out.println("Empleado jefe: " + solicitud.getEmpleadoJefe().getPersona().getNombreCompleto());
            } else {
                System.out.println("El Empleado jefe esta vacio ");
            }
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error consultado jefe: " + msj);
            MensajesUI.error(msj);
        }
//        fechaContratacion = administrarCrearSolicitud.consultarFechaContrato(empleado);
//        administrarCrearSolicitud.consultarCodigoJornada(empleado, new Date());
    }

    private void consularListaPeriodos() {
        listaVaca = administrarCrearSolicitud.consultarPeriodosVacacionesEmpl(empleado);
    }

    public boolean selecTipoVac() {
        return (solicitud.getKioNovedadesSolici().getFechaInicialDisfrute() == null);
    }

    public void procesarTipoSelecto(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarTipoSelecto()");
        System.out.println("Tipo de vacacion seleccionado: " + tipoSelecto);
    }

    public void onDateSelect(SelectEvent event) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
        System.out.println("Fecha seleccionada: " + event.getObject());
        Calendar cl = Calendar.getInstance();
        cl.setTime(administrarCrearSolicitud.fechaPago(empleado));
        if (!validaFechaPago()) {
            MensajesUI.error("La fecha seleccionada es inferior a la última fecha de pago.");
            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        }
    }

    private boolean validaFechaPago() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(administrarCrearSolicitud.fechaPago(empleado));
        return solicitud.getKioNovedadesSolici().getFechaInicialDisfrute().after(cl.getTime());
    }

    public void procesarDiasSelec(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarDiasSelec()");
        System.out.println("dia seleccionado: " + diaSelecto);
        System.out.println("fecha inicial disfrute: " + solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
        solicitud.getKioNovedadesSolici().setDias(new BigInteger(diaSelecto.toString()));
        /*Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH, 30);
        solicitud.setFechaVencimiento(c2.getTime());*/
        try {
            administrarCrearSolicitud.calcularFechasFin(solicitud);
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error seleccionando fechas fin: " + msj);
            MensajesUI.error(msj);
        }

        /*Calendar cl = Calendar.getInstance();
        cl.setTime(solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
        cl.add(Calendar.DAY_OF_MONTH, diaSelecto);
        System.out.println("fecha nueva: " + cl.getTime());*/
 /*solicitud.getKioNovedadesSolici().setFechaSiguienteFinVaca(cl.getTime());
        cl.add(Calendar.DAY_OF_MONTH, -1);
        solicitud.getKioNovedadesSolici().setAdelantaPagoHasta(cl.getTime());*/
    }

    public void guardarSolicitud() {
        System.out.println(this.getClass().getName() + ".guardarSolicitud()");
        boolean res = false;
        if (!validaFechaPago()) {
            MensajesUI.error("La fecha seleccionada es inferior a la última fecha de pago.");
            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        } else {
            try {
                administrarCrearSolicitud.guardarSolicitud(solicitud, empleado);
                res = true;
            } catch (Exception e) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error guardarSolicitud: " + msj);
                MensajesUI.error(msj);
            }
            if (res) {
                MensajesUI.info("Solicitud guardada correctamente");
            }
        }
        //Administrar guardar solicitud junto con la novedad y el estado.
        //hay que manejar las excepciones provocadas por:
        //- ya existe la solicitud con mismo inicio de disfrute
        //- no se pudo registrar la solicitud.
    }

    public void enviarSolicitud() {
        System.out.println(this.getClass().getName() + ".enviarSolicitud()");
        boolean res = false;
        if (!validaFechaPago()) {
            mensajeCreacion = "La fecha seleccionada es inferior a la última fecha de pago.";
            MensajesUI.error(mensajeCreacion);
            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        } else {
            try {
                administrarCrearSolicitud.enviarSolicitud(solicitud, empleado);
                res = true;
            } catch (Exception e) {
//                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error guardarSolicitud: " + mensajeCreacion);
//                MensajesUI.error(msj);
                MensajesUI.error(mensajeCreacion);
            }
            if (res) {
                try {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ExternalContext ec = context.getExternalContext();
                    String mensaje = "Se acaba de crear una solicitud de vacaciones en el módulo de Kiosco. \n"
                            + "Por favor llevar el caso desde su cuenta de usuario en el Kiosco de nómina "
                            + "y continuar con el proceso. \n"
                            + "La persona que creó la solicitud es: "
                            + empleado.getPersona().getNumerodocumento()
                            + " " + empleado.getPersona().getNombreCompleto() + "\n";
                    if (solicitud.getEmpleadoJefe() != null) {
                        mensaje = mensaje + "La persona a cargo de dar aprobación es: "
                                + solicitud.getEmpleadoJefe().getPersona().getNumerodocumento()
                                + " " + solicitud.getEmpleadoJefe().getPersona().getNombreCompleto()
                                + "\n";
                    }
                    mensaje = mensaje + "Por favor seguir el proceso en: "
                            + ec.getRequestScheme()+ ec.getApplicationContextPath() + ec.getRequestServerPort()
                            + ec.getRequestContextPath() + "/" + "?grupo=" + grupoEmpre;
                    String respuesta1 = "";
                    String respuesta2 = "";
                    if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
                        administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                                empleado.getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
                        respuesta1 = "Solicitud enviada correctamente al empleado";
                    } else {
                        respuesta1 = "El empleado no tiene correo registrado";
                    }
                    if (solicitud.getEmpleadoJefe() != null) {
                        if (solicitud.getEmpleadoJefe().getPersona().getEmail() != null && !solicitud.getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                            administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                                    solicitud.getEmpleadoJefe().getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
                            respuesta2 = " Solicitud enviada correctamente al jefe inmediato";
                        } else {
                            respuesta2 = " El jefe inmediato no tiene correo";
                        }
                    } else {
                        respuesta2 = " No hay jefe inmediato relacionado";
                    }
                    String respuesta = respuesta1 + respuesta2;
                    mensajeCreacion = respuesta;
                    MensajesUI.info(respuesta);
                } catch (Exception e) {
//                    String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                    mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                    System.out.println("Error guardarSolicitud: " + mensajeCreacion);
//                    MensajesUI.error(msj);
                    MensajesUI.error(mensajeCreacion);
                }
            }
        }
        PrimefacesContextUI.ejecutar("PF('creandoSolici').hide()");
        PrimefacesContextUI.ejecutar("PF('resulEnvio').show()");
    }

    public void limpiarSolicitud() {
        this.solicitud = null;
        dias.clear();
        this.topeDias = 0;
        perVacaSelecto = null;
    }

    /////Accesores
    public Empleados getEmpleado() {
        System.out.println(this.getClass().getName() + ".getEmpleado()");
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        System.out.println(this.getClass().getName() + ".setEmpleado()");
        this.empleado = empleado;
    }

    public KioSoliciVacas getSolicitud() {
        System.out.println(this.getClass().getName() + ".getSolicitud()");
        if (solicitud == null) {
//            crearSolicitud();
            inicializar();
        }
        return solicitud;
    }

    public void setSolicitud(KioSoliciVacas solicitud) {
        System.out.println(this.getClass().getName() + ".setSolicitud()");
        this.solicitud = solicitud;
    }

    public List<Integer> getDias() {
        System.out.println(this.getClass().getName() + ".getDias()");
        System.out.print("tope dias: ");
        System.out.println(this.topeDias);
        if (this.topeDias != null && this.topeDias > 0 && dias.isEmpty()) {
            for (int i = 0; i <= topeDias; i++) {
                dias.add(i);
            }
            diaSelecto = dias.get(0);
        }
        return dias;
    }

    public Integer getDiaSelecto() {
        System.out.println(this.getClass().getName() + ".getDiaSelecto()");
        System.out.println("dia selecto: " + this.diaSelecto);
        return diaSelecto;
    }

    public void setDiaSelecto(Integer diaSelecto) {
        System.out.println(this.getClass().getName() + ".setDiaSelecto()");
        this.diaSelecto = diaSelecto;
        System.out.println("dia selecto: " + this.diaSelecto);
    }

    public List<String> getTiposVaca() {
        System.out.println(this.getClass().getName() + ".getTiposVaca()");
        return tiposVaca;
    }

    public String getTipoSelecto() {
        System.out.println(this.getClass().getName() + ".getTipoSelecto()");
        return tipoSelecto;
    }

    public void setTipoSelecto(String tipoSelecto) {
        System.out.println(this.getClass().getName() + ".setTipoSelecto()");
        System.out.println("tipoSelecto: " + tipoSelecto);
        this.tipoSelecto = tipoSelecto;
        this.solicitud.getKioNovedadesSolici().setSubtipo(tipoSelecto);
    }

    public List<VwVacaPendientesEmpleados> getListaVaca() {
        System.out.println(this.getClass().getName() + ".getListaVaca()");
        if (listaVaca == null) {
            consularListaPeriodos();
        }
        return listaVaca;
    }

    public void setListaVaca(List<VwVacaPendientesEmpleados> listaVaca) {
        System.out.println(this.getClass().getName() + ".setListaVaca()");
        this.listaVaca = listaVaca;
    }

    public VwVacaPendientesEmpleados getPerVacaSelecto() {
        System.out.println(this.getClass().getName() + ".getPerVacaSelecto()");
        if (perVacaSelecto == null) {
            setPerVacaSelecto(administrarCrearSolicitud.consultarPeriodoMasAntiguo(empleado));
        }
        return perVacaSelecto;
    }

    public void setPerVacaSelecto(VwVacaPendientesEmpleados perVacaSelecto) {
        System.out.println(this.getClass().getName() + ".setPerVacaSelecto()");
        this.perVacaSelecto = perVacaSelecto;
        this.topeDias = this.perVacaSelecto.getDiasPendientes().intValue();
        System.out.println("dias pendientes del periodo: " + this.perVacaSelecto.getDiasPendientes());
        solicitud.getKioNovedadesSolici().setVacacion(this.perVacaSelecto);
    }

    public String getMensajeCreacion() {
        return mensajeCreacion;
    }

    public void setMensajeCreacion(String mensajeCreacion) {
        this.mensajeCreacion = mensajeCreacion;
    }

}
