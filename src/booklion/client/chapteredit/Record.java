package booklion.client.chapteredit;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 1/3/14
 */
public class Record implements Serializable {

    private long chapterId;
    private long bookId;
    private short seqno;
    private String chapterDesignation;
    private String chapterName;
    private String logs;

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public short getSeqno() {
        return seqno;
    }

    public void setSeqno(short seqno) {
        this.seqno = seqno;
    }

    public String getChapterDesignation() {
        return chapterDesignation;
    }

    public void setChapterDesignation(String chapterDesignation) {
        this.chapterDesignation = chapterDesignation;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public Record copy() {
        Record ret = new Record();
        ret.chapterId = this.chapterId;
        ret.bookId = this.bookId;
        ret.seqno = this.seqno;
        ret.chapterDesignation = this.chapterDesignation;
        ret.chapterName = this.chapterName;
        ret.logs = this.logs;
        return ret;
    }

}
