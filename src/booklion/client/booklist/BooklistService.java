package booklion.client.booklist;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
@RemoteServiceRelativePath("BooklistService")
public interface BooklistService extends RemoteService {

    PagingLoadResult<Record> getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec,
                                      int totalLength, String searchText1, String searchText2);

    StandardReturn updateRecord(String uuid, Record rec);
    StandardReturn addRecord(String uuid, Record rec);
    StandardReturn deleteRecord(String uuid, Record rec);
    /**
     * Utility/Convenience class.
     * Use BooklistService.Util.getInstance() to access static instance of BooklistServiceAsync
     */
//    public static class Util {
//        private static final BooklistServiceAsync ourInstance = (BooklistServiceAsync) GWT.create(BooklistService.class);
//
//        public static BooklistServiceAsync getInstance() {
//            return ourInstance;
//        }
//    }


    public static class Util {
        private static BooklistServiceAsync instance;
        public static BooklistServiceAsync getInstance(){
            if (instance == null) {
                instance = GWT.create(BooklistService.class);
            }
            return instance;
        }
    }
}
