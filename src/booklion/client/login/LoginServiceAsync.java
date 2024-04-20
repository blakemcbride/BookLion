package booklion.client.login;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void login(String userID, String pw, AsyncCallback<UserData> callback);

    void newUser(String pw, UserRecord rec, AsyncCallback<StandardReturn> async);

    void forgotPassword(String pw, UserRecord rec, AsyncCallback<StandardReturn> async);

    void isLoggedIn(AsyncCallback<UserData> async);

    void logout(AsyncCallback<Void> async);

}
