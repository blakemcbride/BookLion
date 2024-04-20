package business;

import beans.LocationLog;
import beans.LocationLogH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.StringUtils;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/25/14
 */
public class BLocationLog {
    private static final Logger logger = Logger.getLogger(BLocationLog.class);

    private LocationLog locationLog;

    public BLocationLog(User user) {
        locationLog = new LocationLog();
        locationLog.setRecordChangeDate(now());
        locationLog.setRecordChangeType("N");
        locationLog.setRecordUserId(user.getBean().getUserId());
    }

    public BLocationLog(long locationLogId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<LocationLog> hcu = new HibernateCriteriaUtil<LocationLog>(hsu, LocationLog.class);
        hcu.eq(LocationLog.LOCATION_LOG_ID, locationLogId);
        locationLog = (LocationLog) hcu.getFirst();
    }

    public BLocationLog(LocationLog rec) {
        locationLog = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(locationLog);
    }

    public LocationLog getBean() {
        return locationLog;
    }

    public BLocationLog createHistoryModify(User user) {
        createHistory();

        locationLog.setRecordChangeDate(now());
        locationLog.setRecordChangeType("M");
        locationLog.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BLocationLog createHistoryDelete(User user) {
        createHistory();

        locationLog.setRecordChangeDate(now());
        locationLog.setRecordChangeType("D");
        locationLog.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        LocationLogH hist = new LocationLogH();
        hist.setLocationLogId(locationLog.getLocationLogId());
        hist.setLocationId(locationLog.getLocationId());
        hist.setChapterId(locationLog.getChapterId());
        hist.setDescription(locationLog.getDescription());
        hist.setRecordChangeDate(locationLog.getRecordChangeDate());
        hist.setRecordChangeType(locationLog.getRecordChangeType());
        hist.setRecordUserId(locationLog.getRecordUserId());
        hist.setDescription(locationLog.getDescription());
        hist.setTextDescription(locationLog.getTextDescription());
        HibernateUtil.getHSU().save(hist);
    }

    public void setDescription(String txt) {
        txt = StringUtils.normalize(txt);
        if (txt == null) {
            locationLog.setDescription(null);
            locationLog.setTextDescription(null);
        } else {
            if (txt.length() > 2000) {
                locationLog.setDescription(null);
                locationLog.setTextDescription(txt);
            } else {
                locationLog.setDescription(txt);
                locationLog.setTextDescription(null);
            }
        }
    }

    public String getDescription() {
        String txt = locationLog.getDescription();
        if (txt == null)
            txt = locationLog.getTextDescription();
        return txt;
    }


}
