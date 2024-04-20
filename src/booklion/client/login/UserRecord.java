package booklion.client.login;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 4/11/14
 */
public class UserRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lname;
	private String fname;
	private String email;
	private int birthDate;
	private Character sex;

	public UserRecord() {}

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