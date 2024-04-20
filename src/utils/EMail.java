package utils;

/**
 * @author Blake McBride
 * Date: 4/9/14
 */
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;


public class EMail {

    private static final Logger logger = Logger.getLogger(EMail.class);
    static private String host = "localhost";
    static private String emailUser = "admin";
    static private String emailPassword = "htYmvr#78226";

    public static void test() {
        EMail.send("BookLion <do-not-reply@booklion.com>", "Blake McBride <blake@arahant.com>", "Test " + DateUtils.getCurrentTimeStamp(), "<h2>Welcome to BookLion</h2>"+
        "<p>By signing on to BookLion you are agreeing to our terms of service.  Please click <a href=\"http://booklion.com/terms.html\">here</a> to see those terms.</p>");
    }

    public static void send(String from, String to, String subject, String outMessage) {
        new EMail().sendMail(from, to, subject, outMessage);
    }

    public boolean sendMail(String from, String to, String subject, String outMessage) {
        return sendMail(from, to, subject, outMessage, (List) null);
    }

    public boolean sendMail(String from, String to, String subject, String outMessage, String attachFilename) {
        List<String> list = new ArrayList<String>();
        list.add(attachFilename);
        return sendMail(from, to, subject, outMessage, list);
    }

    public static boolean send(String from, String to, String subject, String outMessage, String attachFilename) {
        EMail m = new EMail();
        return m.sendMail(from, to, subject, outMessage, attachFilename);
    }

    public static boolean send(String from, String to, String subject, String outMessage, List attachFilenames) {
        EMail m = new EMail();
        return m.sendMail(from, to, subject, outMessage, attachFilenames);
    }

    private static String getName(String s) {
        return s.replaceFirst(" <.*", "");
    }

    private static String getAddress(String s) {
        return s.replaceFirst("[^<]*<", "").replaceFirst(">", "");
    }

    private boolean sendMail(String from, String to, String subject, String outMessage, List attachFilenames) {
        try {
            SendEmailGeneric ep = SendEmailProvider.newEmail();
            ep.setHTMLMessage(outMessage);
            ep.sendEmail(getAddress(from), getName(from), getAddress(to), getName(to), subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private class Authenticator extends javax.mail.Authenticator {

        private PasswordAuthentication authentication;

        public Authenticator() {
            authentication = new PasswordAuthentication(emailUser, emailPassword);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    private class MailSender extends Thread {

        private String from, to, subject, outMessage;
        private List attachFilenames;

        @Override
        public void run() {
            sendMail();
        }

        public boolean sendMail() {
            try {
                if ("".equals(host))
                    return false;

                Authenticator authenticator = new Authenticator();

                // Get system properties
                Properties props = System.getProperties();

                props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
                props.setProperty("mail.smtp.auth", "true");

                // Setup mail server
                props.put("mail.smtp.host", host);

                // Get session
                Session session;

                session = Session.getInstance(props, authenticator);

                // Define message
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));

                StringTokenizer tokens = new StringTokenizer(to, ",;");
                while (tokens.hasMoreTokens()) {
                    String address = tokens.nextToken().trim();
                    Message.RecipientType type = Message.RecipientType.TO;

                    if (address.toLowerCase().startsWith("to:")) {
                        address = address.substring(3);
                        type = Message.RecipientType.TO;
                    } else if (address.toLowerCase().startsWith("cc:")) {
                        address = address.substring(3);
                        type = Message.RecipientType.CC;
                    } else if (address.toLowerCase().startsWith("bcc:")) {
                        address = address.substring(4);
                        type = Message.RecipientType.BCC;
                    }

                    message.addRecipient(type, new InternetAddress(address));
                }

                message.setSubject(subject);

                // Create the multi-part
                Multipart multipart = new MimeMultipart();

                // Create part one
                boolean containsHTML = outMessage.matches(".*\\<[^>]+>.*");
                BodyPart messageBodyPart = new MimeBodyPart();

                if (containsHTML) {
                    System.out.println("Sending HTML formatted email: " + outMessage);
                    messageBodyPart.setContent(outMessage, "text/html");
                } else
                    // Fill the message
                    messageBodyPart.setText(outMessage);

                // Add the first part
                multipart.addBodyPart(messageBodyPart);

                if (attachFilenames != null)
                    for (int loop = 0; loop < attachFilenames.size(); loop++) {
                        String attachFilename = (String) attachFilenames.get(loop);

                        // Part two is attachment
                        messageBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(attachFilename);
                        messageBodyPart.setDataHandler(new DataHandler(source));

                        File attachedFile = new File(attachFilename);
                        messageBodyPart.setFileName(attachedFile.getName());

                        // Add the second part
                        multipart.addBodyPart(messageBodyPart);
                    }

                // Put parts in message
                message.setContent(multipart);

                // Send message
                Transport.send(message);
                return true;
            } catch (AddressException e) {
                logger.error("EMail address exception", e);
            } catch (MessagingException e) {
                logger.error("EMail message exception", e);
            }
            return false;
        }
    }
}
