package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarHistoRepoTiempos;
import co.com.kiosko.clasesAyuda.EstadoSolicitud;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
//import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.OpcionLista;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
//import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.NoSuchObjectLocalException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Edwin Hastamorir
 */
//@Named(value = "controladorKio_VerTiemposEmpleado")
//@SessionScoped
@ManagedBean
@ViewScoped
public class controladorKio_VerTiemposEmpleado implements Serializable {

    @EJB
    IAdministrarGenerarReporte administrarGenerarReporte;
    @EJB
    private IAdministrarHistoRepoTiempos administrarHistoRepoTiempos;
    private Empleados empleado;
    private String urlMenuNavegation;
    private List<KioEstadosLocalizaSolici> estadoSoliciGuardadas;
    private List<KioEstadosLocalizaSolici> estadoSoliciEnviadas;
    private List<KioEstadosLocalizaSolici> estadoSoliciAprobadas;
    private List<KioEstadosLocalizaSolici> estadoSoliciRechazadas;
    private List<KioEstadosLocalizaSolici> estadoSoliciLiquidadas;
    private List<KioEstadosLocalizaSolici> estadoSoliciCanceladas;
    private KioEstadosLocalizaSolici estSoliciSelec;
    private List<KioLocalizacionesEmpl> localizacionesEmpleado;
//    private String estadoNuevo;
    private OpcionLista estadoNuevo;
    private boolean inacMotivo;
    private String motivo;
    private String mensajeCreacion;
    private String grupoEmpre;
    private List listaEstados;

    public controladorKio_VerTiemposEmpleado() {
        System.out.println(this.getClass().getName());
        System.out.println("Llamada al constructor");
        listaEstados = new ArrayList();
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
            inicializarEJB();
            consultasIniciales(ses, nit);
            /*estadoSoliciGuardadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "GUARDADO");
            estadoSoliciEnviadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "ENVIADO");
            estadoSoliciAprobadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "PREAPROBADO");
            estadoSoliciRechazadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "RECHAZADO");
            estadoSoliciLiquidadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "PENDIENTE_CORR");
            estadoSoliciCanceladas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "CANCELADO");*/
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    private void inicializarEJB() {
        FacesContext x = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        administrarHistoRepoTiempos.obtenerConexion(ses.getId());
//        administrarProcesarSolicitud.obtenerConexion(ses.getId());
        administrarGenerarReporte.obtenerConexion(ses.getId());
    }

    private void consultasIniciales(HttpSession ses, long nit) {
        System.out.println(this.getClass().getName() + ".consultasIniciales()");
        System.out.println("consultasIniciales: nit: " + nit);
//        setEstadoNuevo(new OpcionLista("ENVIAR","ENVIADO"));
        setEstadoNuevo(new OpcionLista(EstadoSolicitud.ENVIADO.getEvento(), EstadoSolicitud.ENVIADO.getEstado()));
        motivo = "";
        try {
//            administrarVerSolicitudesEmpl.obtenerConexion(ses.getId());
//            inicializarEJB();
            estadoSoliciGuardadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "GUARDADO");
            estadoSoliciEnviadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "ENVIADO");
            estadoSoliciAprobadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "PREAPROBADO");
            estadoSoliciRechazadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "RECHAZADO");
            estadoSoliciLiquidadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "PENDIENTE_CORR");
            estadoSoliciCanceladas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "CANCELADO");
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    public boolean mostrarPanelAcciones() {
        System.out.println(this.getClass().getName() + ".mostrarPanelAcciones()");
        if (estSoliciSelec != null) {
            if ("GUARDADO".equals(estSoliciSelec.getEstado()) || "ENVIADO".equals(estSoliciSelec.getEstado())) {
                return true;
            }
        }
        return false;
    }
    
    public void consultarDetalleReporte() {
        if (this.estSoliciSelec != null) {
            localizacionesEmpleado = this.administrarHistoRepoTiempos.obtenerLocalizacionesXSolicitud(estSoliciSelec.getKioSoliciLocaliza());
        }
    }

    public void limpiarListas() {
        System.out.println(this.getClass().getName() + ".limpiarListas()");
        this.estadoSoliciGuardadas = null;
        this.estadoSoliciEnviadas = null;
        this.estadoSoliciAprobadas = null;
        this.estadoSoliciRechazadas = null;
        this.estadoSoliciLiquidadas = null;
        this.estadoSoliciCanceladas = null;
        this.estSoliciSelec = null;
//        this.estadoNuevo = "ENVIADO";
    }

//    public void onRowSelect(SelectEvent event) {
//        System.out.println(this.getClass().getName() + ".onRowSelect()");
//        setEstSoliciSelec((KioEstadosSolici) event.getObject());
//        FacesMessage msg = new FacesMessage("Empleado elegido", ((Empleados) event.getObject()).getSecuencia().toString());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
//
//    public void onRowUnselect(UnselectEvent event) {
//        System.out.println(this.getClass().getName() + ".onRowUnselect()");
//        FacesMessage msg = new FacesMessage("Empleado liberado", ((Empleados) event.getObject()).getSecuencia().toString());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
//    public void recargarSolici() {
//        System.out.println(this.getClass().getName() + ".recargarSolici()");
////        soliciFiltradas = null;
//        this.estSoliciSelec = null;
//        PrimefacesContextUI.ejecutar("PF('emplDialog').hide();");
//        PrimefacesContextUI.ejecutar("PF('recarDatos').hide();");
//    }
    public void cambiarEstado() {
        System.out.println(this.getClass().getName() + ".cambiarEstado()");
        System.out.println("cambiarEstado-soliciSelec: " + this.estSoliciSelec);
        inacMotivo = !estadoNuevo.getValor().equals("CANCELADO");
    }

    private void construirCorreo(String proceso, String procesoConj) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de " + proceso + " una solicitud de vacaciones "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que " + procesoConj.toUpperCase() + " LA SOLICITUD es: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            if (estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador() != null) {
                mensaje = mensaje + "La persona a cargo de HACER EL SEGUIMIENTO es: "
                        + estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador().getNombreCompleto()
                        + "\n";
            } else {
                if (estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe() != null) {
                    mensaje = mensaje + "La persona a cargo de HACER EL SEGUIMIENTO es: "
                            + estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getNombreCompleto()
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
                    //                            + "su empresa.";
                    + empleado.getEmpresa().getNombre() + " \n\n"
                    + "Cordial saludo. ";
            String respuesta1 = "";
            String respuesta2 = "";
            Calendar fechaEnvio = Calendar.getInstance();
//            Calendar fechaDisfrute = Calendar.getInstance();
//            fechaDisfrute.setTime(estSoliciSelec.getKioSoliciLocaliza());
            String asunto = "Reporte de Tiempos Laborados Kiosco - "
                    + procesoConj.toLowerCase() + ": " + fechaEnvio.get(Calendar.YEAR) + "/" + (fechaEnvio.get(Calendar.MONTH) + 1) + "/" + fechaEnvio.get(Calendar.DAY_OF_MONTH)
//                    + ". Inicio de vacaciones: " //                    + fechaDisfrute.get(Calendar.YEAR) + "/" + (fechaDisfrute.get(Calendar.MONTH) + 1) + "/" + fechaDisfrute.get(Calendar.DAY_OF_MONTH)
                    ;
            if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                        empleado.getPersona().getEmail(), asunto, mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            if (estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador() != null) {
                if (estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador().getEmail() != null && !estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            estSoliciSelec.getKioSoliciLocaliza().getKioAutorizador().getEmail(), asunto, mensaje, "");
                    respuesta2 = " Solicitud enviada correctamente al autorizador";
                } else {
                    respuesta2 = " La persona que autoriza la solicitud no tiene correo";
                }
            } else if (estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe() != null) {
                if (estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail() != null && !estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            estSoliciSelec.getKioSoliciLocaliza().getEmpleadoJefe().getPersona().getEmail(), asunto, mensaje, "");
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

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + ".procesarSolicitud()");
        System.out.println("procesarSolicitud-estado: " + estadoNuevo);
        System.out.println("procesarSolicitud-estadoSolicitud: " + estSoliciSelec);
        System.out.println("procesarSolicitud-empleado: " + empleado);
        System.out.println("procesarSolicitud-motivo: " + motivo);
        boolean res = false;
        boolean res2 = false;
        if ("CANCELADO".equals(estadoNuevo.getValor())) {
            try {
                try {
                    administrarHistoRepoTiempos.cambiarEstadoSolicitud(estSoliciSelec.getKioSoliciLocaliza(), empleado, motivo, motivo, empleado.getPersona());
//                    administrarProcesarSolicitud.cambiarEstadoSolicitud(this.estSoliciSelec.getKioSoliciVaca(), empleado, estadoNuevo.getValor(), motivo, null);
                } catch (NoSuchObjectLocalException nsole) {
                    System.out.println("procesarSolicitud-nsole-1: " + nsole.getMessage());
                    inicializarEJB();
                    administrarHistoRepoTiempos.cambiarEstadoSolicitud(estSoliciSelec.getKioSoliciLocaliza(), empleado, motivo, motivo, empleado.getPersona());
//                    administrarProcesarSolicitud.cambiarEstadoSolicitud(this.estSoliciSelec.getKioSoliciVaca(), empleado, estadoNuevo.getValor(), motivo, null);
                }
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error rechazando la Solicitud: " + msj);
                mensajeCreacion = msj;
                MensajesUI.error(msj);
            }
            if (res) {
                if ("ENVIADO".equalsIgnoreCase(estSoliciSelec.getEstado())) {
                    construirCorreo("cancelar", "canceló");
                }
//                mensajeCreacion = "Solicitud guardada correctamente";
//                    MensajesUI.info("Solicitud guardada correctamente");
//                MensajesUI.info(mensajeCreacion);
                PrimefacesContextUI.ejecutar("PF('soliciDialog').hide();");
            }
        } else if ("ENVIADO".equals(estadoNuevo.getValor())) {
            try {
                try {
                    administrarHistoRepoTiempos.cambiarEstadoSolicitud(estSoliciSelec.getKioSoliciLocaliza(), empleado, estadoNuevo.getValor(), motivo, empleado.getPersona());
                } catch (NoSuchObjectLocalException nsole) {
                    System.out.println("procesarSolicitud-nsole-2: " + nsole.getMessage());
                    inicializarEJB();
                    administrarHistoRepoTiempos.cambiarEstadoSolicitud(estSoliciSelec.getKioSoliciLocaliza(), empleado, estadoNuevo.getValor(), motivo, empleado.getPersona());
                }
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error registrando la solicitud: " + msj);
                mensajeCreacion = msj;
                MensajesUI.error(msj);
            }
            if (res) {
                construirCorreo("enviar", "envió");
//                mensajeCreacion = "Solicitud guardada correctamente";
////                MensajesUI.info("Solicitud guardada correctamente");
//                MensajesUI.info(mensajeCreacion);
                PrimefacesContextUI.ejecutar("PF('soliciDialog').hide();");
            }
        } else {
            mensajeCreacion = "No hay un estado seleccionado.";
//            MensajesUI.error("No hay un estado seleccionado.");
            MensajesUI.error(mensajeCreacion);
        }
        PrimefacesContextUI.ejecutar("PF('creandoSolici').hide();");
        PrimefacesContextUI.ejecutar("PF('resulEnvio').show();");
    }

    public Empleados getEmpleado() {
        System.out.println("getEmpleado-empleado: " + empleado);
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        System.out.println("setEmpleado-empleado: " + empleado);
        this.empleado = empleado;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciGuardadas() {
        System.out.println("getEstadoSoliciGuardadas-estadoSoliciGuardadas: " + estadoSoliciGuardadas);
        if (estadoSoliciGuardadas == null) {
            try {
                estadoSoliciGuardadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "GUARDADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciGuardadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciGuardadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "GUARDADO");
            }
        }
        return estadoSoliciGuardadas;
    }

    public void setEstadoSoliciGuardadas(List<KioEstadosLocalizaSolici> estadoSoliciGuardadas) {
        System.out.println("setEstadoSoliciGuardadas-estadoSoliciGuardadas: " + estadoSoliciGuardadas);
        this.estadoSoliciGuardadas = estadoSoliciGuardadas;
    }

    public KioEstadosLocalizaSolici getEstSoliciSelec() {
        System.out.println("getEstSoliciSelec-estSoliciSelec: " + estSoliciSelec);
        return estSoliciSelec;
    }

    public void setEstSoliciSelec(KioEstadosLocalizaSolici estSoliciSelec) {
        System.out.println("setEstSoliciSelec-estSoliciSelec: " + estSoliciSelec);
        if (estSoliciSelec != null) {
            OpcionLista enviado = new OpcionLista("ENVIAR", "ENVIADO");
            OpcionLista cancelado = new OpcionLista("CANCELAR", "CANCELADO");
            if ("GUARDADO".equals(estSoliciSelec.getEstado())) {
                this.listaEstados.clear();
                this.listaEstados.add(enviado);
                this.listaEstados.add(cancelado);
                this.estadoNuevo = enviado;
            } else if ("ENVIADO".equals(estSoliciSelec.getEstado())) {
                this.listaEstados.clear();
                this.listaEstados.add(cancelado);
                this.estadoNuevo = cancelado;
            }
        }
        this.estSoliciSelec = estSoliciSelec;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciEnviadas() {
        System.out.println("getEstadoSoliciEnviadas-estadoSoliciEnviadas: " + estadoSoliciEnviadas);
        if (estadoSoliciEnviadas == null) {
            try {
                estadoSoliciEnviadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "ENVIADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciEnviadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciEnviadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "ENVIADO");
            }
        }
        return estadoSoliciEnviadas;
    }

    public void setEstadoSoliciEnviadas(List<KioEstadosLocalizaSolici> estadoSoliciEnviadas) {
        System.out.println("setEstadoSoliciEnviadas-estadoSoliciEnviadas: " + estadoSoliciEnviadas);
        this.estadoSoliciEnviadas = estadoSoliciEnviadas;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciAprobadas() {
        System.out.println("getEstadoSoliciAprobadas-estadoSoliciAprobadas: " + estadoSoliciAprobadas);
        if (estadoSoliciAprobadas == null) {
            try {
                estadoSoliciAprobadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "AUTORIZADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciEnviadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciAprobadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "AUTORIZADO");
            }
        }
        return estadoSoliciAprobadas;
    }

    public void setEstadoSoliciAprobadas(List<KioEstadosLocalizaSolici> estadoSoliciAprobadas) {
        System.out.println("setEstadoSoliciAprobadas-estadoSoliciAprobadas: " + estadoSoliciAprobadas);
        this.estadoSoliciAprobadas = estadoSoliciAprobadas;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciRechazadas() {
        System.out.println("getEstadoSoliciRechazadas-estadoSoliciRechazadas: " + estadoSoliciRechazadas);
        if (estadoSoliciRechazadas == null) {
            try {
                estadoSoliciRechazadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "RECHAZADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciEnviadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciRechazadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "RECHAZADO");
            }
        }
        return estadoSoliciRechazadas;
    }

    public void setEstadoSoliciRechazadas(List<KioEstadosLocalizaSolici> estadoSoliciRechazadas) {
        System.out.println("setEstadoSoliciRechazadas-estadoSoliciRechazadas: " + estadoSoliciRechazadas);
        this.estadoSoliciRechazadas = estadoSoliciRechazadas;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciLiquidadas() {
        System.out.println("getEstadoSoliciLiquidadas-estadoSoliciLiquidadas: " + estadoSoliciLiquidadas);
        if (estadoSoliciLiquidadas == null) {
            try {
                estadoSoliciLiquidadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "LIQUIDADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciEnviadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciLiquidadas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "LIQUIDADO");
            }
        }
        return estadoSoliciLiquidadas;
    }

    public void setEstadoSoliciLiquidadas(List<KioEstadosLocalizaSolici> estadoSoliciLiquidadas) {
        System.out.println("setEstadoSoliciLiquidadas-estadoSoliciLiquidadas: " + estadoSoliciLiquidadas);
        this.estadoSoliciLiquidadas = estadoSoliciLiquidadas;
    }

    public List<KioEstadosLocalizaSolici> getEstadoSoliciCanceladas() {
        System.out.println("getEstadoSoliciCanceladas-estadoSoliciCanceladas: " + estadoSoliciCanceladas);
        if (estadoSoliciCanceladas == null) {
            try {
                estadoSoliciCanceladas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "CANCELADO");
            } catch (NoSuchObjectLocalException nsole) {
                System.out.println("getEstadoSoliciEnviadas-nsole: " + nsole.getMessage());
                inicializarEJB();
                estadoSoliciCanceladas = administrarHistoRepoTiempos.obtenerEstadosLocalizaSoliciXEmpleado(empleado, "CANCELADO");
            }
        }
        return estadoSoliciCanceladas;
    }

    public void setEstadoSoliciCanceladas(List<KioEstadosLocalizaSolici> estadoSoliciCanceladas) {
        System.out.println("setEstadoSoliciCanceladas-estadoSoliciCanceladas: " + estadoSoliciCanceladas);
        this.estadoSoliciCanceladas = estadoSoliciCanceladas;
    }

    public OpcionLista getEstadoNuevo() {
        System.out.println("getEstadoNuevo-estadoNuevo: " + estadoNuevo);
        return estadoNuevo;
    }

    public void setEstadoNuevo(OpcionLista estadoNuevo) {
        System.out.println("setEstadoNuevo-estadoNuevo: " + estadoNuevo);
        this.estadoNuevo = estadoNuevo;
    }

    public boolean isInacMotivo() {
        return inacMotivo;
    }

    public void setInacMotivo(boolean inacMotivo) {
        this.inacMotivo = inacMotivo;
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

    public String getGrupoEmpre() {
        return grupoEmpre;
    }

    public void setGrupoEmpre(String grupoEmpre) {
        this.grupoEmpre = grupoEmpre;
    }

    public List getListaEstados() {
        System.out.println("getListaEstados");
        if (listaEstados != null && !listaEstados.isEmpty()) {
            System.out.println("getListaEstados-size: " + listaEstados.size());
        } else {
            System.out.println("getListaEstados-vacia");
        }
        return listaEstados;
    }

    public void setListaEstados(List listaEstados) {
        System.out.println("setListaEstados");
        if (listaEstados != null && !listaEstados.isEmpty()) {
            System.out.println("setListaEstados-size: " + listaEstados.size());
        } else {
            System.out.println("setListaEstados-vacia");
        }
        this.listaEstados = listaEstados;
    }

    public List<KioLocalizacionesEmpl> getLocalizacionesEmpleado() {
        return localizacionesEmpleado;
    }

    public void setLocalizacionesEmpleado(List<KioLocalizacionesEmpl> localizacionesEmpleado) {
        this.localizacionesEmpleado = localizacionesEmpleado;
    }

}
