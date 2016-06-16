package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaPreguntasKioskos {

    public java.util.List<co.com.kiosko.entidades.PreguntasKioskos> obtenerPreguntasSeguridad(javax.persistence.EntityManager eManager);

    public co.com.kiosko.entidades.PreguntasKioskos consultarPreguntaSeguridad(javax.persistence.EntityManager eManager, java.math.BigDecimal secuencia);
    
}
