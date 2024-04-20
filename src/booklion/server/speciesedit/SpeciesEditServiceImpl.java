package booklion.server.speciesedit;

import beans.Chapter;
import beans.Species;
import beans.SpeciesLog;
import booklion.client.speciesedit.*;
import booklion.client.utils.StandardReturn;
import business.BChapter;
import business.BSpecies;
import business.BSpeciesLog;
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
public class SpeciesEditServiceImpl extends RemoteServiceServlet implements SpeciesEditService {

    private static final Logger logger = Logger.getLogger(SpeciesEditServiceImpl.class);

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

            HibernateCriteriaUtil<Species> hcu2 = hsu.createCriteria(Species.class);
            hcu2.in(Species.CHAPTER_ID, chapterIDs);
            hcu2.orderByIgnoreCase(Species.SPECIES_NAME);
            List<Species> bcLst = hcu2.list();

            //  get all logs for all characters at one time
            List<Long> speciesList = new LinkedList<Long>();
            for (Species bchar : bcLst)
                speciesList.add(bchar.getSpeciesId());
            HibernateCriteriaUtil<SpeciesLog> hcu3 = hsu.createCriteria(SpeciesLog.class);
            hcu3.in(SpeciesLog.SPECIES_ID, speciesList);
            hcu3.orderBy(SpeciesLog.SPECIES_ID);
            List<SpeciesLog> logLst = hcu3.list();

            Record[] recs = new Record[bcLst.size()];
            int i = 0;
            for (Species bc : bcLst) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setSpeciesId(bc.getSpeciesId());
                rec.setChapterId(bc.getChapterId());
                rec.setSpeciesName(bc.getSpeciesName());
                rec.setLogs(getSpeciesLogs(bc.getSpeciesId(), logLst));
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
            logger.error("Error getting species info", e);
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

    private LogRecord[] getSpeciesLogs(long speciesId, List<SpeciesLog> logLst) {
        ArrayList<LogRecord> ret = new ArrayList<LogRecord>();

        for (SpeciesLog log : logLst) {
            if (log.getSpeciesId() != speciesId)
                if (ret.isEmpty())
                    continue;
                else
                    break;
            LogRecord rec = new LogRecord();
            rec.setChapterId(log.getChapterId());
            rec.setLog(new BSpeciesLog(log).getDescription());
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

            BSpecies bchap = new BSpecies(user);
            Species newRec = bchap.getBean();
            newRec.setSpeciesId(rec.getSpeciesId());
            newRec.setChapterId(rec.getChapterId());
            newRec.setSpeciesName(rec.getSpeciesName());

            hsu.save(newRec);

            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error adding species info", e);
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

            BSpecies brec = new BSpecies(rec.getSpeciesId());
            Species urec = brec.getBean();
            if (urec.getSpeciesId() != rec.getSpeciesId()  ||
                    urec.getChapterId() != rec.getChapterId()  ||
                    different(urec.getSpeciesName(), rec.getSpeciesName())) {
                brec.createHistoryModify(user);
                urec.setSpeciesId(rec.getSpeciesId());
                urec.setChapterId(rec.getChapterId());
                urec.setSpeciesName(rec.getSpeciesName());
                hsu.commit();
            }
            updateLogRecords(hsu, user, rec);

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error updating species info", e);
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

        HibernateCriteriaUtil<SpeciesLog> hcu2 = hsu.createCriteria(SpeciesLog.class);
        hcu2.eq(SpeciesLog.SPECIES_ID, rec.getSpeciesId());
        hcu2.orderBy(SpeciesLog.SPECIES_ID);
        hcu2.orderBy(SpeciesLog.CHAPTER_ID);
        List<SpeciesLog> logLst = hcu2.list();  //  logs for all chapters

        List<Long> chapterIds = getChapterIds(hsu, bookId);
        for (long chapterId : chapterIds) {
            String logStr = null;
            for (LogRecord r : logRecs)
                if (r.getChapterId() == chapterId) {
                    logStr = normalize(r.getLog());
                    break;
                }

            SpeciesLog log = null;
            BSpeciesLog blog = null;
            for (SpeciesLog r : logLst)
                if (r.getChapterId() == chapterId) {
                    log = r;
                    blog = new BSpeciesLog(log);
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
                blog = new BSpeciesLog(user);
                log = blog.getBean();
                log.setSpeciesId(rec.getSpeciesId());
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

            BSpecies brec = new BSpecies(rec.getSpeciesId());
            brec.delete(user);
            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error deleting species info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }
}