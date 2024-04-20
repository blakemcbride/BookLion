package booklion.client.locationedit;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 3/18/14
 */
public class LogRecord implements Serializable {

    private long chapterId;
    private String log;

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
