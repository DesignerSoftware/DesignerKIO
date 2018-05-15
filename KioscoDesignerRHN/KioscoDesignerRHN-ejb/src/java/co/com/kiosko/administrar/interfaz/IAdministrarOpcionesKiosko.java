package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.OpcionesKioskos;
import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IAdministrarOpcionesKiosko {

    public void obtenerConexion(java.lang.String idSesion);

    public BigInteger obtenerSecEmpresa(long nit);
    public OpcionesKioskos obtenerOpcionesKiosko(BigInteger secuenciaEmpresa);
    public String determinarRol(ConexionesKioskos conexionK, CadenasKioskos cadenaK);
}
