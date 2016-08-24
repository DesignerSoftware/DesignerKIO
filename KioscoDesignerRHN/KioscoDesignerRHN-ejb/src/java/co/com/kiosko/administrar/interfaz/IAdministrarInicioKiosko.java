package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import java.math.BigInteger;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarInicioKiosko {

    public void obtenerConexion(String idSesion);

    public Empleados consultarEmpleado(BigInteger codigoEmpleado, long nit);

    public String fotoEmpleado();
}
