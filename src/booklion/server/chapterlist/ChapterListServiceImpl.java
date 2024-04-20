package booklion.server.chapterlist;

import beans.Chapter;
import beans.Species;
import booklion.client.chapterlist.Record;
import booklion.client.chapterlist.RecordReturn;
import booklion.client.utils.StandardReturn;
import business.BChapter;
import business.BSpecies;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.chapterlist.ChapterListService;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * @author Blake McBride
 * Date: 1/4/14
 */
public class ChapterListServiceImpl extends RemoteServiceServlet implements ChapterListService {

    private static final Logger logger = Logger.getLogger(ChapterListServiceImpl.class);

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
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

 }