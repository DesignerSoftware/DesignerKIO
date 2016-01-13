package co.com.kiosko.administrar.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarOpcionesKiosko {

    public void obtenerConexion(java.lang.String idSesion);

    public co.com.kiosko.administrar.entidades.OpcionesKioskos obtenerOpcionesKiosko();
    
}
