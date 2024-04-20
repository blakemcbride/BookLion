package business;

import beans.Chapter;
import beans.ChapterH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.StringUtils;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/6/14
 */
public class BChapter {

    private static final Logger logger = Logger.getLogger(BChapter.class);

    private Chapter chapter;

    public BChapter(User user) {
        chapter = new Chapter();
        chapter.setRecordChangeDate(now());
        chapter.setRecordChangeType("N");
        chapter.setRecordUserId(user.getBean().getUserId());
    }

    public BChapter(long chapterId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Chapter> hcu = new HibernateCriteriaUtil<Chapter>(hsu, Chapter.class);
        hcu.eq(Chapter.CHAPTER_ID, chapterId);
        chapter = (Chapter) hcu.getFirst();
    }

    public BChapter(Chapter rec) {
        chapter = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(chapter);
    }

    public Chapter getBean() {
        return chapter;
    }

    public BChapter createHistoryModify(User user) {
        createHistory();

        chapter.setRecordChangeDate(now());
        chapter.setRecordChangeType("M");
        chapter.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BChapter createHistoryDelete(User user) {
        createHistory();

        chapter.setRecordChangeDate(now());
        chapter.setRecordChangeType("D");
        chapter.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        ChapterH hist = new ChapterH();
        hist.setChapterId(chapter.getChapterId());
        hist.setBookId(chapter.getBookId());
        hist.setSeqno(chapter.getSeqno());
        hist.setChapterDesignation(chapter.getChapterDesignation());
        hist.setChapterName(chapter.getChapterName());
        hist.setRecordChangeDate(chapter.getRecordChangeDate());
        hist.setRecordChangeType(chapter.getRecordChangeType());
        hist.setRecordUserId(chapter.getRecordUserId());
        hist.setDescription(chapter.getDescription());
        hist.setTextDescription(chapter.getTextDescription());
        HibernateUtil.getHSU().save(hist);
    }

    public void setDescription(String txt) {
        txt = StringUtils.normalize(txt);
        if (txt == null) {
            chapter.setDescription(null);
            chapter.setTextDescription(null);
        } else {
            if (txt.length() > 2000) {
                chapter.setDescription(null);
                chapter.setTextDescription(txt);
            } else {
                chapter.setDescription(txt);
                chapter.setTextDescription(null);
            }
        }
    }

    public String getDescription() {
        String txt = chapter.getDescription();
        if (txt == null)
            txt = chapter.getTextDescription();
        return txt;
    }

}
