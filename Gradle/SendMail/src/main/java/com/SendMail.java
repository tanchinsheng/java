package com;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    private static final String SENDERS_EMAIL = "raohongling1977@gmail.com";
    private static final String SENDERS_PWD = "rao@hong@ling";
    private static final String RECIPIENTS_EMAIL = "tanchinsheng@gmail.com";

    public static void main(String[] args) {

        Properties mailProps = new Properties();

        // Set properties required to connect to Gmail's SMTP server
        mailProps.put("mail.smtp.host", "smtp.gmail.com");
        mailProps.put("mail.smtp.port", "587");
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");

        // Create a username-password authenticator to authenticate SMTP session
        Authenticator authenticator = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDERS_EMAIL, SENDERS_PWD);
            }
        };

        // Create the mail session
        Session session = Session.getDefaultInstance(mailProps, authenticator);
        try {
            // Create a default MimeMessage object.
            final MimeMessage message = new MimeMessage(session);

            // Set the sender's email address
            message.setFrom(new InternetAddress(SENDERS_EMAIL));

            // Set recipient's email address
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENTS_EMAIL));

            // Set the subject of the email
            message.setSubject("Hello!");

            // Now set the actual message body of the email
            message.setText("This email was sent using JavaMail API through Gmail! Isn't it awesome?");

            System.out.println("Sending email...");

            // Send message
            Transport.send(message);

            System.out.println("Email sent!");

        } catch (Exception e) {
            System.err.println("Problem sending email. Exception : " + e.getMessage());
        }
    }
}
