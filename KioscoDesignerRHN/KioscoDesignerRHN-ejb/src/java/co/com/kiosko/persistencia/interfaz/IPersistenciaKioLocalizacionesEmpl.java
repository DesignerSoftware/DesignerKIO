package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioEstadosLocalizaSolici;
import co.com.kiosko.entidades.KioLocalizacionesEmpl;
import co.com.kiosko.entidades.KioSolicisLocaliza;
import java.math.BigDecimal;
import java.util.Calendar;
//import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

/**
 *
 * @author usuario
 */
@Local
public interface IPersistenciaKioLocalizacionesEmpl {

    public List<KioLocalizacionesEmpl> crearLocalizacionEmpleado(EntityManager em, KioSolicisLocaliza solicitud, List<KioLocalizacionesEmpl> listaLocalizaEmpl) throws EntityExistsException, TransactionRolledbackLocalException, Exception;

    public List<KioEstadosLocalizaSolici> consultarEstadosLocalizaXEmpleado(EntityManager em, BigDecimal secEmpleado, Calendar fechaEnvio) throws Exception;

}
