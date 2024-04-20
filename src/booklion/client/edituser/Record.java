package booklion.client.edituser;

import java.io.Serializable;

/**
 * @author Blake McBride
 */
public class Record implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int recNo;  // sequential number for each record in the entire set (not just in this page)
	
	private long userId;
	private String admin;
	private String lname;
	private String fname;
	private String email;
	private String password;
	private int birthDate;
	private Character sex;
    private String newEmail;
    private String newPassword;
    private String userStatus;
	
	public Record() {}
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
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

	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userBanned) {
        this.userStatus = userBanned;
    }

    public Record copy() {
		Record ret = new Record();
		ret.recNo = this.recNo;
		ret.userId = this.userId;
		ret.admin = new String(this.admin);
		ret.lname = new String(this.lname);
		ret.fname = new String(this.fname);
		ret.email = new String(this.email);
		ret.password = new String(this.password);
		ret.birthDate = this.birthDate;
		ret.sex = new Character(this.sex);
        ret.newEmail = this.newEmail;
        ret.newPassword = this.newPassword;
        ret.userStatus = this.userStatus;
		return ret;
	}

}