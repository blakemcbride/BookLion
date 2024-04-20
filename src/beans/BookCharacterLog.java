
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
@Table(name="book_character_log")
public class BookCharacterLog implements Serializable {

    public static final String CHARACTER_LOG_ID = "bcLogId";
    public static final String CHARACTER_ID = "bookCharacterId";
    public static final String CHAPTER_ID = "chapterId";
    public static final String SEQNO = "seqno";


	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BookCharacterLog.class);

    private long bc_log_id;
    private long book_character_id;
    private long chapter_id;
    private String description;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;
    private String text_description;

    
    public BookCharacterLog() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_character_log_bc_log_id_seq")
    @SequenceGenerator(name="book_character_log_bc_log_id_seq", sequenceName="book_character_log_bc_log_id_seq", allocationSize=1)
    @Column(name="bc_log_id")
    public long getBcLogId() {
        return bc_log_id;
    }

    public void setBcLogId(long bc_log_id) {
        this.bc_log_id = bc_log_id;
    }

    @Column(name="book_character_id")
    public long getBookCharacterId() {
        return book_character_id;
    }

    public void setBookCharacterId(long book_character_id) {
        this.book_character_id = book_character_id;
    }

    @Column(name="chapter_id")
    public long getChapterId() {
        return chapter_id;
    }

    public void setChapterId(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column(name="text_description")
    public String getTextDescription() {
        return text_description;
    }

    public void setTextDescription(String text_description) {
        this.text_description = text_description;
    }

}
