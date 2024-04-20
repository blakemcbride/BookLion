package business;

import beans.Book;
import beans.BookH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 11/16/13
 */
public class BBook {

    private static final Logger logger = Logger.getLogger(BBook.class);

    private Book book;

    public BBook(Book b) {
        book = b;
    }

    public BBook(User user) {
        book = new Book();
        book.setRecordChangeDate(now());
        book.setRecordChangeType("N");
        book.setRecordUserId(user.getBean().getUserId());
    }

    public BBook(long bookId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Book> hcu = new HibernateCriteriaUtil<Book>(hsu, Book.class);
        hcu.eq(Book.BOOK_ID, bookId);
        book = (Book) hcu.getFirst();
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(book);
    }

    public Book getBean() {
        return book;
    }

    public BBook createHistoryModify(User user) {
        createHistory();

        book.setRecordChangeDate(now());
        book.setRecordChangeType("M");
        book.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BBook createHistoryDelete(User user) {
        createHistory();

        book.setRecordChangeDate(now());
        book.setRecordChangeType("D");
        book.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        BookH hist = new BookH();
        hist.setBookId(book.getBookId());
        hist.setGenreId(book.getGenreId());
        hist.setAuthor1Id(book.getAuthor1Id());
        hist.setAuthor2Id(book.getAuthor2Id());
        hist.setAuthor3Id(book.getAuthor3Id());
        hist.setBookTitle(book.getBookTitle());
        hist.setYearPublished(book.getYearPublished());
        hist.setDescription(book.getDescription());
        hist.setRecordChangeDate(book.getRecordChangeDate());
        hist.setRecordChangeType(book.getRecordChangeType());
        hist.setRecordUserId(book.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }
}
