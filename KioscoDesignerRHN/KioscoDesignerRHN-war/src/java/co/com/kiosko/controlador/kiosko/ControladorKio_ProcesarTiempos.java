package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarHistoRepoTiempos;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
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
//@Named(value = "controladorKio_ProcesarTiempos")
//@ViewScoped
@ManagedBean
@SessionScoped
public class ControladorKio_ProcesarTiempos implements Serializable {

    private Empleados empleado;
    private List<KioEstadosLocalizaSolici> reportesTiemposEmpleados;
    private List<KioLocalizacionesEmpl> localizacionesEmpleado;
    private KioEstadosLocalizaSolici solicitudSelec;
    @EJB
    private IAdministrarHistoRepoTiempos administrarHistoRepoTiempos;
    @EJB
    IAdministrarGenerarReporte administrarGenerarReporte;
    private String grupoEmpre;
    private String estadoNuevo;
    private String mensajeCreacion;
    private String motivo;
    private LeerArchivoXML leerArchivoXML;

    public ControladorKio_ProcesarTiempos() {
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
            administrarHistoRepoTiempos.obtenerConexion(ses.getId());
//            administrarProcesarSolicitud.obtenerConexion(ses.getId());
//            reportesTiemposEmpleados = administrarHistoRepoTiempos.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", null);
            administrarGenerarReporte.obtenerConexion(ses.getId());
            reportesTiemposEmpleados = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXJefe(empleado, "SIN PROCESAR");
            System.out.println("consultasIniciales: num estados solicitudes localiza: " + reportesTiemposEmpleados.size());
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    public void consultarDetalleReporte() {
        if (this.solicitudSelec != null) {
            localizacionesEmpleado = this.administrarHistoRepoTiempos.obtenerLocalizacionesXSolicitud(solicitudSelec.getKioSoliciLocaliza());
        }
    }

    public void recargarSolici() {
        System.out.println(this.getClass().getName() + ".recargarSolici()");
        solicitudSelec = null;
//        soliciEmpleado = null;
//        getSoliciEmpleado();
//        PrimefacesContextUI.ejecutar("PF('emplDialog').hide();");
        PrimefacesContextUI.ejecutar("PF('recarDatos').hide();");
    }

    public void limpiarListas() {
        reportesTiemposEmpleados.clear();
    }

    private String construirCorreo() {
        String procesado = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZAR" : ("PENDIENTE_CORR".equals(estadoNuevo) ? "DEVOLVER" : "APROBAR"));
        String procesadoConj = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZÓ" : ("PENDIENTE_CORR".equals(estadoNuevo) ? "DEVOLVIÓ" : "APROBÓ"));
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de " + procesado.toLowerCase() + " un reporte de tiempos "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que " + procesadoConj + " EL REPORTE DE TIEMPOS es: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            if (solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe() != null) {
                mensaje = mensaje + "La persona a cargo de HACER SEGUIMIENTO es: "
                        + solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getNombreCompleto()
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
            if (solicitudSelec.getKioSoliciLocaliza().getEmpleado().getPersona().getEmail() != null && 
                    !solicitudSelec.getKioSoliciLocaliza().getEmpleado().getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
//                        empleado.getPersona().getEmail(), 
                        solicitudSelec.getKioSoliciLocaliza().getEmpleado().getPersona().getEmail(), 
                        "Reporte de tiempos laborados Kiosco", mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            if (solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe() != null) {
                if (solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail() != null && !solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            solicitudSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
                    respuesta2 = " Solicitud enviada correctamente al jefe inmediato";
                } else {
                    respuesta2 = " El jefe inmediato no tiene correo";
                }
            } else {
                respuesta2 = " No hay jefe inmediato relacionado";
            }
            String respuesta = respuesta1 + respuesta2;
            return respuesta;
        } catch (Exception e) {
            mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error construirCorreo -1: " + mensajeCreacion);
            MensajesUI.error(mensajeCreacion);
            return "";
        }

    }

    private void generarAuditoria() {
        System.out.println(this.getClass().getName() + ".generarAuditoria()");
        String procesado = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZAR" : ("PENDIENTE_CORR".equals(estadoNuevo) ? "DEVOLVER" : "APROBAR"));
        String procesadoConj = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZÓ" : ("PENDIENTE_CORR".equals(estadoNuevo) ? "DEVOLVIÓ" : "APROBÓ"));
        Calendar dtGen = Calendar.getInstance();
        List<String> cuentasAud = leerArchivoXML.getCuentasAudOp("reporteTiempos", empleado.getEmpresa().getNit(), "0144");
        System.out.println("cuentas: " + cuentasAud);
        System.out.println("Enviando mensaje de auditoria en la creacion de la solicitud de vacaciones");
        if (cuentasAud != null && !cuentasAud.isEmpty()) {
            for (String cuentaAud : cuentasAud) {
                String mensaje = "Apreciado usuario(a): \n\n"
                        + "Nos permitimos informar que se acaba de " + procesado.toLowerCase() + " un reporte de tiempos "
                        + "a nombre de "
                        + solicitudSelec.getKioSoliciLocaliza().getEmpleado().getPersona().getNombreCompleto() + " "
                        + "en el módulo de Kiosco Nómina Designer. \n"
                        + "La persona que " + procesadoConj + " EL REPORTE DE TIEMPOS es: "
                        + empleado.getPersona().getNombreCompleto() + "\n\n"
                        + "Recuerde: \n"
                        + "Esta dirección de correo es utilizada solamente para envíos automáticos de la información solicitada. " + "\n\n"
                        + "Cordial saludo." + "\n\n--\n"
                        + "Por favor no responda este correo, ya que no podrá ser atendido. "
                        + "Si desea contactarse con nosotros, envíe un correo o comuníquese telefónicamente con Talento Humano de " + empleado.getEmpresa().getNombre() + "\n\n";
                String asunto = "Auditoria: Procesamiento de Reporte de tiempos ";
                if (administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(), cuentaAud,
                        asunto,
                        mensaje,
                        "")) {
                    System.out.println("El reporte de auditoria de la solicitud de Kiosko ha sido enviado exitosamente.");
                } else {
                    System.out.println("No fue posible enviar el correo de auditoria, por favor comuníquese con soporte.");
                }
            }
        }
    }

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + "procesarSolicitud()");
        if (solicitudSelec != null) {
            System.out.println("solicitudSelec: " + solicitudSelec.getSecuencia());
        }
        boolean res = false;
        if ("RECHAZADO".equals(estadoNuevo) || "PENDIENTE_CORR".equals(estadoNuevo)) {
            if ((motivo == null || motivo.isEmpty())) {
                MensajesUI.error("El motivo no debe estar vacio");
                res = false;
            } else {
                res = true;
                PrimefacesContextUI.ejecutar("PF('confirmEnvio').hide(); PF('creandoSolici').show();");
            }
        } else {
            res = true;
//            PrimefacesContextUI.ejecutar("PF('confirmEnvio').hide(); PF('creandoSolici').show();");
        }
        if (res) {
            try {
                administrarHistoRepoTiempos.cambiarEstadoSolicitud(solicitudSelec.getKioSoliciLocaliza(), empleado, estadoNuevo, motivo, empleado.getPersona());
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error procesanddo Solicitud: " + msj);
                MensajesUI.error(msj);
                res = false;
            }
        }
        if (res) {
            reportesTiemposEmpleados.clear();
        }
        if (res) {
            try {
                String respuesta = construirCorreo();
                mensajeCreacion = respuesta;
                MensajesUI.info(respuesta);
                generarAuditoria();
                res = true;
            } catch (Exception e) {
                res = false;
                mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error procesando la solicitud: " + mensajeCreacion);
                MensajesUI.error(mensajeCreacion);
            }
            PrimefacesContextUI.ejecutar("recargartablap(); PF('soliciDialog').hide(); PF('confirmEnvio').hide(); PF('creandoSolici').hide();");
        }
//        if (res){
//            PrimefacesContextUI.ejecutar("PF('soliciDialog').hide(); PF('creandoSolici').hide();");
//        }else{
//            PrimefacesContextUI.ejecutar("PF('creandoSolici').hide();");
//        }
    }

    public void validaMotivo() {
        System.out.println("validaMotivo");
        if ("RECHAZADO".equals(estadoNuevo) || "PENDIENTE_CORR".equals(estadoNuevo)) {
            if ((motivo == null || motivo.isEmpty())) {
                MensajesUI.error("El motivo no debe estar vacio");
            } else {
                PrimefacesContextUI.ejecutar("PF('confirmEnvio').show();");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmEnvio').show();");
        }
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println(this.getClass().getName() + ".onRowSelect()");
        setSolicitudSelec((KioEstadosLocalizaSolici) event.getObject());
        FacesMessage msg = new FacesMessage("Empleado elegido", ((Empleados) event.getObject()).getSecuencia().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        System.out.println(this.getClass().getName() + ".onRowUnselect()");
        FacesMessage msg = new FacesMessage("Registro liberado", event.getObject().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<KioEstadosLocalizaSolici> getReportesTiemposEmpleados() {
        if (reportesTiemposEmpleados == null || reportesTiemposEmpleados.isEmpty()) {
            try {
                reportesTiemposEmpleados = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXJefe(empleado, "SIN PROCESAR");
            } catch (Exception e) {
                System.out.println("Error getSoliciEmpleado: " + this.getClass().getName() + ": " + e);
                System.out.println("Causa: " + e.getCause());
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                MensajesUI.error(msj);
            }
        }
        return reportesTiemposEmpleados;
    }

    public void setReportesTiemposEmpleados(List<KioEstadosLocalizaSolici> reportesTiemposEmpleados) {
        this.reportesTiemposEmpleados = reportesTiemposEmpleados;
    }

    public KioEstadosLocalizaSolici getSolicitudSelec() {
        return solicitudSelec;
    }

    public void setSolicitudSelec(KioEstadosLocalizaSolici solicitudSelec) {
        this.solicitudSelec = solicitudSelec;
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

    public String getMensajeCreacion() {
        return mensajeCreacion;
    }

    public void setMensajeCreacion(String mensajeCreacion) {
        this.mensajeCreacion = mensajeCreacion;
    }

    public List<KioLocalizacionesEmpl> getLocalizacionesEmpleado() {
        return localizacionesEmpleado;
    }

    public void setLocalizacionesEmpleado(List<KioLocalizacionesEmpl> localizacionesEmpleado) {
        this.localizacionesEmpleado = localizacionesEmpleado;
    }

}
