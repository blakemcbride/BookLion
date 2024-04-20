package booklion.client.authorselect;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
@RemoteServiceRelativePath("AuthorSelectService")
public interface AuthorSelectService extends RemoteService {

    RecordReturn getRecords(String uuid, String lname);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use AuthorSelectService.App.getInstance() to access static instance of AuthorSelectServiceAsync
     */
    public static class App {
        private static AuthorSelectServiceAsync ourInstance;

        public static AuthorSelectServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(AuthorSelectService.class);
            return ourInstance;
        }
    }
}
