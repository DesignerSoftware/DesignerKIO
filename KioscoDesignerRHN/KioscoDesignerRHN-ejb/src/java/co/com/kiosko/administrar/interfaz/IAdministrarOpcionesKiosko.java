package co.com.kiosko.administrar.interfaz;

import java.math.BigDecimal;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarOpcionesKiosko {

    public void obtenerConexion(java.lang.String idSesion);

    public co.com.kiosko.entidades.OpcionesKioskos obtenerOpcionesKiosko(BigDecimal secuenciaEmpresa);
}
