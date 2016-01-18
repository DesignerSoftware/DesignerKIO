/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarGenerarReporte {

    public void obtenerConexion(java.lang.String idSesion);

    public java.lang.String generarReporte(java.lang.String nombreReporte, java.lang.String tipoReporte, java.util.Map parametros);

    public boolean modificarConexionKisko(co.com.kiosko.administrar.entidades.ConexionesKioskos cnx);

    public boolean enviarCorreo(java.math.BigDecimal secuenciaEmpresa, java.lang.String destinatario, java.lang.String asunto, java.lang.String mensaje, java.lang.String pathAdjunto);
    
}
