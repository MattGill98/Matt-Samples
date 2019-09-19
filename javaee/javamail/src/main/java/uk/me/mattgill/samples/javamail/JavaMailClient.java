package uk.me.mattgill.samples.javamail;

import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Session.getInstance;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class JavaMailClient {

    private Session session;

    @Inject
    @ConfigProperty(name = "email.username")
    private String username;

    @Inject
    @ConfigProperty(name = "email.password")
    private String password;

    @PostConstruct
    public void startSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        this.session = getInstance(props, new Authenticator() {
            @SuppressWarnings("PMD.AccessorMethodGeneration")
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendMessageToSelf(String subject, String body) throws AddressException, MessagingException {
        Message message = new MimeMessage(this.session);
        // Set from and to
        message.setFrom(new InternetAddress(username));
        message.setRecipient(TO, new InternetAddress(username));
        // Set content
        message.setSubject(subject);
        message.setContent(body, TEXT_PLAIN);
        // Send
        Transport.send(message);
    }

}