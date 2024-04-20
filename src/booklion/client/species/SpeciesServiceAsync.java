package booklion.client.species;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 1/10/14
 */
public interface SpeciesServiceAsync {
    void getRecords(String uuid, long bookId, AsyncCallback<RecordReturn> async);

}
