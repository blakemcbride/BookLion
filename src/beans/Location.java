
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
@Table(name="location")
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
    public static final String LOCATION_ID = "locationId";
    public static final String LOCATION_NAME = "locName";
    public static final String CHAPTER_ID = "chapterId";

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Location.class);

    private long location_id;
    private Long inside_location_id;  // can be null
    private long chapter_id;
    private String loc_name;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    
    public Location() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="location_location_id_seq")
    @SequenceGenerator(name="location_location_id_seq", sequenceName="location_location_id_seq", allocationSize=1)
    @Column(name="location_id")
    public long getLocationId() {
        return location_id;
    }

    public void setLocationId(long location_id) {
        this.location_id = location_id;
    }

    @Column(name="chapter_id")
    public long getChapterId() {
        return chapter_id;
    }

    public void setChapterId(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    @Column(name="inside_location_id")
    public Long getInsideLocationId() {
        return inside_location_id;
    }

    public void setInsideLocationId(Long inside_location_id) {
        this.inside_location_id = inside_location_id;
    }

    @Column(name="loc_name")
    public String getLocName() {
        return loc_name;
    }

    public void setLocName(String loc_name) {
        this.loc_name = loc_name;
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
