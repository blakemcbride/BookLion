package booklion.client.supervisormain;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 9/5/14
 */
public interface SupervisorMainServiceAsync {
    void sendSupportRequest(String uuid, String subject, String msg, AsyncCallback<StandardReturn> async);
}
