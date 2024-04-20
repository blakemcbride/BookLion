package utils;

import javax.mail.MessagingException;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 *
 * Using this technique, I can change email providers with a single line of code.
 */
public class SendEmailProvider {

    public static SendEmailGeneric newEmail() throws MessagingException {
       return new SendEmailPostmark();
       // return new SendEmailAWS();
    }
}
