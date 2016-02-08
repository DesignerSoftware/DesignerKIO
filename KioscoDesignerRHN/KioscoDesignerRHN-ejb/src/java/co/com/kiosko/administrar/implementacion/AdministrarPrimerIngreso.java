package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.entidades.Empleados;
import co.com.kiosko.administrar.entidades.ParametrizaClave;
import co.com.kiosko.administrar.entidades.PreguntasKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarPrimerIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaParametrizaClave;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPreguntasKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaUtilidadesBD;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarPrimerIngreso implements IAdministrarPrimerIngreso {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaPreguntasKioskos persistenciaPreguntasKioskos;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaParametrizaClave persistenciaParametrizaClave;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<PreguntasKioskos> obtenerPreguntasSeguridad() {
        return persistenciaPreguntasKioskos.obtenerPreguntasSeguridad(em);
    }

    @Override
    public PreguntasKioskos consultarPreguntaSeguridad(BigDecimal secuencia) {
        return persistenciaPreguntasKioskos.consultarPreguntaSeguridad(em, secuencia);
    }

    @Override
    public boolean registrarConexionKiosko(ConexionesKioskos cnk) {
        return persistenciaConexionesKioskos.registrarConexion(em, cnk);
    }

    @Override
    public Empleados consultarEmpleado(BigInteger codigoEmpleado) {
        return persistenciaEmpleados.consultarEmpleado(em, codigoEmpleado);
    }

    @Override
    public byte[] encriptar(String valor) {
        return persistenciaUtilidadesBD.encriptar(em, valor);
    }

    @Override
    public String desencriptar(byte[] valor) {
        return persistenciaUtilidadesBD.desencriptar(em, valor);
    }

    @Override
    public ParametrizaClave obtenerFormatoClave(long nitEmpresa) {
        return persistenciaParametrizaClave.obtenerFormatoClave(em, nitEmpresa);
    }
}
