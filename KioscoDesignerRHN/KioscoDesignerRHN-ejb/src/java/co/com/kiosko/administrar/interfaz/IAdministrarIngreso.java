package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarDatosIngreso(String usuario, String clave);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);
}
