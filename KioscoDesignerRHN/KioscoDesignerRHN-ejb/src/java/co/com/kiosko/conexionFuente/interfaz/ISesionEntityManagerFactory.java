package co.com.kiosko.conexionFuente.interfaz;

import javax.persistence.EntityManagerFactory;

public interface ISesionEntityManagerFactory {

    public boolean crearConexionUsuario(String unidadPersistencia);

    public EntityManagerFactory getEmf();

    public void setEmf(EntityManagerFactory emf);
}
