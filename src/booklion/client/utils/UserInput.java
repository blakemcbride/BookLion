package booklion.client.utils;

import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Blake McBride
 * Date: 5/15/14
 */
public class UserInput {

    private static PopupPanel panel;
    private static int levels = 0;

    /**
     * Stop user input
     */
    public static void disable() {
        if (panel == null) {
            panel = new PopupPanel();
            panel.setModal(true);
            panel.setVisible(false);
        }
        panel.show();
        levels++;
    }

    /**
     * re-start/enable user input
     */
    public static void enable() {
        if (levels > 0)
            levels--;
        if (panel == null  ||  levels != 0)
            return;
        panel.hide();
    }
}
