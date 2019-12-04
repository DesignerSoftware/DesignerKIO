package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empleados;
import java.math.BigInteger;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarInicioKiosko {

    public EntityManagerFactory obtenerConexion(String idSesion);

    public Empleados consultarEmpleado(String idSesion, BigInteger codigoEmpleado, long nit);

    public String fotoEmpleado(String idSesion);
    
    public String consultarLogoEmpresa(String idSesion, long nit);
}
