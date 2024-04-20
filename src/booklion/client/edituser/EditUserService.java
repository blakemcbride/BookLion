
package booklion.client.edituser;

import booklion.client.utils.StandardReturn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * @author Blake McBride
 */
@RemoteServiceRelativePath("EditUserService")
public interface EditUserService extends RemoteService {
	
	PagingLoadResult<Record> getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec, 
			int totalLength, String searchText1, String searchText2);
	
	StandardReturn updateRecord(String uuid, Record rec);
	StandardReturn addRecord(String uuid, Record rec);
	StandardReturn deleteRecord(String uuid, Record rec);

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static EditUserServiceAsync instance;
		public static EditUserServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(EditUserService.class);
			}
			return instance;
		}
	}
}
