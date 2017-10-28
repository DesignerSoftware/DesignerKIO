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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class ControladorKio_SoliciSinProcesar implements Serializable {

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

    public ControladorKio_SoliciSinProcesar() {
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
        try {
            administrarHistoVacas.obtenerConexion(ses.getId());
            administrarProcesarSolicitud.obtenerConexion(ses.getId());
            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", null);
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
        getSoliciEmpleado();
        PrimefacesContextUI.ejecutar("PF('emplDialog').hide();");
        PrimefacesContextUI.ejecutar("PF('recarDatos').hide();");
    }

    public void cambiarEstado() {
        System.out.println(this.getClass().getName() + ".cambiarEstado()");
        System.out.println("cambiarEstado-soliciSelec: " + this.solicitudSelec);
        inacMotivo = !estadoNuevo.equals("RECHAZADO");
    }

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + "procesarSolicitud()");
        System.out.println("solicitudSelec: " + solicitudSelec.getSecuencia());
        boolean res = false;
        if ("RECHAZADO".equals(estadoNuevo)) {
            if ((motivo == null || motivo.isEmpty())) {
                MensajesUI.error("El motivo no debe estar vacio");
            }
        }
        try {
            administrarProcesarSolicitud.cambiarEstadoSolicitud(solicitudSelec.getKioSoliciVaca(), empleado, estadoNuevo, motivo);
            res = true;
        } catch (Exception ex) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
            System.out.println("Error procesanddo Solicitud: " + msj);
            MensajesUI.error(msj);
        }
        if (res) {
            String procesado = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZAR" : "APROBAR");
            String procesadoConj = ("RECHAZADO".equals(estadoNuevo) ? "RECHAZÓ" : "APROBÓ");
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext ec = context.getExternalContext();
                String mensaje = "Apreciado usuario(a): \n\n"
                        + "Nos permitimos informar que se acaba de "+procesado.toLowerCase()+" una solicitud de vacaciones "
                        + "en el módulo de Kiosco Nómina Designer. "
                        + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                        + "y continuar con el proceso. \n\n"
                        + "La persona que "+procesadoConj+" LA SOLICITUD es: "
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
                if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            empleado.getPersona().getEmail(), "Solicitud de vacaciones Kiosco", mensaje, "");
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
                mensajeCreacion = respuesta;
                MensajesUI.info(respuesta);
            } catch (Exception e) {
                mensajeCreacion = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                System.out.println("Error guardarSolicitud: " + mensajeCreacion);
                MensajesUI.error(mensajeCreacion);
            }
            PrimefacesContextUI.ejecutar("PF('soliciDialog').hide();");
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
                empleadosACargo = administrarHistoVacas.consultarEmpleadosEmpresa(empleado.getEmpresa().getNit());
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
                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "SIN PROCESAR", null);
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

    public String getGrupoEmpre() {
        return grupoEmpre;
    }

    public void setGrupoEmpre(String grupoEmpre) {
        this.grupoEmpre = grupoEmpre;
    }

}
