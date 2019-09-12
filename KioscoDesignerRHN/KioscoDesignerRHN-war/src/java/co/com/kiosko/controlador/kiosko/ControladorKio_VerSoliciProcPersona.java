package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author PC_Angelo
 */
@ManagedBean
@ViewScoped
public class ControladorKio_VerSoliciProcPersona implements Serializable {

    private Personas personaCon;
    private List<Empleados> empleadosACargo;
    private List<Empleados> emplACargoFiltro;
    private List<KioEstadosSolici> soliciEmpleado;
    private List<KioEstadosSolici> soliciFiltradas;
    private Empleados empleadoSelec;
    private KioEstadosSolici solicitudSelec;
    private String urlMenuNavegation;
    private String grupoEmpre;
    @EJB
    IAdministrarHistoVacas administrarHistoVacas;
    private long nit;

    public ControladorKio_VerSoliciProcPersona() {
    }

    @PostConstruct
    public void inicializar() {
        System.out.println(this.getClass().getName() + ".inicializar()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            personaCon = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getPersona();
            nit = Long.parseLong(((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit());
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
            soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre(nit, "PROCESADO", personaCon);
            System.out.println("consultasIniciales: num estados solicitudes: " + soliciEmpleado.size());
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            System.out.println("consultasIniciales-msj: " + msj);
            MensajesUI.error(msj);
            PrimefacesContextUI.actualizar("principalForm:growl");
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

    public List<Empleados> getEmpleadosACargo() {
        System.out.println(this.getClass().getName() + ".getEmpleadosACargo()");
        if (empleadosACargo == null || empleadosACargo.isEmpty()) {
            try {
                //empleadosACargo = administrarHistoVacas.consultarEmpleadosEmpresa(nit); 
                empleadosACargo=administrarHistoVacas.consultarEmpleadosAutorizador(nit, personaCon); //se añadió un filtro por autorizador
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
                    soliciEmpleado = administrarHistoVacas.consultarEstadoSoliciEmpre( nit, "PROCESADO", personaCon);
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

}
