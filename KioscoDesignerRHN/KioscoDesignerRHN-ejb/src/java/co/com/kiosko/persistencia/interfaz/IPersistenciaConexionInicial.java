package co.com.kiosko.persistencia.interfaz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {

    public void setearKiosko(EntityManager eManager);

    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa);

    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario, String nitEmpresa);

    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa);

    public EntityManager validarConexionUsuario(EntityManagerFactory emf);

    public boolean validarEstadoUsuario(EntityManager eManager, String usuario, String nitEmpresa);

}
