package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kissweb.FileUtils;
import org.kissweb.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 2/13/23
 *
 * Email interface for SendEmailPostmark.
 *
 *  This is the first email provider that was done generically so that it'll be easier to switch email providers in the future.
 */

//  should not be public!
class SendEmailPostmark extends SendEmailGeneric {

    private static final String URL = "https://api.postmarkapp.com/email";
    private static final String STACK360_API_KEY = "28d698d2-5aba-4253-8968-e4ce5951b23b";
    private static final String WAY_TO_GO_API_KEY = "c22f9d8b-35fc-4da6-af30-eac04385f63d";
    private static final String BOOKLION_API_KEY = "06e350a3-5830-48fe-82f4-3487bdeefae8";

    private final boolean testMode;
    private String apiKey;
    private String message;
    private boolean isHTML;
    private List<EmailAttachment> attachments = null;

    //  Do not make this public!
    SendEmailPostmark() {
        testMode = false;
        apiKey = BOOKLION_API_KEY;
    }

    public SendEmailPostmark sendEmail(String from, String fromname, String to, String toname, String subject) throws IOException {
        EmailAddress ad = new EmailAddress(to, toname);
        List<EmailAddress> adl = new ArrayList<>();
        adl.add(ad);
        return sendEmail(from, fromname, adl, null, null, subject);
    }

    /**
     * This is the method that actually sends the message.
     *
     * @param from
     * @param fromname
     * @param toAddresses
     * @param ccAddresses
     * @param bccAddresses
     * @param subject
     * @return
     */
    public SendEmailPostmark sendEmail(String from, String fromname, List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws IOException {
        if ((toAddresses == null || toAddresses.isEmpty()) &&
                (ccAddresses == null || ccAddresses.isEmpty()) &&
                (bccAddresses == null || bccAddresses.isEmpty()))
            return null;
        RestClient rc = new RestClient();
        JSONObject headers = new JSONObject();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("X-Postmark-Server-Token", apiKey);
        JSONObject data = new JSONObject();
        data.put("MessageStream", "outbound");
        data.put("Subject", subject);
        if (fromname != null && !fromname.isEmpty())
            data.put("From", fromname + " <" + from + ">");
        else
            data.put("From", from);

        if (!testMode) {
            if (toAddresses != null && !toAddresses.isEmpty()) {
                boolean addComma = false;
                StringBuilder to = new StringBuilder();
                for (EmailAddress t : toAddresses) {
                    if (addComma)
                        to.append(", ");
                    else
                        addComma = true;
                    if (t.getName() != null && !t.getName().isEmpty())
                        if (t.getName().contains(","))
                            to.append("\"").append(t.getName()).append("\"").append(" <").append(t.getAddress()).append(">");
                        else
                            to.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                    else
                        to.append(t.getAddress());
                }
                data.put("To", to.toString());
            }
        } else {
            data.put("To", "blake1024@gmail.com");
        }

        if (!testMode && ccAddresses != null && !ccAddresses.isEmpty()) {
            boolean addComma = false;
            StringBuilder cc = new StringBuilder();
            for (EmailAddress t : ccAddresses) {
                if (addComma)
                    cc.append(", ");
                else
                    addComma = true;
                if (t.getName() != null && !t.getName().isEmpty())
                    cc.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                else
                    cc.append(t.getAddress());
            }
            data.put("Cc", cc.toString());
        }

        if (!testMode && bccAddresses != null && !bccAddresses.isEmpty()) {
            boolean addComma = false;
            StringBuilder bcc = new StringBuilder();
            for (EmailAddress t : bccAddresses) {
                if (addComma)
                    bcc.append(", ");
                else
                    addComma = true;
                if (t.getName() != null && !t.getName().isEmpty())
                    bcc.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                else
                    bcc.append(t.getAddress());
            }
            data.put("Bcc", bcc.toString());
        }

        if (message == null || message.isEmpty())
            message = "Blank message";
        if (message != null && !message.isEmpty())
            if (isHTML)
                data.put("HtmlBody", message);
            else
                data.put("TextBody", message);

        if (attachments != null && !attachments.isEmpty()) {
            JSONArray att = new JSONArray();
            for (EmailAttachment a : attachments) {
                JSONObject aobj = new JSONObject();
                if (a.getDiskFileName() == null && a.getByteArray() != null) {
                    aobj.put("Content", org.kissweb.Crypto.base64Encode(a.getByteArray()));
                    aobj.put("Name", a.getAttachmentName());
                    aobj.put("ContentType", getMimeType(a.getAttachmentName()));
                } else if (a.getDiskFileName() != null && a.getByteArray() == null) {
                    aobj.put("Content", org.kissweb.Crypto.base64Encode(FileUtils.readFileBytes(a.getDiskFileName())));
                    aobj.put("Name", a.getAttachmentName());
                    aobj.put("ContentType", getMimeType(a.getDiskFileName()));
                } else
                    continue;
                att.put(aobj);
            }
            data.put("Attachments", att);
        }

        JSONObject res = rc.jsonCall("POST", URL, data, headers);
        String rs = rc.getResponseString();
        if (rs != null) {
            try {
                JSONObject json = new JSONObject(rs);
                int ec = json.getInt("ErrorCode");
                if (ec != 0)
                    System.out.println("Postmark error response - " + rs);
            } catch (Exception e) {
                // ignore
            }
        }

        return this;
    }

    public SendEmailGeneric setTextMessage(String txt) {
        message = txt;
        isHTML = false;
        return this;
    }

    public SendEmailGeneric setHTMLMessage(String txt) {
        message = txt;
        isHTML = true;
        return this;
    }

    public SendEmailGeneric addAttachement(String diskFileName, String attachementName) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(diskFileName, attachementName));
        return this;
    }

    public SendEmailGeneric addAttachement(byte [] data, String attachementName, String type) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(data, attachementName, type));
        return this;
    }

    public SendEmailGeneric setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public SendEmailGeneric addAttachement(byte [] data, String attachementName) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(data, attachementName, getMimeType(attachementName)));
        return this;
    }

    public static void main(String[] args) throws IOException {
        RestClient rc = new RestClient();
        JSONObject headers = new JSONObject();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        //headers.put("X-Postmark-Server-Token", WAY_TO_GO_API_KEY);
        headers.put("X-Postmark-Server-Token", STACK360_API_KEY);
        JSONObject data = new JSONObject();
//        data.put("From", "blake@stack360.io");
        data.put("From", "Sam Jones <do-not-reply@stack360.io>");
        data.put("To", "John Smith <blake@stack360.io>, Jane Doe <blake1024@gmail.com>");
        data.put("Subject", "Hello 1");
      //  data.put("TextBody", "Hello message body");
        data.put("HtmlBody", "<h1>The Title</h1><p>Hello message body</p>");
        data.put("MessageStream", "outbound");

        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        attachment.put("Content", org.kissweb.Crypto.base64Encode(FileUtils.readFileBytes("/home/blake/Personal/BlakeSmall.jpg")));
        attachment.put("Name", "BlakeSmall.jpg");
        attachment.put("ContentType", "image/jpeg");
        attachments.put(attachment);
        data.put("Attachments", attachments);

        JSONObject res = rc.jsonCall("POST", URL, data, headers);
        String rs = rc.getResponseString();
        int i = 1;
    }

    @Override
    public void close() throws Exception {
        apiKey = null;
    }

    private static String getMimeType(String fname) {
        final String dflt = "application/octet-stream";
        String ext;
        if (fname == null || fname.isEmpty())
            return dflt;
        int idx = fname.lastIndexOf('.');
        if (idx == -1)
            ext = fname;  //  only the extension was passed in
        else if (idx == fname.length() - 1)
            return dflt;
        else
            ext = fname.substring(idx+1);
        switch (ext.toLowerCase()) {
            case "aac": return "audio/aac";
            case "abw": return "application/x-abiword";
            case "arc": return "application/x-freearc";
            case "avi": return "video/x-msvideo";
            case "azw": return "application/vnd.amazon.ebook";
            case "bin": return "application/octet-stream";
            case "bmp": return "image/bmp";
            case "bz": return "application/x-bzip";
            case "bz2": return "application/x-bzip2";
            case "cda": return "application/x-cdf";
            case "csh": return "application/x-csh";
            case "css": return "text/css";
            case "csv": return "text/csv";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "eot": return "application/vnd.ms-fontobject";
            case "epub": return "application/epub+zip";
            case "gz": return "application/gzip";
            case "gif": return "image/gif";
            case "htm": return "text/html";
            case "html": return "text/html";
            case "ico": return "image/vnd.microsoft.icon";
            case "ics": return "text/calendar";
            case "jar": return "application/java-archive";
            case "jpeg": return "image/jpeg";
            case "jpg": return "image/jpeg";
            case "js": return "text/javascript";
            case "json": return "application/json";
            case "jsonld": return "application/ld+json";
            case "mid": return "audio/x-midi";
            case "midi": return "audio/x-midi";
            case "mjs": return "text/javascript";
            case "mp3": return "audio/mpeg";
            case "mp4": return "video/mp4";
            case "mpeg": return "video/mpeg";
            case "mpg": return "video/mpeg";
            case "mpkg": return "application/vnd.apple.installer+xml";
            case "odp": return "application/vnd.oasis.opendocument.presentation";
            case "ods": return "application/vnd.oasis.opendocument.spreadsheet";
            case "odt": return "application/vnd.oasis.opendocument.text";
            case "oga": return "audio/ogg";
            case "ogv": return "video/ogg";
            case "ogx": return "application/ogg";
            case "opus": return "audio/opus";
            case "otf": return "font/otf";
            case "png": return "image/png";
            case "pdf": return "application/pdf";
            case "php": return "application/x-httpd-php";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "rar": return "application/vnd.rar";
            case "rtf": return "application/rtf";
            case "sh": return "application/x-sh";
            case "svg": return "image/svg+xml";
            case "swf": return "application/x-shockwave-flash";
            case "tar": return "application/x-tar";
            case "tif": return "image/tiff";
            case "tiff": return "image/tiff";
            case "ts": return "video/mp2t";
            case "ttf": return "font/ttf";
            case "txt": return "text/plain";
            case "vsd": return "application/vnd.visio";
            case "wav": return "audio/wav";
            case "weba": return "audio/webm";
            case "webm": return "video/webm";
            case "webp": return "image/webp";
            case "woff": return "font/woff";
            case "woff2": return "font/woff2";
            case "xhtml": return "application/xhtml+xml";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xml": return "application/xml";
            case "xul": return "application/vnd.mozilla.xul+xml";
            case "zip": return "application/zip";
            case "3gp": return "audio/3gpp";
            case "3g2": return "audio/3gpp2";
            case "7z": return "application/x-7z-compressed";
            default: return dflt;
        }
    }

}
