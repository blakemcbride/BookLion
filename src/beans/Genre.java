
package beans;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Blake McBride
 */

@Entity
@Table(name="genre")
public class Genre implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String GENRE_ID = "genreId";
    public static final String GENRE_NAME = "genreName";

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Genre.class);

    private int genre_id;
    private Integer sub_genre_id;
    private String genre_name;
    
    public Genre() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="genre_genre_id_seq")
    @SequenceGenerator(name="genre_genre_id_seq", sequenceName="genre_genre_id_seq", allocationSize=1)
    @Column(name="genre_id")
    public int getGenreId() {
        return genre_id;
    }

    public void setGenreId(int genre_id) {
        this.genre_id = genre_id;
    }

    @Column(name="genre_name")
    public String getGenreName() {
        return genre_name;
    }

    public void setGenreName(String genre_name) {
        this.genre_name = genre_name;
    }

    @Column(name="sub_genre_id")
    public Integer getSubGenreId() {
        return sub_genre_id;
    }

    public void setSubGenreId(Integer sub_genre_id) {
        this.sub_genre_id = sub_genre_id;
    }

    
}
