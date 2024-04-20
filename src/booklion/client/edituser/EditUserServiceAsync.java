package booklion.client.edituser;

import booklion.client.utils.StandardReturn;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * @author Blake McBride
 */
public interface EditUserServiceAsync {

	void getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec, int totalLength, 
			String searchText1, String searchText2,
			AsyncCallback<PagingLoadResult<Record>> callback);

	void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback);

	void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback);

	void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback);

}
