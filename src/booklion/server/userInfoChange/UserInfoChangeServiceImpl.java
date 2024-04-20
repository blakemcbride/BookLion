package booklion.server.userInfoChange;

import beans.LoginUser;
import booklion.client.userInfoChange.Record;
import booklion.client.utils.StandardReturn;
import business.BLoginUser;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.userInfoChange.UserInfoChangeService;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.Crypto;

/**
 * @author Blake McBride
 * Date: 4/29/14
 */
public class UserInfoChangeServiceImpl extends RemoteServiceServlet implements UserInfoChangeService {

    private static final Logger logger = Logger.getLogger(UserInfoChangeServiceImpl.class);

    @Override
    public Record getUserInfo(String uuid) {
        HibernateSessionUtil hsu = null;
        Record res = new Record(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return new Record(user.badLoginMessage());
            LoginUser rec = user.getBean();
            res.setFname(rec.getFname());
            res.setLname(rec.getLname());
            res.setEmail(rec.getEmailAddress());
            res.setBirthDate(rec.getBirthDate());
            res.setSex(rec.getSex().charAt(0));
        } catch (Exception e) {
            res = new Record(e.getMessage());
            logger.error("Error getting user info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    private static boolean validInput(String x) {
        return x != null  &&  x.length() > 0;
    }

    @Override
    public StandardReturn saveUserInfo(String uuid, Record newRec) {
        HibernateSessionUtil hsu = null;
        Record res = new Record(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return new Record(user.badLoginMessage());
            LoginUser rec = user.getBean();
            new BLoginUser(rec).createHistoryModify(user);

            String oldPw = Crypto.decode(newRec.getOldPassword());
            String newPw = Crypto.decode(newRec.getNewPassword());
            if (validInput(oldPw)  &&  validInput(newPw)) {
                if (!Crypto.hashCheck(oldPw, rec.getPassword()))
                    return new Record("Old password is invalid.");
                rec.setPassword(Crypto.hashPW(newPw));
            }

            rec.setFname(newRec.getFname());
            rec.setLname(newRec.getLname());
            rec.setBirthDate(newRec.getBirthDate());
            rec.setSex(newRec.getSex()+"");

            hsu.commit();
        } catch (Exception e) {
            res = new Record(e.getMessage());
            logger.error("Error saving user info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }
}