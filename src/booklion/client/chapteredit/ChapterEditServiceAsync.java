package booklion.client.chapteredit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 2/17/14
 */
public interface ChapterEditServiceAsync {
    void getRecords(String uuid, long bookId, AsyncCallback<RecordReturn> async);

    void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

}
