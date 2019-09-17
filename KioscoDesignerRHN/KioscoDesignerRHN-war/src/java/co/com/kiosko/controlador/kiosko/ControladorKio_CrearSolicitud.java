package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarCrearSolicitud;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.*;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
    private LeerArchivoXML leerArchivoXML;

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
            System.out.println("fecha periodo vaca: " + perVacaSelecto.getInicialCausacion());
            HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println("URL: " + origRequest.getRequestURL());
            leerArchivoXML = new LeerArchivoXML();
            leerArchivoXML.leerArchivoConfigModulos();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getMessage());
        }
    }

    private void crearSolicitud() {
        solicitud = new KioSoliciVacas();
        solicitud.setEmpleado(empleado);
        solicitud.getKioNovedadesSolici().setEmpleado(empleado);
        solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        setTipoSelecto("TIEMPO");
    }

    private void consultaIniciales(HttpSession ses) {
        administrarCrearSolicitud.obtenerConexion(ses.getId());
        administrarGenerarReporte.obtenerConexion(ses.getId());
        try {
            setPerVacaSelecto(administrarCrearSolicitud.consultarPeriodoMasAntiguo(empleado));
        } catch (Exception ex) {
            MensajesUI.error(ex.getMessage());
        }
        Personas autorizador = null;
        try {
            autorizador = administrarCrearSolicitud.consultarAutorizador(empleado);
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error consultado autorizador: " + msj);
//            MensajesUI.error(msj);
        }
        if (autorizador == null) {
            try {
                solicitud.setEmpleadoJefe(administrarCrearSolicitud.consultarEmpleadoJefe(empleado));
                if (solicitud.getEmpleadoJefe() != null) {
                    System.out.println("Empleado jefe: " + solicitud.getEmpleadoJefe().getPersona().getNombreCompleto());
                } else {
                    System.out.println("El Empleado jefe esta vacio ");
                    throw new Exception("El empleado jefe no esta registrado por lo que no se puede crear la solicitud.");
                }
            } catch (Exception e) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error consultado jefe: " + msj);
                MensajesUI.error(msj);
            }
        } else {
            solicitud.setAutorizador(autorizador);
        }
    }

    private void consularListaPeriodos() {
        try {
            listaVaca = administrarCrearSolicitud.consultarPeriodosVacacionesEmpl(empleado);
        } catch (Exception e) {
            MensajesUI.error(e.getMessage());
        }
    }

    public boolean selecTipoVac() {
        return (solicitud.getKioNovedadesSolici().getFechaInicialDisfrute() == null);
    }

    public void procesarTipoSelecto(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarTipoSelecto()");
        System.out.println("Tipo de vacacion seleccionado: " + tipoSelecto);
    }

    public void onDateSelect(SelectEvent event) {
        System.out.println("Fecha seleccionada: " + event.getObject());
        Calendar cl = Calendar.getInstance();
        cl.setTime(administrarCrearSolicitud.fechaPago(empleado));
        if (!validaFechaPago()) {
            MensajesUI.error("La fecha seleccionada es inferior a la última fecha de pago.");
            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        }
        if (validaFestivo()){
            MensajesUI.error("La fecha seleccionada es un día festivo.");
//            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        }
        if (!validaDiaLaboral() ){
            MensajesUI.error("La fecha seleccionada no es un día laboral.");
//            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        }
        asignarFechas();
        PrimefacesContextUI.actualizar("principalForm:frmcreasolicitud:ffindis");
        PrimefacesContextUI.actualizar("principalForm:frmcreasolicitud:fregrelab");
    }

    private boolean validaFechaPago() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(administrarCrearSolicitud.fechaPago(empleado));
        return solicitud.getKioNovedadesSolici().getFechaInicialDisfrute().after(cl.getTime());
    }
    
    private boolean validaFestivo() {
        BigDecimal codigoJornada = administrarCrearSolicitud.consultarCodigoJornada(empleado, solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
        return administrarCrearSolicitud.verificarDiaFestivo(solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(), codigoJornada);
        //retorna true si es dia festivo.
    }
    
    private boolean validaDiaLaboral() {
        BigDecimal codigoJornada = administrarCrearSolicitud.consultarCodigoJornada(empleado, solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
        return administrarCrearSolicitud.verificarDiaLaboral(solicitud.getKioNovedadesSolici().getFechaInicialDisfrute(), codigoJornada);
        //retorna true si es dia laboral.
    }

    private void asignarFechas() {
        System.out.println(this.getClass().getName() + ".procesarDiasSelec()");
        System.out.println("dia seleccionado: " + diaSelecto);
        System.out.println("fecha inicial disfrute: " + solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
        if (diaSelecto > 0) {
            solicitud.getKioNovedadesSolici().setDias(new BigInteger(diaSelecto.toString()));
            try {
                administrarCrearSolicitud.calcularFechasFin(solicitud);
            } catch (Exception e) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error seleccionando fechas fin: " + msj);
                MensajesUI.error(msj);
            }
        }
    }

    public void procesarDiasSelec(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarDiasSelec()");
        asignarFechas();
    }

    private boolean validaFechaInicial() {
        boolean res;
        try {
            res = administrarCrearSolicitud.existeSolicitudFecha(solicitud);
        } catch (Exception ex) {
            System.out.println("validaFechaInicial-excepcion: " + ex.getMessage());
            res = true;
        }
        return res;
    }

    private boolean validaTraslapamientos() {
        boolean res = false;
        try {
            res = !BigDecimal.ZERO.equals(administrarCrearSolicitud.consultarTraslapamientos(solicitud.getKioNovedadesSolici()));
            //si es igual a cero, no hay traslapamientos.
            //falso si es cero, verdadero si es diferente de cero.
        } catch (Exception e) {
            System.out.println("validaTraslapamientos-excepcion: " + e.getMessage());
        }
        return res;
    }

    private void construirCorreo() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de crear una solicitud de vacaciones "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que CREÓ LA SOLICITUD es: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            if (solicitud.getAutorizador() != null) {
                mensaje = mensaje + "La persona a cargo de DAR APROBACIÓN es: "
                        + solicitud.getAutorizador().getNombreCompleto()
                        + "\n";
            } else {
                if (solicitud.getEmpleadoJefe() != null) {
                    mensaje = mensaje + "La persona a cargo de DAR APROBACIÓN es: "
                            + solicitud.getEmpleadoJefe().getPersona().getNombreCompleto()
                            + "\n";
                }
            }
            mensaje = mensaje + "Por favor seguir el proceso en: "
                    + ec.getRequestScheme() + "://" + ec.getRequestServerName() + ":" + ec.getRequestServerPort()
                    + ec.getRequestContextPath() + "/" + "?grupo=" + grupoEmpre
                    + "\n\n" + "Si no puede ingresar, necesitará instalar la última versión de su navegador, "
                    + "la cual podrá descargar de forma gratuita. \n\n"
                    + "En caso de que haya olvidado su clave, ingrese a la página de internet, y de clic en "
                    + "¿Olvidó su clave? y siga los pasos. \n\n"
                    + "Le recordamos que esta dirección de correo es utilizada solamente para envíos "
                    + "automáticos de la información solicitada. Por favor no responda este correo, "
                    + "ya que no podrá ser atendido. Si desea contactarse con nosotros, envíe un correo "
                    + "o comuníquese telefónicamente con Talento Humano de "
                    + empleado.getEmpresa().getNombre() + " \n\n"
                    + "Cordial saludo. ";
            String respuesta1 = "";
            String respuesta2 = "";
            Calendar fechaEnvio = Calendar.getInstance();
            Calendar fechaDisfrute = Calendar.getInstance();
            fechaDisfrute.setTime(solicitud.getKioNovedadesSolici().getFechaInicialDisfrute());
            String asunto = "Solicitud de vacaciones Kiosco - Nueva solicitud" + ": "
                    + fechaEnvio.get(Calendar.YEAR) + "/" + (fechaEnvio.get(Calendar.MONTH) + 1) + "/" + fechaEnvio.get(Calendar.DAY_OF_MONTH)
                    + ". Inicio de vacaciones: "
                    + fechaDisfrute.get(Calendar.YEAR) + "/" + (fechaDisfrute.get(Calendar.MONTH) + 1) + "/" + fechaDisfrute.get(Calendar.DAY_OF_MONTH);
            if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                        empleado.getPersona().getEmail(), asunto, mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            if (solicitud.getAutorizador() != null) {
                if (solicitud.getAutorizador().getEmail() != null && !solicitud.getAutorizador().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            solicitud.getAutorizador().getEmail(), asunto, mensaje, "");
                    respuesta2 = " Solicitud enviada correctamente al autorizador";
                } else {
                    respuesta2 = " La persona que autoriza la solicitud no tiene correo";
                }
            } else if (solicitud.getEmpleadoJefe() != null) {
                if (solicitud.getEmpleadoJefe().getPersona().getEmail() != null && !solicitud.getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            solicitud.getEmpleadoJefe().getPersona().getEmail(), asunto, mensaje, "");
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
            mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error guardarSolicitud: " + mensajeCreacion);
            MensajesUI.error(mensajeCreacion);
        }
        try {
            generarAuditoria();
        } catch (Exception ex) {
            System.out.println("construirCorreo ERROR generando auditoria: ");
            ex.printStackTrace();
        }
    }

    private void generarAuditoria() {
        System.out.println(this.getClass().getName() + ".generarAuditoria()");
        Calendar dtGen = Calendar.getInstance();
        List<String> cuentasAud = leerArchivoXML.getCuentasAudOp("solicitudVacaciones", empleado.getEmpresa().getNit(), "0131");
        System.out.println("cuentas: " + cuentasAud);
        System.out.println("Enviando mensaje de auditoria en la creacion de la solicitud de vacaciones");
        if (cuentasAud != null && !cuentasAud.isEmpty()) {
            for (String cuentaAud : cuentasAud) {
                String mensaje = "Apreciado usuario(a):\n\n"
                        + "Nos permitimos informar que "+empleado.getPersona().getNombreCompleto()+" generó la SOLICITUD DE VACACIONES el "
                        + dtGen.get(Calendar.DAY_OF_MONTH) + "/" + (dtGen.get(Calendar.MONTH) + 1) + "/" + dtGen.get(Calendar.YEAR) 
                        + " a las " + dtGen.get(Calendar.HOUR_OF_DAY) + ":" + dtGen.get(Calendar.MINUTE)+" en el módulo de Kiosco Nómina Designer.\n\n"
                        + "Recuerde:\n"
                        + "Esta dirección de correo es utilizada solamente para envíos automáticos de la información solicitada. "+"\n\n"
                        + "Cordial saludo."+"\n\n--\n"
                        + "Por favor no responda este correo, ya que no podrá ser atendido. "
                        + "Si desea contactarse con nosotros, envíe un correo o comuníquese telefónicamente con Talento Humano de "+empleado.getEmpresa().getNombre()+"\n\n"
                        ;
                String asunto = "Auditoria: Nueva Solicitud de vacaciones Kiosco ";
                if (administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(), cuentaAud,
                        asunto,
                        mensaje,
                        "")) {
                    System.out.println("El reporte de auditoria de la solicitud de Kiosko ha sido enviado exitosamente.");
                    //MensajesUI.info("El reporte de auditoria de la solicitud de Kiosko ha sido enviado exitosamente.");
                    //PrimefacesContextUI.actualizar("principalForm:growl");
                } else {
                    System.out.println("No fue posible enviar el correo de auditoria, por favor comuníquese con soporte.");
                    //MensajesUI.error("No fue posible enviar el correo de auditoria, por favor comuníquese con soporte.");
                    //PrimefacesContextUI.actualizar("principalForm:growl");
                }
            }
        }
//        estaAuditado(reporte.getCodigo(), conexionEmpleado.getEmpleado().getEmpresa().getNit());
    }

    public void guardarSolicitud() {
        System.out.println(this.getClass().getName() + ".guardarSolicitud()");
        boolean res = false;
        boolean valFPago = !validaFechaPago();
        boolean valTraslap = validaTraslapamientos();
        boolean valFInicial = validaFechaInicial();
        System.out.println("guardarSolicitud-valFPago: " + valFPago);
        System.out.println("guardarSolicitud-valFInicial: " + valFInicial);
        if (valFPago || valFInicial || valTraslap) {
            mensajeCreacion = (valFPago ? "La fecha seleccionada es inferior a la última fecha de pago." : "");
            mensajeCreacion = (valFInicial ? mensajeCreacion + "Ya existe una solicitud con la fecha inicial de disfrute." : mensajeCreacion);
            mensajeCreacion = (valTraslap ? mensajeCreacion + "Las fechas utilizadas se cruzan con otras solicitudes." : mensajeCreacion);
            MensajesUI.error(mensajeCreacion);
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
        PrimefacesContextUI.ejecutar("PF('creandoSolici').hide()");
        PrimefacesContextUI.ejecutar("PF('resulEnvio').show()");

    }

    public void enviarSolicitud() {
        System.out.println(this.getClass().getName() + ".enviarSolicitud()");
        boolean res = false;
        boolean valFPago = !validaFechaPago();
        boolean valTraslap = validaTraslapamientos();
        boolean valFInicial = validaFechaInicial();
        System.out.println("enviarSolicitud-valFPago: " + valFPago);
        System.out.println("enviarSolicitud-valFInicial: " + valFInicial);
        if (valFPago || valFInicial || valTraslap) {
            mensajeCreacion = (valFPago ? "La fecha seleccionada es inferior a la última fecha de pago." : "");
            mensajeCreacion = (valFInicial ? mensajeCreacion + "Ya existe una solicitud con la fecha inicial de disfrute." : mensajeCreacion);
            mensajeCreacion = (valTraslap ? mensajeCreacion + "Las fechas utilizadas se cruzan con las fechas de otras solicitudes." : mensajeCreacion);
            MensajesUI.error(mensajeCreacion);
            solicitud.getKioNovedadesSolici().setFechaInicialDisfrute(new Date());
        } else {
            try {
                administrarCrearSolicitud.enviarSolicitud(solicitud, empleado);
                res = true;
            } catch (Exception e) {
                mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error guardarSolicitud: " + mensajeCreacion);
                MensajesUI.error(mensajeCreacion);
            }
            if (res) {
                construirCorreo();
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
        this.mensajeCreacion = "";
        this.listaVaca = null;
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
            try {
                setPerVacaSelecto(administrarCrearSolicitud.consultarPeriodoMasAntiguo(empleado));
            } catch (Exception ex) {
                MensajesUI.error(ex.getMessage());
            }
        }
        return perVacaSelecto;
    }

    public void setPerVacaSelecto(VwVacaPendientesEmpleados perVacaSelecto) {
        System.out.println(this.getClass().getName() + ".setPerVacaSelecto()");
        this.perVacaSelecto = perVacaSelecto;
//        this.topeDias = this.perVacaSelecto.getDiasPendientes().intValue();
        this.topeDias = this.perVacaSelecto.getDiasreales().intValue();
        System.out.println("dias pendientes del periodo: " + this.perVacaSelecto.getDiasPendientes());
        System.out.println("dias realmente pendientes del periodo: " + this.perVacaSelecto.getDiasreales());
        solicitud.getKioNovedadesSolici().setVacacion(this.perVacaSelecto);
    }

    public String getMensajeCreacion() {
        return mensajeCreacion;
    }

    public void setMensajeCreacion(String mensajeCreacion) {
        this.mensajeCreacion = mensajeCreacion;
    }

}
