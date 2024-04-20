
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
@Table(name="book_character_h")
public class BookCharacterH implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BookCharacterH.class);

    private long history_id;
    private long book_character_id;
    private long chapter_id;
    private long species_id;
    private String lname;
    private String fname;
    private String suffix;
    private String nickname;
    private String gender;
    private String relevance;
    private String affiliation;
    private String occupation;
    private String relationship;
    private Timestamp record_change_date;
    private String record_change_type;
    private long record_user_id;

    
    public BookCharacterH() {}

    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="book_character_h_history_id_seq")
    @SequenceGenerator(name="book_character_h_history_id_seq", sequenceName="book_character_h_history_id_seq", allocationSize=1)
    @Column(name="history_id")
	public long getHistoryId() {
		return history_id;
	}

	public void setHistoryId(long history_id) {
		this.history_id = history_id;
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

    @Column(name="suffix")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Column(name="nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name="gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name="relevance")
    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    @Column(name="affiliation")
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Column(name="occupation")
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Column(name="relationship")
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

 
}
