package business;

import beans.LoginUser;
import beans.LoginUserH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * User: Blake McBride
 * Date: 10/26/14
 */
public class BLoginUser {
    private static final Logger logger = Logger.getLogger(BLoginUser.class);

    private LoginUser loginUser;

    public BLoginUser(User user) {
        loginUser = new LoginUser();
        loginUser.setRecordChangeDate(now());
        loginUser.setRecordChangeType("N");
        loginUser.setRecordUserId(user.getBean().getUserId());
    }

    public BLoginUser(long userId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<LoginUser> hcu = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class);
        hcu.eq(LoginUser.USER_ID, userId);
        loginUser = (LoginUser) hcu.getFirst();
    }

    public BLoginUser(LoginUser rec) {
        loginUser = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(loginUser);
    }

    public LoginUser getBean() {
        return loginUser;
    }

    public BLoginUser createHistoryModify(User user) {
        createHistory();

        loginUser.setRecordChangeDate(now());
        loginUser.setRecordChangeType("M");
        loginUser.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BLoginUser createHistoryDelete(User user) {
        createHistory();

        loginUser.setRecordChangeDate(now());
        loginUser.setRecordChangeType("D");
        loginUser.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        LoginUserH hist = new LoginUserH();
        hist.setUserId(loginUser.getUserId());
        hist.setPassword(loginUser.getPassword());
        hist.setLname(loginUser.getLname());
        hist.setFname(loginUser.getFname());
        hist.setEmailAddress(loginUser.getEmailAddress());
        hist.setBirthDate(loginUser.getBirthDate());
        hist.setSex(loginUser.getSex());
        hist.setWhenAdded(loginUser.getWhenAdded());
        hist.setWhenAuthorized(loginUser.getWhenAuthorized());
        hist.setAdministrator(loginUser.getAdministrator());
        hist.setCurrentUuid(loginUser.getCurrentUuid());
        hist.setUuidLastUsed(loginUser.getUuidLastUsed());
        hist.setNewEmail(loginUser.getNewEmail());
        hist.setNewPassword(loginUser.getNewPassword());
        hist.setUserStatus(loginUser.getUserStatus());
        hist.setRecordChangeDate(loginUser.getRecordChangeDate());
        hist.setRecordChangeType(loginUser.getRecordChangeType());
        hist.setRecordUserId(loginUser.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }

}
