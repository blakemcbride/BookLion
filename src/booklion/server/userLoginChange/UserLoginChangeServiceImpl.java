package booklion.server.userLoginChange;

import beans.LoginUser;
import booklion.client.userLoginChange.Record;
import booklion.client.utils.StandardReturn;
import booklion.server.login.LoginEmailNotification;
import business.BLoginUser;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.userLoginChange.UserLoginChangeService;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.Crypto;

import static booklion.server.login.LoginServiceImpl.makePassword;

/**
 * @author Blake McBride
 * Date: 5/7/14
 */
public class UserLoginChangeServiceImpl extends RemoteServiceServlet implements UserLoginChangeService {

    private static final Logger logger = Logger.getLogger(UserLoginChangeServiceImpl.class);


    @Override
    public Record getOldEmail(String uuid) {
        HibernateSessionUtil hsu = null;
        Record res = new Record(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return new Record(user.badLoginMessage());
            LoginUser rec = user.getBean();
            res.setOldEmail(rec.getEmailAddress());
        } catch (Exception e) {
            res = new Record(e.getMessage());
            logger.error("Error getting old email address", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    @Override
    public StandardReturn saveNewEmail(String uuid, Record newRec) {
        HibernateSessionUtil hsu = null;
        Record res = new Record(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return new Record(user.badLoginMessage());
            LoginUser rec = user.getBean();

            if (!Crypto.hashCheck(Crypto.decode(newRec.getOldPassword()), rec.getPassword()))
                return new StandardReturn("The Old Password you entered is incorrect.");


            //  make sure the new email is not already registered
            HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
            hcu.eqIgnoreCase(LoginUser.EMAIL, newRec.getNewEmail().toLowerCase());
            LoginUser u = (LoginUser) hcu.getFirst();
            if (u != null)
                return new StandardReturn("Email address " + newRec.getNewEmail() + " is already registered.");

            new BLoginUser(rec).createHistoryModify(user);
            rec.setNewEmail(newRec.getNewEmail());
            String pw = makePassword();
            rec.setNewPassword(Crypto.hashPW(pw));

            hsu.commit();

            LoginEmailNotification.send(rec.getFname() + " " + rec.getLname(), newRec.getNewEmail(), newRec.getNewEmail(), pw);

        } catch (Exception e) {
            res = new Record(e.getMessage());
            logger.error("Error saving changing login ID", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }
}