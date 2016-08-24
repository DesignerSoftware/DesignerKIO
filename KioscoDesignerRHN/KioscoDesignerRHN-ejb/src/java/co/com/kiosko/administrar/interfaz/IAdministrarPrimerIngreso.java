package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.ParametrizaClave;
import co.com.kiosko.entidades.PreguntasKioskos;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarPrimerIngreso {

    public void obtenerConexion(java.lang.String idSesion);

    public java.util.List<co.com.kiosko.entidades.PreguntasKioskos> obtenerPreguntasSeguridad();

    public boolean registrarConexionKiosko(ConexionesKioskos cnk);

    public PreguntasKioskos consultarPreguntaSeguridad(BigDecimal secuencia);

    public Empleados consultarEmpleado(BigInteger codigoEmpleado, long nit);

    public byte[] encriptar(String valor);

    public String desencriptar(byte[] valor);

    public ParametrizaClave obtenerFormatoClave(long nitEmpresa);
    
}
