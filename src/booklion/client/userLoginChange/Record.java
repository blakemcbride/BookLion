package booklion.client.userLoginChange;

import booklion.client.utils.StandardReturn;

/**
 * @author Blake McBride
 * Date: 5/7/14
 */
public class Record extends StandardReturn {

    private String oldEmail;
    private String oldPassword;
    private String newEmail;

    public Record() {}

    public Record(int ret) {
        super(ret);
    }

    public Record(String msg) {
        super(msg);
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
