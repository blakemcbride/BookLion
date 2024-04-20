package booklion.client.utils;

import booklion.client.global.Information;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;

/**
 * @author Blake McBride
 * Date: 3/8/14
 */
public abstract class ExecuteIfNotDirty  extends ConfirmMessageBox {
    /**
     * Creates a message box that prompts for confirmation with YES and NO
     * buttons.
     *
     * @param message the message that appears in the message box
     */
    public ExecuteIfNotDirty(String message) {
        super("Confirm", message);
        if (!Information.isDirty()) {
            execute();
            return;
        }
        addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (getHideButton() == getButtonById(Dialog.PredefinedButton.YES.name())) {
                    Information.setDirty(false);
                    execute();
                } else if (getHideButton() == getButtonById(Dialog.PredefinedButton.NO.name())) {

                }
            }
        });
        show();
    }

    public abstract void execute();

}
