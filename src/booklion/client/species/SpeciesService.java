package booklion.client.species;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 1/10/14
 */
@RemoteServiceRelativePath("SpeciesService")
public interface SpeciesService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);

    /**
     * Utility/Convenience class.
     * Use SpeciesService.App.getInstance() to access static instance of SpeciesServiceAsync
     */
    public static class App {
        private static final SpeciesServiceAsync ourInstance = (SpeciesServiceAsync) GWT.create(SpeciesService.class);

        public static SpeciesServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
