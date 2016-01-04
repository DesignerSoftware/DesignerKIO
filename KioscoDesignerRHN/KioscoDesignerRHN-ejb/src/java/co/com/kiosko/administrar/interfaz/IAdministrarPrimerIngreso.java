package co.com.kiosko.administrar.interfaz;

import java.math.BigInteger;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarPrimerIngreso {

    public void obtenerConexion(java.lang.String idSesion);

    public java.util.List<co.com.kiosko.administrar.entidades.PreguntasKioskos> obtenerPreguntasSeguridad();

    public boolean registrarConexionKiosko(co.com.kiosko.administrar.entidades.ConexionesKioskos cnk);

    public co.com.kiosko.administrar.entidades.PreguntasKioskos consultarPreguntaSeguridad(java.math.BigDecimal secuencia);

    public co.com.kiosko.administrar.entidades.Empleados consultarEmpleado(java.math.BigInteger codigoEmpleado);

    public byte[] encriptar(java.lang.String valor);

    public java.lang.String desencriptar(byte[] valor);
    
}
