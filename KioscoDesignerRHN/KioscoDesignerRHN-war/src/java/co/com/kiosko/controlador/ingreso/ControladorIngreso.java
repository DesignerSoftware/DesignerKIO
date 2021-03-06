package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
//import co.com.kiosko.clasesAyuda.ExtraeCausaExcepcion;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.autenticacion.Util;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Trivi�o
 */
@ManagedBean
@SessionScoped
public class ControladorIngreso implements Serializable {

    @EJB
    private IAdministrarIngreso administrarIngreso;
//    @EJB
    private LeerArchivoXML leerArchivoXML;
    private String usuario, clave, unidadPersistenciaIngreso, bckUsuario;
    private Date ultimaConexion;
    private boolean ingresoExitoso;
    private int intento;
    private ConexionesKioskos conexionEmpleado;
    private String nit;
    private final String logo;
    private String grupo;
    private List<SelectItem> listaGrupos;
    private String grupoSeleccionado;

    public ControladorIngreso() {
        imprimir("constructor ControladorIngreso");
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        imprimir("sessionA: "+ses);
        intento = 0;
        logo = "logonominadesignertrans.png";
        leerArchivoXML = new LeerArchivoXML();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        imprimir("inicializarAdministrador");
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        imprimir("sessionB: "+ses);
        validarGrupo();
    }

    public List<CadenasKioskos> obtenerCadenasKiosko() {
        return obtenerCadenasKioskoGrupo();
    }

    public List<CadenasKioskos> obtenerCadenasKioskoGrupo() {
        List<CadenasKioskos> listaResultado;
        boolean resultadoValidacion = false;
        if ((grupo != null)) {
            if (!grupo.isEmpty()) {
                resultadoValidacion = true;
            }
        }
        if (resultadoValidacion) {
            listaResultado = leerArchivoXML.leerArchivoEmpresasKioskoGrupo(this.grupo);
        } else {
            listaResultado = leerArchivoXML.leerArchivoEmpresasKioskoGrupo("0");
        }
        return listaResultado;
    }

    public List<SelectItem> obtenerGruposCadenasKiosko() {
        List<String> listaOriginal = leerArchivoXML.obtenerGruposEmpresasKiosko();
        Collections.sort(listaOriginal);
        List<SelectItem> listaRetorno = new ArrayList<SelectItem>();
        for (int i = 0; i < listaOriginal.size(); i++) {
            listaRetorno.add(new SelectItem(listaOriginal.get(i), listaOriginal.get(i)));
        }
        return listaRetorno;
    }

    public CadenasKioskos validarUnidadPersistencia(String unidadP) {
        CadenasKioskos resultado = null;
        for (CadenasKioskos elemento : leerArchivoXML.leerArchivoEmpresasKiosko()) {
            if (elemento.getId().equals(unidadP)) {
                resultado = elemento;
                break;
            }
        }
        return resultado;
    }

    public String ingresar() throws IOException {
        String retorno = null;
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        try {
            if (!ingresoExitoso) {
                CadenasKioskos cadena;
                cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
                usuario = usuario.trim();
                if (usuario != null && !usuario.isEmpty()
                        && clave != null && !clave.isEmpty()
                        && cadena != null) {
                    nit = cadena.getNit();
                    try {
                        if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                            try {
// 191129
//                                if (validarCodigoUsuario()
//                                        && (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit(), cadena.getEsquema())
//                                        || administrarIngreso.validarAutorizador(usuario, cadena.getEsquema()))) {
                                if (validarCodigoUsuario()) {
                                    if (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit(), cadena.getEsquema())
                                            || administrarIngreso.validarAutorizador(usuario, cadena.getEsquema())) {
                                        if (administrarIngreso.validarUsuarioRegistrado(usuario, cadena.getNit())) {
                                            if (administrarIngreso.validarEstadoUsuario(usuario, cadena.getNit())) {
                                                if (administrarIngreso.validarIngresoUsuarioRegistrado(usuario, clave, cadena.getNit())) {
                                                    //nit = cadena.getNit();
                                                    imprimir("El usuario que ingresa es: " + usuario);
                                                    imprimir("Intervalo inactividad: " + ses.getMaxInactiveInterval());
//                                            if (ses.getMaxInactiveInterval() >= 200) {
//                                                System.out.println("metodo autenticacion: " + contexto.getExternalContext().getAuthType());
//                                                ses.invalidate();
//                                                ses = (HttpSession) contexto.getExternalContext().getSession(true);
//                                                
//                                            }
                                                    if (ses.isNew()) {
                                                        imprimir("La sesion es nueva. "+ses);
                                                    } else {
                                                        imprimir("La sesion NO es nueva. "+ses);
                                                    }
                                                    administrarIngreso.adicionarConexionUsuario(ses.getId(), cadena.getEsquema());
                                                    ingresoExitoso = true;
                                                    intento = 0;
                                                    //return "inicio";
                                                    conexionEmpleado = administrarIngreso.obtenerConexionEmpelado(usuario, nit);
                                                    ultimaConexion = conexionEmpleado.getUltimaconexion();
                                                    administrarIngreso.modificarUltimaConexion(conexionEmpleado);
                                                    HttpSession session = Util.getSession();
                                                    session.setAttribute("idUsuario", usuario);
                                                    imprimir("Conectado a: " + session.getId());
                                                    PrimefacesContextUI.ejecutar("PF('estadoSesion').hide()");
                                                    retorno = "plantilla";
                                                } else {
                                                    // LA CONTRASE�A ES INCORRECTA.
                                                    if (bckUsuario == null || bckUsuario.equals(usuario)) {
                                                        intento++;
                                                    } else {
                                                        intento = 1;
                                                    }
                                                    bckUsuario = usuario;
                                                    PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                                                    MensajesUI.error(intento < 3 ? "La contrase�a es inv�lida. Intento #" + intento
                                                            : "La contrase�a es inv�lida. Intento #" + intento + " Cuenta bloqueada.");

                                                    if (intento == 2) {
                                                        PrimefacesContextUI.ejecutar("PF('dlgAlertaIntentos').show()");
                                                    } else if (intento >= 3) {
                                                        administrarIngreso.bloquearUsuario(usuario, nit);
                                                        intento = 0;
                                                    }
                                                    ingresoExitoso = false;
                                                }
                                            } else {
                                                //USUARIO BLOQUEADO
                                                PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
//                                                MensajesUI.error("El empleado " + usuario + " se encuentra bloqueado, por favor comun�quese con el �rea de recursos humanos de su empresa.");
                                                MensajesUI.error("El empleado se encuentra bloqueado, por favor comun�quese con el �rea de recursos humanos de su empresa.");
                                                ingresoExitoso = false;
                                            }
                                        } else {
//                                administrarIngreso.adicionarConexionUsuario(ses.getId());
                                            administrarIngreso.adicionarConexionUsuario(ses.getId(), cadena.getEsquema());
                                            nit = cadena.getNit();
                                            ingresoExitoso = true;
                                            HttpSession session = Util.getSession();
                                            session.setAttribute("idUsuario", usuario);
                                            PrimefacesContextUI.ejecutar("PF('dlgPrimerIngreso').show()");
                                        }
                                    } else {
                                        //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                                        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
//                                        MensajesUI.error("El empleado " + usuario + " no existe, no pertenece � no esta activo a la empresa seleccionada.");
                                        MensajesUI.error("El empleado no existe, no pertenece � no esta activo a la empresa seleccionada.");
                                        ingresoExitoso = false;
                                    }
                                } else {
                                    //EL USUARIO DIGITADO NO ES UN NUMERO
                                    PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
//                                    MensajesUI.error("El empleado " + usuario + " no es un c�digo de empleado.");
                                    MensajesUI.error("El empleado no es un c�digo de empleado.");
                                    ingresoExitoso = false;
                                }
                            } catch (Exception ex1) {
                                imprimir(ex1.getMessage());
                                String mensajeExcep = "Fallo en la conexion: " + ex1.getMessage().substring(0, 40);
                                PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                                MensajesUI.error("ERROR: " + mensajeExcep);
                                ingresoExitoso = false;
                            }
                        } else {
                            //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                            PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                            MensajesUI.fatal("Unidad de persistencia inv�lida, por favor contactar al �rea de recursos humanos de su empresa.");
                            ingresoExitoso = false;
                        }
                    } catch (Exception ex1) {
                        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                        imprimir("MSJ_RAW: " + ex1);
//                        String msjex = ExtraeCausaExcepcion.getLastThrowable(ex1).getMessage();
//                        System.out.println("MSJ_EX: "+msjex);
                        //MensajesUI.fatal("Hubo un error de conexion, por favor ingresa nuevamente.");    
                        MensajesUI.fatal("Hubo un error de conexion, por favor ingresa nuevamente!."); //20190827 Tm
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                    MensajesUI.error("Todos los campos son de obligatorio ingreso.");
                    ingresoExitoso = false;
                }
            } else {
                usuario = "";
                HttpSession session = Util.getSession();
                imprimir("la session con " + session.getAttribute("idUsuario") + " termino.");
                session.setAttribute("idUsuario", "");
                session.removeAttribute("idUsuario");
                ingresoExitoso = false;
                conexionEmpleado = null;
                nit = null;

                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext ec = context.getExternalContext();

                try {
                    ec.invalidateSession();
                } catch (NullPointerException npe) {
                    imprimir("ExternalContext vacio");
                }
                PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                administrarIngreso.cerrarSession(ses.getId());
                ec.redirect(ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado);
            }
        } catch (EJBTransactionRolledbackException etre) {
            imprimir(".ingresar() exception");
            imprimir("La transacci�n se deshizo.");
            System.out.println(etre);
        }
        return retorno;
    }

    private boolean validarCodigoUsuario() {
        boolean resultado = false;
        BigInteger numUsuario;
        try {
            numUsuario = new BigInteger(usuario);
            resultado = true;
        } catch (NumberFormatException nfe) {
            resultado = false;
        }
        return resultado;
    }

    public String olvidoClave() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);

        CadenasKioskos cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
        if (usuario != null && clave != null && cadena != null) {
            nit = cadena.getNit();
            if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                try {
                    if ((administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit(), cadena.getEsquema())
                            || administrarIngreso.validarAutorizador(usuario, cadena.getEsquema()))
                            && validarCodigoUsuario()) {
                        if (administrarIngreso.validarUsuarioRegistrado(usuario, cadena.getNit())) {
                            if (administrarIngreso.validarEstadoUsuario(usuario, cadena.getNit())) {
//                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                                ingresoExitoso = true;
                                HttpSession session = Util.getSession();
                                session.setAttribute("idUsuario", usuario);
                                return "olvidoClave";
                            } else {
                                //USUARIO BLOQUEADO
                                PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                                MensajesUI.error("El empleado " + usuario + " se encuentra bloqueado, por favor comun�quese con el �rea de recursos humanos de su empresa.");
                            }
                        } else {
                            PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                            MensajesUI.error("El empleado no ha realizado el primer ingreso.");
                        }
                    } else {
                        //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                        MensajesUI.error("El empleado " + usuario + " no existe, no pertenece � no esta activo a la empresa seleccionada.");
                    }
                } catch (Exception ex1) {
                    String mensajeExcep = ex1.getMessage();
                    PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                    MensajesUI.error("ERROR: " + mensajeExcep);

                }
            } else {
                //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
                MensajesUI.fatal("Unidad de persistencia inv�lida, por favor contactar al �rea de recursos humanos de su empresa.");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('estadoSesion').hide();");
            MensajesUI.error("Para recuperar su clave, es necesario ingresar el numero de empleado y empresa.");
        }
        return "";
    }

    public void actualizarConexionEmpleado() {
        conexionEmpleado = administrarIngreso.obtenerConexionEmpelado(usuario, nit);
    }

    public String entrar() throws IOException {
        String retorno;
        if (ingresoExitoso) {
            retorno = "plantilla";
        } else {
            try {
                retorno = ingresar();
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        return retorno;
    }

    private boolean validarGrupo() {
        boolean respuesta = false;
        if ((this.grupo == null) || (this.grupo.isEmpty()) || ("null".equalsIgnoreCase(this.grupo))) {
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').show();");
            respuesta = false;
        } else {
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').hide();");
            this.grupoSeleccionado = this.grupo;
            respuesta = true;
        }
        return respuesta;
    }

    public void obtenerParametroURL() {
        String ruta;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        imprimir("aplicacion: " + ec.getRequestContextPath());
        ruta = ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado;
        imprimir("ruta: " + ruta);
        try {
            ec.redirect(ruta);
        } catch (IOException ex) {
            imprimir("error al redireccionar");
            ex.printStackTrace();
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
        if (unidadPersistenciaIngreso == null) {
            List<CadenasKioskos> cadenas = obtenerCadenasKiosko();
            unidadPersistenciaIngreso = (cadenas.size() == 1) ? cadenas.get(0).getId() : null;
        }
        return unidadPersistenciaIngreso;
    }

    public void setUnidadPersistenciaIngreso(String unidadPersistenciaIngreso) {
        this.unidadPersistenciaIngreso = unidadPersistenciaIngreso;
    }

    public boolean isIngresoExitoso() {
        return ingresoExitoso;
    }

    public ConexionesKioskos getConexionEmpleado() {
        imprimir("." + "getConexionEmpleado" + "()");
        return conexionEmpleado;
    }

    public Date getUltimaConexion() {
        imprimir("." + "getUltimaConexion" + "()");
        return ultimaConexion;
    }

    public String getNit() {
        imprimir("." + "getNit" + "()");
        return nit;
    }

    public String getLogo() {
        return logo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        imprimir(".setGrupo:grupo: "+grupo);
        this.grupo = grupo;
        validarGrupo();
    }

    public String getGrupoSeleccionado() {
        imprimir("getGrupoSeleccionado: " + this.grupoSeleccionado);
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(String grupoSeleccionado) {
        imprimir("setGrupoSeleccionado: " + grupoSeleccionado);
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        imprimir("sessionC: "+ses);
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public List<SelectItem> getListaGrupos() {
        imprimir("getListaGrupos");
        listaGrupos = obtenerGruposCadenasKiosko();
        return listaGrupos;
    }

    public void setListaGrupos(List<SelectItem> listaGrupos) {
        imprimir("setListaGrupos");
        this.listaGrupos = listaGrupos;
    }

    private void imprimir(String mensajeConsola) {
        if (true) {
            System.out.println(this.getClass().getName()+": "+mensajeConsola);
        }
    }
}
