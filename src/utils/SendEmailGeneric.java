package utils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 */
public abstract class SendEmailGeneric implements AutoCloseable {

    public abstract SendEmailGeneric sendEmail(String from, String fromname, String to, String toname, String subject) throws IOException, MessagingException;

    public abstract SendEmailGeneric sendEmail(String from, String fromname, List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws IOException, MessagingException;
    public abstract SendEmailGeneric setTextMessage(String txt);

    public abstract SendEmailGeneric setHTMLMessage(String txt);

    public abstract SendEmailGeneric addAttachement(String diskFileName, String attachementName);

    public abstract SendEmailGeneric addAttachement(byte [] data, String attachementName, String type);

    public abstract SendEmailGeneric setAttachments(List<EmailAttachment> attachments);

    public abstract SendEmailGeneric addAttachement(byte [] data, String attachementName);

    @Override
    public abstract void close() throws Exception;
}
