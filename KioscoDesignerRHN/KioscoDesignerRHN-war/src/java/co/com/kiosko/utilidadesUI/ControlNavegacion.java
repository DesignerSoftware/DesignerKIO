package co.com.kiosko.utilidadesUI;

//import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
//import co.com.kiosko.clasesAyuda.CadenasKioskos;
//import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.clasesAyuda.NavegationPageURL;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import org.primefaces.model.DefaultStreamedContent;
//import co.com.kiosko.controlador.ingreso.ControladorIngreso;
//import java.io.IOException;
//import javax.ejb.EJB;
//import javax.faces.context.FacesContext;
//import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlNavegacion implements Serializable {

    private String urlMenuNavegation;
    //private String nitEmpresa;
    //private String pathFoto;
    //private FileInputStream fis;
    //private StreamedContent fondoEmpresa;

    @PostConstruct
    public void init() {
        System.out.println("ControlNavegacion.init");
        //FacesContext x = FacesContext.getCurrentInstance();
        System.out.println("URL: "+NavegationPageURL.OPCIONESKIOSKO.getUrl());
        urlMenuNavegation = NavegationPageURL.OPCIONESKIOSKO.getUrl();
        //nitEmpresa = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
        //pathFoto = administrarInicioKiosko.fotoEmpleado();
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
            System.out.println("ControlNavegacion.configuracionAction_OpcionesKiosko");
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
         System.out.println("ControlNavegacion.pantallaDinamica");
        try {
            System.out.println("url: "+url);
            this.urlMenuNavegation = url;
        } catch (Exception e) {
            System.out.println("Error pantallaDinamica: " + e.getMessage());
        }
    }
     /*
     public void obtenerFondoEmpresa(){
        String rutaFondo = null;
        //consultaNitEmpresa();
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if ( elemento.getNit().equals( nitEmpresa ) ){
                rutaFondo = pathFoto + elemento.getFondo();
            }
        }
        if (rutaFondo != null){
            try {
                fis = new FileInputStream(new File(rutaFondo));
                fondoEmpresa = new DefaultStreamedContent(fis, "image/png");
            } catch (FileNotFoundException e) {
                try {
                    fis = new FileInputStream(new File(pathFoto + "fondoMenu.png"));
                    fondoEmpresa = new DefaultStreamedContent(fis, "image/png");
                } catch (FileNotFoundException ex) {
                    System.out.println("ERROR. No se encontro el fondo de la empresa. \n" + e);
                }
            }
        }
    }

    public StreamedContent getFondoEmpresa() {
        if (fondoEmpresa != null) {
            try {
                fondoEmpresa.getStream().available();
            } catch (IOException e) {
                obtenerFondoEmpresa();
            }
        } else {
            obtenerFondoEmpresa();
        }
        return fondoEmpresa;
    }

    public void setFondoEmpresa(StreamedContent fondoEmpresa) {
        this.fondoEmpresa = fondoEmpresa;
    }
    */
}