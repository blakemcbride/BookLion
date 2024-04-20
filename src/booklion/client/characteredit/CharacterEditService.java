package booklion.client.characteredit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
@RemoteServiceRelativePath("CharacterEditService")
public interface CharacterEditService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use CharacterEditService.App.getInstance() to access static instance of CharacterEditServiceAsync
     */
    public static class App {
        private static CharacterEditServiceAsync ourInstance;

        public static CharacterEditServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(CharacterEditService.class);
            return ourInstance;
        }
    }
}
