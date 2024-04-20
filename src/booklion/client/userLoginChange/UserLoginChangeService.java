package booklion.client.userLoginChange;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * User: blake
 * Date: 5/7/14
 */
@RemoteServiceRelativePath("UserLoginChangeService")
public interface UserLoginChangeService extends RemoteService {

    Record getOldEmail(String uuid);
    StandardReturn saveNewEmail(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use UserLoginChangeService.App.getInstance() to access static instance of UserLoginChangeServiceAsync
     */
    public static class App {
        private static UserLoginChangeServiceAsync ourInstance = null;

        public static UserLoginChangeServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(UserLoginChangeService.class);
            return ourInstance;
        }
    }
}
