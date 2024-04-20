package business;

import beans.Species;
import beans.SpeciesH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/10/14
 */
public class BSpecies {
    private static final Logger logger = Logger.getLogger(BSpecies.class);

    private Species species;

    public BSpecies(User user) {
        species = new Species();
        species.setRecordChangeDate(now());
        species.setRecordChangeType("N");
        species.setRecordUserId(user.getBean().getUserId());
    }

    public BSpecies(long speciesId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<Species> hcu = new HibernateCriteriaUtil<Species>(hsu, Species.class);
        hcu.eq(Species.SPECIES_ID, speciesId);
        species = (Species) hcu.getFirst();
    }

    public BSpecies(Species rec) {
        species = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(species);
    }

    public Species getBean() {
        return species;
    }

    public BSpecies createHistoryModify(User user) {
        createHistory();

        species.setRecordChangeDate(now());
        species.setRecordChangeType("M");
        species.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    private BSpecies createHistoryDelete(User user) {
        createHistory();

        species.setRecordChangeDate(now());
        species.setRecordChangeType("D");
        species.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        SpeciesH hist = new SpeciesH();
        hist.setSpeciesId(species.getSpeciesId());
        hist.setChapterId(species.getChapterId());
        hist.setSpeciesName(species.getSpeciesName());
        hist.setRecordChangeDate(species.getRecordChangeDate());
        hist.setRecordChangeType(species.getRecordChangeType());
        hist.setRecordUserId(species.getRecordUserId());
        HibernateUtil.getHSU().save(hist);
    }

}
