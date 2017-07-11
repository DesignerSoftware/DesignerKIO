package co.com.kiosko.temporizadores;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timer;

//@Singleton
@Stateless
public class TemporizadorSesiones {

    @EJB
    private IAdministrarSesiones administrarSesiones;

    //@Schedule(hour = "23", minute = "40", second = "00")
    public void ejecutarBalance(Timer timer) {
        if (administrarSesiones.borrarSesiones()) {
            System.out.println("BORRADO DE SESIONES EXITOSO: " + new Date());
        } else {
            System.out.println("ERROR BORRADO DE SESIONES: " + new Date());
        }
    }
}
