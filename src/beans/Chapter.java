
package beans;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Blake McBride
 */

@Entity
@Table(name="chapter")
public class Chapter implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Chapter.class);

    public static final String CHAPTER_ID = "chapterId";
    public static final String BOOK_ID = "bookId";
    public static final String SEQNO = "seqno";

    private long chapter_id;
    private long book_id;
    private short seqno;
    private String chapter_designation;
    private String chapter_name;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;
    private String description;
    private String text_description;

    
    public Chapter() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="chapter_chapter_id_seq")
    @SequenceGenerator(name="chapter_chapter_id_seq", sequenceName="chapter_chapter_id_seq", allocationSize=1)
    @Column(name="chapter_id")
    public long getChapterId() {
        return chapter_id;
    }

    public void setChapterId(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    @Column(name="book_id")
    public long getBookId() {
        return book_id;
    }

    public void setBookId(long book_id) {
        this.book_id = book_id;
    }

    @Column(name="chapter_designation")
    public String getChapterDesignation() {
        return chapter_designation;
    }

    public void setChapterDesignation(String chapter_designation) {
        this.chapter_designation = chapter_designation;
    }

    @Column(name="chapter_name")
    public String getChapterName() {
        return chapter_name;
    }

    public void setChapterName(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    @Column(name="record_change_date")
    public Timestamp getRecordChangeDate() {
        return record_change_date;
    }

    public void setRecordChangeDate(Timestamp record_change_date) {
        this.record_change_date = record_change_date;
    }

    @Column(name="record_change_type")
    public String getRecordChangeType() {
        return record_change_type;
    }

    public void setRecordChangeType(String record_change_type) {
        this.record_change_type = record_change_type;
    }

    @Column(name="record_user_id")
    public long getRecordUserId() {
        return record_user_id;
    }

    public void setRecordUserId(long record_user_id) {
        this.record_user_id = record_user_id;
    }

    @Column(name="seqno")
    public short getSeqno() {
        return seqno;
    }

    public void setSeqno(short seqno) {
        this.seqno = seqno;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="text_description")
    public String getTextDescription() {
        return text_description;
    }

    public void setTextDescription(String text_description) {
        this.text_description = text_description;
    }
}
