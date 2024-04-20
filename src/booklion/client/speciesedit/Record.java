package booklion.client.speciesedit;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class Record implements Serializable {

    private long speciesId;
    private long chapterId;
    private String speciesName;
    private LogRecord [] logs;

    public long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(long speciesId) {
        this.speciesId = speciesId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesComboBoxLabel() {
        return speciesName;
    }

    public void setSpeciesComboBoxLabel(String label) {
    }

    public LogRecord[] getLogs() {
        return logs;
    }

    public void setLogs(LogRecord[] logs) {
        this.logs = logs;
    }

    public Record copy() {
        Record ret = new Record();
        ret.speciesId = this.speciesId;
        ret.chapterId = this.chapterId;
        ret.speciesName = new String(this.speciesName);
        ret.logs = this.logs;
        return ret;
    }

}
