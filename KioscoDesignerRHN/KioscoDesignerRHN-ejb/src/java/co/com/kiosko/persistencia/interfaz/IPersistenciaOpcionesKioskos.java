/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaOpcionesKioskos {

    public java.util.List<co.com.kiosko.administrar.entidades.OpcionesKioskos> consultarOpcionesPorPadre(javax.persistence.EntityManager eManager, java.math.BigDecimal secuenciaPadre);
    
}
