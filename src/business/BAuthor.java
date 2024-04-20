package business;

import beans.Author;
import beans.AuthorH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public class BAuthor {
    private static final Logger logger = Logger.getLogger(BAuthor.class);

    private Author author;

    public BAuthor(User user) {
        author = new Author();
        author.setRecordChangeDate(now());
        author.setRecordChangeType("N");
        author.setRecordUserId(user.getBean().getUserId());
    }

    public BAuthor(long authorId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Author> hcu = new HibernateCriteriaUtil<Author>(hsu, Author.class);
        hcu.eq(Author.AUTHOR_ID, authorId);
        author = (Author) hcu.getFirst();
    }

    public BAuthor(Author rec) {
        author = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(author);
    }

    public Author getBean() {
        return author;
    }

    public BAuthor createHistoryModify(User user) {
        createHistory();

        author.setRecordChangeDate(now());
        author.setRecordChangeType("M");
        author.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BAuthor createHistoryDelete(User user) {
        createHistory();

        author.setRecordChangeDate(now());
        author.setRecordChangeType("D");
        author.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        AuthorH hist = new AuthorH();
        hist.setAuthorId(author.getAuthorId());
        hist.setLname(author.getLname());
        hist.setFname(author.getFname());
        hist.setRecordChangeDate(author.getRecordChangeDate());
        hist.setRecordChangeType(author.getRecordChangeType());
        hist.setRecordUserId(author.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }

}
