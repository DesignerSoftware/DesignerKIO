package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaConfiguracionCorreo {

    public co.com.kiosko.administrar.entidades.ConfiguracionCorreo consultarConfiguracionServidorCorreo(javax.persistence.EntityManager eManager, java.math.BigDecimal secuenciaEmpresa);
    
}
