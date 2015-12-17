package co.com.kiosko.persistencia.interfaz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {

    public void setearKiosko(EntityManager eManager);

    public boolean validarIngresoUsuario(EntityManager eManager, String usuario, String clave, String nitEmpresa);

    public EntityManager validarConexionUsuario(EntityManagerFactory emf);
}
