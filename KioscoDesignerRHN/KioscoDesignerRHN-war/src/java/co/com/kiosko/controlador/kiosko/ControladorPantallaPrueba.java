package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.entidades.Empleados;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@ViewScoped
public class ControladorPantallaPrueba implements Serializable {

    private Empleados empleado;

    public ControladorPantallaPrueba() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }
}
