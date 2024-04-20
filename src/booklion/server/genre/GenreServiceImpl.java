package booklion.server.genre;

import beans.Genre;
import booklion.client.genre.GenreItem;
import booklion.client.global.Information;
import booklion.client.utils.StandardReturn;
import booklion.server.login.LoginEmailNotification;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.genre.GenreService;
import dbutils.ExceptionMessage;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.DateUtils;
import utils.EMail;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 10/28/13
 */
public class GenreServiceImpl extends RemoteServiceServlet implements GenreService {

    private static final Logger logger = Logger.getLogger(GenreServiceImpl.class);

    @Override
    public GenreItem[] loadGenre(String uuid) {
        HibernateSessionUtil hsu = null;
        GenreItem [] res = new GenreItem[0];
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isValidLogin())
                return null;
            List<Genre> lst = hsu.createCriteria(Genre.class).orderBy(Genre.GENRE_NAME).list();
            res = new GenreItem[lst.size()];
            sort(lst, res, null, 0);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (hsu != null)
                HibernateSessionUtil.close(hsu);
        }
        return res;
    }

    @Override
    public StandardReturn addGenre(String uuid, String name, Integer parent) {
        HibernateSessionUtil hsu = null;
        StandardReturn ret;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
            if (!user.isAdministrator())
                return new StandardReturn("Error: action requires supervisor privileges");
            Genre rec = new Genre();
            rec.setGenreName(name);
            rec.setSubGenreId(parent);
            hsu.save(rec);
            hsu.commit();
            ret = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            ret = ExceptionMessage.getReturn(e);
        } finally {
            if (hsu != null)
                HibernateSessionUtil.close(hsu);
        }
        return ret;
    }

    @Override
    public StandardReturn removeGenre(String uuid, int id) {
        HibernateSessionUtil hsu = null;
        StandardReturn ret;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
            if (!user.isAdministrator())
                return new StandardReturn("Error: action requires supervisor privileges");
            Genre rec = (Genre) hsu.createCriteria(Genre.class).eq(Genre.GENRE_ID, id).getFirst();
            hsu.delete(rec);
            hsu.commit();
            ret = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            ret = ExceptionMessage.getReturn(e);
        } finally {
            if (hsu != null)
                HibernateSessionUtil.close(hsu);
        }
        return ret;
    }

    @Override
    public StandardReturn renameGenre(String uuid, int id, String newName) {
        HibernateSessionUtil hsu = null;
        StandardReturn ret;
        try {
            hsu = HibernateUtil.openHSU();
            User user = new User(uuid);
            if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
            if (!user.isAdministrator())
                return new StandardReturn("Error: action requires supervisor privileges");
            Genre rec = (Genre) hsu.createCriteria(Genre.class).eq(Genre.GENRE_ID, id).getFirst();
            rec.setGenreName(newName);
            hsu.commit();
            ret = new StandardReturn(StandardReturn.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            ret = ExceptionMessage.getReturn(e);
        } finally {
            if (hsu != null)
                HibernateSessionUtil.close(hsu);
        }
        return ret;
    }

    private int sort(List<Genre> lst, GenreItem [] res, Integer parent, int idx) {
        Genre rec;
        while (null != (rec = getNext(lst, parent))) {
            copy(res, rec, idx++);
            idx = sort(lst, res, rec.getGenreId(), idx);
        }
        return idx;
    }

    private void copy(GenreItem [] res, Genre rec, int idx) {
        res[idx] = new GenreItem();
        res[idx].setId(rec.getGenreId());
        res[idx].setParentId(rec.getSubGenreId());
        res[idx].setName(rec.getGenreName());
    }

    private Genre getNext(List<Genre> lst, Integer parent) {
        for (int i=0 ; i < lst.size() ; i++) {
            Integer id = lst.get(i).getSubGenreId();
            if (id == parent  ||  id != null  &&  parent != null  &&  id.equals(parent))
                return lst.remove(i);
        }
        return null;
    }
}