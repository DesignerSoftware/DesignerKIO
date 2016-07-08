package co.com.kiosko.persistencia.interfaz;

import javax.ejb.Local;

/**
 *
 * @author Felipe Triviño
 */
@Local
public interface IPersistenciaOpcionesKioskos {

    public java.util.List<co.com.kiosko.entidades.OpcionesKioskos> consultarOpcionesPorPadre(javax.persistence.EntityManager eManager, java.math.BigInteger secuenciaPadre, java.math.BigInteger secuenciaEmpresa);
    
}
