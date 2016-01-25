package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa);

    public boolean validarUsuarioRegistrado(String usuario);

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public boolean validarEstadoUsuario(java.lang.String usuario);

    public boolean bloquearUsuario(java.lang.String codigoEmpleado, String nitEmpresa);

    public javax.persistence.EntityManager getEm();

    public co.com.kiosko.administrar.entidades.ConexionesKioskos obtenerConexionEmpelado(java.lang.String codigoEmpleado, String nitEmpresa);

    public void modificarUltimaConexion(co.com.kiosko.administrar.entidades.ConexionesKioskos cnx);
}
