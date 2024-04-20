package booklion.client.characteredit;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 3/12/14
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
