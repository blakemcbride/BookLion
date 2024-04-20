package booklion.client.chapterlist;

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

    public String getChapterComboBoxLabel() {
        if (chapterName != null  &&  !chapterName.isEmpty())
            return chapterDesignation + " - " + chapterName;
        else
            return chapterDesignation;
    }

    public void setChapterComboBoxLabel(String label) {
    }

    public Record copy() {
        Record ret = new Record();
        ret.chapterId = this.chapterId;
        ret.bookId = this.bookId;
        ret.seqno = this.seqno;
        ret.chapterDesignation = this.chapterDesignation;
        ret.chapterName = this.chapterName;
        return ret;
    }

}
