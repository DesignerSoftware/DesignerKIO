package co.com.kiosko.persistencia.interfaz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {

    /**
     * Método para setear el rol de la base de datos con el que se van a hacer
     * las consultas.
     *
     * @param eManager
     */
    @Deprecated
    public void setearKiosko(EntityManager eManager) throws Exception;

    /**
     * Método que setea el kiosko usando el esquema que se proporcione para
     * complementar el nombre del rol con que se harán las consultas.
     *
     * @param eManager
     * @param esquema
     */
    public void setearKiosko(EntityManager eManager, String esquema) throws Exception;

    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa) throws Exception;

    public boolean validarAutorizador(EntityManager eManager, String usuario) throws Exception;

    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario, String nitEmpresa) throws Exception ;

    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa) throws Exception ;

    public EntityManager validarConexionUsuario(EntityManagerFactory emf) throws Exception ;

    public boolean validarEstadoUsuario(EntityManager eManager, String usuario, String nitEmpresa) throws Exception;

    @Deprecated
    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario) throws Exception;

    public boolean validarEstadoUsuario(EntityManager eManager, String usuario) throws Exception;

    @Deprecated
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave) throws Exception;
}
