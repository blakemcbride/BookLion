package booklion.client.locationedit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
@RemoteServiceRelativePath("LocationEditService")
public interface LocationEditService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use LocationEditService.App.getInstance() to access static instance of LocationEditServiceAsync
     */
    public static class App {
        private static LocationEditServiceAsync ourInstance;

        public static LocationEditServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(LocationEditService.class);
            return ourInstance;
        }
    }
}
