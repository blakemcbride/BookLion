package booklion.client.genre;

import java.io.Serializable;

/**
 * @author Blake McBride
 * Date: 10/28/13
 */
public class GenreItem implements Serializable {

    private long id;
    private Integer parent_id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parent_id;
    }

    public void setParentId(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
