package co.com.kiosko.convertidor;

import co.com.kiosko.entidades.PreguntasKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaPreguntasKioskos;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class ConvertidorPreguntasKiosko implements Converter {

    @EJB
    IAdministrarSesiones administrarSesiones;
    @EJB
    IPersistenciaPreguntasKioskos persistenciaActa;
    private EntityManagerFactory emf;

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            emf = administrarSesiones.obtenerConexionSesion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Object dato =null;
        if (value != null && value.trim().length() > 0) {
            try {
                EntityManager em = emf.createEntityManager();
                dato = persistenciaActa.consultarPreguntaSeguridad(em, new BigDecimal(value));
                em.close();
            } catch (NumberFormatException e) {
                System.out.println("getAsObject: "+e);
            }
        } else {
            dato = null;
        }
        return dato;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((PreguntasKioskos) object).getSecuencia());
        } else {
            return null;
        }
    }
}
