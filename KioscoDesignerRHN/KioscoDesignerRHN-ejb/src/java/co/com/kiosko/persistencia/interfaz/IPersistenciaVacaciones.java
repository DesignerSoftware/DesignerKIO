package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Vacaciones;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin
 */
@Local
public interface IPersistenciaVacaciones {
    public List<Vacaciones> consultarVacasDispEmpleado(EntityManager em, BigDecimal secEmpleado);
}
