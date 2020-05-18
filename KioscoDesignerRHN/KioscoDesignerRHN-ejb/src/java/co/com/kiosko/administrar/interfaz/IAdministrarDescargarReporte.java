package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.clasesAyuda.ReporteGenerado;
import co.com.kiosko.entidades.ConexionesKioskos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Edwin Hastamorir
 */
@Local
public interface IAdministrarDescargarReporte {

    public void obtenerConexion(String idSesion);

    public List<ReporteGenerado> consultarReporte(String nombreDirectorio, String codigoEmpleado, Date fechaDesde, Date fechaHasta);

    public boolean modificarConexionKisko(ConexionesKioskos cnx);

    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, List<ReporteGenerado> pathArchivos);

    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa);
    
}
