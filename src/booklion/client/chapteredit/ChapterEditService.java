package booklion.client.chapteredit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 2/17/14
 */
@RemoteServiceRelativePath("ChapterEditService")
public interface ChapterEditService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use ChapterListService.App.getInstance() to access static instance of ChapterListServiceAsync
     */
    public static class App {
        private static ChapterEditServiceAsync ourInstance;

        public static ChapterEditServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(ChapterEditService.class);
            return ourInstance;
        }
    }
}
