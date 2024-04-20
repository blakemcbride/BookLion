package business;

import beans.Genre;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public class BGenre {
    private static final Logger logger = Logger.getLogger(BAuthor.class);

    private Genre genre;

    public BGenre(Genre g) {
        genre = g;
    }

    public BGenre(User user) {
        genre = new Genre();
    }

    public BGenre(int genreId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Genre> hcu = new HibernateCriteriaUtil<Genre>(hsu, Genre.class);
        hcu.eq(Genre.GENRE_ID, genreId);
        genre = (Genre) hcu.getFirst();
    }

    public Genre getBean() {
        return genre;
    }

}
