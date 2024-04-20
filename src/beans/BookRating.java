
package beans;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Blake McBride
 */

@Entity
@Table(name="book_rating")
public class BookRating implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BookRating.class);

    private long book_rating_id;
    private long user_id;
    private long book_id;
    private short rating;
    private int date_made;

    
    public BookRating() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_rating_book_rating_id_seq")
    @SequenceGenerator(name="book_rating_book_rating_id_seq", sequenceName="book_rating_book_rating_id_seq", allocationSize=1)
    @Column(name="book_rating_id")
    public long getBookRatingId() {
        return book_rating_id;
    }

    public void setBookRatingId(long book_rating_id) {
        this.book_rating_id = book_rating_id;
    }

    @Column(name="book_id")
    public long getBookId() {
        return book_id;
    }

    public void setBookId(long book_id) {
        this.book_id = book_id;
    }

    @Column(name="date_made")
    public int getDateMade() {
        return date_made;
    }

    public void setDateMade(int date_made) {
        this.date_made = date_made;
    }

    @Column(name="rating")
    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    @Column(name="user_id")
    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }


}
