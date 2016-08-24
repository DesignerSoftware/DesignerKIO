package co.com.kiosko.administrar.interfaz;

import java.math.BigInteger;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarGenerarReporte {

    public void obtenerConexion(String idSesion);

    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros);

    public boolean modificarConexionKisko(co.com.kiosko.entidades.ConexionesKioskos cnx);

    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto);
    
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa);
    
}
