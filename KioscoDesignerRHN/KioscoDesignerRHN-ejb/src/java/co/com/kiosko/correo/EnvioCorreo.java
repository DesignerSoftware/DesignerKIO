package co.com.kiosko.correo;

import co.com.kiosko.entidades.ConfiguracionCorreo;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
//import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Felipe Triviño
 */
public class EnvioCorreo {

    public EnvioCorreo() {
    }

    public static boolean enviarCorreo(ConfiguracionCorreo cfc, String destinatario, String asunto, String mensaje, String pathAdjunto) {
//        try {
        boolean resEnvio = false;
        // Propiedades de la conexión
        Properties propiedadesConexion = new Properties();
        propiedadesConexion.setProperty("mail.smtp.host", cfc.getServidorSmtp()); //IP DEL SERVIDOR SMTP
        propiedadesConexion.setProperty("mail.smtp.port", cfc.getPuerto());

        if (cfc.getAutenticado().equalsIgnoreCase("S")) {
            propiedadesConexion.setProperty("mail.smtp.auth", "true");
            if (cfc.getUsarssl().equalsIgnoreCase("S")) {
                propiedadesConexion.put("mail.smtp.socketFactory.port", cfc.getPuerto());
                propiedadesConexion.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }else if (cfc.getStarttls().equalsIgnoreCase("S")) {
                propiedadesConexion.setProperty("mail.smtp.starttls.enable", "true");
            }
        }

        // Preparamos la sesion
        Session session = Session.getDefaultInstance(propiedadesConexion);
        /*Session session = Session.getDefaultInstance(propiedadesConexion, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(cfc.getRemitente(), cfc.getClave());
                }
            });*/
        try {
            //Mensaje que va en el correo
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);

            //Archivo adjunto
            BodyPart adjunto = null;
            if (pathAdjunto != null && !pathAdjunto.isEmpty()) {
                adjunto = new MimeBodyPart();
                FileDataSource archivo = new FileDataSource(pathAdjunto);
                adjunto.setDataHandler(new DataHandler(archivo));
                adjunto.setFileName(archivo.getFile().getName());
            }

            //Estructura del contenido (Texto y Adjnto)
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);

            if (adjunto != null) {
                multiParte.addBodyPart(adjunto);
            }

            // Construimos la estructura del correo final
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(cfc.getRemitente())); //REMITENTE
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario)); //DESTINATARIO
            message.setSubject(asunto); //ASUNTO
            message.setContent(multiParte); //CONTENIDO

            //Preparamos la conexion con el servidor SMTP
            Transport t = session.getTransport("smtp");

            //Validamos si requiere autenticacion o no.
            if (cfc.getAutenticado().equalsIgnoreCase("S")) {
                t.connect(cfc.getRemitente(), cfc.getClave());
            } else {
                t.connect();
            }

            //Enviamos el mensaje
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            // Cierre de la conexion.
            t.close();

            //System.out.println("CORREO ENVIADO EXITOSAMENTE");
//            return true;
            resEnvio = true;
        } catch (NoSuchProviderException nspe) {
            System.out.println("Error enviarCorreo: " + nspe.getMessage());
            resEnvio = false;
        } catch (MessagingException e) {
            System.out.println("Error enviarCorreo: " + e.getMessage());
            resEnvio = false;
        }
        return resEnvio;
    }
}
