package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin Hastamorir
 */
@Local
public interface IPersistenciaKioEstadosLocalizaSolici {

    public void crearEstadoSolicitud(EntityManager em, KioSolicisLocaliza solicitud, BigDecimal secEmplEjecuta, String estado, String motivo, BigInteger secPersona) throws EntityExistsException, TransactionRolledbackLocalException, Exception;

    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXJefe(EntityManager em, BigDecimal secEmpleado, String estado) throws Exception;
    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXEmpleado(EntityManager em, BigDecimal secEmpleado, String estado) throws Exception;

    public List<KioLocalizacionesEmpl> consultarLocalizacionesXSolicitud(EntityManager em, BigDecimal secSolicitud) throws Exception;
    
}
