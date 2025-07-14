package Modelo;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private final String remitente;
    private final String password;
    private final Properties props;

    public EmailSender() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.remitente = props.getProperty("email.remitente");
        this.password = props.getProperty("email.password");

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    private Session getSession() {
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });
    }

    public void enviarCodigo(String destinatario, String codigo) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Tu código de acceso");
            message.setText("Tu código de acceso es: " + codigo);

            Transport.send(message);
            System.out.println("Código enviado exitosamente");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void enviarConfirmacion(String destinatario) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Inicio de sesión exitoso");
            message.setText("¡Has iniciado sesión correctamente en la aplicación!");

            Transport.send(message);
            System.out.println("Correo de confirmación enviado");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);
            System.out.println("Correo enviado exitosamente");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
