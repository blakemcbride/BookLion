package booklion.client.login;

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
 * Date: 4/22/14
 */
public class ForgotPassword {

    private Dialog dlg;
    private TextControl email;

    public ForgotPassword() {

        dlg = new Dialog();
        dlg.setHeadingText("Forgot Password");
        dlg.setModal(true);
        dlg.setPredefinedButtons(Dialog.PredefinedButton.CANCEL, Dialog.PredefinedButton.OK);
        dlg.setBodyStyleName("background: none; padding: 5px");
        TextButton cancelBtn = dlg.getButtonById(Dialog.PredefinedButton.CANCEL.name());
        TextButton okBtn = dlg.getButtonById(Dialog.PredefinedButton.OK.name());
        okBtn.addSelectHandler(new LocalEditRecordHandler(dlg, this));
        cancelBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                dlg.hide();
            }
        });

        int lblWidth = 90;

        dlg.setSize("340", "150");

        VerticalLayout v = new VerticalLayout();
        v.setPosition(5, 10);
        v.add(new Label("Please enter the information below.  An email with a NEW password will be sent to the email address you provide."));
        SimplePanel sp = new SimplePanel();
        sp.setHeight("1em");
        v.add(sp);
        email =  v.addTextField(lblWidth, 200, 3, 80, "Email:");

        dlg.add(v);

        setInitialFocus(email);

        dlg.show();
    }

    private final static String f1 = "qvvrt";

    private class LocalEditRecordHandler extends EditRecordHandler {

        private Dialog dlg;
        private ForgotPassword newUser;
        private final static String f2 = "55227";

        public LocalEditRecordHandler(Dialog dlg, ForgotPassword newUser) {
            super(dlg);
            this.dlg = dlg;
            this.newUser = newUser;
        }

        @Override
        protected ErrorCheck errorCheck() {
            ErrorCheck ec = new ErrorCheck();
            String add = email.getText();
            if (add == null  ||  !add.contains("@"))
                ec.add("Invalid email address.");
            return ec;
        }

        @Override
        protected void handleData() {
            String f3 = f1;
            UserRecord rec = new UserRecord();
            rec.setEmail(email.getText());

            f3 = f3 + "#$";

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
                        Message.msgOk("Your email has been sent.  Please see it for further instructions.");
                    else
                        Message.msgOk(result.getMsg());
                }
            };
            f3 = f3 + f2;
            UserInput.disable();
            LoginService.Util.getInstance().forgotPassword(f3, rec, callback);
        }
    }

}
