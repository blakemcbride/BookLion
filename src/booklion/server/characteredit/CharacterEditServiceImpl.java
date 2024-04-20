package booklion.server.characteredit;

import beans.BookCharacter;
import beans.BookCharacterLog;
import beans.Chapter;
import booklion.client.characteredit.*;
import booklion.client.utils.StandardReturn;
import business.*;
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
 * Date: 2/24/14
 */
public class CharacterEditServiceImpl extends RemoteServiceServlet implements CharacterEditService {

    private static final Logger logger = Logger.getLogger(CharacterEditServiceImpl.class);

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

            HibernateCriteriaUtil<BookCharacter> hcu2 = hsu.createCriteria(BookCharacter.class);
            hcu2.in(BookCharacter.CHAPTER_ID, chapterIDs);
            hcu2.orderByIgnoreCase(BookCharacter.LAST_NAME);
            hcu2.orderByIgnoreCase(BookCharacter.FIRST_NAME);
            List<BookCharacter> bcLst = hcu2.list();

            //  get all logs for all characters at one time
            List<Long> characterList = new LinkedList<Long>();
            for (BookCharacter bchar : bcLst)
                characterList.add(bchar.getBookCharacterId());
            HibernateCriteriaUtil<BookCharacterLog> hcu3 = hsu.createCriteria(BookCharacterLog.class);
            hcu3.in(BookCharacterLog.CHARACTER_ID, characterList);
            hcu3.orderBy(BookCharacterLog.CHARACTER_ID);
            hcu3.orderBy(BookCharacterLog.CHAPTER_ID);
            List<BookCharacterLog> logLst = hcu3.list();

            Record[] recs = new Record[bcLst.size()];
            int i = 0;
            for (BookCharacter bc : bcLst) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setBookCharacterId(bc.getBookCharacterId());
                rec.setChapterId(bc.getChapterId());
                rec.setSpeciesId(bc.getSpeciesId());
                rec.setFname(bc.getFname());
                rec.setLname(bc.getLname());
                rec.setSuffix(bc.getSuffix());
                rec.setNickname(bc.getNickname());
                rec.setGender(bc.getGender());
                rec.setRelevance(bc.getRelevance());
                rec.setAffiliation(bc.getAffiliation());
                rec.setOccupation(bc.getOccupation());
                rec.setRelationship(bc.getRelationship());
                rec.setSpeciesName(bc.getSpecies().getSpeciesName());
                rec.setLogs(getCharacterLogs(bc.getBookCharacterId(), logLst));
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
            logger.error("Error getting character info", e);
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

    private LogRecord [] getCharacterLogs(long characterId, List<BookCharacterLog> logLst) {
        ArrayList<LogRecord> ret = new ArrayList<LogRecord>();

        for (BookCharacterLog log : logLst) {
            if (log.getBookCharacterId() != characterId)
                if (ret.isEmpty())
                    continue;
                else
                    break;
            LogRecord rec = new LogRecord();
            rec.setChapterId(log.getChapterId());
            rec.setLog(new BBookCharacterLog(log).getDescription());
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

            BBookCharacter bchap = new BBookCharacter(user);
            BookCharacter newRec = bchap.getBean();

            newRec.setChapterId(rec.getChapterId());
            newRec.setSpecies(new BSpecies(rec.getSpeciesId()).getBean());
            newRec.setLname(rec.getLname());
            newRec.setFname(rec.getFname());
            newRec.setSuffix(rec.getSuffix());
            newRec.setNickname(rec.getNickname());
            newRec.setGender(rec.getGender());
            newRec.setRelevance(rec.getRelevance());
            newRec.setAffiliation(rec.getAffiliation());
            newRec.setOccupation(rec.getOccupation());
            newRec.setRelationship(rec.getRelationship());
            hsu.save(newRec);

            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error adding character info", e);
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

            BBookCharacter brec = new BBookCharacter(rec.getBookCharacterId());
            BookCharacter urec = brec.getBean();
            if (urec.getChapterId() != rec.getChapterId()  ||
                    urec.getSpeciesId() != rec.getSpeciesId()  ||
                    different(urec.getLname(), rec.getLname())  ||
                    different(urec.getFname(), rec.getFname())  ||
                    different(urec.getSuffix(), rec.getSuffix())  ||
                    different(urec.getNickname(), rec.getNickname())  ||
                    different(urec.getGender(), rec.getGender())  ||
                    different(urec.getRelevance(), rec.getRelevance())  ||
                    different(urec.getAffiliation(), rec.getAffiliation())  ||
                    different(urec.getOccupation(), rec.getOccupation())  ||
                    different(urec.getRelationship(), rec.getRelationship())) {
                brec.createHistoryModify(user);
                urec.setChapterId(rec.getChapterId());
                urec.setSpecies(new BSpecies(rec.getSpeciesId()).getBean());
                urec.setLname(rec.getLname());
                urec.setFname(rec.getFname());
                urec.setSuffix(rec.getSuffix());
                urec.setNickname(rec.getNickname());
                urec.setGender(rec.getGender());
                urec.setRelevance(rec.getRelevance());
                urec.setAffiliation(rec.getAffiliation());
                urec.setOccupation(rec.getOccupation());
                urec.setRelationship(rec.getRelationship());
                hsu.commit();
            }
            updateLogRecords(hsu, user, rec);

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error updating character info", e);
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

        HibernateCriteriaUtil<BookCharacterLog> hcu2 = hsu.createCriteria(BookCharacterLog.class);
        hcu2.eq(BookCharacterLog.CHARACTER_ID, rec.getBookCharacterId());
        hcu2.orderBy(BookCharacterLog.CHARACTER_ID);
        hcu2.orderBy(BookCharacterLog.CHAPTER_ID);
        List<BookCharacterLog> logLst = hcu2.list();  //  logs for all chapters

        List<Long> chapterIds = getChapterIds(hsu, bookId);
        for (long chapterId : chapterIds) {
            String logStr = null;
            for (LogRecord r : logRecs)
                if (r.getChapterId() == chapterId) {
                    logStr = normalize(r.getLog());
                    break;
                }

            BookCharacterLog log = null;
            BBookCharacterLog blog = null;
            for (BookCharacterLog r : logLst)
                if (r.getChapterId() == chapterId) {
                    log = r;
                    blog = new BBookCharacterLog(log);
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
                blog = new BBookCharacterLog(user);
                log = blog.getBean();
                log.setBookCharacterId(rec.getBookCharacterId());
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

            new BBookCharacter(rec.getBookCharacterId()).delete(user);
            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error deleting character info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

}