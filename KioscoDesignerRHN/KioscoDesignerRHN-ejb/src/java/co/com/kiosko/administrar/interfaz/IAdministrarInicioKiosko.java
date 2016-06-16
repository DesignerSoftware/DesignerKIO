package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarInicioKiosko {

    public void obtenerConexion(java.lang.String idSesion);

    public co.com.kiosko.entidades.Empleados consultarEmpleado(java.math.BigInteger codigoEmpleado);

    public java.lang.String fotoEmpleado();
}
