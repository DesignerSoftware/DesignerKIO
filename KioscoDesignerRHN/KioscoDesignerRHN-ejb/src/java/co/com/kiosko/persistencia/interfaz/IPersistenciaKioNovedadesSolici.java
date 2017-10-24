/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioNovedadesSolici;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TransactionRequiredException;

/**
 *
 * @author Edwin
 */
@Local
public interface IPersistenciaKioNovedadesSolici {
    public void crearNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws EntityExistsException, TransactionRolledbackLocalException, Exception;
    public void modificarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws EntityExistsException, TransactionRolledbackLocalException, Exception;
    public KioNovedadesSolici recargarNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws NoResultException, NonUniqueResultException, IllegalStateException;
    public void removerNovedadSolici(EntityManager em, KioNovedadesSolici novedadSolici) throws IllegalArgumentException, TransactionRequiredException, Exception;
}
