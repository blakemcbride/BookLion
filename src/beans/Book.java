
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
@Table(name="book")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Book.class);

    private long book_id;
    private int genre_id;
    private long author1_id;
    private Long author2_id;
    private Long author3_id;
    private String book_title;
    private int year_published;
    private String description;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    public static final String BOOK_ID = "bookId";
    public static final String BOOK_TITLE = "bookTitle";
    public static final String AUTHOR_1_ID = "author1Id";
    public static final String AUTHOR_2_ID = "author2Id";
    public static final String AUTHOR_3_ID = "author3Id";
    public static final String GENRE_ID = "genreId";

    
    public Book() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_book_id_seq")
    @SequenceGenerator(name="book_book_id_seq", sequenceName="book_book_id_seq", allocationSize=1)
    @Column(name="book_id")
    public long getBookId() {
        return book_id;
    }

    public void setBookId(long book_id) {
        this.book_id = book_id;
    }

    @Column(name="author1_id", insertable = false, updatable = false)
    public long getAuthor1Id() {
        return author1_id;
    }

    public void setAuthor1Id(long author1_id) {
        this.author1_id = author1_id;
    }

    @Column(name="author2_id", insertable = false, updatable = false)
    public Long getAuthor2Id() {
        return author2_id;
    }

    public void setAuthor2Id(Long author2_id) {
        this.author2_id = author2_id;
    }

    @Column(name="author3_id", insertable = false, updatable = false)
    public Long getAuthor3Id() {
        return author3_id;
    }

    public void setAuthor3Id(Long author3_id) {
        this.author3_id = author3_id;
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

    @Column(name="genre_id", insertable = false, updatable = false)
    public int getGenreId() {
        return genre_id;
    }

    public void setGenreId(int genre_id) {
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

    //////////////////////////////////////////////////////////////////////////////

    private Genre genre;

    /*  In this and elsewhere in this class, using the lazy fetch caused a manual select statement when filling a grid.
        So if you read 100 books there will also be 100 separate select statements to get each genre.
        When not lazy, hibernate gets all the data in a single select.

        Also, the ID field must be declared insertable = false, updatable = false
     */

//    @ManyToOne(fetch = FetchType.LAZY)    //  Many of these records matches one of the other table's
    @ManyToOne                              //  Many of these records matches one of the other table's
    @JoinColumn(name = "genre_id")      //  the name of the linking column in this table (the joined column is the @Id column of the other table)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    //////////////////////////////////////////////////////////////////////////////

    private Author author1;

//    @ManyToOne(fetch = FetchType.LAZY)    //  Many of these records matches one of the other table's
    @ManyToOne                              //  Many of these records matches one of the other table's
    @JoinColumn(name = "author1_id")      //  the name of the linking column in this table (the joined column is the @Id column of the other table)
    public Author getAuthor1() {
        return author1;
    }

    public void setAuthor1(Author auth) {
        this.author1 = auth;
    }

    //////////////////////////////////////////////////////////////////////////////

    private Author author2;

    //    @ManyToOne(fetch = FetchType.LAZY)    //  Many of these records matches one of the other table's
    @ManyToOne                              //  Many of these records matches one of the other table's
    @JoinColumn(name = "author2_id")      //  the name of the linking column in this table (the joined column is the @Id column of the other table)
    public Author getAuthor2() {
        return author2;
    }

    public void setAuthor2(Author auth) {
        this.author2 = auth;
    }

    //////////////////////////////////////////////////////////////////////////////

    private Author author3;

//    @ManyToOne(fetch = FetchType.LAZY)    //  Many of these records matches one of the other table's
    @ManyToOne                              //  Many of these records matches one of the other table's
    @JoinColumn(name = "author3_id")        //  the name of the linking column in this table (the joined column is the @Id column of the other table)
    public Author getAuthor3() {
        return author3;
    }

    public void setAuthor3(Author auth) {
        this.author3 = auth;
    }


}
