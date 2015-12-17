package co.com.kisosko.controlador.ingreso;

import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import java.io.Serializable;
import java.util.List;
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
public class ControladorIngreso implements Serializable {

    @EJB
    private IAdministrarIngreso administrarIngreso;
    @EJB
    private IAdministrarSesiones administrarSesiones;
    private String usuario, clave, unidadPersistenciaIngreso;
    private boolean ingresoExitoso;

    public ControladorIngreso() {
    }

    public List<CadenasKioskos> obtenerCadenasKiosko() {
        return (new LeerArchivoXML()).leerArchivoEmpresasKiosko();
    }

    public void ingresar() {
        System.out.println("Usuario: " + usuario);
        System.out.println("Clave: " + clave);
        System.out.println("Unidad persistencia: " + unidadPersistenciaIngreso);

        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        if (!ingresoExitoso) {
            if (administrarIngreso.conexionIngreso(unidadPersistenciaIngreso)) {
                if (administrarIngreso.validarDatosIngreso(usuario, clave)) {
                    administrarIngreso.adicionarConexionUsuario(ses.getId());
                    administrarSesiones.consultarSessionesActivas();
                    ingresoExitoso = true;
                } else {
                    //EL USUARIO O CLAVE SON INCORRECTOS
                    ingresoExitoso = false;
                }
            } else {
                //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                ingresoExitoso = false;
            }
        } else {
            administrarIngreso.cerrarSession(ses.getId());
            administrarSesiones.consultarSessionesActivas();
            ingresoExitoso = false;
        }
    }

    //GETTER AND SETTER
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUnidadPersistenciaIngreso() {
        return unidadPersistenciaIngreso;
    }

    public void setUnidadPersistenciaIngreso(String unidadPersistenciaIngreso) {
        this.unidadPersistenciaIngreso = unidadPersistenciaIngreso;
    }

    public boolean isIngresoExitoso() {
        return ingresoExitoso;
    }
}
