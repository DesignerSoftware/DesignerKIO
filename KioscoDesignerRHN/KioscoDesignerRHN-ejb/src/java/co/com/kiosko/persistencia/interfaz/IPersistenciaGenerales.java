/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Triviño
 */
public interface IPersistenciaGenerales {

    public co.com.kiosko.administrar.entidades.Generales consultarRutasGenerales(javax.persistence.EntityManager eManager);
    
}
