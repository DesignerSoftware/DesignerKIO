package co.com.kiosko.utilidadesUI;

import co.com.kiosko.clasesAyuda.NavegationPageURL;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class ControlNavegacion implements Serializable {

    private String urlMenuNavegation;

    @PostConstruct
    public void init() {
        urlMenuNavegation = NavegationPageURL.OPCIONESKIOSKO.getUrl();
    }

    public String getUrlNavegation() {
        return urlMenuNavegation;
    }

    public String getUrlMenuNavegation() {
        return urlMenuNavegation;
    }

    public void setUrlMenuNavegation(String urlMenuNavegation) {
        this.urlMenuNavegation = urlMenuNavegation;
    }

    public void setUrlNavegation(String urlNavegation) {
        this.urlMenuNavegation = urlNavegation;
    }

    public void configuracionAction_OpcionesKiosko() throws Exception {
        try {
            this.urlMenuNavegation = NavegationPageURL.OPCIONESKIOSKO.getUrl();
        } catch (Exception e) {
            System.out.println("Error configuracionAction_OpcionesKiosko: " + e.getMessage());
        }
    }

    public void configuracionAction_GenerarReporte() throws Exception {
        try {
            this.urlMenuNavegation = NavegationPageURL.GENERARREPORTE.getUrl();
        } catch (Exception e) {
            System.out.println("Error configuracionAction_GenerarReporte: " + e.getMessage());
        }
    }

    public void configuracionAction_CambiarClave() throws Exception {
        try {
            this.urlMenuNavegation = NavegationPageURL.CAMBIARCLAVE.getUrl();
        } catch (Exception e) {
            System.out.println("Error configuracionAction_CambiarClave: " + e.getMessage());
        }
    }
    
     public void pantallaDinamica(String url) throws Exception {
        try {
            this.urlMenuNavegation = url;
        } catch (Exception e) {
            System.out.println("Error pantallaDinamica: " + e.getMessage());
        }
    }
}