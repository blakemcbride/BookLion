package booklion.server.chapteredit;

import beans.Chapter;
import beans.Species;
import booklion.client.chapteredit.*;
import booklion.client.utils.StandardReturn;
import business.BChapter;
import business.BSpecies;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.List;

import static utils.StringUtils.different;
import static utils.StringUtils.normalize;

/**
 * @author Blake McBride
 * Date: 2/17/14
 */
public class ChapterEditServiceImpl extends RemoteServiceServlet implements ChapterEditService {

    private static final Logger logger = Logger.getLogger(ChapterEditServiceImpl.class);

    @Override
    public RecordReturn getRecords(String uuid, long bookId) {
        HibernateSessionUtil hsu = null;
        RecordReturn res = new RecordReturn(StandardReturn.SUCCESS);
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin())
                return new RecordReturn("Invalid login");

            HibernateCriteriaUtil<Chapter> hcu1 = hsu.createCriteria(Chapter.class);
            hcu1.eq(Chapter.BOOK_ID, bookId);
            hcu1.orderBy(Chapter.BOOK_ID);
            hcu1.orderBy(Chapter.SEQNO);
            List<Chapter> chapters = hcu1.list();

            Record[] recs = new Record[chapters.size()];
            int i = 0;
            for (Chapter chap : chapters) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setChapterId(chap.getChapterId());
                rec.setBookId(chap.getBookId());
                rec.setSeqno(chap.getSeqno());
                rec.setChapterDesignation(chap.getChapterDesignation());
                rec.setChapterName(chap.getChapterName());
                rec.setLogs(new BChapter(chap).getDescription());
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
            logger.error("Error getting chapter info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
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

            BChapter bchap = new BChapter(user);
            Chapter newRec = bchap.getBean();

            newRec.setBookId(rec.getBookId());
            newRec.setSeqno(rec.getSeqno());
            newRec.setChapterDesignation(rec.getChapterDesignation());
            newRec.setChapterName(rec.getChapterName());

            hsu.save(newRec);

            hsu.commit();

            addHumans(user, rec.getBookId());

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error adding chapter info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    private void addHumans(User user, long bookId) {
        HibernateSessionUtil hsu = null;
        try {
            hsu = HibernateUtil.getHSU();
            HibernateCriteriaUtil<Chapter> crit = hsu.createCriteria(Chapter.class);
            crit.eq(Chapter.BOOK_ID, bookId);
            List<Chapter> chaps = crit.list();
            if (chaps.size() == 1) {  //  this is the first chapter being added.  Add default Human species
                hsu.beginTransaction();
                BSpecies bsp = new BSpecies(user);
                Species sp = bsp.getBean();
                sp.setChapterId(chaps.get(0).getChapterId());
                sp.setSpeciesName("Human");
                hsu.save(sp);
                hsu.commit();
            }
        } catch (Exception e) {
            logger.error("Error adding humans", e);
        }
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

            BChapter brec = new BChapter(rec.getChapterId());
            Chapter urec = brec.getBean();
            if (urec.getSeqno() != rec.getSeqno()  ||
                    different(urec.getChapterDesignation(), rec.getChapterDesignation())  ||
                    different(urec.getChapterName(), rec.getChapterName())  ||
                    different(urec.getDescription(), normalize(rec.getLogs()))) {
                brec.createHistoryModify(user);
                urec.setSeqno(rec.getSeqno());
                urec.setChapterDesignation(rec.getChapterDesignation());
                urec.setChapterName(rec.getChapterName());
                brec.setDescription(rec.getLogs());

                hsu.commit();
            }
            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error updating chapter info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
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

            BChapter brec = new BChapter(rec.getChapterId());
            brec.delete(user);
            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
            logger.error("Error deleting chapter info", e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

}