package booklion.client.authorselect;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public class Record implements Serializable {

    private long authorId;
    private String lname;
    private String fname;

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
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

    public String getName() {
        if (fname == null  ||  fname.isEmpty())
            return lname;
        return lname + ", " + fname;
    }

    public void setName(String name) {
    }

    public Record copy() {
        Record ret = new Record();
        ret.authorId = this.authorId;
        ret.lname = this.lname;
        ret.fname = this.fname;
        return ret;
    }

}
