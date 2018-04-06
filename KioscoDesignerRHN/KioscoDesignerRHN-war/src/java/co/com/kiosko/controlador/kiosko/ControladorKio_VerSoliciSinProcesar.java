package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.administrar.interfaz.IAdministrarProcesarSolicitud;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Edwin
 */
@ManagedBean
@ViewScoped
public class ControladorKio_VerSoliciSinProcesar implements Serializable {

    private Empleados empleado;
    private List<Empleados> empleadosACargo;
    private List<Empleados> emplACargoFiltro;
    private List<KioEstadosSolici> soliciEmpleado;
    private List<KioEstadosSolici> soliciFiltradas;
    private Empleados empleadoSelec;
    private KioEstadosSolici solicitudSelec;
    private String urlMenuNavegation;
    private String estadoNuevo;
    private String motivo;
    private boolean inacMotivo;
    private String mensajeCreacion;
    private String grupoEmpre;
    private boolean salirPagina;
    @EJB
    IAdministrarHistoVacas administrarHistoVacas;
    @EJB
    IAdministrarProcesarSolicitud administrarProcesarSolicitud;
    @EJB
    IAdministrarGenerarReporte administrarGenerarReporte;

    public ControladorKio_VerSoliciSinProcesar() {
        empleadosACargo = new ArrayList<Empleados>();
        inacMotivo = true;
    }

    @PostConstruct
    public void inicializar() {
        System.out.println(this.getClass().getName() + ".inicializar()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            long nit = Long.parseLong(((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit());
            grupoEmpre = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getGrupoSeleccionado();
            consultasIniciales(ses, nit);
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        } catch (NumberFormatException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    private void consultasIniciales(HttpSession ses, long nit) {
        System.out.println(this.getClass().getName() + ".consultasIniciales()");
        System.out.println("consultasIniciales: nit: " + nit);
        salirPagina = true;
        estadoNuevo = "AUTORIZADO";
        try {
            administrarHistoVacas.obtenerConexion(ses.getId());
            administrarProcesarSolicitud.obtenerConexion(ses.getId());
            administrarGenerarReporte.obtenerConexion(ses.getId());
            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", empleado);
            System.out.println("consultasIniciales: num estados solicitudes: " + soliciEmpleado.size());
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    public void limpiarListas() {
        System.out.println(this.getClass().getName() + ".limpiarListas()");
        this.empleadosACargo = null;
        this.emplACargoFiltro = null;
        this.soliciEmpleado = null;
        this.soliciFiltradas = null;
        this.solicitudSelec = null;
        this.motivo = "";
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println(this.getClass().getName() + ".onRowSelect()");
        setSolicitudSelec((KioEstadosSolici) event.getObject());
        FacesMessage msg = new FacesMessage("Empleado elegido", ((Empleados) event.getObject()).getSecuencia().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        System.out.println(this.getClass().getName() + ".onRowUnselect()");
        FacesMessage msg = new FacesMessage("Empleado liberado", ((Empleados) event.getObject()).getSecuencia().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void buscarEmpleado() {
        System.out.println(this.getClass().getName() + ".buscarEmpleado()");
        PrimefacesContextUI.ejecutar("PF('emplDialog').show();");
    }

    public void mostrarEmpleados() {
        System.out.println(this.getClass().getName() + ".mostrarEmpleados()");
        limpiarListas();
        getSoliciEmpleado();
    }

    public void recargarSolici() {
        System.out.println(this.getClass().getName() + ".mostrarEmpleados()");
        solicitudSelec = null;
        soliciEmpleado = null;
        getSoliciEmpleado();
        PrimefacesContextUI.ejecutar("PF('emplDialog').hide();");
        PrimefacesContextUI.ejecutar("PF('recarDatos').hide();");
    }

    public void cambiarEstado() {
        System.out.println(this.getClass().getName() + ".cambiarEstado()");
        System.out.println("cambiarEstado-soliciSelec: " + this.solicitudSelec);
        inacMotivo = !estadoNuevo.equals("RECHAZADO");
    }

    private boolean validaFechaPago() {
        Calendar cl = Calendar.getInstance();
//        cl.setTime(administrarProcesarSolicitud.fechaUltimoPago(empleado));
        cl.setTime(administrarProcesarSolicitud.fechaUltimoPago(this.solicitudSelec.getKioSoliciVaca().getEmpleado()));
        return solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute().after(cl.getTime());
    }

    private boolean validaFechaInicial() {
        boolean res;
        try {
            res = administrarProcesarSolicitud.existeSolicitudFecha(solicitudSelec.getKioSoliciVaca());
        } catch (Exception ex) {
            System.out.println("validaFechaInicial-excepcion: " + ex.getMessage());
            res = true;
        }
        return res;
    }

    private void construirCorreo() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String procesado = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZAR" : "APROBAR");
            String procesadoConj = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZÓ" : "APROBÓ");
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de " + procesado + " una solicitud de vacaciones "
                    + "creada para "+solicitudSelec.getKioSoliciVaca().getEmpleado().getPersona().getNombreCompleto()+" "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que " + procesadoConj + " LA SOLICITUD fue: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            mensaje = mensaje + "Por favor seguir el proceso en: "
                    + ec.getRequestScheme() + "://" + ec.getRequestServerName() + ":" + ec.getRequestServerPort()
                    + "/" + ec.getRequestContextPath() + "/" + "?grupo=" + grupoEmpre
                    + "\n\n" + "Si no puede ingresar, necesitará instalar la última versión de su navegador, "
                    + "la cual podrá descargar de forma gratuita. \n\n"
                    + "En caso de que haya olvidado su clave, ingrese a la página de internet, y de clic en "
                    + "¿Olvidó su clave? y siga los pasos. \n\n"
                    + "Le recordamos que esta dirección de correo es utilizada solamente para envíos "
                    + "automáticos de la información solicitada. Por favor no responda este correo, "
                    + "ya que no podrá ser atendido. Si desea contactarse con nosotros, envíe un correo "
                    + "o comuníquese telefónicamente con Talento Humano de "
                    + empleado.getEmpresa().getNombre() + ". \n\n"
                    + "Cordial saludo. ";
            String respuesta1 = "";
            String respuesta2 = "";
            Calendar fechaEnvio = Calendar.getInstance();
            Calendar fechaDisfrute = Calendar.getInstance();
            fechaDisfrute.setTime(solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute());
            String asunto = "Solicitud de vacaciones Kiosco - "+procesadoConj.toLowerCase()+": "
                    +fechaEnvio.get(Calendar.YEAR)+"/"+(fechaEnvio.get(Calendar.MONTH)+1)+"/"+fechaEnvio.get(Calendar.DAY_OF_MONTH)
                    +". Inicio de vacaciones: "
                    +fechaDisfrute.get(Calendar.YEAR)+"/"+(fechaDisfrute.get(Calendar.MONTH)+1)+"/"+fechaDisfrute.get(Calendar.DAY_OF_MONTH);
            if (this.solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getEmpleado().getPersona().getEmail() != null
                    && !this.solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getEmpleado().getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                        this.solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getEmpleado().getPersona().getEmail(), 
                        asunto, mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            if (this.solicitudSelec.getKioSoliciVaca().getEmpleadoJefe() != null) {
                if (this.solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail() != null
                        && !this.solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            this.solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
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
    }

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + "procesarSolicitud()");
        System.out.println("procesarSolicitud-solicitudSelec: " + solicitudSelec.getSecuencia());
        System.out.println("procesarSolicitud-estadoNuevo: " + estadoNuevo);
        boolean res = false;
        boolean continuar = true;
        salirPagina = true;
        if (estadoNuevo == null) {
            MensajesUI.error("El estado no debe ser nulo.");
            continuar = false;
            salirPagina = false;
        } else if (estadoNuevo.isEmpty()) {
            MensajesUI.error("El estado no debe estar vacio");
            continuar = false;
            salirPagina = false;
        }

        if ("RECHAZADO".equals(estadoNuevo)) {
            if ((motivo == null || motivo.isEmpty())) {
                MensajesUI.error("El motivo no debe estar vacio");
                continuar = false;
                salirPagina = false;
            } else {
                continuar = true;
            }
        }
        if ("AUTORIZADO".equals(estadoNuevo)) {
            boolean valFPago = !validaFechaPago();
            boolean valFInicial = validaFechaInicial();
            System.out.println("enviarSolicitud-valFPago: " + valFPago);
            System.out.println("enviarSolicitud-valFInicial: " + valFInicial);
            if (valFPago || valFInicial) {
                mensajeCreacion = (valFPago ? "La fecha inicial de disfrute es inferior a la última fecha de pago del empleado." : "");
                mensajeCreacion = (valFInicial ? mensajeCreacion + "Ya existe una solicitud con la fecha inicial de disfrute de la solicituds." : mensajeCreacion);
                continuar = false;
                salirPagina = false;
                MensajesUI.error(mensajeCreacion);
            } else {
                continuar = true;
            }
        }
        if (continuar) {
            try {
                administrarProcesarSolicitud.cambiarEstadoSolicitud(solicitudSelec.getKioSoliciVaca(), empleado, estadoNuevo, motivo);
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error procesanddo Solicitud: " + msj);
                MensajesUI.error(msj);
            }
        }
        // Si la respuesta es positiva
        if (res) {
            construirCorreo();
        }

        PrimefacesContextUI.ejecutar("PF('creandoSolici').hide()");
        PrimefacesContextUI.ejecutar("PF('resulEnvio').show()");
    }

    public void actualizarPostResultado() {
        if (this.salirPagina) {
            PrimefacesContextUI.ejecutar("PF('soliciDialog').hide()");
            PrimefacesContextUI.ejecutar("refrescarListas()");
        }
    }

    public Empleados getEmpleado() {
        System.out.println(this.getClass().getName() + ".getEmpleado()");
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        System.out.println(this.getClass().getName() + ".setEmpleado()");
        this.empleado = empleado;
    }

    public List<Empleados> getEmpleadosACargo() {
        System.out.println(this.getClass().getName() + ".getEmpleadosACargo()");
        if (empleadosACargo == null || empleadosACargo.isEmpty()) {
            try {
//                empleadosACargo = administrarHistoVacas.consultarEmpleadosEmpresa(empleado.getEmpresa().getNit());
                empleadosACargo = administrarHistoVacas.consultarEmpleadosJefe(empleado.getEmpresa().getNit(), empleado);
            } catch (Exception e) {
                System.out.println("Error getEmpleadosACargo: " + this.getClass().getName() + ": " + e);
                System.out.println("Causa: " + e.getCause());
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                MensajesUI.error(msj);
            }
        }
        return empleadosACargo;
    }

    public void setEmpleadosACargo(List<Empleados> empleadosACargo) {
        System.out.println(this.getClass().getName() + ".setEmpleadosACargo()");
        this.empleadosACargo = empleadosACargo;
    }

    public List<Empleados> getEmplACargoFiltro() {
        System.out.println(this.getClass().getName() + ".getEmplACargoFiltro()");
        return emplACargoFiltro;
    }

    public void setEmplACargoFiltro(List<Empleados> emplACargoFiltro) {
        System.out.println(this.getClass().getName() + ".setEmplACargoFiltro()");
        this.emplACargoFiltro = emplACargoFiltro;
    }

    public List<KioEstadosSolici> getSoliciEmpleado() {
        System.out.println(this.getClass().getName() + ".getSoliciEmpleado()");
        if (soliciEmpleado == null || soliciEmpleado.isEmpty()) {
            try {
                if (empleadoSelec == null) {
                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", empleado);
                } else {
                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpl(empleadoSelec);
                }
            } catch (Exception e) {
                System.out.println("Error getSoliciEmpleado: " + this.getClass().getName() + ": " + e);
                System.out.println("Causa: " + e.getCause());
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                MensajesUI.error(msj);
            }
        }
        return this.soliciEmpleado;
    }

    public void setSoliciEmpleado(List<KioEstadosSolici> soliciEmpleado) {
        System.out.println(this.getClass().getName() + ".setSoliciEmpleado()");
        this.soliciEmpleado = soliciEmpleado;
    }

    public Empleados getEmpleadoSelec() {
        System.out.println(this.getClass().getName() + ".getEmpleadoSelec()");
        return empleadoSelec;
    }

    public void setEmpleadoSelec(Empleados empleadoSelec) {
        System.out.println(this.getClass().getName() + ".setEmpleadoSelec()");
        this.empleadoSelec = empleadoSelec;
        if (empleadoSelec != null) {
            setSoliciEmpleado(null);
            getSoliciEmpleado();
        }
    }

    public KioEstadosSolici getSolicitudSelec() {
        System.out.println(this.getClass().getName() + ".getSolicitudSelec()");
        return solicitudSelec;
    }

    public void setSolicitudSelec(KioEstadosSolici solicitudSelec) {
        System.out.println(this.getClass().getName() + ".setSolicitudSelec()");
        this.solicitudSelec = solicitudSelec;
    }

    public List<KioEstadosSolici> getSoliciFiltradas() {
        System.out.println(this.getClass().getName() + ".getSoliciFiltradas()");
        return soliciFiltradas;
    }

    public void setSoliciFiltradas(List<KioEstadosSolici> soliciFiltradas) {
        System.out.println(this.getClass().getName() + ".setSoliciFiltradas()");
        if (soliciFiltradas != null && !soliciFiltradas.isEmpty()) {
            System.out.println("setSoliciFiltradas-soliciFiltradas: " + soliciFiltradas.size());
        }
        this.soliciFiltradas = soliciFiltradas;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isInacMotivo() {
        return inacMotivo;
    }

    public void setInacMotivo(boolean isNotMotivo) {
        this.inacMotivo = isNotMotivo;
    }

    public String getMensajeCreacion() {
        return mensajeCreacion;
    }

    public void setMensajeCreacion(String mensajeCreacion) {
        this.mensajeCreacion = mensajeCreacion;
    }

}
