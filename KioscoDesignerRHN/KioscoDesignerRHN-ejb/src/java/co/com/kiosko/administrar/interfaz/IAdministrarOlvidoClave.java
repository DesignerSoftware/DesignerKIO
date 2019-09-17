package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.ParametrizaClave;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarOlvidoClave {

    public void obtenerConexion(String idSesion);

    public ConexionesKioskos obtenerConexionEmpleado(String codigoEmpleado, String nitEmpresa);
    
    public ConexionesKioskos obtenerConexionPersona(String numeroDocumento, long nitEmpresa);

    public boolean validarRespuestas(String respuesta1, String respuesta2, byte[] respuestaC1, byte[] respuestaC2);

    public boolean cambiarClave(ConexionesKioskos ck);

    public byte[] encriptar(String valor);

    public String desEncriptar(byte[] valor);

    public ParametrizaClave obtenerFormatoClave(long nitEmpresa);
    
    public ConexionesKioskos obtenerConexionEmpleado(String numeroDocumento);
    
}
