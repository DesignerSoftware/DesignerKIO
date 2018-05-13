package co.com.kiosko.administrar.interfaz;

import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    //public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa);
    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa, String esquema);
    
    public boolean validarAutorizador(String usuario, String esquema);

    public boolean validarUsuarioRegistrado(String usuario, String nitEmpresa);

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa);

    public boolean adicionarConexionUsuario(String idSesion);
    
    public boolean adicionarConexionUsuario(String idSesion, String esquema);

    public void cerrarSession(String idSesion);

    public boolean validarEstadoUsuario(String usuario, String nitEmpresa);

    public boolean bloquearUsuario(java.lang.String codigoEmpleado, String nitEmpresa);

    public co.com.kiosko.entidades.ConexionesKioskos obtenerConexionEmpelado(java.lang.String codigoEmpleado, String nitEmpresa);

    public void modificarUltimaConexion(co.com.kiosko.entidades.ConexionesKioskos cnx);
}
