package booklion.client.genre;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 10/28/13
 */
public interface GenreServiceAsync {

    void loadGenre(String uuid, AsyncCallback<GenreItem[]> async);

    void addGenre(String uuid, String name, Integer parent, AsyncCallback<StandardReturn> async);

    void removeGenre(String uuid, int id, AsyncCallback<StandardReturn> async);

    void renameGenre(String uuid, int id, String newName, AsyncCallback<StandardReturn> async);
}
