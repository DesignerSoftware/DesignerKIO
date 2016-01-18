package co.com.kiosko.controlador.pruebas;

import java.io.*;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorPruebas {

    private String remitente, contrasenha, destinatarios, cco, cc, asunto, mensaje;
    private String destinos[];
    private String destinosOcultos[];
    private String destinosCopia[];
    private String mensajeValidacion;
    private String nombreArchivo;
    private String tipoTrabajador;

    public ControladorPruebas() {
    }

    public void cargarArchivo(FileUploadEvent event) throws IOException {
        nombreArchivo = event.getFile().getFileName();
        transformarArchivo(event.getFile().getSize(), event.getFile().getInputstream(), event.getFile().getFileName());
        RequestContext.getCurrentInstance().update("form:nombreArchivo");
    }

    public void transformarArchivo(long size, InputStream in, String nombreArchivo) {
        try {
            String destino = "C:\\Prueba\\Archivos_Envio_Correo\\" + nombreArchivo;

            OutputStream out = new FileOutputStream(new File(destino));
            int reader = 0;
            byte[] bytes = new byte[(int) size];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Pailander: " + e);
        }
    }

    public void validarEnvioCorreo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (enviarCorreo()) {
            mensajeValidacion = "Mensaje enviado con éxito";
        } else {
            mensajeValidacion = "El correo no pudo ser enviado, lo sentimos.";
        }
        context.update("form:validarEnvioCorreo");
        context.execute("validarEnvioCorreo.show();");
    }

    //ENVIAR CORREO
    public boolean enviarCorreo() {
        try {
            // Propiedades de la conexión
            Properties propiedadesConexion = new Properties();
            propiedadesConexion.setProperty("mail.smtp.host", "smtp.gmail.com"); //IP DEL SERVIDOR SMTP
            propiedadesConexion.setProperty("mail.smtp.starttls.enable", "true");
            propiedadesConexion.setProperty("mail.smtp.port", "587");
            propiedadesConexion.setProperty("mail.smtp.auth", "true");
            // Preparamos la sesion
            Session session = Session.getDefaultInstance(propiedadesConexion);
            //destinos = destinatarios.split(",");
            destinosOcultos = cco.split(",");
            destinosCopia = cc.split(",");

            //PRUEBA ENVIAR ARCHIVOS ADJUNTOS
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            /*
             * //List<Empleados> listaEmpleados =
             * administarReportes.empleadosEnvioCorreo(); for (int i = 0; i <
             * listaEmpleados.size(); i++) { BodyPart adjunto = new
             * MimeBodyPart();
             * administarReportes.generarReporte(listaEmpleados.get(i).getCodigoempleado());
             * adjunto.setDataHandler(new DataHandler(new
             * FileDataSource("C:/Reportes/Reportes_PDF/DesprendibleConTercero.pdf")));
             * adjunto.setFileName("DesprendibleConTercero.pdf"); BigDecimal a;
             * MimeMultipart multiParte = new MimeMultipart();
             * multiParte.addBodyPart(texto); multiParte.addBodyPart(adjunto);
             * // Construimos el mensaje MimeMessage message = new
             * MimeMessage(session); message.setFrom(new
             * InternetAddress(remitente));
             *
             * //direcciones de email a quienes se enviar el correo electronico
             *
             * Address[] receptoresOcultos = new
             * Address[destinosOcultos.length]; int k = 0; while (k <
             * destinosOcultos.length) { receptoresOcultos[k] = new
             * InternetAddress(destinosOcultos[k]); k++; }
             *
             * Address[] receptoresCopia = new Address[destinosCopia.length];
             * int l = 0; while (l < destinosCopia.length) { receptoresCopia[l]
             * = new InternetAddress(destinosCopia[l]); l++; } Address
             * receptinho = new
             * InternetAddress(listaEmpleados.get(i).getPersona().getEmail());
             * //receptores. message.addRecipient(Message.RecipientType.TO,
             * receptinho); message.addRecipients(Message.RecipientType.CC,
             * receptoresCopia);
             * message.addRecipients(Message.RecipientType.BCC,
             * receptoresOcultos); message.setSubject(asunto);
             * message.setContent(multiParte);
             *
             * // Lo enviamos. Transport t = session.getTransport("smtp");
             * t.connect(remitente, contrasenha); t.sendMessage(message,
             * message.getRecipients(Message.RecipientType.TO));
             * t.sendMessage(message,
             * message.getRecipients(Message.RecipientType.CC));
             * t.sendMessage(message,
             * message.getRecipients(Message.RecipientType.BCC)); // Cierre de
             * la conexion. t.close(); }
             */
            return true;
        } catch (Exception e) {
            System.out.println("Error enviarCorreo: " + e);
            return false;
        }
    }

    //GETTERS AND SETTERS
    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getCco() {
        return cco;
    }

    public void setCco(String cco) {
        this.cco = cco;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getTipoTrabajador() {
        return tipoTrabajador;
    }

    public void setTipoTrabajador(String tipoTrabajador) {
        this.tipoTrabajador = tipoTrabajador.toUpperCase();
    }
}
