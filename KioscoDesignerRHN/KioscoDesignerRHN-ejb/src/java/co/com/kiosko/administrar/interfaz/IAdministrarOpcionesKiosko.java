package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarOpcionesKiosko {

    public void obtenerConexion(java.lang.String idSesion);

    public co.com.kiosko.entidades.OpcionesKioskos obtenerOpcionesKiosko(BigInteger secuenciaEmpresa);
    public String determinarRol(Empleados empleado, String unidadPersistenciaIngreso);
}
