package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOpcionesKiosko;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
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
    private OpcionesKioskos opcionesPrincipales, opcionActual, opcionReporte;
    private List<OpcionesKioskos> navegacionOpciones;
    //ACTUALIZAR COMPONENTES
    private List<String> actualizar;
    //INFO USUARIO
    private String usuario;
    private Empleados empleado;

    public ControladorOpcionesKiosko() {
        navegacionOpciones = new ArrayList<OpcionesKioskos>();
        actualizar = new ArrayList<String>();
        actualizar.add("principalForm:pnlOpciones");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOpcionesKiosko.obtenerConexion(ses.getId());
            empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            requerirOpciones();
            opcionActual = opcionesPrincipales;
            navegacionOpciones.add(opcionActual);
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void requerirOpciones() {
        opcionesPrincipales = administrarOpcionesKiosko.obtenerOpcionesKiosko(empleado.getEmpresa().getSecuencia());
    }

    public void seleccionOpcion(OpcionesKioskos opc) {
        if (opc.getOpcionesHijas() != null && !opc.getOpcionesHijas().isEmpty()) {
            navegacionOpciones.add(opc);
            opcionActual = opc;
            PrimefacesContextUI.actualizarLista(actualizar);
        } else {
            opcionReporte = opc;
            if (opc.getClase().equals("PANTALLA")) {
                PrimefacesContextUI.ejecutar("pantallaDinamica();");
            } else if (opc.getClase().equals("REPORTE")) {
                PrimefacesContextUI.ejecutar("reporte();");
            }
        }
    }

    public void volver() {
        opcionActual = navegacionOpciones.get(navegacionOpciones.size() - 1);
        navegacionOpciones.remove(navegacionOpciones.size() - 1);
        PrimefacesContextUI.actualizarLista(actualizar);
    }

    public void volverOpcionNavegada(OpcionesKioskos opc) {
        int indice = navegacionOpciones.indexOf(opc);
        opcionActual = opc;
        while (true) {
            int indiceBorrar = navegacionOpciones.size() - 1;
            if (indiceBorrar != indice) {
                navegacionOpciones.remove(indiceBorrar);
            } else {
                break;
            }
        }
        PrimefacesContextUI.actualizarLista(actualizar);
    }
    //GETTER AND SETTER

    public OpcionesKioskos getOpcionActual() {
        return opcionActual;
    }

    public List<OpcionesKioskos> getNavegacionOpciones() {
        return navegacionOpciones;
    }

    public OpcionesKioskos getOpcionReporte() {
        return opcionReporte;
    }
}
