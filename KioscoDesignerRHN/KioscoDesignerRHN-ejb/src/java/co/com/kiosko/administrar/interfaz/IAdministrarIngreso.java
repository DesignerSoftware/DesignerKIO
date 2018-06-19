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
    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa, String esquema) throws Exception;
    
    public boolean validarAutorizador(String usuario, String esquema) throws Exception;

    public boolean validarUsuarioRegistrado(String usuario, String nitEmpresa) throws Exception;

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa);

    @Deprecated
    public boolean adicionarConexionUsuario(String idSesion) throws Exception;
    
    public boolean adicionarConexionUsuario(String idSesion, String esquema) throws Exception;

    public void cerrarSession(String idSesion);

    public boolean validarEstadoUsuario(String usuario, String nitEmpresa);

    public boolean bloquearUsuario(java.lang.String codigoEmpleado, String nitEmpresa);

    public co.com.kiosko.entidades.ConexionesKioskos obtenerConexionEmpelado(java.lang.String codigoEmpleado, String nitEmpresa);

    public void modificarUltimaConexion(co.com.kiosko.entidades.ConexionesKioskos cnx);
}
