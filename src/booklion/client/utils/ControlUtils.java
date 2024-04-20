package booklion.client.utils;

import com.google.gwt.core.client.Scheduler;
import com.sencha.gxt.widget.core.client.Component;

/**
 * @author Blake McBride
 * Date: 11/6/13
 */
public class ControlUtils {

    public static void setInitialFocus(final Component w) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                w.focus();
            }
        });
    }

    public static void setInitialFocus(final TextAreaControl w) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                w.setFocus(true);
            }
        });
    }


    public static boolean isEnterKey(char c) {
        // Firefox 27.0 returns 0 when the enter key is pressed.  All others return '\r'
        return c == '\r'  ||  c == 0;
    }

}
