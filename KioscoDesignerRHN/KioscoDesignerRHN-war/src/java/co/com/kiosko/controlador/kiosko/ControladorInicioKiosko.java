package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.*;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorInicioKiosko implements Serializable {

    @EJB
    private IAdministrarInicioKiosko administrarInicioKiosko;
    private String usuario;
    private ConexionesKioskos conexionEmpleado;
    private Date ultimaConexionEmpleado;
    //FOTO EMPLEADO
    private FileInputStream fis;
    private StreamedContent fotoEmpleado;
    private String pathFoto;
    private BigInteger identificacionEmpleado;

    public ControladorInicioKiosko() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicioKiosko.obtenerConexion(ses.getId());
            conexionEmpleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getConexionEmpleado();
            ultimaConexionEmpleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUltimaConexion();
            pathFoto = administrarInicioKiosko.fotoEmpleado();
            obtenerFotoEmpleado();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void obtenerFotoEmpleado() {
        String rutaFoto = pathFoto + conexionEmpleado.getEmpleado().getCodigoempleado() + ".jpg";
        if (rutaFoto != null) {
            try {
                fis = new FileInputStream(new File(rutaFoto));
                fotoEmpleado = new DefaultStreamedContent(fis, "image/jpg");
            } catch (IOException e) {
                try {
                    fis = new FileInputStream(new File(pathFoto + "sinFoto.jpg"));
                    fotoEmpleado = new DefaultStreamedContent(fis, "image/jpg");
                    //System.out.println("Foto del empleado no encontrada. \n" + e);
                } catch (Exception ex) {
                    System.out.println("ERROR. \n" + e);
                }
            }
        }
    }

    //SUBIR FOTO EMPLEADO
    public void subirFotoEmpleado(FileUploadEvent event) throws IOException {
        RequestContext context = RequestContext.getCurrentInstance();
        String extension = event.getFile().getFileName().split("[.]")[1];
        Long tamanho = event.getFile().getSize();
        if (extension.equals("jpg") || extension.equals("JPG")) {
            if (tamanho <= 153600) {
                identificacionEmpleado = conexionEmpleado.getEmpleado().getPersona().getNumerodocumento();
                transformarArchivo(tamanho, event.getFile().getInputstream());
                obtenerFotoEmpleado();
                PrimefacesContextUI.ejecutar("PF('subirFoto').hide()");
                PrimefacesContextUI.actualizar("principalForm");
                MensajesUI.info("Foto cargada con éxito.");
            } else {
                MensajesUI.error("El tamaño máximo permitido es de 150 KB.");
            }
        } else {
            MensajesUI.error("Solo se admiten imágenes con formato (.JPG).");
        }
    }

    public void transformarArchivo(long size, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(pathFoto + identificacionEmpleado + ".jpg"));
            int reader = 0;
            byte[] bytes = new byte[(int) size];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Error: ControladorInicioKiosko.transformarArchivo: " + e);
        }
    }

    //GETTER AND SETTER
    public ConexionesKioskos getConexionEmpleado() {
        return conexionEmpleado;
    }

    public StreamedContent getFotoEmpleado() {
        if (fotoEmpleado != null) {
            try {
                fotoEmpleado.getStream().available();
            } catch (Exception e) {
                obtenerFotoEmpleado();
            }
        }
        return fotoEmpleado;
    }

    public void setFotoEmpleado(StreamedContent fotoEmpleado) {
        this.fotoEmpleado = fotoEmpleado;
    }

    public Date getUltimaConexionEmpleado() {
        return ultimaConexionEmpleado;
    }
}
