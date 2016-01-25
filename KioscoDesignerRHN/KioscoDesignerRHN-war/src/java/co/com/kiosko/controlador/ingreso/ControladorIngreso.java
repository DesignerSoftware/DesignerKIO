package co.com.kiosko.controlador.ingreso;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.controlador.autenticacion.Util;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
    private String usuario, clave, unidadPersistenciaIngreso, bckUsuario;
    private Date ultimaConexion;
    private boolean ingresoExitoso;
    private int intento;
    private ConexionesKioskos conexionEmpleado;

    public ControladorIngreso() {
        intento = 0;
    }

    public List<CadenasKioskos> obtenerCadenasKiosko() {
        return (new LeerArchivoXML()).leerArchivoEmpresasKiosko();
    }

    public String ingresar() throws IOException {
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        if (!ingresoExitoso) {
            CadenasKioskos cadena = null;
            for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
                if (elemento.getId().equals(unidadPersistenciaIngreso)) {
                    cadena = elemento;
                    break;
                }
            }
            if (usuario != null && !usuario.isEmpty()
                    && clave != null && !clave.isEmpty()
                    && cadena != null) {
                if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                    if (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit())) {
                        if (administrarIngreso.validarUsuarioRegistrado(usuario)) {
                            if (administrarIngreso.validarEstadoUsuario(usuario)) {
                                if (administrarIngreso.validarIngresoUsuarioRegistrado(usuario, clave)) {
                                    administrarIngreso.adicionarConexionUsuario(ses.getId());
                                    ingresoExitoso = true;
                                    intento = 0;
                                    //return "inicio";
                                    conexionEmpleado = administrarIngreso.obtenerConexionEmpelado(usuario);
                                    ultimaConexion = conexionEmpleado.getUltimaconexion();
                                    administrarIngreso.modificarUltimaConexion(conexionEmpleado);
                                    HttpSession session = Util.getSession();
                                    session.setAttribute("idUsuario", usuario);
                                    return "opcionesKiosko";
                                } else {
                                    // LA CONTRASEÑA ES INCORRECTA.
                                    if (bckUsuario == null || bckUsuario.equals(usuario)) {
                                        intento++;
                                    } else {
                                        intento = 1;
                                    }
                                    bckUsuario = usuario;
                                    MensajesUI.error(intento < 3 ? "La contraseña es invalida. Intento #" + intento
                                            : "La contraseña es invalida. Intento #" + intento + " Cuenta bloqueada.");

                                    if (intento == 2) {
                                        PrimefacesContextUI.ejecutar("PF('dlgAlertaIntentos').show()");
                                    } else if (intento == 3) {
                                        administrarIngreso.bloquearUsuario(usuario);
                                        intento = 0;
                                    }
                                    administrarIngreso.getEm().getEntityManagerFactory().close();
                                    ingresoExitoso = false;
                                }
                            } else {
                                //USUARIO BLOQUEADO
                                MensajesUI.error("El usuario " + usuario + " se encuentra bloqueado, porfavor comuniquese con el area de soporte.");
                                ingresoExitoso = false;
                            }
                        } else {
                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                            ingresoExitoso = true;
                            HttpSession session = Util.getSession();
                            session.setAttribute("idUsuario", usuario);
                            PrimefacesContextUI.ejecutar("PF('dlgPrimerIngreso').show()");
                        }
                        //          administrarIngreso.getEm().getEntityManagerFactory().close();
                    } else {
                        //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                        MensajesUI.error("El usuario " + usuario + " no existe ó no pertenece a la empresa seleccionada.");
                        ingresoExitoso = false;
                    }
                } else {
                    //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                    MensajesUI.fatal("Unidad de persistencia invalida, por favor contactar al area de soporte.");
                    ingresoExitoso = false;
                }
            } else {
                MensajesUI.error("Todos lo campos son de obligatorio ingreso.");
                ingresoExitoso = false;
            }
        } else {
            administrarIngreso.cerrarSession(ses.getId());
            
            ingresoExitoso = false;
            conexionEmpleado = null;

            HttpSession session = Util.getSession();
            session.invalidate();

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext ec = context.getExternalContext();
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath());
        }
        PrimefacesContextUI.ejecutar("PF('estadoSesion').hide()");
        return "";
    }

    public String olvidoClave() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);

        CadenasKioskos cadena = null;
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if (elemento.getId().equals(unidadPersistenciaIngreso)) {
                cadena = elemento;
                break;
            }
        }
        if (usuario != null && clave != null && cadena != null) {
            if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                if (administrarIngreso.validarUsuarioyEmpresa(usuario, cadena.getNit())) {
                    if (administrarIngreso.validarUsuarioRegistrado(usuario)) {
                        if (administrarIngreso.validarEstadoUsuario(usuario)) {
                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                            ingresoExitoso = true;
                            HttpSession session = Util.getSession();
                            session.setAttribute("idUsuario", usuario);
                            return "olvidoClave";
                        } else {
                            //USUARIO BLOQUEADO
                            MensajesUI.error("El usuario " + usuario + " se encuentra bloqueado, porfavor comuniquese con el area de soporte.");
                        }
                    } else {
                        MensajesUI.error("El usuario no ha realizado el primer ingreso.");
                    }
                    administrarIngreso.getEm().getEntityManagerFactory().close();
                } else {
                    //EL USUARIO NO EXISTE O LA EMPRESA SELECCIONADA NO ES CORRECTA.
                    MensajesUI.error("El usuario " + usuario + " no existe ó no pertenece a la empresa seleccionada.");
                }
            } else {
                //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                MensajesUI.fatal("Unidad de persistencia invalida, por favor contactar al area de soporte.");
            }
        } else {
            MensajesUI.error("Para recuperar su clave, es necesario ingresar Usuario y Empresa.");
        }
        return "";
    }

    public void actualizarConexionEmpleado() {
        conexionEmpleado = administrarIngreso.obtenerConexionEmpelado(usuario);
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
        if (cadenas.size() == 1) {
            unidadPersistenciaIngreso = cadenas.get(0).getId();
        } else {
            unidadPersistenciaIngreso = null;
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
        return conexionEmpleado;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }
}
