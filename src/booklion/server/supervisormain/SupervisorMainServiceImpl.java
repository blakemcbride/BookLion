package booklion.server.supervisormain;

import booklion.client.utils.StandardReturn;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.supervisormain.SupervisorMainService;
import dbutils.ExceptionMessage;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.EMail;

/**
 * @author Blake McBride
 * Date: 9/5/14
 */
public class SupervisorMainServiceImpl extends RemoteServiceServlet implements SupervisorMainService {

    private static final Logger logger = Logger.getLogger(SupervisorMainServiceImpl.class);

    @Override
    public StandardReturn sendSupportRequest(String uuid, String subject, String msg) {
        HibernateSessionUtil hsu = null;
        StandardReturn res;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();

            msg = "Support request from " + user.getBean().getEmailAddress() + "<br>" + msg;
            EMail.send("support@booklion.com", "support@booklion.com", subject, msg);

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error adding species info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;

    }
}