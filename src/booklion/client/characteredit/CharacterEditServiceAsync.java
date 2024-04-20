package booklion.client.characteredit;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
public interface CharacterEditServiceAsync {
    void getRecords(String uuid, long bookId, AsyncCallback<RecordReturn> async);

    void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> async);
}
