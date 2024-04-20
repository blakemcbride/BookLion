
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
@Table(name="species_log_h")
public class SpeciesLogH implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SpeciesLogH.class);

    private long history_id;
    private long species_log_id;
    private long species_id;
    private long chapter_id;
    private String description;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;
    private String text_description;

    
    public SpeciesLogH() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="species_log_h_history_id_seq")
    @SequenceGenerator(name="species_log_h_history_id_seq", sequenceName="species_log_h_history_id_seq", allocationSize=1)
    @Column(name="history_id")
	public long getHistoryId() {
		return history_id;
	}

	public void setHistoryId(long history_id) {
		this.history_id = history_id;
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

    @Column(name="species_id")
    public long getSpeciesId() {
        return species_id;
    }

    public void setSpeciesId(long species_id) {
        this.species_id = species_id;
    }

    @Column(name="species_log_id")
    public long getSpeciesLogId() {
        return species_log_id;
    }

    public void setSpeciesLogId(long species_log_id) {
        this.species_log_id = species_log_id;
    }

    @Column(name="text_description")
    public String getTextDescription() {
        return text_description;
    }

    public void setTextDescription(String text_description) {
        this.text_description = text_description;
    }

}
