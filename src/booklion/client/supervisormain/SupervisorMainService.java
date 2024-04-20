package booklion.client.supervisormain;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author Blake McBride
 * Date: 9/5/14
 */
@RemoteServiceRelativePath("SupervisorMainService")
public interface SupervisorMainService extends RemoteService {

    StandardReturn sendSupportRequest(String uuid, String subject, String msg);
    /**
     * Utility/Convenience class.
     * Use SupervisorMainService.App.getInstance() to access static instance of SupervisorMainServiceAsync
     */
    public static class App {
        private static SupervisorMainServiceAsync ourInstance = null;

        public static SupervisorMainServiceAsync getInstance() {
            if (ourInstance == null)
                ourInstance = GWT.create(SupervisorMainService.class);
            return ourInstance;
        }
    }
}
