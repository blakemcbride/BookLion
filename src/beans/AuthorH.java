
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
@Table(name="author_h")
public class AuthorH implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AuthorH.class);
    private long history_id;
    private long author_id;
    private String lname;
    private String fname;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    
    public AuthorH() {}

    @Id
//  @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="author_h_history_id_seq")
    @SequenceGenerator(name="author_h_history_id_seq", sequenceName="author_h_history_id_seq", allocationSize=1)
    @Column(name="history_id")
    public long getHistoryId() {
        return history_id;
    }

    public void setHistoryId(long history_id) {
        this.history_id = history_id;
    }

    @Column(name="author_id")
    public long getAuthorId() {
        return author_id;
    }

    public void setAuthorId(long author_id) {
        this.author_id = author_id;
    }

    @Column(name="fname")
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Column(name="lname")
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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


}
