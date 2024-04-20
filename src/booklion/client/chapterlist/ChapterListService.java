package booklion.client.chapterlist;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 1/4/14
 */
@RemoteServiceRelativePath("ChapterListService")
public interface ChapterListService extends RemoteService {

    RecordReturn getRecords(String uuid, long bookId);

    /**
     * Utility/Convenience class.
     * Use ChapterListService.App.getInstance() to access static instance of ChapterListServiceAsync
     */
    public static class App {
        private static ChapterListServiceAsync ourInstance;

        public static ChapterListServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(ChapterListService.class);
            return ourInstance;
        }
    }
}
