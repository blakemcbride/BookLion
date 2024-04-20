
package dbutils;

import java.lang.reflect.InvocationTargetException;

import beans.*;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import utils.Crypto;
import utils.StringUtils;


/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author Blake McBride
 */
public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory;
    private static final ThreadLocal<HibernateSessionUtil> tlHSU = new ThreadLocal<HibernateSessionUtil>();
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            Configuration config = new Configuration();
//            config.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
//            config.setNamingStrategy(DefaultNamingStrategy.INSTANCE);
            readBeanAnnotations(config);
            sessionFactory = config.configure().buildSessionFactory();
//            fixPasswords();
        } catch (Throwable ex) {
        	if (ex instanceof HibernateException  &&  ex.getCause() instanceof InvocationTargetException) {
        		InvocationTargetException te = (InvocationTargetException) ex.getCause();
        		if (te.getTargetException() instanceof PropertyNotFoundException) {
        			PropertyNotFoundException pnf = (PropertyNotFoundException) te.getTargetException();
        			logger.error(pnf.getMessage());
        		}
        	}
            logger.error(ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Eliminate this once run once
     */
    private static void fixPasswords() {
        boolean doConversion = false;
        HibernateSessionUtil hsu = HibernateUtil.openHSU();
        ScrollableResults scr = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class).scroll();
        while (scr.next()) {
            LoginUser user = (LoginUser) scr.get(0);
            String pw = user.getPassword();
            pw = StringUtils.rightStrip(pw);
            if (pw.length() < 80) {
                doConversion = true;
                break;
            }
        }
        if (doConversion) {
            scr = new HibernateCriteriaUtil<LoginUser>(hsu, LoginUser.class).scroll();
            while (scr.next()) {
                LoginUser user = (LoginUser) scr.get(0);
                user.setPassword(Crypto.hashPW(StringUtils.rightStrip(user.getPassword())));
            }
            hsu.commit();
        }
        HibernateUtil.closeHSU();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Open a session and begin a new transaction.
     *
     * @return the open session
     */
    public static HibernateSessionUtil openHSU() {
        HibernateSessionUtil hsu = tlHSU.get();
        if (hsu != null  &&  hsu.getSession() != null  &&  hsu.getSession().isOpen()) {
            logger.error("openHSU called but open session already associated");
        } else {
            hsu = new HibernateSessionUtil(sessionFactory.openSession());
            tlHSU.set(hsu);
        }
        hsu.beginTransaction();
        return hsu;
    }

    public static HibernateSessionUtil getHSU() {
        HibernateSessionUtil hsu = tlHSU.get();
        if (hsu == null)
            throw new Error("getHSU called but no session associated");
        Session s = hsu.getSession();
        if (s == null  ||  !s.isOpen())
            throw new Error("getHSU called but associated session is closed");
        return hsu;
    }
    
    /**
     * This method is a thread-final-cleanup routine.  
     * It should only be called by BookServlet.java
     * 
     * @return void
     */
    public static void closeHSU() {
        HibernateSessionUtil hsu = tlHSU.get();
        if (hsu == null)
            return;
        tlHSU.set(null);
        hsu.close();
    }
    
    
    private static void readBeanAnnotations(Configuration config) {
        config.addAnnotatedClass(Author.class);
        config.addAnnotatedClass(AuthorH.class);
        config.addAnnotatedClass(Book.class);
        config.addAnnotatedClass(BookH.class);
        config.addAnnotatedClass(BookCharacter.class);
        config.addAnnotatedClass(BookCharacterH.class);
        config.addAnnotatedClass(BookCharacterLog.class);
        config.addAnnotatedClass(BookCharacterLogH.class);
        config.addAnnotatedClass(BookComment.class);
        config.addAnnotatedClass(BookRating.class);
        config.addAnnotatedClass(Chapter.class);
        config.addAnnotatedClass(ChapterH.class);
        config.addAnnotatedClass(Genre.class);
        config.addAnnotatedClass(Location.class);
        config.addAnnotatedClass(LocationH.class);
        config.addAnnotatedClass(LocationLog.class);
        config.addAnnotatedClass(LocationLogH.class);
        config.addAnnotatedClass(LoginHistory.class);
        config.addAnnotatedClass(LoginUser.class);
        config.addAnnotatedClass(Species.class);
        config.addAnnotatedClass(SpeciesH.class);
        config.addAnnotatedClass(SpeciesLog.class);
        config.addAnnotatedClass(SpeciesLogH.class);
        config.addAnnotatedClass(LoginUserH.class);
    }
}
