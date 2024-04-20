package booklion.client.speciesedit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
@RemoteServiceRelativePath("SpeciesEditService")
public interface SpeciesEditService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use SpeciesEditService.App.getInstance() to access static instance of SpeciesEditServiceAsync
     */
    public static class App {
        private static SpeciesEditServiceAsync ourInstance;

        public static SpeciesEditServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(SpeciesEditService.class);
            return ourInstance;
        }
    }
}
