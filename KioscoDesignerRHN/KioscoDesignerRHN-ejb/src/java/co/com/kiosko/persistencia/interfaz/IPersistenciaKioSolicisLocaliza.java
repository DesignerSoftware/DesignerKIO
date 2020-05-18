package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioSolicisLocaliza;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Edwin Hastamorir
 */
@Local
public interface IPersistenciaKioSolicisLocaliza {
    public void crearSolicitud(EntityManager em, KioSolicisLocaliza solicitud) throws EntityExistsException, TransactionRolledbackLocalException, Exception;
    public KioSolicisLocaliza recargarSolicitud(EntityManager em, KioSolicisLocaliza solicitud) throws NoResultException, NonUniqueResultException, IllegalStateException ;
}
