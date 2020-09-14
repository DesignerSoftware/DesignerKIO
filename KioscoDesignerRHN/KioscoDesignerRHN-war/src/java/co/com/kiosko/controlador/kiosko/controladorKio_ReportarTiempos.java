package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarReporteTiempos;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizaciones;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
//import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Edwin Hastamorir
 */
@ManagedBean
@ViewScoped
public class controladorKio_ReportarTiempos implements Serializable {

    @EJB
    private IAdministrarReporteTiempos administrarReporteTiempos;
    @EJB
    private IAdministrarGenerarReporte administrarGenerarReporte;
    private Empleados empleado;
    private List<KioLocalizaciones> listaLocalizaciones;
    private List<KioLocalizaciones> listaLocalFiltro;
    private List<KioLocalizacionesEmpl> listaLocalizaEmpl;
    private KioLocalizacionesEmpl localizaEmplSelec;
    private KioSolicisLocaliza solicitud;
    private String mesAgno;
    private Calendar fechaActual;
    private BigDecimal sumPorcentaje;
    private String mensaje;
    private String observacion;
    private String grupoEmpre;
    private LeerArchivoXML leerArchivoXML;
    private HttpSession ses;
    private List<KioEstadosLocalizaSolici> listaEstadoLocaliza;
    private String estadoReporte;

    public controladorKio_ReportarTiempos() {
        System.out.println(this.getClass().getName() + ".controladorKio_ReportarTiempos()");
    }

    @PostConstruct
    public void inicializar() {
        System.out.println(this.getClass().getName() + ".inicializar()");
        fechaActual = Calendar.getInstance();
        fechaActual.set(Calendar.DAY_OF_MONTH, 1);
        sumPorcentaje = BigDecimal.ZERO;
        estadoReporte = "NUEVO";
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            ses = (HttpSession) x.getExternalContext().getSession(false);
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            long nit = Long.parseLong(((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit());
            grupoEmpre = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getGrupoSeleccionado();
            listaLocalizaEmpl = new ArrayList<KioLocalizacionesEmpl>();
            consultasIniciales(ses, nit);
            leerArchivoXML = new LeerArchivoXML();
            leerArchivoXML.leerArchivoConfigModulos();
//            crearSolicitud();
        } catch (ELException e) {
            System.out.println(this.getClass().getName() + "Error postconstruct " + ": " + e);
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        } catch (NumberFormatException e) {
            System.out.println(this.getClass().getName() + "Error postconstruct " + ": " + e);
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + "Error postconstruct " + ": " + e);
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    private void consultasIniciales(HttpSession ses, long nit) {
        System.out.println(this.getClass().getName() + ".consultasIniciales()");
        try {
            administrarReporteTiempos.obtenerConexion(ses.getId());
            administrarGenerarReporte.obtenerConexion(ses.getId());
            listaLocalizaciones = administrarReporteTiempos.obtenerKioLocalizaciones(empleado);
            consultaExistente();
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    private void consultaExistente() {
        try {
            listaEstadoLocaliza = administrarReporteTiempos.obtenerEstadoLocalizacion(empleado, fechaActual);
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + "-consultadoSolicitudExistente: " + ex);
            listaEstadoLocaliza = new ArrayList();
//            this.estadoReporte = "NUEVO";
        }
        if (listaEstadoLocaliza != null && !listaEstadoLocaliza.isEmpty()) {
            if (listaEstadoLocaliza.size() == 1) {
                estadoReporte = listaEstadoLocaliza.get(0).getEstado();
                solicitud = listaEstadoLocaliza.get(0).getKioSoliciLocaliza();
                listaLocalizaEmpl = administrarReporteTiempos.obtenerLocalizacionesXSolicitud(solicitud);
            }
        } else {
            listaEstadoLocaliza = new ArrayList();
            listaLocalizaEmpl = new ArrayList<KioLocalizacionesEmpl>();
            this.estadoReporte = "NUEVO";
        }
    }

    public void limpiarSolicitud() {
        System.out.println(this.getClass().getName() + " limpiarSolicitud()");
        solicitud = null;
        listaLocalizaciones = null;
        listaLocalFiltro = null;
        listaEstadoLocaliza = null;
        listaLocalizaEmpl = null;
        observacion = null;
    }

    public void preparaListaLocalizaciones() {
        System.out.println(this.getClass().getName() + ".preparaListaLocalizaciones()");
        this.localizaEmplSelec = new KioLocalizacionesEmpl();
        this.localizaEmplSelec.setEmpleado(empleado);
        this.localizaEmplSelec.setKioSolicisLocaliza(solicitud);
        this.localizaEmplSelec.setFecha(this.fechaActual.getTime());
        this.localizaEmplSelec.setPorcentaje(BigDecimal.ZERO);
    }

    public void adicionarLocalizacion() {
        System.out.println(this.getClass().getName() + ".adicionarLocalizacion()");
        try {
            System.out.println(this.getClass().getName() + localizaEmplSelec.getKioLocaliza().getCodigo() + localizaEmplSelec.getKioLocaliza().getNombre());
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + " error " + ex);
        }
        switch (verificarRegistro()) {
            case 0:
                this.listaLocalizaEmpl.add(localizaEmplSelec);
                PrimefacesContextUI.ejecutar("PF('dlgnuevoreg').hide();");
                break;
            case 1:
                MensajesUI.error("No ha seleccionado una localizacion. Por favor seleccione una de la lista.");
                PrimefacesContextUI.ejecutar("PF('dlgnuevoreg').show();");
                break;
            case 2:
                MensajesUI.error("No ha seleccionado una localizacion. Por favor seleccione una de la lista.");
                PrimefacesContextUI.ejecutar("PF('dlgnuevoreg').show();");
                break;
            case 3:
                MensajesUI.error("El porcentaje total supera el 100% al adicionar el registro que esta creando. Por favor disminuya el valor del porcentaje.");
                PrimefacesContextUI.ejecutar("PF('dlgnuevoreg').show();");
                break;
            case 4:
                MensajesUI.error("La localización seleccionada ya se encuentra en la lista a reportar. Por favor seleccione otra localización.");
                PrimefacesContextUI.ejecutar("PF('dlgnuevoreg').show();");
                break;
            default:
                MensajesUI.error("La verificación realizó una operación no controlada. Por favor vuelva a ingresar para evitar errores.");
        }

    }

    public boolean habilitarEnviar() {
        return sumPorcentaje.compareTo(new BigDecimal("100")) == 0;
    }

    private int verificarRegistro() {
        int res = 0;
        if (localizaEmplSelec.getKioLocaliza() == null) {
            res = 1;
        }
        if (localizaEmplSelec.getPorcentaje().compareTo(BigDecimal.ZERO) < 1) {
            res = 2;
        }
        if ((localizaEmplSelec.getPorcentaje().add(sumPorcentaje).compareTo(new BigDecimal("100")) == 1)) {
            res = 3;
        }
        if (revisarListaLocalizaElegidas(localizaEmplSelec.getKioLocaliza())) {
            res = 4;
        }
        return res;
    }

    private boolean revisarListaLocalizaElegidas(KioLocalizaciones localizaRev) {
        try {
            for (KioLocalizacionesEmpl elemento : listaLocalizaEmpl) {
                if (elemento.getKioLocaliza().getSecuencia().compareTo(localizaRev.getSecuencia()) == 0) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException npe) {
            System.out.println(this.getClass().getName() + " " + npe);
            return false;
        }
    }

    public void retrocederMes() {
        /*if (listaLocalizaEmpl.isEmpty()) {
            fechaActual.add(Calendar.MONTH, -1);
        } else {
            MensajesUI.error("Ya tiene registros asociados a este mes");
        }*/
        fechaActual.add(Calendar.MONTH, -1);
//        MensajesUI.info("El mes es: " + fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.forLanguageTag("es-CO")) + " " + fechaActual.get(Calendar.YEAR));
        consultaExistente();
    }

    public void avanzarMes() {
        /*if (listaLocalizaEmpl.isEmpty()) {
            fechaActual.add(Calendar.MONTH, 1);
        } else {
            MensajesUI.error("Ya tiene registros asociados a este mes");
        }*/
        fechaActual.add(Calendar.MONTH, 1);
//        MensajesUI.info("El mes es: " + fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.forLanguageTag("es-CO")) + " " + fechaActual.get(Calendar.YEAR));
        consultaExistente();
    }

    public void resumaPorcentaje() {
        getSumPorcentaje();
        PrimeFaces.current().ajax().update("principalForm:frmreportatiempo:tbreporte:sumporcen");
    }

    private void generarAuditoria() {
        System.out.println(this.getClass().getName() + ".generarAuditoria()");
        Calendar dtGen = Calendar.getInstance();
        List<String> cuentasAud = leerArchivoXML.getCuentasAudOp("reporteTiempos", empleado.getEmpresa().getNit(), "0141");
        System.out.println("cuentas: " + cuentasAud);
        System.out.println("Enviando mensaje de auditoria en la creacion de la solicitud de vacaciones");
        if (cuentasAud != null && !cuentasAud.isEmpty()) {
            for (String cuentaAud : cuentasAud) {
                String mensaje = "Apreciado usuario(a):\n\n"
                        + "Nos permitimos informar que " + empleado.getPersona().getNombreCompleto() + " generó un REPORTE DE TIEMPOS LABORADOS el "
                        + dtGen.get(Calendar.DAY_OF_MONTH) + "/" + (dtGen.get(Calendar.MONTH) + 1) + "/" + dtGen.get(Calendar.YEAR)
                        + " a las " + dtGen.get(Calendar.HOUR_OF_DAY) + ":" + dtGen.get(Calendar.MINUTE) + " en el módulo de Kiosco Nómina Designer.\n\n"
                        + "Recuerde:\n"
                        + "Esta dirección de correo es utilizada solamente para envíos automáticos de la información solicitada. " + "\n\n"
                        + "Cordial saludo." + "\n\n--\n"
                        + "Por favor no responda este correo, ya que no podrá ser atendido. "
                        + "Si desea contactarse con nosotros, envíe un correo o comuníquese telefónicamente con Talento Humano de " + empleado.getEmpresa().getNombre() + "\n\n";
                String asunto = "Auditoria: Nuevo Reporte de tiempos ";
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

    private void construirCorreo() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            String mensaje = "Apreciado usuario(a): \n\n"
                    + "Nos permitimos informar que se acaba de crear un reporte de tiempos laborados "
                    + "en el módulo de Kiosco Nómina Designer. "
                    + "Por favor llevar el caso desde su cuenta de usuario en el portal de Kiosco "
                    + "y continuar con el proceso. \n\n"
                    + "La persona que CREÓ EL REPORTE es: "
                    + empleado.getPersona().getNombreCompleto() + "\n";
            if (solicitud.getKioAutorizador() != null) {
                mensaje = mensaje + "La persona a cargo de DAR APROBACIÓN es: "
                        + solicitud.getKioAutorizador().getNombreCompleto()
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
            System.out.println("mensaje: " + mensaje);
            String respuesta1 = "";
            String respuesta2 = "";
            Calendar fechaEnvio = Calendar.getInstance();
            String asunto = "Reporte de tiempos laborados - Nueva solicitud" + ": "
                    + fechaEnvio.get(Calendar.YEAR) + "/" + (fechaEnvio.get(Calendar.MONTH) + 1) + "/" + fechaEnvio.get(Calendar.DAY_OF_MONTH)
                    + ". Mes reportado: "
                    + mesAgno;
            System.out.println("email: " + empleado.getPersona().getEmail());
            if (empleado.getPersona().getEmail() != null && !empleado.getPersona().getEmail().isEmpty()) {
                administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                        empleado.getPersona().getEmail(), asunto, mensaje, "");
                respuesta1 = "Solicitud enviada correctamente al empleado";
            } else {
                respuesta1 = "El empleado no tiene correo registrado";
            }
            System.out.println("kioautorizador: " + solicitud.getKioAutorizador());
            System.out.println("EmpleadoJefe: " + solicitud.getEmpleadoJefe());
            if (solicitud.getKioAutorizador() != null) {
                System.out.println("kioautorizador-email: " + solicitud.getKioAutorizador().getEmail());
                if (solicitud.getKioAutorizador().getEmail() != null && !solicitud.getKioAutorizador().getEmail().isEmpty()) {
                    administrarGenerarReporte.enviarCorreo(empleado.getEmpresa().getSecuencia(),
                            solicitud.getKioAutorizador().getEmail(), asunto, mensaje, "");
                    respuesta2 = " Solicitud enviada correctamente al autorizador";
                } else {
                    respuesta2 = " La persona que autoriza la solicitud no tiene correo";
                }
            } else if (solicitud.getEmpleadoJefe() != null) {
                System.out.println("getEmpleadoJefe-email: " + solicitud.getEmpleadoJefe().getPersona().getEmail());
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
            mensaje = respuesta;
            MensajesUI.info(respuesta);
        } catch (Exception e) {
            mensaje = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error guardarSolicitud: " + mensaje);
            MensajesUI.error(mensaje);
        }
        try {
            generarAuditoria();
        } catch (Exception ex) {
            System.out.println("construirCorreo ERROR generando auditoria: ");
            System.out.println(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    private void crearSolicitud() {
        System.out.println(this.getClass().getName() + ".crearSolicitud()");
        solicitud = new KioSolicisLocaliza();
        solicitud.setEmpleado(empleado);
        solicitud.setActiva("S");
        try {
            solicitud.setUsuario(administrarReporteTiempos.consultarUsuario());
        } catch (Exception ex1) {
            System.out.println(this.getClass().getName() + " consultado usuario ex: " + ex1);
        }
        solicitud
                .setFechaGeneracion(Calendar.getInstance().getTime());
        try {
            solicitud.setEmpleadoJefe(administrarReporteTiempos.consultarEmpleadoJefe(empleado));
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + " consultando jefe ex: " + ex);
        }
    }

    public void enviarSolicitud() {
        System.out.println(this.getClass().getName() + ".enviarSolicitud()");
        boolean res = false;
        this.crearSolicitud();
        try {
            administrarReporteTiempos.enviarReporteTiemposLab(solicitud, listaLocalizaEmpl, empleado, observacion);
            res = true;
        } catch (Exception e) {
            mensaje = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("Error guardarSolicitud: " + mensaje);
            MensajesUI.error(mensaje);
        }
        if (res) {
            construirCorreo();
        }
        PrimefacesContextUI.ejecutar("PF('confirmEnvio').hide()");
        PrimefacesContextUI.ejecutar("PF('creandoSolici').hide()");
        PrimefacesContextUI.ejecutar("PF('resulEnvio').show()");
    }

    public boolean permiteAdicionar() {
        if (this.sumPorcentaje.compareTo(new BigDecimal("100")) == -1) {
            return true;
        } else {
            return false;
        }
    }

    ////** ACCESORES **////
    public String getMesAgno() {
        mesAgno = fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.forLanguageTag("es-CO")) + " " + fechaActual.get(Calendar.YEAR);
        System.out.println(this.getClass().getName() + " mesAgno: " + mesAgno);
        return mesAgno;
    }

    public void setMesAgno(String mesAgno) {
        this.mesAgno = mesAgno;
    }

    public KioSolicisLocaliza getSolicitud() {
        if (solicitud == null) {
            inicializar();
        }
        return solicitud;
    }

    public void setSolicitud(KioSolicisLocaliza solicitud) {
        this.solicitud = solicitud;
    }

    public List<KioLocalizaciones> getListaLocalizaciones() {
        if (listaLocalizaciones != null && !listaLocalizaciones.isEmpty()) {
        } else {
            listaLocalizaciones = administrarReporteTiempos.obtenerKioLocalizaciones(empleado);
        }
        return listaLocalizaciones;
    }

    public void setListaLocalizaciones(List<KioLocalizaciones> listaLocalizaciones) {
        this.listaLocalizaciones = listaLocalizaciones;
    }

    public List<KioLocalizaciones> getListaLocalFiltro() {
        System.out.println(this.getClass().getName() + ".getListaLocalFiltro()");
        return listaLocalFiltro;
    }

    public void setListaLocalFiltro(List<KioLocalizaciones> listaLocalFiltro) {
        this.listaLocalFiltro = listaLocalFiltro;
    }

    public List<KioLocalizacionesEmpl> getListaLocalizaEmpl() {
        System.out.println(this.getClass().getName() + ".getListaLocalizaEmpl()");
        if (listaLocalizaEmpl == null) {
            listaLocalizaEmpl = new ArrayList<KioLocalizacionesEmpl>();
        }
        return listaLocalizaEmpl;
    }

    public void setListaLocalizaEmpl(List<KioLocalizacionesEmpl> listaLocalizaEmpl) {
        this.listaLocalizaEmpl = listaLocalizaEmpl;
    }

    public BigDecimal getSumPorcentaje() {
        if (listaLocalizaEmpl != null && !listaLocalizaEmpl.isEmpty()) {
            sumPorcentaje = BigDecimal.ZERO;
            for (KioLocalizacionesEmpl localizaEmpl : listaLocalizaEmpl) {
                sumPorcentaje = sumPorcentaje.add(localizaEmpl.getPorcentaje());
            }
        } else {
            sumPorcentaje = BigDecimal.ZERO;
        }
        return sumPorcentaje;
    }

    public void setSumPorcentaje(BigDecimal sumPorcentaje) {
        this.sumPorcentaje = sumPorcentaje;
    }

    public List<KioEstadosLocalizaSolici> getListaEstadoLocaliza() {
        return listaEstadoLocaliza;
    }

    public void setListaEstadoLocaliza(List<KioEstadosLocalizaSolici> listaEstadoLocaliza) {
        this.listaEstadoLocaliza = listaEstadoLocaliza;
    }
    
    public KioLocalizacionesEmpl getLocalizaEmplSelec() {
        return localizaEmplSelec;
    }

    public void setLocalizaEmplSelec(KioLocalizacionesEmpl localizaEmplSelec) {
        this.localizaEmplSelec = localizaEmplSelec;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        System.out.println(this.getClass().getName() + ".setObservacion()");
        System.out.println(this.getClass().getName() + ".OBS: " + observacion);
        this.observacion = observacion;
    }

    public String getEstadoReporte() {
        return estadoReporte;
    }

    public void setEstadoReporte(String estadoReporte) {
        this.estadoReporte = estadoReporte;
    }

}
