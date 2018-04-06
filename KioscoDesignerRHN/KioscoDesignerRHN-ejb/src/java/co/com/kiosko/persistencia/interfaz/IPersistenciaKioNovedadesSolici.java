package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioNovedadesSolici;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
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
    public BigDecimal consultaTraslapamientos(EntityManager em, BigDecimal secEmpleado, Date fechaIniVaca, Date fechaFinVaca) throws PersistenceException, NullPointerException, Exception;
}
