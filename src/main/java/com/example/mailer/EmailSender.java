package com.example.mailer;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Handles the core email sending logic utilizing the Jakarta Mail API.
 * <p>
 * This class abstracts the complexities of SMTP configuration and MIME message 
 * construction, providing a single method to dispatch plain-text emails.
 * </p>
 * 
 * @author Your Name
 * @version 1.0
 */
public class EmailSender {

    /**
     * Sends a plain-text email via an SMTP server.
     *
     * @param host     The SMTP server hostname (e.g., "smtp.gmail.com").
     * @param port     The SMTP server port (e.g., 465 for SSL, 587 for STARTTLS).
     * @param username The SMTP account username (usually the full email address).
     * @param password The SMTP account password or app-specific authorization code.
     * @param from     The sender's email address.
     * @param to       The recipient's email address (comma-separated for multiple).
     * @param subject  The subject line of the email.
     * @param body     The plain-text body content of the email.
     * @param useSSL   True to use implicit SSL (typically port 465), false otherwise.
     * @throws MessagingException If there is an error connecting to the server or sending the message.
     */
    public static void sendEmail(String host, int port,
                                 String username, String password,
                                 String from, String to,
                                 String subject, String body,
                                 boolean useSSL) throws MessagingException {

        // Configure SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        // Enable SSL if requested
        if (useSSL) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", String.valueOf(port));
            props.put("mail.smtp.socketFactory.fallback", "false");
        }

        // Create an authenticated session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Construct the email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        
        // Set content with UTF-8 encoding to support international characters
        message.setContent(body, "text/plain; charset=UTF-8");

        // Dispatch the email
        Transport.send(message);
    }
}