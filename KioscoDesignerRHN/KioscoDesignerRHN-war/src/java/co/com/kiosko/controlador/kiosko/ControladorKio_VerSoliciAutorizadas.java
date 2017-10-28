package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.administrar.interfaz.IAdministrarRegistrarSolicitud;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
public class ControladorKio_VerSoliciAutorizadas implements Serializable {

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
    private Date fpago;
    private String proceso;
    private String mensajeCreacion;
    private String tituloProcesar;

    @EJB
    IAdministrarHistoVacas administrarHistoVacas;
    @EJB
    IAdministrarRegistrarSolicitud administrarRegistrarSolicitud;

    public ControladorKio_VerSoliciAutorizadas() {
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
        setEstadoNuevo("LIQUIDADO");
        fpago = new Date();
        proceso = "N";
        try {
            administrarHistoVacas.obtenerConexion(ses.getId());
            administrarRegistrarSolicitud.obtenerConexion(ses.getId());
            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "AUTORIZADO", null);
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
        this.motivo = null;
        this.setEstadoNuevo(null);
        this.proceso = null;
        this.fpago = null;
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
        System.out.println("cambiarEstado-soliciSelec: " + this.estadoNuevo);
        inacMotivo = !estadoNuevo.equals("RECHAZADO");
    }

    public void cambiarProceso() {
        System.out.println(this.getClass().getName() + ".cambiarProceso()");
        System.out.println("cambiarProceso-soliciSelec: " + this.solicitudSelec);
        System.out.println("cambiarProceso-proceso: " + this.proceso);
        fpago = administrarRegistrarSolicitud.calcularFechaPago(solicitudSelec.getKioSoliciVaca().getEmpleado(),
                solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute(),
                solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getAdelantaPagoHasta(),
                proceso);
        System.out.println("cambiarProceso-fpago: " + this.fpago);
    }

    public void onDateSelect(SelectEvent event) {
        System.out.println("Fecha seleccionada: " + event.getObject());
        if (validaFechaPago()) {
            Calendar cl = Calendar.getInstance();
            MensajesUI.error("La fecha seleccionada es inferior a la última fecha de pago.");
            cl.add(Calendar.DAY_OF_MONTH, 1);
            solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().setFechaInicialDisfrute(cl.getTime());
        }
        PrimefacesContextUI.actualizar(":principalForm:dtfpago");
    }

    private boolean validaFechaPago() {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(administrarRegistrarSolicitud.fechaPago(solicitudSelec.getKioSoliciVaca().getEmpleado()));
        return solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute().after(c2.getTime());
    }

    public boolean activarCamposLiqui() {
        return !inacMotivo;
    }

    public void procesarSolicitud() {
        System.out.println(this.getClass().getName() + ".procesarSolicitud()");
        System.out.println("procesarSolicitud-estado: " + estadoNuevo);
        boolean contin = false;
        boolean res = false;
        boolean res2 = false;
        solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().setFechaPago(fpago);
        try {
            solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().setPagarPorFuera(proceso);
            contin = true;
        } catch (Exception e) {
            System.out.println("Error asignando el proceso a la novedad: " + e);
            mensajeCreacion = e.getMessage();
//            MensajesUI.error(e.getMessage());
            MensajesUI.error(mensajeCreacion);
            contin = false;
        }
        if ("RECHAZADO".equals(estadoNuevo) && contin) {
            if ((motivo == null || motivo.isEmpty())) {
                mensajeCreacion = "El motivo no debe estar vacio";
//                MensajesUI.error("El motivo no debe estar vacio");
                MensajesUI.error(mensajeCreacion);
                contin = false;
            } else {
                try {
                    administrarRegistrarSolicitud.cambiarEstadoSolicitud(this.solicitudSelec.getKioSoliciVaca(), empleado, estadoNuevo, motivo);
                    res = true;
                } catch (Exception ex) {
                    String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                    System.out.println("Error rechazando la Solicitud: " + msj);
                    mensajeCreacion = msj;
                    MensajesUI.error(msj);
                }
                if (res) {
                    mensajeCreacion = "Solicitud guardada correctamente";
//                    MensajesUI.info("Solicitud guardada correctamente");
                    MensajesUI.info(mensajeCreacion);
                    PrimefacesContextUI.ejecutar("PF('soliciDialog').hide();");
                }
            }
        } else if ("LIQUIDADO".equals(estadoNuevo) && contin) {
            try {
                administrarRegistrarSolicitud.cambiarEstadoSolicitud(solicitudSelec.getKioSoliciVaca(), empleado, estadoNuevo, motivo);
                administrarRegistrarSolicitud.registrarNovedad(solicitudSelec.getKioSoliciVaca());
                res = true;
            } catch (Exception ex) {
                String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(ex);
                System.out.println("Error registrando la solicitud: " + msj);
                mensajeCreacion = msj;
                MensajesUI.error(msj);
            }
            if (res) {
                mensajeCreacion = "Solicitud guardada correctamente";
//                MensajesUI.info("Solicitud guardada correctamente");
                MensajesUI.info(mensajeCreacion);
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
                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(empleado.getEmpresa(), "AUTORIZADO", null);
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
        fpago = administrarRegistrarSolicitud.calcularFechaPago(solicitudSelec.getKioSoliciVaca().getEmpleado(),
                solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getFechaInicialDisfrute(),
                solicitudSelec.getKioSoliciVaca().getKioNovedadesSolici().getAdelantaPagoHasta(),
                proceso);
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
        System.out.println("setEstadoNuevo-estadoNuevo: "+estadoNuevo);
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

    public Date getFpago() {
        return fpago;
    }

    public void setFpago(Date fpago) {
        this.fpago = fpago;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getMensajeCreacion() {
        System.out.println("getMensajeCreacion-mensajeCreacion: "+mensajeCreacion);
        return mensajeCreacion;
    }

    public void setMensajeCreacion(String mensajeCreacion) {
        this.mensajeCreacion = mensajeCreacion;
    }

    public String getTituloProcesar() {
        tituloProcesar = (solicitudSelec == null ? "SELECCIONE UNA SOLICITUD" : "DETALLE DE LA SOLICITUD");
        return tituloProcesar;
    }

    public void setTituloProcesar(String tituloProcesar) {
        this.tituloProcesar = tituloProcesar;
    }

}
