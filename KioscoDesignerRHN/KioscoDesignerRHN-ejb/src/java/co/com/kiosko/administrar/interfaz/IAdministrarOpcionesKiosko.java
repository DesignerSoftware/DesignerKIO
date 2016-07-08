package co.com.kiosko.administrar.interfaz;

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
}
