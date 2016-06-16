package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarOlvidoClave {

    public void obtenerConexion(java.lang.String idSesion);

    public co.com.kiosko.entidades.ConexionesKioskos obtenerConexionEmpleado(java.lang.String codigoEmpleado, String nitEmpresa);

    public boolean validarRespuestas(java.lang.String respuesta1, java.lang.String respuesta2, byte[] respuestaC1, byte[] respuestaC2);

    public boolean cambiarClave(co.com.kiosko.entidades.ConexionesKioskos ck);

    public byte[] encriptar(java.lang.String valor);

    public java.lang.String desEncriptar(byte[] valor);

    public co.com.kiosko.entidades.ParametrizaClave obtenerFormatoClave(long nitEmpresa);
    
    
}
