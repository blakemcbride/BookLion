package booklion.client.locationedit;

import java.io.Serializable;

/**
 *  @author Blake McBride
 * Date: 2/25/14
 */
public class Record implements Serializable {

    private long locationId;
    private Long insideLocationId;
    private long chapterId;
    private String locName;
    private LogRecord [] logs;

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public Long getInsideLocationId() {
        return insideLocationId;
    }

    public void setInsideLocationId(Long insideLocationId) {
        this.insideLocationId = insideLocationId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public LogRecord[] getLogs() {
        return logs;
    }

    public void setLogs(LogRecord[] logs) {
        this.logs = logs;
    }

    public Record copy() {
        Record ret = new Record();
        ret.locationId = this.locationId;
        ret.insideLocationId = this.insideLocationId == null ? null : new Long(this.insideLocationId);
        ret.chapterId = this.chapterId;
        ret.locName = new String(this.locName);
        ret.logs = this.logs;
        return ret;
    }

}
