package co.com.kiosko.conexionFuente.interfaz;

import javax.persistence.EntityManagerFactory;


public interface ISesionEntityManagerFactory {

    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia);

}
