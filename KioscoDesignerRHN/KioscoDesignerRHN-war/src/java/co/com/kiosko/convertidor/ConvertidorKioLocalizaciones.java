package co.com.kiosko.convertidor;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.KioLocalizaciones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioLocalizaciones;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Edwin Hastamorir
 */
@ManagedBean
@javax.faces.bean.RequestScoped
public class ConvertidorKioLocalizaciones implements Converter{
    @EJB
    IAdministrarSesiones administrarSesiones;
    private EntityManagerFactory emf;
    
    @EJB
    IPersistenciaKioLocalizaciones persistenciaKioLocalizaciones;

    public ConvertidorKioLocalizaciones() {
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            emf = administrarSesiones.obtenerConexionSesion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println(this.getClass().getName()+ "Causa: " + e.getCause());
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object dato =null;
        if (value != null && value.trim().length() > 0) {
            try {
                EntityManager em = emf.createEntityManager();
                dato = persistenciaKioLocalizaciones.consultarKioLocalizacion(em, new BigDecimal(value));
                em.close();
            } catch (NumberFormatException e) {
                System.out.println("getAsObject: "+e);
            } catch (Exception ex) {
                System.out.println("getAsObject: "+ex);
                Logger.getLogger(ConvertidorKioLocalizaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            dato = null;
        }
        return dato;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((KioLocalizaciones) value).getSecuencia());
        } else {
            return null;
        }
    }
    
}
