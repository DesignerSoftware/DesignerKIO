/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioSoliciVacas;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Edwin
 */
@Local
public interface IPersistenciaKioSoliciVacas {
    public void crearSolicitud(EntityManager em, KioSoliciVacas solicitud) throws EntityExistsException, TransactionRolledbackLocalException, Exception;
    public KioSoliciVacas recargarSolicitud(EntityManager em, KioSoliciVacas solicitud) throws NoResultException, NonUniqueResultException, IllegalStateException;
    public void modificarSolicitud(EntityManager em, KioSoliciVacas solicitud);
    /**
     * Método para consultar las solicitudes que tienen estado ENVIADO
     * @param em EntityManager
     * @return Lista de la las solicitudes obtenidas.
     * @throws Exception 
     */
    public List consultaSolicitudesEnviadas(EntityManager em) throws Exception;
}
