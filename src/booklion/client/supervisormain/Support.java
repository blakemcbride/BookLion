package booklion.client.supervisormain;

import booklion.client.global.Information;
import booklion.client.utils.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;


import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 9/5/14
 */
public class Support {

    private static Dialog dlg;
    private static TextControl subject;
    private static TextAreaEx message;

    static void support() {
        if (Information.isGuest()) {
            Message.msgOk("Support is only available to registered users.  Please create a login and then submit your support request.");
            return;
        }
        dlg = new Dialog();
        dlg.setHeadingText("Support Request");
        dlg.setModal(true);
        dlg.setPredefinedButtons(Dialog.PredefinedButton.CANCEL, Dialog.PredefinedButton.OK);
        dlg.setBodyStyleName("background: none; padding: 5px");
        TextButton cancelBtn = dlg.getButtonById(Dialog.PredefinedButton.CANCEL.name());
        TextButton okBtn = dlg.getButtonById(Dialog.PredefinedButton.OK.name());
        okBtn.addSelectHandler(new LocalEditRecordHandler(dlg));
        cancelBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                dlg.hide();
            }
        });

        int lblWidth = 50;

        dlg.setSize("340", "290");

        VerticalLayout v = new VerticalLayout();
        v.setPosition(5, 10);
        v.add(new Label("Please report your request here.  An email response will be sent to your registered email address."));
        SimplePanel sp = new SimplePanel();
        sp.setHeight("1em");
        v.add(sp);
        subject =  v.addTextField(lblWidth, 250, 1, 80, "Subject:");
        v.add(new Label("Details"));
        message = new TextAreaEx();
        message.setSize("300em", "150em");
        v.add(message);

        dlg.add(v);

        setInitialFocus(subject);

        dlg.show();

    }

    private static class LocalEditRecordHandler extends EditRecordHandler {

        private Dialog dlg;

        public LocalEditRecordHandler(Dialog dlg) {
            super(dlg);
            this.dlg = dlg;
        }

        @Override
        protected ErrorCheck errorCheck() {
            ErrorCheck ec = new ErrorCheck();
            return ec;
        }

        @Override
        protected void handleData() {

            AsyncCallback<StandardReturn> callback = new AsyncCallback<StandardReturn>() {

                @Override
                public void onFailure(Throwable caught) {
                    UserInput.enable();
                    String msg = caught.getMessage();
                    Information.clearAll();
                    if (msg == null)
                        Message.systemError("Error communicating with the server.");
                    else
                        Message.systemError(msg);
                }

                @Override
                public void onSuccess(StandardReturn result) {
                    UserInput.enable();
                    if (result.getReturnCode() == 0)
                        Message.msgOk("Your support request has been received.  You will receive a response via your registered email account.");
                    else
                        Message.msgOk(result.getMsg());
                }
            };
            UserInput.disable();
            SupervisorMainService.App.getInstance().sendSupportRequest(Information.getUserUuid(), subject.getText(), message.getText(), callback);
        }
    }

}
