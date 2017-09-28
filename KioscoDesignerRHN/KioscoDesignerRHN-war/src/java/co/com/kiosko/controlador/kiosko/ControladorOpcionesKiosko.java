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
    private String roles;
    private String unidadPersistenciaIngreso;

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
            unidadPersistenciaIngreso = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUnidadPersistenciaIngreso();
            determinarRol();
            requerirOpciones();
            filtrarOpciones();
            opcionActual = opcionesPrincipales;
            navegacionOpciones.add(opcionActual);
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    private void determinarRol() {
        System.out.println(this.getClass().getName() + ".determinarRol()");
        roles = administrarOpcionesKiosko.determinarRol(empleado, unidadPersistenciaIngreso);
        System.out.println("El rol del empleado es: " + roles);
    }

    private void requerirOpciones() {
        System.out.println(this.getClass().getName() + ".requerirOpciones()");
        opcionesPrincipales = administrarOpcionesKiosko.obtenerOpcionesKiosko(empleado.getEmpresa().getSecuencia());
    }

    private void filtrarOpciones() {
        System.out.println(this.getClass().getName() + ".filtrarOpciones()");
        String[] codigos = null;
        System.out.println("antes: " + recorreOpciones(opcionesPrincipales));
        if (roles.contains("EMPLEADO")) {
            System.out.println("es rol empleado.");
//            String[] codigos = {"0131", "0132"};
        }
        if (!roles.contains("NOMINA")) {
            //recorrer la lista de opciones en profundidad con el fin de encontrar las opciones propias de la nomina
            //y quitarlas.
            System.out.println("es rol nomina.");
            codigos = new String[3];
            codigos[0] = "0136";
            codigos[1] = "0137";
            codigos[2] = "0138";
        }
        if (!roles.contains("JEFE")) {
            //recorrer la lista de opciones en profundidad con el fin de encontrar las opciones propias del jefe
            //y quitarlas.
            System.out.println("es rol jefe.");
            if (codigos == null) {
                codigos = new String[2];
                codigos[0] = "0133";
                codigos[1] = "0134";
            } else {
                String[] tmp = new String[codigos.length + 2];
                System.out.println("longitud tmp: "+tmp.length);
                System.out.println("longitud codigos: "+codigos.length);
                for (int j=0; j < codigos.length ; j++){
                    tmp[j] = codigos[j];
                }
                tmp[tmp.length-2] = "0133";
                tmp[tmp.length-1] = "0134";
                codigos = tmp;
            }
        }
        if (codigos != null && codigos.length > 0) {
            for (String codigo : codigos) {
                retirarOpcion(opcionesPrincipales, codigo);
            }
        }
        System.out.println("despues: " + recorreOpciones(opcionesPrincipales));
    }

    private String recorreOpciones(OpcionesKioskos opcion) {
//        System.out.println(this.getClass().getName()+".recorreOpciones()");
//        System.out.println(opcion.getCodigo());
        String res = opcion.getCodigo();
        if (opcion.getOpcionesHijas() != null && !opcion.getOpcionesHijas().isEmpty()) {
            for (int i = 0; i < opcion.getOpcionesHijas().size(); i++) {
                res = res + ";" + recorreOpciones(opcion.getOpcionesHijas().get(i));
            }
        }
        return res;
    }

    private boolean retirarOpcion(OpcionesKioskos opcion, String codigo) {
        if (opcion.getCodigo().equalsIgnoreCase(codigo)) {
            return true;
        }
        if (opcion.getOpcionesHijas() != null && !opcion.getOpcionesHijas().isEmpty()) {
            for (int i = 0; i < opcion.getOpcionesHijas().size(); i++) {
                if (retirarOpcion(opcion.getOpcionesHijas().get(i), codigo)) {
                    System.out.println("tamagno antes de remover: "+opcion.getOpcionesHijas().size());
                    System.out.println("se va a remover: "+opcion.getOpcionesHijas().get(i).getCodigo());
                    opcion.getOpcionesHijas().remove(i);
                    System.out.println("tamagno despues de remover: "+opcion.getOpcionesHijas().size());
                }
            }
        }
        return false;
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
