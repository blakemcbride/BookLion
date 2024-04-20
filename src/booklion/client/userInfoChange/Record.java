package booklion.client.userInfoChange;

import booklion.client.utils.StandardReturn;

/**
 * @author Blake McBride
 * Date: 4/29/14
 */
public class Record extends StandardReturn {

    private String lname;
    private String fname;
    private String email;
    private String oldPassword;
    private String newPassword;
    private int birthDate;
    private Character sex;

    public Record() {}

    public Record(int ret) {
        super(ret);
    }

    public Record(String msg) {
        super(msg);
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }
}
