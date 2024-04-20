package business;

import beans.BookCharacter;
import beans.BookCharacterH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/9/14
 */
public class BBookCharacter {

    private static final Logger logger = Logger.getLogger(BBookCharacter.class);

    private BookCharacter character;

    public BBookCharacter(User user) {
        character = new BookCharacter();
        character.setRecordChangeDate(now());
        character.setRecordChangeType("N");
        character.setRecordUserId(user.getBean().getUserId());
    }

    public BBookCharacter(long bookCharacterId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<BookCharacter> hcu = new HibernateCriteriaUtil<BookCharacter>(hsu, BookCharacter.class);
        hcu.eq(BookCharacter.CHARACTER_ID, bookCharacterId);
        character = (BookCharacter) hcu.getFirst();
    }

    public BBookCharacter(BookCharacter rec) {
        character = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(character);
    }

    public BookCharacter getBean() {
        return character;
    }

    public BBookCharacter createHistoryModify(User user) {
        createHistory();

        character.setRecordChangeDate(now());
        character.setRecordChangeType("M");
        character.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BBookCharacter createHistoryDelete(User user) {
        createHistory();

        character.setRecordChangeDate(now());
        character.setRecordChangeType("D");
        character.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        BookCharacterH hist = new BookCharacterH();
        hist.setBookCharacterId(character.getBookCharacterId());
        hist.setChapterId(character.getChapterId());
        hist.setSpeciesId(character.getSpeciesId());
        hist.setLname(character.getLname());
        hist.setFname(character.getFname());
        hist.setSuffix(character.getSuffix());
        hist.setNickname(character.getNickname());
        hist.setGender(character.getGender());
        hist.setRelevance(character.getRelevance());
        hist.setAffiliation(character.getAffiliation());
        hist.setOccupation(character.getOccupation());
        hist.setRelationship(character.getRelationship());
        hist.setRecordChangeDate(character.getRecordChangeDate());
        hist.setRecordChangeType(character.getRecordChangeType());
        hist.setRecordUserId(character.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }

}
