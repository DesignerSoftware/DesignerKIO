package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOlvidoClave;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@ViewScoped
public class ControladorOlvidoClave implements Serializable {

    @EJB
    private IAdministrarOlvidoClave administrarOlvidoClave;
    private ConexionesKioskos conexion;
    private String claveActual, clave, confirmacion, respuesta1, respuesta2;
    //VALORES INGRESO
    String usuario;

    public ControladorOlvidoClave() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOlvidoClave.obtenerConexion(ses.getId());
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            conexion = administrarOlvidoClave.obtenerConexionEmpleado(usuario);
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void cambiarClave() {
        if (respuesta1 != null && !respuesta1.isEmpty()
                && respuesta2 != null && !respuesta2.isEmpty()
                && claveActual != null && !claveActual.isEmpty()
                && clave != null && !clave.isEmpty()
                && confirmacion != null && !confirmacion.isEmpty()) {
            if (administrarOlvidoClave.validarRespuestas(respuesta1, respuesta2,
                    conexion.getRespuesta1(), conexion.getRespuesta2())) {
                if (claveActual.equals(administrarOlvidoClave.desEncriptar(conexion.getPwd()))) {
                    if (clave.equals(confirmacion)) {
                        conexion.setPwd(administrarOlvidoClave.encriptar(clave));
                        if (administrarOlvidoClave.cambiarClave(conexion)) {
                            PrimefacesContextUI.ejecutar("PF('dlgProcesoFinalizado').show()");
                        }
                    } else {
                        MensajesUI.warn("La contraseña no coincide.");
                    }
                } else {
                    MensajesUI.warn("La contraseña actual es incorrecta, por favor verifique.");
                }
            } else {
                MensajesUI.warn("Respuestas incorrectas, por favor verifique.");
            }
        } else {
            MensajesUI.warn("Todos los campos son obligatorios.");
        }
    }

    public void cambiarClaveOlvido() {
        if (respuesta1 != null && !respuesta1.isEmpty()
                && respuesta2 != null && !respuesta2.isEmpty()
                && clave != null && !clave.isEmpty()
                && confirmacion != null && !confirmacion.isEmpty()) {
            if (administrarOlvidoClave.validarRespuestas(respuesta1, respuesta2,
                    conexion.getRespuesta1(), conexion.getRespuesta2())) {
                if (clave.equals(confirmacion)) {
                    conexion.setPwd(administrarOlvidoClave.encriptar(clave));
                    if (administrarOlvidoClave.cambiarClave(conexion)) {
                        PrimefacesContextUI.ejecutar("PF('dlgProcesoFinalizado').show()");
                    }
                } else {
                    MensajesUI.warn("La contraseña no coincide.");
                }
            } else {
                MensajesUI.warn("Respuestas incorrectas, por favor verifique.");
            }
        } else {
            MensajesUI.warn("Todos los campos son obligatorios.");
        }
    }

    //GETTER AND SETTER
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public ConexionesKioskos getConexion() {
        return conexion;
    }

    public void setConexion(ConexionesKioskos conexion) {
        this.conexion = conexion;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getClaveActual() {
        return claveActual;
    }

    public void setClaveActual(String claveActual) {
        this.claveActual = claveActual;
    }
}
