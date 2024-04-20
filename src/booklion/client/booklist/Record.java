package booklion.client.booklist;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private int recNo;  // sequential number for each record in the entire set (not just in this page)

    private long bookId;
    private String bookTitle;

    private int genreId;
    private String genreName;

    private long author1Id;
    private String author1Name;

    private Long author2Id;  // can be null
    private String author2Name;

    private Long author3Id;  // can be null
    private String author3Name;

    private int yearPublished;
    private String description;

    public Record() {}

    public int getRecNo() {
        return recNo;
    }

    public void setRecNo(int recNo) {
        this.recNo = recNo;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public long getAuthor1Id() {
        return author1Id;
    }

    public void setAuthor1Id(long author1Id) {
        this.author1Id = author1Id;
    }

    public String getAuthor1Name() {
        return author1Name;
    }

    public void setAuthor1Name(String author1Name) {
        this.author1Name = author1Name;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getAuthor2Id() {
        return author2Id;
    }

    public void setAuthor2Id(Long author2Id) {
        this.author2Id = author2Id;
    }

    public String getAuthor2Name() {
        return author2Name;
    }

    public void setAuthor2Name(String author2Name) {
        this.author2Name = author2Name;
    }

    public Long getAuthor3Id() {
        return author3Id;
    }

    public void setAuthor3Id(Long author3Id) {
        this.author3Id = author3Id;
    }

    public String getAuthor3Name() {
        return author3Name;
    }

    public void setAuthor3Name(String author3Name) {
        this.author3Name = author3Name;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorsName() {
        String r;
        String t = getAuthor1Name();
        if (t != null  &&  !t.isEmpty())
            r = t;
        else
            r = "";
        t = getAuthor2Name();
        if (t != null  &&  !t.isEmpty()) {
            if (!r.isEmpty())
                r += ", ";
            r += t;
        }
        t = getAuthor3Name();
        if (t != null  &&  !t.isEmpty()) {
            if (!r.isEmpty())
                r += ", ";
            r += t;
        }
        return r;
    }

    public void setAuthorsName(String name) {}

    private String strcpy(String str) {
        return str == null ? null : new String(str);
    }

    public Record copy() {
        Record ret = new Record();
        ret.bookId = this.bookId;
        ret.genreId = this.genreId;
        ret.genreName = strcpy(this.genreName);
        ret.author1Id = this.author1Id;
        ret.author1Name = strcpy(this.author1Name);
        ret.author2Id = this.author2Id;
        ret.author2Name = strcpy(this.author2Name);
        ret.author3Id = this.author3Id;
        ret.author3Name = strcpy(this.author3Name);
        ret.bookTitle = strcpy(this.bookTitle);
        ret.yearPublished = this.yearPublished;
        ret.description = strcpy(this.description);
        return ret;
    }

}
