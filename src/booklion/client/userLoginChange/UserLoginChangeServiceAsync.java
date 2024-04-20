package booklion.client.userLoginChange;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: blake
 * Date: 5/7/14
 */
public interface UserLoginChangeServiceAsync {
    void getOldEmail(String uuid, AsyncCallback<Record> async);

    void saveNewEmail(String uuid, Record rec, AsyncCallback<StandardReturn> async);
}
