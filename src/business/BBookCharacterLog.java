package business;

import beans.BookCharacterLog;
import beans.BookCharacterLogH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.StringUtils;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/23/14
 */
public class BBookCharacterLog {
    private static final Logger logger = Logger.getLogger(BBookCharacterLog.class);

    private BookCharacterLog characterLog;

    public BBookCharacterLog(User user) {
        characterLog = new BookCharacterLog();
        characterLog.setRecordChangeDate(now());
        characterLog.setRecordChangeType("N");
        characterLog.setRecordUserId(user.getBean().getUserId());
    }

    public BBookCharacterLog(long characterLogId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<BookCharacterLog> hcu = new HibernateCriteriaUtil<BookCharacterLog>(hsu, BookCharacterLog.class);
        hcu.eq(BookCharacterLog.CHARACTER_LOG_ID, characterLogId);
        characterLog = (BookCharacterLog) hcu.getFirst();
    }

    public BBookCharacterLog(BookCharacterLog rec) {
        characterLog = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(characterLog);
    }

    public BookCharacterLog getBean() {
        return characterLog;
    }

    public BBookCharacterLog createHistoryModify(User user) {
        createHistory();

        characterLog.setRecordChangeDate(now());
        characterLog.setRecordChangeType("M");
        characterLog.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BBookCharacterLog createHistoryDelete(User user) {
        createHistory();

        characterLog.setRecordChangeDate(now());
        characterLog.setRecordChangeType("D");
        characterLog.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        BookCharacterLogH hist = new BookCharacterLogH();
        hist.setBcLogId(characterLog.getBcLogId());
        hist.setBookCharacterId(characterLog.getBookCharacterId());
        hist.setChapterId(characterLog.getChapterId());
        hist.setDescription(characterLog.getDescription());
        hist.setRecordChangeDate(characterLog.getRecordChangeDate());
        hist.setRecordChangeType(characterLog.getRecordChangeType());
        hist.setRecordUserId(characterLog.getRecordUserId());
        hist.setDescription(characterLog.getDescription());
        hist.setTextDescription(characterLog.getTextDescription());
        HibernateUtil.getHSU().save(hist);
    }

    public void setDescription(String txt) {
        txt = StringUtils.normalize(txt);
        if (txt == null) {
            characterLog.setDescription(null);
            characterLog.setTextDescription(null);
        } else {
            if (txt.length() > 2000) {
                characterLog.setDescription(null);
                characterLog.setTextDescription(txt);
            } else {
                characterLog.setDescription(txt);
                characterLog.setTextDescription(null);
            }
        }
    }

    public String getDescription() {
        String txt = characterLog.getDescription();
        if (txt == null)
            txt = characterLog.getTextDescription();
        return txt;
    }


}
