package booklion.server.booklist;

import beans.Author;
import beans.Book;
import booklion.client.booklist.PageData;
import booklion.client.booklist.Record;
import booklion.client.utils.StandardReturn;
import business.BAuthor;
import business.BBook;
import business.BGenre;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.booklist.BooklistService;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static utils.DateUtils.now;
import static utils.StringUtils.different;

/**
 * @author Blake McBride
 * Date created: 11/11/13
 */
public class BooklistServiceImpl extends RemoteServiceServlet implements BooklistService {
    private static final Logger logger = Logger.getLogger(BooklistServiceImpl.class);
    private static final long serialVersionUID = 1L;

    /**
     * Returns a page full of records.
     *
     * @param uuid
     * @return
     *
     */
    @Override
    public PagingLoadResult<Record> getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec,
                                             int numOfRecs, String searchText1, String searchText2) {
        HibernateSessionUtil hsu = null;
        PageData res = null;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin())
                return null;

            int limit = config.getLimit();  //  max number of recs on page
            int offset = config.getOffset();
            res = new PageData();
            HibernateCriteriaUtil<Book> hcu;

            //  I was attempting to cache the number of records in the DB to avoid getting the count each time but
            //  I can't handle adding or deleting records that way
//			if (numOfRecs != 0)
//				res.setTotalLength(numOfRecs);
//			else {
//				hcu = hsu.createCriteria(Book.class);
//				numOfRecs = (int) hcu.numberOfRecords();
//				res.setTotalLength(numOfRecs);
//			}



            String sortField = Book.BOOK_TITLE;  // default sort field
            boolean ascending = true;
            List<? extends SortInfo> sortInfo = config.getSortInfo();
            if (!sortInfo.isEmpty()) {
                SortInfo si = sortInfo.get(0);
                if ("bookTitle".equals(si.getSortField())) {
                    sortField = Book.BOOK_TITLE;
                    ascending = si.getSortDir().ordinal() == 0;
                } else if ("author".equals(si.getSortField())) {
                    sortField = Book.AUTHOR_1_ID;
                    ascending = si.getSortDir().ordinal() == 0;
                } else if ("genre".equals(si.getSortField())) {
                    sortField = Book.GENRE_ID;
                    ascending = si.getSortDir().ordinal() == 0;
                }
            }


            hcu = hsu.createCriteria(Book.class);
            setSelection(hcu, sortField, searchText1, searchText2);
            numOfRecs = (int) hcu.numberOfRecords();
            res.setTotalLength(numOfRecs);

            int num = offset+limit > numOfRecs ? numOfRecs - offset : limit;  //  actual number of recs on page


            boolean reverse = false;
            List<Record> data = new LinkedList<Record>();
            res.setOffset(offset);
            hcu = hsu.createCriteria(Book.class);
            hcu.setMaxResults(num);
            List<Book> recs;




            if (offset == 0  ||  firstRec == null  ||  lastRec == null) { // forward from beginning
                orderBy(hcu, sortField, ascending);
            } else if (offset-1 == lastRec.getRecNo()) {  //  one page forward
                orderBy(hcu, sortField, ascending);
                if (ascending) {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameGTCriteria(lastRec));
//                    } else
                        hcu.gtIgnoreCase(sortField, getSortField(lastRec, sortField));
                } else {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameLTCriteria(lastRec));
//                    } else
                        hcu.ltIgnoreCase(sortField, getSortField(lastRec, sortField));
                }
            } else if (offset+num == firstRec.getRecNo())  {  //  one page back
                orderBy(hcu, sortField, !ascending);
                if (ascending) {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameLTCriteria(firstRec));
//                    } else
                        hcu.ltIgnoreCase(sortField, getSortField(firstRec, sortField));
                } else {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameGTCriteria(firstRec));
//                    } else
                        hcu.gtIgnoreCase(sortField, getSortField(firstRec, sortField));
                }
                reverse = true;
            } else if (offset+num == numOfRecs) {  //  last page
                orderBy(hcu, sortField, !ascending);
                reverse = true;
            } else if (firstRec.getRecNo() == offset) {  // page reload (not on the first page)
                orderBy(hcu, sortField, ascending);
                if (ascending) {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameGECriteria(firstRec));
//                    } else
                        hcu.geIgnoreCase(sortField, getSortField(firstRec, sortField));
                } else {
//                    if (Book.LNAME.equals(sortField)) {
//                        hcu.add(addNameLECriteria(firstRec));
//                    } else
                        hcu.leIgnoreCase(sortField, getSortField(firstRec, sortField));
                }
            } else {  //  unknown page operation
                logger.error("unknown orientation");
                hcu = null;
            }

            setSelection(hcu, sortField, searchText1, searchText2);

            recs = hcu.list();
            for (int i=0 ; i < num ; i++) {
                Book rec = recs.get(reverse ? num-i-1 : i);
                Record p = new Record();
                p.setRecNo(i+offset);
                p.setBookId(rec.getBookId());
                p.setBookTitle(rec.getBookTitle());
                p.setGenreId(rec.getGenreId());
                p.setGenreName(rec.getGenre().getGenreName());
                p.setAuthor1Id(rec.getAuthor1Id());
                p.setAuthor1Name(authorName(rec.getAuthor1()));
                p.setAuthor2Id(rec.getAuthor2Id());
                p.setAuthor2Name(authorName(rec.getAuthor2()));
                p.setAuthor3Id(rec.getAuthor3Id());
                p.setAuthor3Name(authorName(rec.getAuthor3()));
                p.setYearPublished(rec.getYearPublished());
                p.setDescription(rec.getDescription());

                data.add(p);
            }

            res.setData(data);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (hsu != null)
                HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    private String authorName(Author auth) {
        if (auth == null)
            return null;
        String fname = auth.getFname();
        if (fname != null  &&  !fname.isEmpty())
            return auth.getLname() + ", " + fname;
        return auth.getLname();
    }

    private void setSelection(HibernateCriteriaUtil<Book> hcu, String sortField, String  searchText1, String searchText2) {
        if (searchText1 != null  &&  !searchText1.isEmpty())
            if (Book.BOOK_TITLE.equals(sortField))
                hcu.likeIgnoreCase(Book.BOOK_TITLE, searchText1 + "%");
//            else {
//                hcu.likeIgnoreCase(Book.LNAME, searchText1 + "%");
//                if (searchText2 != null  &&  !searchText2.isEmpty())
//                    hcu.likeIgnoreCase(Book.FNAME, searchText2 + "%");
//            }
    }

//    private Criterion addNameGTCriteria(UserData rec) {
//        Criterion c1 = Restrictions.gt(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase();
//        Criterion c2 = Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                Restrictions.gt(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase());
//        Criterion c3 = Restrictions.and(
//                Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                        Restrictions.eq(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
//                Restrictions.gt(Book.USER_ID, rec.getUserId()));
//        Criterion c4 = Restrictions.or(c1,  c2);
//        Criterion c5 = Restrictions.or(c4,  c3);
//        return c5;
//    }
//
//    private Criterion addNameGECriteria(UserData rec) {
//        Criterion c1 = Restrictions.and(
//                Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                        Restrictions.eq(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
//                Restrictions.eq(Book.USER_ID, rec.getUserId()));
//        Criterion c2 = Restrictions.or(c1, addNameGTCriteria(rec));
//        return c2;
//    }
//
//    private Criterion addNameLTCriteria(UserData rec) {
//        Criterion c1 = Restrictions.lt(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase();
//        Criterion c2 = Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                Restrictions.lt(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase());
//        Criterion c3 = Restrictions.and(
//                Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                        Restrictions.eq(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
//                Restrictions.lt(Book.USER_ID, rec.getUserId()));
//        Criterion c4 = Restrictions.or(c1,  c2);
//        Criterion c5 = Restrictions.or(c4,  c3);
//        return c5;
//    }
//
//    private Criterion addNameLECriteria(UserData rec) {
//        Criterion c1 = Restrictions.and(
//                Restrictions.and(Restrictions.eq(Book.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
//                        Restrictions.eq(Book.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
//                Restrictions.eq(Book.USER_ID, rec.getUserId()));
//        Criterion c2 = Restrictions.or(c1, addNameLTCriteria(rec));
//        return c2;
//    }

    private void orderBy(HibernateCriteriaUtil<Book> hcu, String fld, boolean ascending) {
        if (ascending) {
            hcu.orderByIgnoreCase(fld);
//            if (fld.equals(Book.LNAME)) {
//                hcu.orderByIgnoreCase(Book.FNAME);
//                hcu.orderBy(Book.USER_ID);
//            }
        } else {
            hcu.orderByDescIgnoreCase(fld);
//            if (fld.equals(Book.LNAME)) {
//                hcu.orderByDescIgnoreCase(Book.FNAME);
//                hcu.orderBy(Book.USER_ID);
//            }
        }
    }

    private String getSortField(Record rec, String sortField) {
        if (sortField.equals(Book.BOOK_TITLE))
            return rec.getBookTitle().toLowerCase();
//        else if (sortField.equals(Book.LNAME))
//            return rec.getLname().toLowerCase();
        return null;
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

            BBook bbook = new BBook(rec.getBookId());
            Book urec = bbook.getBean();
            if (different(urec.getBookTitle(), rec.getBookTitle())  ||
                    urec.getYearPublished() != rec.getYearPublished()  ||
                    different(urec.getDescription(), rec.getDescription())  ||
                    urec.getGenreId() != rec.getGenreId()  ||
                    urec.getAuthor1Id() != rec.getAuthor1Id()  ||
                    different(urec.getAuthor2Id(), rec.getAuthor2Id())  ||
                    different(urec.getAuthor3Id(), rec.getAuthor3Id())) {
                bbook.createHistoryModify(user);
                urec.setBookTitle(rec.getBookTitle());
                urec.setYearPublished(rec.getYearPublished());
                urec.setDescription(rec.getDescription());

                urec.setGenreId(rec.getGenreId());
                urec.setAuthor1(new BAuthor(rec.getAuthor1Id()).getBean());
                urec.setAuthor2(rec.getAuthor2Id()==null?null:new BAuthor(rec.getAuthor2Id()).getBean());
                urec.setAuthor3(rec.getAuthor3Id()==null?null:new BAuthor(rec.getAuthor3Id()).getBean());
                hsu.commit();
            }
            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
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

            Book newRec = new BBook(user).getBean();

            newRec.setBookTitle(rec.getBookTitle());
            newRec.setYearPublished(rec.getYearPublished());
            newRec.setDescription(rec.getDescription());

            newRec.setGenre((new BGenre(rec.getGenreId()).getBean()));
            newRec.setAuthor1((new BAuthor(rec.getAuthor1Id()).getBean()));
            if (rec.getAuthor2Id() != null)
                newRec.setAuthor2((new BAuthor(rec.getAuthor2Id()).getBean()));
            if (rec.getAuthor3Id() != null)
                newRec.setAuthor3((new BAuthor(rec.getAuthor3Id()).getBean()));

            hsu.save(newRec);

            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
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

            new BBook(rec.getBookId()).delete(user);

            hsu.commit();

            res = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            res = ExceptionMessage.getReturn(e);
        } finally {
            HibernateSessionUtil.close(hsu);
        }
        return res;
    }

}