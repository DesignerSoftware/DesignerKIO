package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarVerSolicitudesEmpl;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.*;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Edwin
 */
@ManagedBean
@ViewScoped
public class ControladorKio_VerSolicitudesEmpleado implements Serializable{

    @EJB
    IAdministrarVerSolicitudesEmpl administrarVerSolicitudesEmpl;

    private Empleados empleado;
    private String urlMenuNavegation;
    private List<KioEstadosSolici> estadoSoliciGuardadas;
    private List<KioEstadosSolici> estadoSoliciEnviadas;
    private List<KioEstadosSolici> estadoSoliciAprobadas;
    private List<KioEstadosSolici> estadoSoliciRechazadas;
    private List<KioEstadosSolici> estadoSoliciLiquidadas;
    private List<KioEstadosSolici> estadoSoliciCanceladas;
    private KioEstadosSolici estSoliciSelec;

    public ControladorKio_VerSolicitudesEmpleado() {
        System.out.println(this.getClass().getName());
        System.out.println("Llamada al constructor");
    }

    @PostConstruct
    public void inicializar() {
        System.out.println(this.getClass().getName() + ".inicializar()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            administrarVerSolicitudesEmpl.obtenerConexion(ses.getId());
            estadoSoliciGuardadas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "GUARDADO");
            estadoSoliciEnviadas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "ENVIADO");
            estadoSoliciAprobadas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "AUTORIZADO");
            estadoSoliciRechazadas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "RECHAZADO");
            estadoSoliciLiquidadas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "LIQUIDADO");
            estadoSoliciCanceladas = administrarVerSolicitudesEmpl.consultarEstaSolici(empleado, "CANCELADO");
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void guardarSolicitud() {
        //Administrar guardar solicitud junto con la novedad y el estado.
        //hay que manejar las excepciones provocadas por:
        //- ya existe la solicitud con mismo inicio de disfrute
        //- no se pudo registrar la solicitud.
    }

    public void enviarSolicitud() {
        //Administrar enviar la solicitud por correo para notificar al jefe. 
        //También debe generar un registro del estado de la solicitud.
        //Hay que manejar las excepciones que se presentan por:
        //- no se pudo enviar el correo.
        //- no se pudo guardar el registro del estado.
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<KioEstadosSolici> getEstadoSoliciGuardadas() {
        return estadoSoliciGuardadas;
    }

    public void setEstadoSoliciGuardadas(List<KioEstadosSolici> estadoSoliciGuardadas) {
        this.estadoSoliciGuardadas = estadoSoliciGuardadas;
    }

    public KioEstadosSolici getEstSoliciSelec() {
        return estSoliciSelec;
    }

    public void setEstSoliciSelec(KioEstadosSolici estSoliciSelec) {
        this.estSoliciSelec = estSoliciSelec;
    }

    public List<KioEstadosSolici> getEstadoSoliciEnviadas() {
        return estadoSoliciEnviadas;
    }

    public void setEstadoSoliciEnviadas(List<KioEstadosSolici> estadoSoliciEnviadas) {
        this.estadoSoliciEnviadas = estadoSoliciEnviadas;
    }

    public List<KioEstadosSolici> getEstadoSoliciAprobadas() {
        return estadoSoliciAprobadas;
    }

    public void setEstadoSoliciAprobadas(List<KioEstadosSolici> estadoSoliciAprobadas) {
        this.estadoSoliciAprobadas = estadoSoliciAprobadas;
    }

    public List<KioEstadosSolici> getEstadoSoliciRechazadas() {
        return estadoSoliciRechazadas;
    }

    public void setEstadoSoliciRechazadas(List<KioEstadosSolici> estadoSoliciRechazadas) {
        this.estadoSoliciRechazadas = estadoSoliciRechazadas;
    }

    public List<KioEstadosSolici> getEstadoSoliciLiquidadas() {
        return estadoSoliciLiquidadas;
    }

    public void setEstadoSoliciLiquidadas(List<KioEstadosSolici> estadoSoliciLiquidadas) {
        this.estadoSoliciLiquidadas = estadoSoliciLiquidadas;
    }

    public List<KioEstadosSolici> getEstadoSoliciCanceladas() {
        return estadoSoliciCanceladas;
    }

    public void setEstadoSoliciCanceladas(List<KioEstadosSolici> estadoSoliciCanceladas) {
        this.estadoSoliciCanceladas = estadoSoliciCanceladas;
    }

}
