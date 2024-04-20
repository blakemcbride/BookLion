package booklion.client.userLoginChange;

import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.PasswordField;

/**
 * @author Blake McBride
 * Date: 5/7/14
 */
public class UserLoginChange implements EntryPoint {

    private TextControl oldEmail;
    private PasswordField oldPassword;
    private TextControl newEmail1;
    private TextControl newEmail2;

    private static final String txt = "In BookLion, your email address and your login ID are the same.  In order to change your email address you must first enter your old (current) password and the new email address.  " +
            "BookLion will send an email to the new email address with a NEW machine generated password that will work with your new address. " +
            "Once you successfully login with your new email address and the machine generated password, you can then change your password to anything you like. " +
            "Your original email address and password will continue to work until the new email address has been activated.";


    public void onModuleLoad() {
        DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        VerticalLayout vp = new VerticalLayout();

        vp.addSpace("2em");

        Label ta = new Label();
        ta.setText(txt);
        vp.add(ta);

        vp.addSpace("2em");

        double lblRoom = 200;
        double ctlRoom = 200;

        oldEmail = vp.addTextField(lblRoom, ctlRoom, 3, 80, "Old Email / Login:");
        oldEmail.disable();
        oldPassword = vp.addPasswordField(lblRoom, ctlRoom, 7, 35, "Old Password:");
        newEmail1 = vp.addTextField(lblRoom, ctlRoom, 3, 80, "New Email / Login:");
        newEmail2 = vp.addTextField(lblRoom, ctlRoom, 3, 80, "New Email / Login (re-enter):");

        vp.addSpace("2em");

        TextButton saveBtn = new TextButton("Save Changes");
        saveBtn.addSelectHandler(new SaveData(panel));
        vp.add(saveBtn);

        SimplePanel sp = new SimplePanel();
        sp.setWidth("2em");
        panel.addWest(sp, 3);
        sp = new SimplePanel();
        sp.setWidth("2em");
        panel.addEast(sp, 3);

        panel.add(vp);

        panel.forceLayout();

        UserInput.disable();
        UserLoginChangeService.App.getInstance().getOldEmail(Information.getUserUuid(), new AsyncCallback<Record>() {
            @Override
            public void onFailure(Throwable caught) {
                UserInput.enable();
                String msg = caught.getMessage();
                if (msg == null)
                    Message.systemError("Error communicating with the server.");
                else
                    Message.systemError(msg);
            }

            @Override
            public void onSuccess(Record rec) {
                UserInput.enable();
                if (rec.getReturnCode() == 0) {
                    oldEmail.setText(rec.getOldEmail());
                } else
                    Message.msgOk(rec.getMsg());
            }
        });

    }

    private class SaveData extends EditRecordHandler {

        public SaveData(DockLayoutPanel dlg) {
            super(dlg);
        }

        @Override
        protected ErrorCheck errorCheck() {
            ErrorCheck ec = new ErrorCheck();
            String e1 = newEmail1.getText();
            if (e1 == null  ||  !e1.contains("@"))
                ec.add("Invalid email address.");
            String e2 = newEmail2.getText();
            if (e2 == null  ||  !e2.contains("@"))
                ec.add("Invalid email address.");
            if (!e1.equals(e2))
                ec.add("New email addresses must match.");
            String old = oldEmail.getText();
            if (old.equals(e1))
                ec.add("Old email address and new email address must be different.");
            return ec;
        }

        @Override
        protected void handleData() {
            Record rec = new Record();
            rec.setOldEmail(oldEmail.getText());
            rec.setOldPassword(StringUtils.encode(oldPassword.getText()));
            rec.setNewEmail(newEmail1.getText());
            UserInput.disable();
            UserLoginChangeService.App.getInstance().saveNewEmail(Information.getUserUuid(), rec, new AsyncCallback<StandardReturn>() {
                @Override
                public void onFailure(Throwable caught) {
                    UserInput.enable();
                    String msg = caught.getMessage();
                    if (msg == null)
                        Message.systemError("Error communicating with the server.");
                    else
                        Message.systemError(msg);
                }

                @Override
                public void onSuccess(StandardReturn rec) {
                    UserInput.enable();
                    oldPassword.setText("");
                    if (rec.getReturnCode() == 0)
                        Message.msgOk("An email has been sent to your new email address with a new password.  You can change your password once you login in.");
                    else
                        Message.msgOk(rec.getMsg());
                }
            });
        }
    }
}
