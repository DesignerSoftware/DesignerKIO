package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.KioSoliciVacas;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Edwin
 */
@ManagedBean
@ViewScoped
public class ControladorKio_ProgramarVacas implements Serializable{

    private Empleados empleado;
    private List<Empleados> empleadosACargo;
    private Empleados empleadoSelec;
    private String urlMenuNavegation;
    private List<Integer> dias;
    private Integer diaSelecto;
    private List<String> tiposVaca;
    private String tipoSelecto;
    private KioSoliciVacas solicitud;
    
    public ControladorKio_ProgramarVacas() {
    }
    
    @PostConstruct
    public void inicializar() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            solicitud = new KioSoliciVacas();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    public void guardarSolicitud(){
        //Administrar guardar solicitud junto con la novedad y el estado.
        //hay que manejar las excepciones provocadas por:
        //- ya existe la solicitud con mismo inicio de disfrute
        //- no se pudo registrar la solicitud.
    }
    public void enviarSolicitud(){
        //Administrar enviar la solicitud por correo para notificar al jefe. 
        //También debe generar un registro del estado de la solicitud.
        //Hay que manejar las excepciones que se presentan por:
        //- no se pudo enviar el correo.
        //- no se pudo guardar el registro del estado.
    }
    
    public void buscarEmpleado(){
        
    }
    
    public void mostrarEmpleados(){
        
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

    public Empleados getEmpleadoSelec() {
        return empleadoSelec;
    }

    public void setEmpleadoSelec(Empleados empleadoSelec) {
        this.empleadoSelec = empleadoSelec;
    }

    public Integer getDiaSelecto() {
        return diaSelecto;
    }

    public void setDiaSelecto(Integer diaSelecto) {
        this.diaSelecto = diaSelecto;
    }

    public String getTipoSelecto() {
        return tipoSelecto;
    }

    public void setTipoSelecto(String tipoSelecto) {
        this.tipoSelecto = tipoSelecto;
    }

    public KioSoliciVacas getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(KioSoliciVacas solicitud) {
        this.solicitud = solicitud;
    }

    public List<Integer> getDias() {
        return dias;
    }

    public List<String> getTiposVaca() {
        return tiposVaca;
    }
    
}
