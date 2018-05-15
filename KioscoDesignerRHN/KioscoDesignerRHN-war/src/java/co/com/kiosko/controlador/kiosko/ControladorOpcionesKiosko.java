package co.com.kiosko.controlador.kiosko;

//import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOpcionesKiosko;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigInteger;
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
//    private Empleados empleado;
    private String roles;
//    private String unidadPersistenciaIngreso;
    private ConexionesKioskos conexionK;
//    private String nitEmpresa;
    private CadenasKioskos cadenaK;

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
            //empleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado().getEmpleado();
            conexionK = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado();
            String unidadPersistenciaIngreso = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUnidadPersistenciaIngreso();
            System.out.println("unidadPersistenciaIngreso: "+unidadPersistenciaIngreso);
            //no funciona
            //cadenaK = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).validarUnidadPersistencia(unidadPersistenciaIngreso);
            cadenaK = validarUnidadPersistencia(unidadPersistenciaIngreso);
            System.out.println("cadenaK-1: "+cadenaK);
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
        roles = administrarOpcionesKiosko.determinarRol(conexionK, cadenaK);
        System.out.println("El rol del empleado es: " + roles);
        System.out.println("cadenaK-2: "+cadenaK);
    }

    private void requerirOpciones() {
        System.out.println(this.getClass().getName() + ".requerirOpciones()");
        System.out.println("cadenaK-3: "+cadenaK);
        System.out.println("nit: "+cadenaK.getNit());
        long nitL = Long.parseLong(cadenaK.getNit());
        System.out.println("nitL: "+nitL);
        BigInteger secEmpresaL = administrarOpcionesKiosko.obtenerSecEmpresa(nitL);
        opcionesPrincipales = administrarOpcionesKiosko.obtenerOpcionesKiosko(secEmpresaL);
//        opcionesPrincipales = administrarOpcionesKiosko.obtenerOpcionesKiosko(conexionK.getEmpleado().getEmpresa().getSecuencia());
    }

    private void filtrarOpciones() {
        System.out.println(this.getClass().getName() + ".filtrarOpciones()");
        String[] codigos = null;
        System.out.println("antes: " + recorreOpciones(opcionesPrincipales));
        if (!roles.contains("NOMINA")) {
            //recorrer la lista de opciones en profundidad con el fin de encontrar las opciones propias de la nomina
            //y quitarlas.
            System.out.println("es rol nomina.");
            if (codigos == null) {
                codigos = new String[3];
                codigos[0] = "0136";
                codigos[1] = "0137";
                codigos[2] = "0138";
            } else {
                String[] tmp = new String[codigos.length + 3];
                for (int j = 0; j < codigos.length; j++) {
                    tmp[j] = codigos[j];
                }
                tmp[tmp.length - 3] = "0136";
                tmp[tmp.length - 2] = "0137";
                tmp[tmp.length - 1] = "0138";
                codigos = tmp;
            }
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
                System.out.println("longitud tmp: " + tmp.length);
                System.out.println("longitud codigos: " + codigos.length);
                for (int j = 0; j < codigos.length; j++) {
                    tmp[j] = codigos[j];
                }
                tmp[tmp.length - 2] = "0133";
                tmp[tmp.length - 1] = "0134";
                codigos = tmp;
            }
        }
        if (!roles.contains("EMPLEADO")) {
            System.out.println("es rol empleado.");
            System.out.println("es rol autorizador.");
            if (codigos == null) {
                codigos = new String[7];
                codigos[0] = "0121";
                codigos[1] = "0122";
                codigos[2] = "0123";
                codigos[3] = "0124";
                codigos[4] = "0125";
                codigos[5] = "0131";
                codigos[6] = "0132";
            } else {
                String[] tmp = new String[codigos.length + 7];
                for (int j = 0; j < codigos.length; j++) {
                    tmp[j] = codigos[j];
                }
                tmp[tmp.length - 7] = "0121";
                tmp[tmp.length - 6] = "0122";
                tmp[tmp.length - 5] = "0123";
                tmp[tmp.length - 4] = "0124";
                tmp[tmp.length - 3] = "0125";
                tmp[tmp.length - 2] = "0131";
                tmp[tmp.length - 1] = "0132";
                codigos = tmp;
            }
        }
        if (!roles.contains("AUTORIZADOR")) {
            System.out.println("es rol autorizador.");
            if (codigos == null) {
                codigos = new String[2];
                codigos[0] = "0139";
                codigos[1] = "01391";
            } else {
                String[] tmp = new String[codigos.length + 2];
                for (int j = 0; j < codigos.length; j++) {
                    tmp[j] = codigos[j];
                }
                tmp[tmp.length - 2] = "0139";
                tmp[tmp.length - 1] = "01391";
                codigos = tmp;
            }
        }
        System.out.println("Codigos listados: ");
        for (String codigo : codigos) {
            System.out.println(codigo);
        }
        if (codigos != null && codigos.length > 0) {
            for (String codigo : codigos) {
                retirarOpcion(opcionesPrincipales, codigo);
            }
        }
//        String[] codRoles = roles.split(";");
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
        System.out.println("compara: " + codigo);
        if (opcion.getCodigo().equalsIgnoreCase(codigo)) {
            return true;
        }
        if (opcion.getOpcionesHijas() != null && !opcion.getOpcionesHijas().isEmpty()) {
            for (int i = 0; i < opcion.getOpcionesHijas().size(); i++) {
                System.out.println("opcion: " + opcion.getOpcionesHijas().get(i));
                if (retirarOpcion(opcion.getOpcionesHijas().get(i), codigo)) {
                    System.out.println("tamagno antes de remover: " + opcion.getOpcionesHijas().size());
                    System.out.println("se va a remover: " + opcion.getOpcionesHijas().get(i).getCodigo());
                    opcion.getOpcionesHijas().remove(i);
                    System.out.println("tamagno despues de remover: " + opcion.getOpcionesHijas().size());
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
    public CadenasKioskos validarUnidadPersistencia(String unidadP) {
        CadenasKioskos resultado = null;
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if (elemento.getId().equals(unidadP)) {
                resultado = elemento;
                break;
            }
        }
        return resultado;
    }
}
