package booklion.client.userInfoChange;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 4/29/14
 */
public interface UserInfoChangeServiceAsync {
    void saveUserInfo(String uuid, Record rec, AsyncCallback<StandardReturn> async);

    void getUserInfo(String uuid, AsyncCallback<Record> async);
}
