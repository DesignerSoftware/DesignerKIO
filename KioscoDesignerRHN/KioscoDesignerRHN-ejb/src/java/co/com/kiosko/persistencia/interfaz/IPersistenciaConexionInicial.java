package co.com.kiosko.persistencia.interfaz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {
    /**
     * M�todo para setear el rol de la base de datos con el que se van a hacer las consultas.
     * @param eManager 
     */
    @Deprecated
    public void setearKiosko(EntityManager eManager);
    /**
     * M�todo que setea el kiosko usando el esquema que se proporcione para complementar 
     * el nombre del rol con que se har�n las consultas.
     * @param eManager
     * @param esquema 
     */
    public void setearKiosko(EntityManager eManager, String esquema);

    public boolean validarUsuarioyEmpresa(EntityManager eManager, String usuario, String nitEmpresa);

    public boolean validarUsuarioRegistrado(EntityManager eManager, String usuario, String nitEmpresa);

    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa);

    public EntityManager validarConexionUsuario(EntityManagerFactory emf);

    public boolean validarEstadoUsuario(EntityManager eManager, String usuario, String nitEmpresa);

}
