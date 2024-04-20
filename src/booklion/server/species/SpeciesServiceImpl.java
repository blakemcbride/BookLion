package booklion.server.species;

import beans.Chapter;
import beans.Species;
import booklion.client.species.Record;
import booklion.client.species.RecordReturn;
import booklion.client.utils.StandardReturn;
import business.BSpecies;
import business.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import booklion.client.species.SpeciesService;
import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 1/10/14
 */
public class SpeciesServiceImpl extends RemoteServiceServlet implements SpeciesService {
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
            hcu1.selectColumns(Chapter.CHAPTER_ID);
            List<Long> chapterIDs = (List) hcu1.list();

            HibernateCriteriaUtil<Species> hcu2 = hsu.createCriteria(Species.class);
            hcu2.in(Species.CHAPTER_ID, chapterIDs);
            hcu2.orderByIgnoreCase(Species.SPECIES_NAME);
            List<Species> bcLst = hcu2.list();

            Record[] recs = new Record[bcLst.size()];
            int i = 0;
            for (Species bc : bcLst) {
                Record rec = new Record();
                recs[i++] = rec;
                rec.setSpeciesId(bc.getSpeciesId());
                rec.setChapterId(bc.getChapterId());
                rec.setSpeciesName(bc.getSpeciesName());
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