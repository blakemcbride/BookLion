package booklion.client.booklist;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public interface BooklistServiceAsync {
    void getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec,
                  int totalLength, String searchText1, String searchText2, AsyncCallback<PagingLoadResult<Record>> async);

    void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);
}
