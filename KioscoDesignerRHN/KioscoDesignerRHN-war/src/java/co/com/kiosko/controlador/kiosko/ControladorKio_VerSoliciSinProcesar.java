package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.utilidadesUI.MensajesUI;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author Edwin
 */
@ManagedBean
@ViewScoped
public class ControladorKio_VerSoliciSinProcesar implements Serializable{

    private Empleados empleado;
    private List<Empleados> empleadosACargo;
    private List<Empleados> emplACargoFiltro;
    private List<KioEstadosSolici> soliciEmpleado;
    private Empleados empleadoSelec;
    private String urlMenuNavegation;
    @EJB
    IAdministrarHistoVacas administrarHistoVacas;
    
    public ControladorKio_VerSoliciSinProcesar() {
        empleadosACargo = new ArrayList<Empleados>();
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
        } catch (Exception e){
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
            empleadosACargo = administrarHistoVacas.consultarEmpleadosEmpresa(nit);
            System.out.println("consultasIniciales: num empleados: " + empleadosACargo.size());
        } catch (Exception e) {
            String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            MensajesUI.error(msj);
        }
    }

    public void onRowSelect(SelectEvent event) {
        setEmpleadoSelec((Empleados) event.getObject());
        getSoliciEmpleado();
        FacesMessage msg = new FacesMessage("Empleado elegido", ((Empleados) event.getObject()).getSecuencia().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Empleado liberado", ((Empleados) event.getObject()).getSecuencia().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void buscarEmpleado() {

    }

    public void mostrarEmpleados() {

    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<Empleados> getEmpleadosACargo() {
        return empleadosACargo;
    }

    public void setEmpleadosACargo(List<Empleados> empleadosACargo) {
        this.empleadosACargo = empleadosACargo;
    }

    public List<Empleados> getEmplACargoFiltro() {
        return emplACargoFiltro;
    }

    public void setEmplACargoFiltro(List<Empleados> emplACargoFiltro) {
        this.emplACargoFiltro = emplACargoFiltro;
    }

    public List<KioEstadosSolici> getSoliciEmpleado() {
        if (empleadoSelec != null) {
            if (soliciEmpleado == null) {
                try {
                    administrarHistoVacas.consultarEstadoSoliciEmpl(empleadoSelec);
                } catch (Exception e) {
                    String msj = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
                    MensajesUI.error(msj);
                }
            }
        }
        return soliciEmpleado;
    }

    public void setSoliciEmpleado(List<KioEstadosSolici> soliciEmpleado) {
        this.soliciEmpleado = soliciEmpleado;
    }

    public Empleados getEmpleadoSelec() {
        return empleadoSelec;
    }

    public void setEmpleadoSelec(Empleados empleadoSelec) {
        this.empleadoSelec = empleadoSelec;
    }
}
