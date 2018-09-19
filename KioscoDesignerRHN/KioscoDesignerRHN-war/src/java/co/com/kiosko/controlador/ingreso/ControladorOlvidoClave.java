package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.ParametrizaClave;
import co.com.kiosko.administrar.interfaz.IAdministrarOlvidoClave;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Trivi�o
 */
@ManagedBean
@ViewScoped
public class ControladorOlvidoClave implements Serializable {

    @EJB
    private IAdministrarOlvidoClave administrarOlvidoClave;
    @EJB
    private IAdministrarIngreso administrarIngreso;
    private ConexionesKioskos conexion;
    private String claveActual, clave, confirmacion, respuesta1, respuesta2;
    //VALORES INGRESO
    private String usuario, nit;
    //FORMATO CLAVE
    private ParametrizaClave pc;

    public ControladorOlvidoClave() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            nit = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
            String unidadPersistencia = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUnidadPersistenciaIngreso();
            CadenasKioskos cadena = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).validarUnidadPersistencia(unidadPersistencia);
            administrarIngreso.conexionIngreso(cadena.getCadena());
            administrarIngreso.adicionarConexionUsuario(ses.getId(), cadena.getEsquema());
            administrarOlvidoClave.obtenerConexion(ses.getId());
            pc = administrarOlvidoClave.obtenerFormatoClave(Long.parseLong(nit));
            //conexion = administrarOlvidoClave.obtenerConexionEmpleado(usuario, nit);
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
                        if (validarClave(clave)) {
                            conexion.setPwd(administrarOlvidoClave.encriptar(clave));
                            if (administrarOlvidoClave.cambiarClave(conexion)) {
                                PrimefacesContextUI.ejecutar("PF('dlgProcesoFinalizado').show()");
                            }
                        }
                    } else {
                        MensajesUI.warn("La contrase�a no coincide.");
                    }
                } else {
                    MensajesUI.warn("La contrase�a actual es incorrecta, por favor verifique.");
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
                    if (validarClave(clave)) {
                        conexion.setPwd(administrarOlvidoClave.encriptar(clave));
                        if (administrarOlvidoClave.cambiarClave(conexion)) {
                            PrimefacesContextUI.ejecutar("PF('dlgProcesoFinalizado').show()");
                        }
                    }
                } else {
                    MensajesUI.warn("La contrase�a no coincide.");
                }
            } else {
                MensajesUI.warn("Respuestas incorrectas, por favor verifique.");
            }
        } else {
            MensajesUI.warn("Todos los campos son obligatorios.");
        }
    }

    // (			# Start of group
    // (?=.*\d)                 #   must contains one digit from 0-9
    // (?=.*[a-z])		#   must contains one lowercase characters
    // (?=.*[A-Z])		#   must contains one uppercase characters
    // (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
    //            .		#     match anything with previous condition checking
    //             {6,20}	#        length at least 6 characters and maximum of 20	
    // )    			# End of group
    public boolean validarClave(String clave) {
        try {
            String PATTERN_PASSWORD = pc.getFormato();
            Pattern pattern = Pattern.compile(PATTERN_PASSWORD);
            Matcher matcher = pattern.matcher(clave);
            if (matcher.matches()) {
                return true;
            } else {
                MensajesUI.error("Contrase�a inv�lida: " + pc.getMensajevalidacion());
            }
        } catch (Exception e) {
            MensajesUI.error("Error en el formato de clave. Por favor contacte a soporte.");
        }

        return false;
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
