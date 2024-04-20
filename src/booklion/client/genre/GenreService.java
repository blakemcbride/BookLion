package booklion.client.genre;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 10/28/13
 */
@RemoteServiceRelativePath("GenreService")
public interface GenreService extends RemoteService {

    public GenreItem [] loadGenre(String uuid);

    public StandardReturn addGenre(String uuid, String name, Integer parent);

    public StandardReturn removeGenre(String uuid, int id);

    public StandardReturn renameGenre(String uuid, int id, String newName);

    /**
     * Utility/Convenience class.
     * Use GenreService.App.getInstance() to access static instance of GenreServiceAsync
     */
    public static class App {
        private static GenreServiceAsync ourInstance;

        public static GenreServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(GenreService.class);
            return ourInstance;
        }
    }
}
