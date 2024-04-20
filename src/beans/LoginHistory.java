
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
@Table(name="login_history")
public class LoginHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LoginHistory.class);
    
    private LoginHistoryPk pk;
    private String ip_address;

    @EmbeddedId
    public LoginHistoryPk getPk() {
        return pk;
    }

    public void setPk(LoginHistoryPk pk) {
        this.pk = pk;
    }

    public static class LoginHistoryPk implements Serializable {

		private static final long serialVersionUID = 1L;

		private Long user_id;
        private Timestamp login_date;

        public LoginHistoryPk() {}

        @Column(name="login_date")
        public Timestamp getLoginDate() {
            return login_date;
        }

        public void setLoginDate(Timestamp login_date) {
            this.login_date = login_date;
        }

        @Column(name="user_id")
        public long getUserId() {
            return user_id;
        }

        public void setUserId(long user_id) {
            this.user_id = user_id;
        }
        
        @Override
        public int hashCode() {
            int h = user_id.hashCode();
            h ^= login_date.hashCode();
            return h;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LoginHistoryPk))
                return false;
            LoginHistoryPk k = (LoginHistoryPk) obj;
            return this.equals((Object)k.user_id) && this.equals((Object)k.login_date);
        }
  
    }

    @Column(name="ip_address")
    public String getIPAddress() {
        return ip_address;
    }

    public void setIPAddress(String ip) {
        ip_address = ip;
    }

}
