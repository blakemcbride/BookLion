package utils;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 */
public class EmailAttachment {
    private String attachmentName;
    private String diskFileName;
    private byte [] byteArray;
    private String type;

    public EmailAttachment(String diskFileName, String attachementName) {
        this.diskFileName = diskFileName;
        this.attachmentName = attachementName;
        //           this.type = getMimeType(attachementName);
    }

    EmailAttachment(byte [] data, String attachementName, String type) {
        this.byteArray = data;
        this.attachmentName = attachementName;
        this.type = type;
    }

    public EmailAttachment(byte [] data, String attachementName) {
        this.byteArray = data;
        this.attachmentName = attachementName;
        //          this.type = getMimeType(attachementName);
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
