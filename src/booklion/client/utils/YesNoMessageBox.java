package booklion.client.utils;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;

/**
 * @author Blake McBride
 * Date: 3/8/14
 */
public abstract class YesNoMessageBox extends ConfirmMessageBox {
    /**
     * Creates a message box that prompts for confirmation with YES and NO
     * buttons.
     *
     * @param message the message that appears in the message box
     */
    public YesNoMessageBox(String message) {
        super("Confirm", message);
        addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (getHideButton() == getButtonById(Dialog.PredefinedButton.YES.name())) {
                    yes();
                } else if (getHideButton() == getButtonById(Dialog.PredefinedButton.NO.name())) {
                    no();
                }
            }
        });
        setModal(true);
        show();
    }

    public abstract void yes();

    public abstract void no();

}
