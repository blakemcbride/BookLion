package business;

import beans.SpeciesLog;
import beans.SpeciesLogH;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import org.apache.log4j.Logger;
import utils.StringUtils;

import static utils.DateUtils.now;

/**
 * @author Blake McBride
 * Date: 1/26/14
 */
public class BSpeciesLog {

    private static final Logger logger = Logger.getLogger(BSpeciesLog.class);

    private SpeciesLog speciesLog;

    public BSpeciesLog(User user) {
        speciesLog = new SpeciesLog();
        speciesLog.setRecordChangeDate(now());
        speciesLog.setRecordChangeType("N");
        speciesLog.setRecordUserId(user.getBean().getUserId());
    }

    public BSpeciesLog(long characterLogId) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        HibernateCriteriaUtil<SpeciesLog> hcu = new HibernateCriteriaUtil<SpeciesLog>(hsu, SpeciesLog.class);
        hcu.eq(SpeciesLog.SPECIES_LOG_ID, characterLogId);
        speciesLog = (SpeciesLog) hcu.getFirst();
    }

    public BSpeciesLog(SpeciesLog rec) {
        speciesLog = rec;
    }

    public void delete(User user) {
        HibernateSessionUtil hsu = HibernateUtil.getHSU();
        createHistoryDelete(user);
        hsu.delete(speciesLog);
    }

    public SpeciesLog getBean() {
        return speciesLog;
    }

    public BSpeciesLog createHistoryModify(User user) {
        createHistory();

        speciesLog.setRecordChangeDate(now());
        speciesLog.setRecordChangeType("M");
        speciesLog.setRecordUserId(user.getBean().getUserId());

        return this;
    }

    public BSpeciesLog createHistoryDelete(User user) {
        createHistory();

        speciesLog.setRecordChangeDate(now());
        speciesLog.setRecordChangeType("D");
        speciesLog.setRecordUserId(user.getBean().getUserId());
        createHistory();

        return this;
    }

    private void createHistory() {
        SpeciesLogH hist = new SpeciesLogH();
        hist.setSpeciesLogId(speciesLog.getSpeciesLogId());
        hist.setSpeciesId(speciesLog.getSpeciesId());
        hist.setChapterId(speciesLog.getChapterId());
        hist.setDescription(speciesLog.getDescription());
        hist.setRecordChangeDate(speciesLog.getRecordChangeDate());
        hist.setRecordChangeType(speciesLog.getRecordChangeType());
        hist.setRecordUserId(speciesLog.getRecordUserId());
        hist.setDescription(speciesLog.getDescription());
        hist.setTextDescription(speciesLog.getTextDescription());
        HibernateUtil.getHSU().save(hist);
    }

    public void setDescription(String txt) {
        txt = StringUtils.normalize(txt);
        if (txt == null) {
            speciesLog.setDescription(null);
            speciesLog.setTextDescription(null);
        } else {
            if (txt.length() > 2000) {
                speciesLog.setDescription(null);
                speciesLog.setTextDescription(txt);
            } else {
                speciesLog.setDescription(txt);
                speciesLog.setTextDescription(null);
            }
        }
    }

    public String getDescription() {
        String txt = speciesLog.getDescription();
        if (txt == null)
            txt = speciesLog.getTextDescription();
        return txt;
    }


}
