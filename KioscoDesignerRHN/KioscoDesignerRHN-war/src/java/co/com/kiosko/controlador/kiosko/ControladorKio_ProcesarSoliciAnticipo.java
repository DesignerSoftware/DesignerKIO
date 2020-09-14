package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.administrar.interfaz.IAdministrarProcesarSolicitud;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.inject.Named;
//import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Edwin Hastamorir
 */
//@Named(value = "controladorKio_ProcesarSoliciAnticipo")
//@ViewScoped
@ManagedBean
@SessionScoped
public class ControladorKio_ProcesarSoliciAnticipo implements Serializable {

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
    @EJB
    IAdministrarHistoVacas administrarHistoVacas;
    @EJB
    IAdministrarProcesarSolicitud administrarProcesarSolicitud;
    @EJB
    IAdministrarGenerarReporte administrarGenerarReporte;
    private Integer diasProv;
    private Integer diasSolici;
    private Integer diasAnticipados;
    private LeerArchivoXML leerArchivoXML;

    public ControladorKio_ProcesarSoliciAnticipo() {
        empleadosACargo = new ArrayList<Empleados>();
//        inacMotivo = true;
        inacMotivo = false;
        diasProv = 0;
        diasSolici = 0;
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
            leerArchivoXML = new LeerArchivoXML();
            leerArchivoXML.leerArchivoConfigModulos();
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
        try {
            administrarHistoVacas.obtenerConexion(ses.getId());
            administrarProcesarSolicitud.obtenerConexion(ses.getId());
//            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", null);
            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", empleado);
            estadoNuevo = "AUTORIZADO";
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
        empleadoSelec = null;
        getSoliciEmpleado();
        PrimefacesContextUI.ejecutar("PF('emplDialog').hide();");
        PrimefacesContextUI.ejecutar("PF('recarDatos').hide();");
    }

    public void cambiarEstado() {
        System.out.println(this.getClass().getName() + ".cambiarEstado()");
        System.out.println("cambiarEstado-soliciSelec: " + this.solicitudSelec);
//        inacMotivo = !estadoNuevo.equals("RECHAZADO");
    }

    private String enviarCorreo() throws Exception {
        String procesado = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZAR" : "APROBAR");
        String procesadoConj = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZÓ" : "APROBÓ");
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de " + procesado.toLowerCase() + " una solicitud de vacaciones "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que " + procesadoConj + " LA SOLICITUD es: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            if (solicitudSelec.getKioSoliciVaca().getEmpleadoJefe() != null) {
                mensaje = mensaje + "La persona a cargo de HACER SEGUIMIENTO es: "
                        + solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getNombreCompleto()
                        + "\n";
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
//            if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
            if (solicitudSelec.getKioSoliciVaca().getEmpleado().getPersona().getEmail() != null && 
                    !solicitudSelec.getKioSoliciVaca().getEmpleado().getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
//                        empleado.getPersona().getEmail(), 
                        solicitudSelec.getKioSoliciVaca().getEmpleado().getPersona().getEmail(), 
                        "Solicitud de vacaciones Kiosco", mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            if (solicitudSelec.getKioSoliciVaca().getEmpleadoJefe() != null) {
                if (solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail() != null && !solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            solicitudSelec.getKioSoliciVaca().getEmpleadoJefe().getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
                    respuesta2 = " Solicitud enviada correctamente al jefe inmediato";
                } else {
                    respuesta2 = " El jefe inmediato no tiene correo";
                }
            } else {
                respuesta2 = " No hay jefe inmediato relacionado";
            }
            String respuesta = respuesta1 + respuesta2;
            generarAuditoria();
            return respuesta;
        } catch (Exception e) {
            System.out.println("Error enviarCorreo: " + mensajeCreacion);
            throw new Exception(e.getCause());
        }

    }

    private void generarAuditoria() {
        System.out.println(this.getClass().getName() + ".generarAuditoria()");
        Calendar dtGen = Calendar.getInstance();
        List<String> cuentasAud = leerArchivoXML.getCuentasAudOp("solicitudVacaciones", empleado.getEmpresa().getNit(), "01393");
        System.out.println("cuentas: " + cuentasAud);
        System.out.println("Enviando mensaje de auditoria en el procesamiento de la solicitud de vacaciones");
        if (cuentasAud != null && !cuentasAud.isEmpty()) {
            for (String cuentaAud : cuentasAud) {
                String estadoEMail ="";
                Calendar calEMail = Calendar.getInstance();
                calEMail.setTime(this.solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute());
                if ("RECHAZADO".equals(estadoNuevo)) {
                    estadoEMail = "RECHAZÓ";
                } else if ("AUTORIZADO".equals(estadoNuevo)){
                    estadoEMail = "APROBÓ";
                } else {
                    estadoEMail = "PROCESÓ";
                }
                String mensaje = "Apreciado usuario(a):\n\n"
                        + "Nos permitimos informar que " + empleado.getPersona().getNombreCompleto() + " " + estadoEMail + " la SOLICITUD DE VACACIONES de "
                        + this.solicitudSelec.getKioSoliciVaca().getEmpleado().getPersona().getNombreCompleto()
                        + " el "+ dtGen.get(Calendar.DAY_OF_MONTH) + "/" + (dtGen.get(Calendar.MONTH) + 1) + "/" + dtGen.get(Calendar.YEAR)
                        + " a las " + dtGen.get(Calendar.HOUR_OF_DAY) + ":" + dtGen.get(Calendar.MINUTE) + " en el módulo de Kiosco Nómina Designer.\n\n"
                        + "La novedad estaba registrada para disfrutarla el "+ calEMail.get(Calendar.DAY_OF_MONTH) + "/"+ calEMail.get(Calendar.MONTH)+"/"+calEMail.get(Calendar.YEAR)
                        + " solicitando "+ this.solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getDias() +"días."
                        + "Recuerde:\n"
                        + "Esta dirección de correo es utilizada solamente para envíos automáticos de la información solicitada. " + "\n\n"
                        + "Cordial saludo." + "\n\n--\n"
                        + "Por favor no responda este correo, ya que no podrá ser atendido. "
                        + "Si desea contactarse con nosotros, envíe un correo o comuníquese telefónicamente con Talento Humano de " + empleado.getEmpresa().getNombre() + "\n\n";
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

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + "procesarSolicitud()");
        System.out.println("solicitudSelec: " + solicitudSelec.getSecuencia());
        boolean res = false;
        if ("RECHAZADO".equals(estadoNuevo)) {
            System.out.println("motivo: " + motivo);
            if ((motivo != null && !motivo.isEmpty())) {
                res = true;
            } else {
                MensajesUI.error("El motivo no debe estar vacio");
                res = false;
            }
        } else {
            res = true;
        }
        if (res) {
            try {
                administrarProcesarSolicitud.cambiarEstadoSolicitud(solicitudSelec.getKioSoliciVaca(), empleado, estadoNuevo, motivo, null);
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error procesanddo Solicitud: " + msj);
                MensajesUI.error(msj);
            }
        }
        if (res){
            soliciEmpleado.clear();
        }
        if (res) {
            try {
                String respuesta = enviarCorreo();
                mensajeCreacion = respuesta;
                recargarSolici();
                MensajesUI.info(respuesta);
            } catch (Exception ex) {
                mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                MensajesUI.error(mensajeCreacion);
            }
            PrimefacesContextUI.ejecutar("recargartablap(); PF('soliciDialog').hide(); PF('creandoSolici').hide();");
        }
    }

    public void consultarDias() {
        consultarDiasProv();
        consultarDiasSolicitados();
    }

    public void consultarDiasProv() {
        try {
            diasProv = administrarProcesarSolicitud.consultarDiasProvisionados(solicitudSelec.getKioSoliciVaca().getEmpleado()).intValue();
        } catch (Exception ex) {
            System.out.println("exception control consultarDiasProv" + ex);
        }
    }

    public void consultarDiasSolicitados() {
        try {
            diasSolici = administrarProcesarSolicitud.consultarDiasSolicitados(solicitudSelec.getKioSoliciVaca().getEmpleado()).intValue();
        } catch (Exception ex) {
            System.out.println("exception control consultarDiasProv" + ex);
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
//                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", null);
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
        consultarDias();
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
        System.out.println(this.getClass().getName()+".getMotivo: "+motivo);
        return motivo;
    }

    public void setMotivo(String motivo) {
        System.out.println(this.getClass().getName()+".setMotivo: "+motivo);
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

    public String getGrupoEmpre() {
        return grupoEmpre;
    }

    public void setGrupoEmpre(String grupoEmpre) {
        this.grupoEmpre = grupoEmpre;
    }

    public Integer getDiasProv() {
        return diasProv;
    }

    public void setDiasProv(Integer diasProv) {
        this.diasProv = diasProv;
    }

    public Integer getDiasSolici() {
        return diasSolici;
    }

    public void setDiasSolici(Integer diasSolici) {
        this.diasSolici = diasSolici;
    }

    public Integer getDiasAnticipados() {
        diasAnticipados = this.diasSolici - this.diasProv;
        if (diasAnticipados < 0) {
            diasAnticipados = 0;
        }
        return diasAnticipados;
    }

    public void setDiasAnticipados(Integer diasAnticipados) {
        this.diasAnticipados = diasAnticipados;
    }

}
