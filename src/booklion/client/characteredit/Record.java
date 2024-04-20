package booklion.client.characteredit;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
public class Record implements Serializable {

    private long bookCharacterId;
    private long chapterId;  // The chapter the character was introduced in
    private long speciesId;
    private String lname;
    private String fname;
    private String suffix;
    private String nickname;
    private String gender;
    private String relevance;
    private String affiliation;
    private String occupation;
    private String relationship;
    private String speciesName;
    private LogRecord [] logs;

    public long getBookCharacterId() {
        return bookCharacterId;
    }

    public void setBookCharacterId(long bookCharacterId) {
        this.bookCharacterId = bookCharacterId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(long speciesId) {
        this.speciesId = speciesId;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesDisplayName() {
        String s = speciesName;
        if (s != null  &&  s.equalsIgnoreCase("Human"))
            return " ";
        return s;
    }

    private boolean isEmpty(String s) {
        return s == null  ||  s.isEmpty();
    }

    public String getName() {
        String name;
        if (isEmpty(lname))
            name = "";
        else
            name = lname;
        if (!isEmpty(fname))
            if (!isEmpty(name))
                name += ", " + fname;
            else
                name = fname;
        if (!isEmpty(suffix))
            if (!isEmpty(name))
                name += " " + suffix;
        if (!isEmpty(nickname))
            if (!isEmpty(name))
                name += " (" + nickname + ")";
            else
                name = nickname;
        return name;
    }

    public String getPriority() {
        if ("P".equals(relevance))
            return "Primary";
        else if ("S".equals(relevance))
            return "Secondary";
        else return "_";
    }

    public LogRecord[] getLogs() {
        return logs;
    }

    public void setLogs(LogRecord[] logs) {
        this.logs = logs;
    }

    public Record copy() {
        Record ret = new Record();
        ret.bookCharacterId = this.bookCharacterId;
        ret.chapterId = this.chapterId;
        ret.speciesId = this.speciesId;
        ret.lname = this.lname;
        ret.fname = this.fname;
        ret.suffix = this.suffix;
        ret.nickname = this.nickname;
        ret.gender = this.gender;
        ret.relevance = this.relevance;
        ret.affiliation = this.affiliation;
        ret.occupation = this.occupation;
        ret.relationship = this.relationship;
        ret.speciesName = this.speciesName;
        ret.logs = this.logs;
        return ret;
    }

}
