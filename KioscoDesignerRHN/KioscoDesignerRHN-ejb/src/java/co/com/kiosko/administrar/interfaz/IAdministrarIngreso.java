package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Trivi�o
 */
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa);

    public boolean validarUsuarioRegistrado(String usuario);

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public boolean validarEstadoUsuario(java.lang.String usuario);

    public boolean bloquearUsuario(java.lang.String codigoEmpleado);

    public javax.persistence.EntityManager getEm();
}