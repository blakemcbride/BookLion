package booklion.client.userInfoChange;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 4/29/14
 */
@RemoteServiceRelativePath("UserInfoChangeService")
public interface UserInfoChangeService extends RemoteService {

    Record getUserInfo(String uuid);
    StandardReturn saveUserInfo(String uuid, Record rec);

    /**
     * Utility/Convenience class.
     * Use UserInfoChangeService.App.getInstance() to access static instance of UserInfoChangeServiceAsync
     */
    public static class App {
        private static UserInfoChangeServiceAsync ourInstance = null;

        public static UserInfoChangeServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(UserInfoChangeService.class);
            return ourInstance;
        }
    }
}
