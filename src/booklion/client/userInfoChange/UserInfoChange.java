package booklion.client.userInfoChange;

import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.Radio;

import java.util.Date;

import static booklion.client.utils.LayoutUtils.topCenter;

/**
 * @author Blake McBride
 * Date: 4/29/14
 */
public class UserInfoChange implements EntryPoint {

    private TextControl fname;
    private TextControl lname;
    private TextControl email;
    private PasswordField oldPw;
    private PasswordField newPw1;
    private PasswordField newPw2;
    private DateField birthDate;
    private RadioGroup sex;

    public void onModuleLoad() {
        DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        VerticalLayout vp = new VerticalLayout();

        vp.addSpace("2em");

        double lblRoom = 200;
        double ctlRoom = 150;

        fname = vp.addTextField(lblRoom, ctlRoom, 1, 20, "First name:");
        lname = vp.addTextField(lblRoom, ctlRoom, 1, 20, "Last name:");
        email = vp.addTextField(lblRoom, ctlRoom, 3, 80, "Email (login):");
        email.disable();
        birthDate =  vp.addDateField(lblRoom, ctlRoom, "Birth Date:");

        HorizontalLayout h = new HorizontalLayout();
        h.addLabel((int) (lblRoom / 2), "Sex:");
        sex = new RadioGroup();
        h.addRadio(sex, 'M', "Male");
        h.addRadio(sex, 'F', "Female");
        h.addRadio(sex, 'U', "Unspecified");
//        vp.add(h, new VerticalLayoutContainer.VerticalLayoutData(300, -1));
        vp.add(h);


        vp.addSpace("2em");

        vp.add(new Label("Only enter password information below if you wish to change your password."));
        vp.addSpace("1em");


        oldPw = vp.addPasswordField(lblRoom, ctlRoom, 0, 35, "Old password:");
        newPw1 = vp.addPasswordField(lblRoom, ctlRoom, 0, 35, "New password:");
        newPw2 = vp.addPasswordField(lblRoom, ctlRoom, 0, 35, "New password (re-enter):");

        vp.addSpace("2em");

        TextButton saveBtn = new TextButton("Save Changes");
        saveBtn.addSelectHandler(new SaveData(panel));
        vp.add(saveBtn);

        SimplePanel sp = new SimplePanel();
        sp.setWidth("2em");
        panel.addWest(sp, 3);

//        panel.add(topCenter(sp));

        panel.add(vp);

        panel.forceLayout();

        UserInput.disable();
        UserInfoChangeService.App.getInstance().getUserInfo(Information.getUserUuid(), new AsyncCallback<Record>() {
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
                    fname.setText(rec.getFname());
                    lname.setText(rec.getLname());
                    email.setText(rec.getEmail());
                    birthDate.setValue(DateUtils.toDate(rec.getBirthDate()));
                    sex.setGroupValue(rec.getSex());
                } else
                    Message.msgOk(rec.getMsg());
            }
        });
    }

    private class SaveData extends EditRecordHandler {

        public SaveData(DockLayoutPanel dlg) {
            super(dlg);
        }

        private boolean hasData(String x) {
            return x != null  &&  x.length() > 0;
        }

        private boolean goodPassword(String pw) {
            if (pw.length() < 7)
                return false;
            char [] chars = pw.toCharArray();
            int letters = 0;
            int numbers = 0;
            int special = 0;
            for (char c : chars)
                if (Character.isLetter(c))
                    letters++;
                else if (Character.isDigit(c))
                    numbers++;
                else
                    special++;
            return letters >= 1  &&  (numbers >= 1  ||  special >= 1);
        }

        @Override
        protected ErrorCheck errorCheck() {
            ErrorCheck ec = new ErrorCheck();
            Character s = (Character) sex.getGroupValue();
            if (s == null)
                ec.add("Please select one of the options under sex.");
            String add = email.getText();
            if (add == null  ||  !add.contains("@"))
                ec.add("Invalid email address.");
            Date bd = birthDate.getValue();
            if (bd != null) {
                int v = DateUtils.toInt(bd);
                int now = DateUtils.now();
                if (v != 0  &&  v < 19100101  ||  v >= now)
                    ec.add("Invalid birth date.");
            }
            String pw1 = oldPw.getText();
            String pw2 = newPw1.getText();
            String pw3 = newPw2.getText();
            if (hasData(pw1)  ||  hasData(pw2)  ||  hasData(pw3)) {
                if (!hasData(pw1)  ||  !hasData(pw2)  ||  !hasData(pw3))
                    ec.add("Password fields are not complete.");
                else if (!pw2.equals(pw3))
                    ec.add("New passwords do not match.");
                else if (!goodPassword(pw2))
                    ec.add("Passwords must be at least 7 characters long and contain at least one letter and at least one non-letter.");
            }
            return ec;
        }

        @Override
        protected void handleData() {
            Record rec = new Record();
            rec.setFname(fname.getText());
            rec.setLname(lname.getText());
            rec.setBirthDate(DateUtils.toInt(birthDate.getCurrentValue()));
            rec.setSex((Character)sex.getGroupValue());
            rec.setOldPassword(StringUtils.encode(oldPw.getText()));
            rec.setNewPassword(StringUtils.encode(newPw1.getText()));
            UserInput.disable();
            UserInfoChangeService.App.getInstance().saveUserInfo(Information.getUserUuid(), rec, new AsyncCallback<StandardReturn>() {
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
                    oldPw.setText("");
                    newPw1.setText("");
                    newPw2.setText("");
                    if (rec.getReturnCode() == 0)
                        Message.notice("Use data updated.");
                    else
                        Message.msgOk(rec.getMsg());
                }
            });

        }
    }
}
