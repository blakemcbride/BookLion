package booklion.server.locationedit;

import beans.Chapter;
import beans.Location;
import beans.LocationLog;
import booklion.client.locationedit.*;
import booklion.client.utils.StandardReturn;
import business.BChapter;
import business.BLocation;
import business.BLocationLog;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static utils.StringUtils.different;
import static utils.StringUtils.normalize;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class LocationEditServiceImpl extends RemoteServiceServlet implements LocationEditService {


    private static final Logger logger = Logger.getLogger(LocationEditServiceImpl.class);

    @Override
    public RecordReturn getRecords(String uuid, long bookId) {
        HibernateSessionUtil hsu = null;
        RecordReturn res = new RecordReturn(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin())
                return new RecordReturn("Invalid login");

            List<Long> chapterIDs = getChapterIds(hsu, bookId);

            HibernateCriteriaUtil<Location> hcu2 = hsu.createCriteria(Location.class);
            hcu2.in(Location.CHAPTER_ID, chapterIDs);
            hcu2.orderByIgnoreCase(Location.LOCATION_NAME);
            List<Location> bcLst = hcu2.list();

            //  get all logs for all characters at one time
            List<Long> locationList = new LinkedList<Long>();
            for (Location bchar : bcLst)
                locationList.add(bchar.getLocationId());
            HibernateCriteriaUtil<LocationLog> hcu3 = hsu.createCriteria(LocationLog.class);
            hcu3.in(LocationLog.LOCATION_ID, locationList);
            hcu3.orderBy(LocationLog.LOCATION_ID);
            List<LocationLog> logLst = hcu3.list();

            Record[] recs = new Record[bcLst.size()];
            int i = 0;
            for (Location bc : bcLst) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setLocationId(bc.getLocationId());
                rec.setInsideLocationId(bc.getInsideLocationId());
                rec.setChapterId(bc.getChapterId());
                rec.setLocName(bc.getLocName());
                rec.setLogs(getLocationLogs(bc.getLocationId(), logLst));
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
            logger.error("Error getting location info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    private List<Long> getChapterIds(HibernateSessionUtil hsu, long bookId) {
        HibernateCriteriaUtil<Chapter> hcu1 = hsu.createCriteria(Chapter.class);
        hcu1.eq(Chapter.BOOK_ID, bookId);
        hcu1.orderBy(Chapter.BOOK_ID);
        hcu1.orderBy(Chapter.SEQNO);
        hcu1.selectColumns(Chapter.CHAPTER_ID);
        return (List) hcu1.list();
    }

    private LogRecord [] getLocationLogs(long locationId, List<LocationLog> logLst) {
        ArrayList<LogRecord> ret = new ArrayList<LogRecord>();

        for (LocationLog log : logLst) {
            if (log.getLocationId() != locationId)
                if (ret.isEmpty())
                    continue;
                else
                    break;
            LogRecord rec = new LogRecord();
            rec.setChapterId(log.getChapterId());
            rec.setLog(new BLocationLog(log).getDescription());
            ret.add(rec);
        }
        return ret.toArray(new LogRecord[ret.size()]);
    }

    @Override
    public StandardReturn addRecord(String uuid, Record rec) {
        HibernateSessionUtil hsu = null;
        StandardReturn res;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();

            BLocation bchap = new BLocation(user);
            Location newRec = bchap.getBean();
            newRec.setLocationId(rec.getLocationId());
            newRec.setInsideLocationId(rec.getInsideLocationId());
            newRec.setChapterId(rec.getChapterId());
            newRec.setLocName(rec.getLocName());

            hsu.save(newRec);

            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error adding location info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;

    }

    @Override
    public StandardReturn updateRecord(String uuid, Record rec) {
        HibernateSessionUtil hsu = null;
        StandardReturn res;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();

            BLocation brec = new BLocation(rec.getLocationId());
            Location urec = brec.getBean();
            if (urec.getLocationId() != rec.getLocationId()  ||
                    urec.getInsideLocationId() != rec.getInsideLocationId()  ||
                    urec.getChapterId() != rec.getChapterId()  ||
                    different(urec.getLocName(), rec.getLocName())) {
                brec.createHistoryModify(user);
                urec.setLocationId(rec.getLocationId());
                urec.setInsideLocationId(rec.getInsideLocationId());
                urec.setChapterId(rec.getChapterId());
                urec.setLocName(rec.getLocName());
                hsu.commit();
            }
            updateLogRecords(hsu, user, rec);

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error updating location info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    private void updateLogRecords(HibernateSessionUtil hsu, User user, Record rec) {
        BChapter chap = new BChapter(rec.getChapterId());  // an arbitrary chapter within the book used to find the bookId
        long bookId = chap.getBean().getBookId();
        LogRecord [] logRecs = rec.getLogs();

        hsu.beginTransaction();

        HibernateCriteriaUtil<LocationLog> hcu2 = hsu.createCriteria(LocationLog.class);
        hcu2.eq(LocationLog.LOCATION_ID, rec.getLocationId());
        hcu2.orderBy(LocationLog.LOCATION_ID);
        hcu2.orderBy(LocationLog.CHAPTER_ID);
        List<LocationLog> logLst = hcu2.list();  //  logs for all chapters

        List<Long> chapterIds = getChapterIds(hsu, bookId);
        for (long chapterId : chapterIds) {
            String logStr = null;
            for (LogRecord r : logRecs)
                if (r.getChapterId() == chapterId) {
                    logStr = normalize(r.getLog());
                    break;
                }

            LocationLog log = null;
            BLocationLog blog = null;
            for (LocationLog r : logLst)
                if (r.getChapterId() == chapterId) {
                    log = r;
                    blog = new BLocationLog(log);
                    break;
                }

            if (log != null  &&  logStr != null) {
                if (different(blog.getDescription(), normalize(logStr))) {
                    blog.createHistoryModify(user);
                    blog.setDescription(logStr);
                }
            } else if (log != null  &&  logStr == null)
                blog.delete(user);
            else if (log == null  &&  logStr != null) {
                blog = new BLocationLog(user);
                log = blog.getBean();
                log.setLocationId(rec.getLocationId());
                log.setChapterId(chapterId);
                blog.setDescription(logStr);
                hsu.save(log);
            }
        }
        hsu.commit();
    }

    @Override
    public StandardReturn deleteRecord(String uuid, Record rec) {
        HibernateSessionUtil hsu = null;
        StandardReturn res;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();

            BLocation brec = new BLocation(rec.getLocationId());
            brec.delete(user);
            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error deleting location info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

}