package business;

import beans.Location;
import beans.LocationH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/13/14
 */
public class BLocation {
    private static final Logger logger = Logger.getLogger(BBookCharacter.class);

    private Location location;

    public BLocation(User user) {
        location = new Location();
        location.setRecordChangeDate(now());
        location.setRecordChangeType("N");
        location.setRecordUserId(user.getBean().getUserId());
    }

    public BLocation(long locationId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Location> hcu = new HibernateCriteriaUtil<Location>(hsu, Location.class);
        hcu.eq(Location.LOCATION_ID, locationId);
        location = (Location) hcu.getFirst();
    }

    public BLocation(Location rec) {
        location = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(location);
    }

    public Location getBean() {
        return location;
    }

    public BLocation createHistoryModify(User user) {
        createHistory();

        location.setRecordChangeDate(now());
        location.setRecordChangeType("M");
        location.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BLocation createHistoryDelete(User user) {
        createHistory();

        location.setRecordChangeDate(now());
        location.setRecordChangeType("D");
        location.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        LocationH hist = new LocationH();
        hist.setLocationId(location.getLocationId());
        hist.setInsideLocationId(location.getInsideLocationId());
        hist.setChapterId(location.getChapterId());
        hist.setLocName(location.getLocName());
        hist.setRecordChangeDate(location.getRecordChangeDate());
        hist.setRecordChangeType(location.getRecordChangeType());
        hist.setRecordUserId(location.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }
}
