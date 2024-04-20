
package beans;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Blake McBride
 */

@Entity
@Table(name="login_user")
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LoginUser.class);
	
    private long user_id;
    private String user_password;
    private String user_lname;
    private String user_fname;
    private String emailAddress;
    private int birth_date;
    private String sex;
    private Timestamp when_added;
    private Timestamp when_authorized;
    private String administrator;
    private String current_uuid;
    private Timestamp uuid_last_used;
    private String new_email;
    private String new_password;
    private String user_status="A";
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "emailAddress";
    public static final String UUID = "currentUuid";
    public static final String LNAME = "lname";
    public static final String FNAME = "fname";
    public static final String NEW_EMAIL = "newEmail";
    
    public LoginUser() {}

    @Id
//  @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="login_user_user_id_seq")
    @SequenceGenerator(name="login_user_user_id_seq", sequenceName="login_user_user_id_seq", allocationSize=1)
    @Column(name="user_id")
    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    @Column(name="email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String email_address) {
        this.emailAddress = email_address;
    }

    @Column(name="birth_date")
    public int getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(int birth_date) {
        this.birth_date = birth_date;
    }

    @Column(name="sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name="user_fname")
    public String getFname() {
        return user_fname;
    }

    public void setFname(String user_fname) {
        this.user_fname = user_fname;
    }

    @Column(name="user_lname")
    public String getLname() {
        return user_lname;
    }

    public void setLname(String user_lname) {
        this.user_lname = user_lname;
    }

    @Column(name="user_password")
    public String getPassword() {
        return user_password;
    }

    public void setPassword(String user_password) {
        this.user_password = user_password;
    }

    @Column(name="when_added")
    public Timestamp getWhenAdded() {
        return when_added;
    }

    public void setWhenAdded(Timestamp when_added) {
        this.when_added = when_added;
    }

    @Column(name="when_authorized")
    public Timestamp getWhenAuthorized() {
        return when_authorized;
    }

    public void setWhenAuthorized(Timestamp when_authorised) {
        this.when_authorized = when_authorised;
    }

    @Column(name="administrator")
	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}

    @Column(name="current_uuid")
	public String getCurrentUuid() {
		return current_uuid;
	}

	public void setCurrentUuid(String current_uuid) {
		this.current_uuid = current_uuid;
	}

    @Column(name="uuid_last_used")
	public Timestamp getUuidLastUsed() {
		return uuid_last_used;
	}

	public void setUuidLastUsed(Timestamp uuid_last_used) {
		this.uuid_last_used = uuid_last_used;
	}

    @Column(name="new_email")
    public String getNewEmail() {
        return new_email;
    }

    public void setNewEmail(String email_address) {
        this.new_email = email_address;
    }

    @Column(name="new_password")
    public String getNewPassword() {
        return new_password;
    }

    public void setNewPassword(String user_password) {
        this.new_password = user_password;
    }

    @Column(name="user_status")
    public String getUserStatus() {
        return user_status;
    }

    public void setUserStatus(String user_status) {
        this.user_status = user_status;
    }

    @Column(name="record_change_date")
    public Timestamp getRecordChangeDate() {
        return record_change_date;
    }

    public void setRecordChangeDate(Timestamp record_change_date) {
        this.record_change_date = record_change_date;
    }

    @Column(name="record_change_type")
    public String getRecordChangeType() {
        return record_change_type;
    }

    public void setRecordChangeType(String record_change_type) {
        this.record_change_type = record_change_type;
    }

    @Column(name="record_user_id")
    public long getRecordUserId() {
        return record_user_id;
    }

    public void setRecordUserId(long record_user_id) {
        this.record_user_id = record_user_id;
    }

}
