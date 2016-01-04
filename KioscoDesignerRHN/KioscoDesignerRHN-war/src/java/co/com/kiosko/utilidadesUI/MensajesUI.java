package co.com.kiosko.utilidadesUI;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Felipe Triviño
 */
public class MensajesUI {

    public static void info(String detail) {
        FacesContext.getCurrentInstance().addMessage("messagess",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Información: ", detail));
    }

    public static void warn(String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención: ", detail));
    }

    public static void error(String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", detail));
    }

    public static void fatal(String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal: ", detail));
    }

    public static void error(List<String> messages) {
        for (String m : messages) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "ingrese: ",
                    m));
        }
    }
}
