package booklion.client.utils;

import booklion.client.global.Information;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * @author Blake McBride
 * Date: 2/3/14
 */
public class Message {

    /**
     * Displays a momentary popup message in the upper right corner.
     *
     * Used to provide unimportant information to the user.
     *
     * @param txt
     */
    public static void notice(String txt) {
        Info.display("Notice", txt);
    }

    /**
     * Produces a modal popup window that displays msg and an OK button.
     * Waits for the user to clock OK and then it returns.<br/>
     * &lt;br&gt; can be used to break lines.
     *
     * Used for normal user messages in which the user is given time to read the message.
     *
     * @param msg the message
     */
    public static void msgOk(String msg) {
        MessageBox mb = new MessageBox("");
        mb.setPredefinedButtons(Dialog.PredefinedButton.OK);
        mb.setBodyBorder(true);  // puts a thin blue border around the message
        mb.setBorders(true);  //  puts a thin blue border around the whole popup
        mb.setBodyStyle("backgroundColor:white");
        mb.setMessage(msg);
        mb.setWidth("2.5in");
        mb.setModal(true);
        mb.show();
    }

    /**
     * If is guest, display message and return true.
     * Return false if not a guest.
     *
     * @return
     */
    public static boolean notForGuest() {
        if (Information.isGuest()) {
            msgOk("This operation can only be performed by registered users.  There is no charge to become a registered user.  You can become a registered user from the initial login screen by clicking on Create New Login (just logout to get back to that screen).");
            return true;
        }
        return false;
    }

    /**
     * Displays a momentary popup message in the upper right corner.
     *
     * @param txt
     */
    public static void error(String txt) {
        Info.display("Error", txt);
    }

    /**
     * Displays an alert modal dialog in middle of screen with message and OK button.
     * Appearance makes it good for system error messages.
     *
     * @param msg
     */
    public static void systemError(String msg) {
        Window.alert(msg);
    }

    private final static AutoProgressMessageBox waitBox = new AutoProgressMessageBox("Progress", "Loading, please wait...");

    public static void loadingMsg() {
        waitBox.setProgressText("Loading; Please wait...");
        waitBox.auto();
        waitBox.setModal(true);
        waitBox.show();
    }

    public static void loadingMsg(String msg) {
        waitBox.setProgressText(msg);
        waitBox.auto();
        waitBox.setModal(true);
        waitBox.show();
    }

    public static boolean loadDone() {
        waitBox.hide();
        return false;  //  false means you get the message each time, true means only the first
    }

    public static void setBackButtonMessage(final String msg) {
        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(Window.ClosingEvent event) {
                event.setMessage(msg);
            }
        });
    }

    public static void deleteBackButtonMessage() {
        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(Window.ClosingEvent event) {
                event.setMessage(null);
            }
        });
    }

}
