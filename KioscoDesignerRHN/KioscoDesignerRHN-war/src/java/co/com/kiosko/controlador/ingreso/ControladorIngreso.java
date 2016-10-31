package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.autenticacion.Util;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
        intento = 0;
        logo = "logonominadesignertrans.png";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        validarGrupo();
    }

    public List<CadenasKioskos> obtenerCadenasKiosko() {
        //return (new LeerArchivoXML()).leerArchivoEmpresasKiosko();
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
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasKioskoGrupo(this.grupo);
        } else {
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasKioskoGrupo("0");
        }
        return listaResultado;
    }

    public List<SelectItem> obtenerGruposCadenasKiosko() {
        List<String> listaOriginal = (new LeerArchivoXML()).obtenerGruposEmpresasKiosko();
        List<SelectItem> listaRetorno = new ArrayList<SelectItem>();
        for (int i = 0; i < listaOriginal.size(); i++) {
            listaRetorno.add(new SelectItem(listaOriginal.get(i), listaOriginal.get(i)));
        }
        return listaRetorno;
    }

    private CadenasKioskos validarUnidadPersistencia(String unidadP) {
        CadenasKioskos resultado = null;
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
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
        if (!ingresoExitoso) {
            CadenasKioskos cadena = null;
            cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
            if (usuario != null && !usuario.isEmpty()
                    && clave != null && !clave.isEmpty()
                    && cadena != null) {
                nit = cadena.getNit();
                if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                    if (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit())) {
                        if (administrarIngreso.validarUsuarioRegistrado(usuario, cadena.getNit())) {
                            if (administrarIngreso.validarEstadoUsuario(usuario, cadena.getNit())) {
                                if (administrarIngreso.validarIngresoUsuarioRegistrado(usuario, clave, cadena.getNit())) {
                                    //nit = cadena.getNit();
                                    administrarIngreso.adicionarConexionUsuario(ses.getId());
                                    ingresoExitoso = true;
                                    intento = 0;
                                    //return "inicio";
                                    conexionEmpleado = administrarIngreso.obtenerConexionEmpelado(usuario, nit);
                                    ultimaConexion = conexionEmpleado.getUltimaconexion();
                                    administrarIngreso.modificarUltimaConexion(conexionEmpleado);
                                    HttpSession session = Util.getSession();
                                    session.setAttribute("idUsuario", usuario);
                                    if (session != null) {
                                        imprimir("Conectado a: " + session.getId());
                                    }
                                    //return "opcionesKiosko";
                                    //return "plantilla";
                                    retorno = "plantilla";
                                } else {
                                    // LA CONTRASEÑA ES INCORRECTA.
                                    if (bckUsuario == null || bckUsuario.equals(usuario)) {
                                        intento++;
                                    } else {
                                        intento = 1;
                                    }
                                    bckUsuario = usuario;
                                    MensajesUI.error(intento < 3 ? "La contraseña es inválida. Intento #" + intento
                                            : "La contraseña es inválida. Intento #" + intento + " Cuenta bloqueada.");

                                    if (intento == 2) {
                                        PrimefacesContextUI.ejecutar("PF('dlgAlertaIntentos').show()");
                                    } else if (intento >= 3) {
                                        administrarIngreso.bloquearUsuario(usuario, nit);
                                        intento = 0;
                                    }
                                    administrarIngreso.getEm().getEntityManagerFactory().close();
                                    ingresoExitoso = false;
                                }
                            } else {
                                //USUARIO BLOQUEADO
                                MensajesUI.error("El empleado " + usuario + " se encuentra bloqueado, por favor comuníquese con el área de soporte.");
                                ingresoExitoso = false;
                            }
                        } else {
                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                            nit = cadena.getNit();
                            ingresoExitoso = true;
                            HttpSession session = Util.getSession();
                            session.setAttribute("idUsuario", usuario);
                            PrimefacesContextUI.ejecutar("PF('dlgPrimerIngreso').show()");
                        }
                        //          administrarIngreso.getEm().getEntityManagerFactory().close();
                    } else {
                        //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                        MensajesUI.error("El empleado " + usuario + " no existe, no pertenece ó no esta activo a la empresa seleccionada.");
                        ingresoExitoso = false;
                    }
                } else {
                    //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                    MensajesUI.fatal("Unidad de persistencia inválida, por favor contactar al área de soporte.");
                    ingresoExitoso = false;
                }
            } else {
                MensajesUI.error("Todos los campos son de obligatorio ingreso.");
                ingresoExitoso = false;
            }
        } else {
            administrarIngreso.cerrarSession(ses.getId());

            ingresoExitoso = false;
            conexionEmpleado = null;
            nit = null;
            HttpSession session = Util.getSession();
            session.invalidate();

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            ec.invalidateSession();
            //ec.redirect(ec.getRequestContextPath());
            ec.redirect(ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado);
        }
        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide()");
        //return "";
        return retorno;
    }

    public String olvidoClave() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);

        CadenasKioskos cadena = null;
        cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
        if (usuario != null && clave != null && cadena != null) {
            nit = cadena.getNit();
            if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                if (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit())) {
                    if (administrarIngreso.validarUsuarioRegistrado(usuario, cadena.getNit())) {
                        if (administrarIngreso.validarEstadoUsuario(usuario, cadena.getNit())) {
                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                            ingresoExitoso = true;
                            HttpSession session = Util.getSession();
                            session.setAttribute("idUsuario", usuario);
                            //nit = cadena.getNit();
                            return "olvidoClave";
                        } else {
                            //USUARIO BLOQUEADO
                            MensajesUI.error("El empleado " + usuario + " se encuentra bloqueado, por favor comuníquese con el área de soporte.");
                        }
                    } else {
                        MensajesUI.error("El empleado no ha realizado el primer ingreso.");
                    }
                    administrarIngreso.getEm().getEntityManagerFactory().close();
                } else {
                    //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                    MensajesUI.error("El empleado " + usuario + " no existe, no pertenece ó no esta activo a la empresa seleccionada.");
                }
            } else {
                //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                MensajesUI.fatal("Unidad de persistencia inválida, por favor contactar al área de soporte.");
            }
        } else {
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
        //FacesContext x = FacesContext.getCurrentInstance();
        //HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        boolean respuesta = false;
        if ((this.grupo == null) || (this.grupo.isEmpty())) {
            //System.out.println("El grupo esta nulo.");
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').show();");
            respuesta = false;
        } else {
            //System.out.println("El grupo es: " + this.grupo);
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
        //return ruta;
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
        List<CadenasKioskos> cadenas = obtenerCadenasKiosko();
//        if (cadenas.size() == 1) {
//            unidadPersistenciaIngreso = cadenas.get(0).getId();
//        } else {
//            unidadPersistenciaIngreso = null;
//        }
        unidadPersistenciaIngreso = (cadenas.size() == 1) ? cadenas.get(0).getId() : null;
        return unidadPersistenciaIngreso;
    }

    public void setUnidadPersistenciaIngreso(String unidadPersistenciaIngreso) {
        this.unidadPersistenciaIngreso = unidadPersistenciaIngreso;
    }

    public boolean isIngresoExitoso() {
        return ingresoExitoso;
    }

    public ConexionesKioskos getConexionEmpleado() {
        return conexionEmpleado;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public String getNit() {
        return nit;
    }

    public String getLogo() {
        return logo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
        validarGrupo();
    }

    public String getGrupoSeleccionado() {
        imprimir("getGrupoSeleccionado: " + this.grupoSeleccionado);
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(String grupoSeleccionado) {
        imprimir("setGrupoSeleccionado: " + grupoSeleccionado);
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
            System.out.println(mensajeConsola);
        }
    }
}
