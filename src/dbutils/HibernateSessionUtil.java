
package dbutils;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Blake McBride
 * @since 6/28/11
 */
public class HibernateSessionUtil {
    
    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(HibernateSessionUtil.class);
    
    private Session s;
    private Transaction tran;
    private boolean inError = false;
    
    public HibernateSessionUtil(Session s) {
        this.s = s;
    }
    
    public Session getSession() {
        return s;
    }
    
    public static void close(HibernateSessionUtil hsu) {
    	if (hsu == null)
    		return;
    	hsu.close();
    }
    
    public void close() {
    	try {
	        if (s != null  &&  s.isOpen()) {
	            if (tran != null  &&  !inError  &&  tran.isActive())
	            	try {
	            		tran.commit();
	            	} catch (Throwable e) {
	            	}
	            s.close();
	        }
    	} finally {
	        s = null;
	        tran = null;
	    }
    }
    
    public void beginTransaction() {
       if (tran != null && tran.isActive())
            tran.commit();
       if (tran == null)
            tran = s.beginTransaction();
        else
            tran.begin();
       inError = false;
    }
    
    public void commit() {
    	try {
    		tran.commit();
    	} catch (HibernateException e) {
    		inError = true;
    		throw e;
    	}
    }
    
    public void rollback() {
        tran.rollback();
    }
    
    public Serializable save(Object rec) {
        return s.save(rec);
    }
     
    public void delete(Object rec) {
        s.delete(rec);
    }
     
	final public <T>HibernateCriteriaUtil<T> createCriteria(final Class<T> clazz)
	{
		return new HibernateCriteriaUtil<T>(s,clazz);
	}

}
