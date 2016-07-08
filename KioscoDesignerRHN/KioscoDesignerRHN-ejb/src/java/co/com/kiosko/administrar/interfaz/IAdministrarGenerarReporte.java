package co.com.kiosko.administrar.interfaz;

import java.math.BigInteger;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarGenerarReporte {

    public void obtenerConexion(java.lang.String idSesion);

    public java.lang.String generarReporte(java.lang.String nombreReporte, java.lang.String tipoReporte, java.util.Map parametros);

    public boolean modificarConexionKisko(co.com.kiosko.entidades.ConexionesKioskos cnx);

    public boolean enviarCorreo(java.math.BigInteger secuenciaEmpresa, java.lang.String destinatario, java.lang.String asunto, java.lang.String mensaje, java.lang.String pathAdjunto);
    
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa);
    
}
