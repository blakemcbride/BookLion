package booklion.server.authorselect;

import beans.Author;
import booklion.client.authorselect.Record;
import booklion.client.authorselect.RecordReturn;
import booklion.client.utils.StandardReturn;
import business.BAuthor;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.authorselect.AuthorSelectService;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public class AuthorSelectServiceImpl extends RemoteServiceServlet implements AuthorSelectService {

    @Override
    public RecordReturn getRecords(String uuid, String lname) {
        HibernateSessionUtil hsu = null;
        RecordReturn res = new RecordReturn(StandardReturn.SUCCESS);
        if (lname == null)
            lname = "";
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin())
                return new RecordReturn("Invalid login");

            HibernateCriteriaUtil<Author> hcu1 = hsu.createCriteria(Author.class);
            hcu1.likeIgnoreCase(Author.LAST_NAME, lname + "%");
            hcu1.orderByIgnoreCase(Author.LAST_NAME);
            hcu1.orderByIgnoreCase(Author.FIRST_NAME);
            hcu1.setMaxResults(30);
            List<Author> authors = hcu1.list();

            Record[] recs = new Record[authors.size()];
            int i = 0;
            for (Author author : authors) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setAuthorId(author.getAuthorId());
                rec.setLname(author.getLname());
                rec.setFname(author.getFname());
            }
            res.setRecords(recs);

        } catch (Exception e) {
            res = new RecordReturn(e.getMessage());
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

            BAuthor bchap = new BAuthor(user);
            Author newRec = bchap.getBean();

            newRec.setLname(rec.getLname());
            newRec.setFname(rec.getFname());

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
    public StandardReturn updateRecord(String uuid, Record rec) {
        HibernateSessionUtil hsu = null;
        StandardReturn res;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();

            BAuthor brec = new BAuthor(rec.getAuthorId());
            brec.createHistoryModify(user);
            Author urec = brec.getBean();
            urec.setLname(rec.getLname());
            urec.setFname(rec.getFname());

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

            BAuthor brec = new BAuthor(rec.getAuthorId());
            brec.delete(user);
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