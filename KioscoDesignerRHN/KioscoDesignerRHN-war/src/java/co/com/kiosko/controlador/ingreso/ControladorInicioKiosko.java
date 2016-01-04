package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.administrar.entidades.Empleados;
import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorInicioKiosko implements Serializable {

    @EJB
    private IAdministrarInicioKiosko administrarInicioKiosko;
    private String usuario;
    private Empleados empleado;

    public ControladorInicioKiosko() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicioKiosko.obtenerConexion(ses.getId());
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            empleado = administrarInicioKiosko.consultarEmpleado(new BigInteger(usuario));
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //GETTER AND SETTER
    public Empleados getEmpleado() {
        return empleado;
    }
}
