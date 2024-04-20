
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
@Table(name="book_comment")
public class BookComment implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BookComment.class);

    private long book_comment_id;
    private long user_id;
    private long book_id;
    private Timestamp date_made;
    private String book_comment;
    
    
    public BookComment() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_comment_book_comment_id_seq")
    @SequenceGenerator(name="book_comment_book_comment_id_seq", sequenceName="book_comment_book_comment_id_seq", allocationSize=1)
    @Column(name="book_comment_id")
    public long getBookCommentId() {
        return book_comment_id;
    }

    public void setBookCommentId(long book_comment_id) {
        this.book_comment_id = book_comment_id;
    }

    @Column(name="book_comment")
    public String getBookComment() {
        return book_comment;
    }

    public void setBookComment(String book_comment) {
        this.book_comment = book_comment;
    }

    @Column(name="book_id")
    public long getBookId() {
        return book_id;
    }

    public void setBookId(long book_id) {
        this.book_id = book_id;
    }

    @Column(name="date_made")
    public Timestamp getDateMade() {
        return date_made;
    }

    public void setDateMade(Timestamp date_made) {
        this.date_made = date_made;
    }

    @Column(name="user_id")
    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }


}
