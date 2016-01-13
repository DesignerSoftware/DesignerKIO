package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOpcionesKiosko;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class ControladorOpcionesKiosko implements Serializable {

    @EJB
    private IAdministrarOpcionesKiosko administrarOpcionesKiosko;
    private OpcionesKioskos opcionesPrincipales;
    private OpcionesKioskos opcionActual;
    private List<OpcionesKioskos> navegacionOpciones;

    public ControladorOpcionesKiosko() {
        navegacionOpciones = new ArrayList<OpcionesKioskos>();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOpcionesKiosko.obtenerConexion(ses.getId());
            requerirOpciones();
            opcionActual = opcionesPrincipales;
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void requerirOpciones() {
        opcionesPrincipales = administrarOpcionesKiosko.obtenerOpcionesKiosko();
    }

    public String seleccionOpcion(OpcionesKioskos opc) {
        if (opc.getOpcionesHijas() != null && !opc.getOpcionesHijas().isEmpty()) {
            navegacionOpciones.add(opcionActual);
            opcionActual = opc;
            PrimefacesContextUI.actualizar("principalForm");
        } else {
        }
        return "";
    }

    public void volver() {
        opcionActual = navegacionOpciones.get(navegacionOpciones.size() - 1);
        navegacionOpciones.remove(navegacionOpciones.size() - 1);
        PrimefacesContextUI.actualizar("principalForm");
    }
    //GETTER AND SETTER

    public OpcionesKioskos getOpcionActual() {
        return opcionActual;
    }

    public List<OpcionesKioskos> getNavegacionOpciones() {
        return navegacionOpciones;
    }
}
