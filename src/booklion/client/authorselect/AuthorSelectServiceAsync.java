package booklion.client.authorselect;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public interface AuthorSelectServiceAsync {
    void getRecords(String uuid, String lname, AsyncCallback<RecordReturn> async);

    void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);
}
