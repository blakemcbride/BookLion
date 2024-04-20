/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package booklion.server.login;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import beans.*;
import booklion.client.login.UserData;
import booklion.client.login.UserRecord;
import booklion.client.utils.StandardReturn;
import business.*;
import org.apache.log4j.Logger;

import booklion.client.login.LoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import utils.Crypto;
import utils.DateUtils;
import utils.StringUtils;

/**
 * @author Blake McBride
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);

	@Override
	public UserData login(String userID, String pw) {
		HibernateSessionUtil hsu = null;
		UserData ret = new UserData();
        String user_id;
        String pwHash;

        pw = Crypto.decode(pw);
        try {
			hsu = HibernateUtil.openHSU();
            if ("_guest_".equals(userID)  &&  "ce04a5c4-efe0-4d4a-9ae7-943e986022b0".equals(pw)) {
                ret.setUuid(User.guestUserUUID);
                ret.setSupervisor("N");
                create_history_record(hsu, null, true);
                return ret;
            }
			HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
			hcu.eqIgnoreCase(LoginUser.EMAIL, userID.toLowerCase());

			// ScrollableResults scr = hcu.scroll();
			// while (scr.next()) {
			// LoginUser u = (LoginUser) scr.get(0);
			// String email = u.getEmail_address();
			// String upw = u.getUser_password();
			// String fname = u.getUser_fname();
			// String lname = u.getUser_lname();
			// String sex = u.getSex();
			// }
			boolean loginChange = false;
			LoginUser u = (LoginUser) hcu.getFirst();
			if (u == null) {
                hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
                hcu.eqIgnoreCase(LoginUser.NEW_EMAIL, userID.toLowerCase());
                u = (LoginUser) hcu.getFirst();
                if (u == null)
                    return new UserData("Invalid Login Id or Password.<br>Perhaps you need to create a new login.");
                loginChange = true;
                user_id = u.getNewEmail();
                pwHash = StringUtils.rightStrip(u.getNewPassword());
                if (pwHash.length() != 80)  // if password not already hashed, do it now
                    u.setNewPassword(pwHash = Crypto.hashPW(pwHash));
            } else {
                user_id = u.getEmailAddress();
                pwHash = StringUtils.rightStrip(u.getPassword());
                if (pwHash.length() != 80)  // if password not already hashed, do it now
                    u.setPassword(pwHash = Crypto.hashPW(pwHash));
            }
            if (!u.getUserStatus().equals("A"))
                return new UserData("Unauthorized access");
			if (userID.equalsIgnoreCase(user_id) &&  Crypto.hashCheck(pw, pwHash)) {
				boolean needNewGuid = true;
				// reuse current guid if good
				if (u.getCurrentUuid() != null  &&  u.getCurrentUuid().length() > 5) {
					long last_used = u.getUuidLastUsed().getTime();
					long current_time = Calendar.getInstance().getTimeInMillis();
					if (last_used <= current_time && ((current_time - last_used) / (1000L * 60L)) <= User.MAX_MINUTES_LIVE) {
                        ret.setUuid(u.getCurrentUuid());
                        ret.setSupervisor(u.getAdministrator());
                        needNewGuid = false;
                    }
				}
				if (needNewGuid) {  // no good old guid, create new
                    ret.setUuid(UUID.randomUUID().toString());
                    u.setCurrentUuid(ret.getUuid());
                    ret.setSupervisor(u.getAdministrator());
				}
				u.setUuidLastUsed(new Timestamp(Calendar.getInstance().getTimeInMillis()));

                if (loginChange) {
                    new BLoginUser(u).createHistoryModify(new User(u));
                    u.setEmailAddress(u.getNewEmail());
                    u.setPassword(u.getNewPassword());  //  already hashed!
                    u.setNewEmail(null);
                    u.setNewPassword(null);
                }

                hsu.commit();

                getThreadLocalRequest().getSession().setAttribute("uuid", ret.getUuid());  // save UUID in session

                create_history_record(hsu, u, false);
			} else
                ret = new UserData("Invalid Login Id or Password.<br>Perhaps you need to create a new login.");

		} catch (Throwable t) {
            ret = new UserData(-2, "Unable to login");
			logger.error(t);
		} finally {
			if (hsu != null)
				hsu.close();
		}
		return ret;
	}

    private void create_history_record(HibernateSessionUtil hsu, LoginUser u, boolean guest) {
        try {
            hsu.beginTransaction();
            LoginHistory h = new LoginHistory();
            LoginHistory.LoginHistoryPk pk = new LoginHistory.LoginHistoryPk();
            if (guest)
                pk.setUserId(-1L);
            else
                pk.setUserId(u.getUserId());
            pk.setLoginDate(DateUtils.now());
            h.setIPAddress(getThreadLocalRequest().getRemoteAddr());
            h.setPk(pk);
            hsu.save(h);
            hsu.commit();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public StandardReturn newUser(String p, UserRecord rec) {
        if (!"qvvrt#$55227".equals(p))
            return new StandardReturn("Invalid login");
        HibernateSessionUtil hsu = null;
        StandardReturn ret = new StandardReturn();

        try {
            hsu = HibernateUtil.openHSU();
            HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
            hcu.eqIgnoreCase(LoginUser.EMAIL, rec.getEmail().toLowerCase());
            LoginUser u = (LoginUser) hcu.getFirst();
            if (u != null)
                if (!u.getUserStatus().equals("A"))
                    return new StandardReturn("Unauthorized access.");
                else
                    return new StandardReturn("Email address " + rec.getEmail() + " is already registered.");

            // check to see if it is a pending email account
            hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
            hcu.eqIgnoreCase(LoginUser.NEW_EMAIL, rec.getEmail().toLowerCase());
            u = (LoginUser) hcu.getFirst();
            if (u != null)
                if (!u.getUserStatus().equals("A"))
                    return new StandardReturn("Unauthorized access.");
                else
                    return new StandardReturn("Email address " + rec.getEmail() + " is already registered (someone is in the process of changing to this email address).");

            u = new BLoginUser(new User(1L)).getBean();
            u.setFname(rec.getFname());
            u.setLname(rec.getLname());
            u.setEmailAddress(rec.getEmail());
            u.setBirthDate(rec.getBirthDate());
            u.setAdministrator("N");
            u.setSex(rec.getSex()+"");
            u.setWhenAdded(DateUtils.now());
            String pw = makePassword();
            u.setPassword(Crypto.hashPW(pw));
            u.setUserStatus("A");

            hsu.save(u);
            hsu.commit();

            LoginEmailNotification.send(rec.getFname() + " " + rec.getLname(), rec.getEmail(), rec.getEmail(), pw);

        } catch (Throwable t) {
            ret = new StandardReturn(-2, "Unable to create new user");
            logger.error(t);
        } finally {
            if (hsu != null)
                hsu.close();
        }
        return ret;
    }

    @Override
    public StandardReturn forgotPassword(String pw, UserRecord rec) {
        if (!"qvvrt#$55227".equals(pw))
            return new StandardReturn("Invalid login");
        HibernateSessionUtil hsu = null;
        StandardReturn ret = new StandardReturn();

        try {
            hsu = HibernateUtil.openHSU();
            HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
            hcu.eqIgnoreCase(LoginUser.EMAIL, rec.getEmail().toLowerCase());
            LoginUser u = (LoginUser) hcu.getFirst();
            if (u == null) {
                hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
                hcu.eqIgnoreCase(LoginUser.NEW_EMAIL, rec.getEmail().toLowerCase());
                u = (LoginUser) hcu.getFirst();
                if (u == null)
                    return new StandardReturn("Email address " + rec.getEmail() + " is not registered.");
            }
            if (!u.getUserStatus().equals("A"))
                return new UserData("Unauthorized access");

            new BLoginUser(u).createHistoryModify(new User(u));
            String newPw = makePassword();
            u.setPassword(Crypto.hashPW(newPw));
            hsu.commit();

            LoginEmailNotification.send(u.getFname() + " " + u.getLname(), u.getEmailAddress(), u.getEmailAddress(), newPw);

        } catch (Throwable t) {
            ret = new StandardReturn(-2, "Unable to access user");
            logger.error(t);
        } finally {
            if (hsu != null)
                hsu.close();
        }
        return ret;
    }

    @Override
    public UserData isLoggedIn() {
        final String gpPass = "$%Trwthdf43BBERg#$%";
        String passedProtectionScreen = (String) getThreadLocalRequest().getSession().getAttribute("pass");
        if (passedProtectionScreen == null  ||  passedProtectionScreen.isEmpty()  ||  !gpPass.equals(passedProtectionScreen)) {
            getThreadLocalRequest().getSession().setAttribute("pass", gpPass);  //  signal that initial GO screen has been passed
            return new UserData(99, "Go to initial GO screen");
        }

        UserData ret = new UserData();
        String uuid = (String) getThreadLocalRequest().getSession().getAttribute("uuid");
        if (uuid == null  ||  uuid.isEmpty())
            return new UserData(100, "User not logged on");

        HibernateSessionUtil hsu = null;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin()) {
                logout();
                return new UserData(100, "User not logged on");
            }

            ret.setUuid(uuid);
            ret.setSupervisor(user.isAdministrator()?"Y":"N");
        } catch (Exception e) {
            ret = new UserData(e.getMessage());
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return ret;
    }

    @Override
    public void logout() {
        getThreadLocalRequest().getSession().removeAttribute("uuid");
    }

    public static String makePassword() {
        return randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4) + randomString("!@#$%&*()", 1) + randomString("0123456789", 4);
    }

    private static String randomString(String set, int n) {
        int len = set.length();
        StringBuilder sb = new StringBuilder();
        Random rn = new Random();
        for (int i=0 ; i++ < n ; )
            sb.append(set.charAt(rn.nextInt(len)));
        return sb.toString();
    }

}
