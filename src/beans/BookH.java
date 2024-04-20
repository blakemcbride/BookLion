
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
@Table(name="book_h")
public class BookH implements Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")

	private static final Logger logger = Logger.getLogger(BookH.class);

    private long history_id;
    private long book_id;
    private long genre_id;
    private long author1_id;
    private Long author2_id;
    private Long author3_id;
    private String book_title;
    private int year_published;
    private String description;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    
    public BookH() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_h_history_id_seq")
    @SequenceGenerator(name="book_h_history_id_seq", sequenceName="book_h_history_id_seq", allocationSize=1)
    @Column(name="history_id")
    public long getHistoryId() {
        return history_id;
    }

    public void setHistoryId(long history_id) {
        this.history_id = history_id;
    }

    @Column(name="author1_id")
    public long getAuthor1Id() {
        return author1_id;
    }

    public void setAuthor1Id(long author1_id) {
        this.author1_id = author1_id;
    }

    @Column(name="author2_id")
    public Long getAuthor2Id() {
        return author2_id;
    }

    public void setAuthor2Id(Long author2_id) {
        this.author2_id = author2_id;
    }

    @Column(name="author3_id")
    public Long getAuthor3Id() {
        return author3_id;
    }

    public void setAuthor3Id(Long author3_id) {
        this.author3_id = author3_id;
    }

    @Column(name="book_id")
    public long getBookId() {
        return book_id;
    }

    public void setBookId(long book_id) {
        this.book_id = book_id;
    }

    @Column(name="book_title")
    public String getBookTitle() {
        return book_title;
    }

    public void setBookTitle(String book_title) {
        this.book_title = book_title;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="genre_id")
    public long getGenreId() {
        return genre_id;
    }

    public void setGenreId(long genre_id) {
        this.genre_id = genre_id;
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

    @Column(name="year_published")
    public int getYearPublished() {
        return year_published;
    }

    public void setYearPublished(int year_published) {
        this.year_published = year_published;
    }


}
