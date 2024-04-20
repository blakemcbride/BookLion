package business;

import java.sql.Timestamp;
import java.util.Calendar;

import booklion.client.utils.StandardReturn;
import org.apache.log4j.Logger;
import beans.LoginUser;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;

/**
 * @author Blake McBride
 */
public class User {
	
	public static final long MAX_MINUTES_LIVE = 60L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(User.class);

	private LoginUser user;
    private boolean guest = false;

    public static final String guestUserUUID = "b1b9df29-124e-4855-af69-6c2ccdd47966s";

    public User(LoginUser rec) {
        user = rec;
    }

    public User(String uuid) {
        if (guestUserUUID.equals(uuid)) {
            guest = true;
            return;
        }
		HibernateSessionUtil hsu = HibernateUtil.getHSU();
		HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
		hcu.eq(LoginUser.UUID, uuid);
		user = (LoginUser) hcu.getFirst();
		if (user == null  ||  user.getUuidLastUsed() == null) {
//			logger.error("uuid " + uuid + " failed login");			
			user = null;
			return;
		}
//		logger.error("uuid " + uuid + " succeeded login");
		long last_used = user.getUuidLastUsed().getTime();
		long current_time = Calendar.getInstance().getTimeInMillis();
		if (last_used > current_time || ((current_time - last_used) / (1000L * 60L)) > MAX_MINUTES_LIVE) {
			user = null;
			return;
		}
		user.setUuidLastUsed(new Timestamp(current_time));
	}
	
	public User(long userLoginId) {
		HibernateSessionUtil hsu = HibernateUtil.getHSU();
		HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
		hcu.eq(LoginUser.USER_ID, userLoginId);
		user = (LoginUser) hcu.getFirst();
	}

    public boolean isValidLogin() {
        return guest ? true : user != null  &&  "A".equals(user.getUserStatus());
    }

    public boolean isNonGuestValidLogin() {
        return guest ? false : user != null;
    }

	public boolean isAdministrator() {
		return (guest  ||  user == null  ||  !"Y".equals(user.getAdministrator())) ? false : true;
	}

    public String badLoginMessage() {
        return guest ? "You are logged in as a guest.  Although guest users can view any data, you must create a normal login in order to edit any data." : "Invalid login";
    }

    public StandardReturn invalidLogin() {
        return new StandardReturn(badLoginMessage());
    }
	
	public LoginUser getBean() {
		return user;
	}
}
