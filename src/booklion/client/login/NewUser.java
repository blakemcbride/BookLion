package booklion.client.login;

import booklion.client.global.Information;
import booklion.client.utils.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.Date;

import static booklion.client.utils.ControlUtils.setInitialFocus;


/**
 * @author Blake McBride
 * Date: 4/9/14
 */
public class NewUser {

    private Dialog dlg;
    private TextControl fname;
    private TextControl lname;
    private TextControl email;
    private DateField birthDate;
    private RadioGroup sex;

    public NewUser() {
        dlg = new Dialog();
        dlg.setHeadingText("New User");
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

        dlg.setSize("340", "290");

        VerticalLayout v = new VerticalLayout();
        v.setPosition(5, 10);
        v.add(new Label("There is no charge or credit card needed to use BookLion.  Please enter the information below.  An email with your password will be sent to the email address you provide."));
        SimplePanel sp = new SimplePanel();
        sp.setHeight("1em");
        v.add(sp);
        fname =  v.addTextField(lblWidth, 200, 1, 20, "First Name:");
        lname =  v.addTextField(lblWidth, 200, 1, 20, "Last Name:");
        email =  v.addTextField(lblWidth, 200, 3, 80, "Email:");
        birthDate =  v.addDateField(lblWidth, 105, "Birth Date:");

        HorizontalLayout h = new HorizontalLayout();
        h.addLabel(lblWidth/2, "Sex:");
        sex = new RadioGroup();
        h.addRadio(sex, 'M', "Male");
        h.addRadio(sex, 'F', "Female");
        h.addRadio(sex, 'U', "Unspecified");
        v.add(h);

        dlg.add(v);

        setInitialFocus(fname);

        dlg.show();
    }

    private final static String f1 = "qvvrt";

    static String part2() {
        return "ego";
    }

    private class LocalEditRecordHandler extends EditRecordHandler {

        private Dialog dlg;
        private NewUser newUser;
        private final static String f2 = "55227";

        public LocalEditRecordHandler(Dialog dlg, NewUser newUser) {
            super(dlg);
            this.dlg = dlg;
            this.newUser = newUser;
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
            return ec;
        }

        @Override
        protected void handleData() {
            String f3 = f1;
            UserRecord rec = new UserRecord();
            rec.setFname(fname.getText());
            rec.setLname(lname.getText());
            rec.setEmail(email.getText());
            Date bd = birthDate.getValue();
            f3 = f3 + "#$";
            if (bd == null)
                rec.setBirthDate(0);
            else
                rec.setBirthDate(DateUtils.toInt(bd));
            rec.setSex((Character) sex.getGroupValue());

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
            LoginService.Util.getInstance().newUser(f3, rec, callback);
        }
    }
}
