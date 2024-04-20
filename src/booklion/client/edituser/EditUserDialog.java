package booklion.client.edituser;

import booklion.client.utils.*;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 11/3/13
 */
public class EditUserDialog extends SpecificDialog {

    private TextControl userID;
    private TextControl password;
    private TextControl fname;
    private TextControl lname;
    private TextControl email;
    private DateField birthDate;
    private CheckBox admin;
    private RadioGroup sex;
    private TextControl newEmail;
    private TextControl newPassword;
    private RadioGroup userStatus;

    public EditUserDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 90;
        int ctlRoom = 100;

        dlg.setSize("340", "400");
        userID = v.addTextField(lblWidth, ctlRoom, 0, 15, "User ID:");
        userID.setEnabled(false);
        email =  v.addTextField(lblWidth, 200, 3, 80, "Email:");
        password = v.addTextField(lblWidth, ctlRoom, 0, 35, "Password:");
        fname =  v.addTextField(lblWidth, 200, 1, 20, "First Name:");
        lname =  v.addTextField(lblWidth, 200, 1, 20, "Last Name:");
        birthDate =  v.addDateField(lblWidth, ctlRoom, "Birth Date:");

        HorizontalLayout h = new HorizontalLayout();
        h.addLabel(lblWidth/2, "Sex:");
        sex = new RadioGroup();
        h.addRadio(sex, 'M', "Male");
        h.addRadio(sex, 'F', "Female");
        h.addRadio(sex, 'U', "Unknown");
        v.add(h);
        admin = v.addCheckBox(lblWidth, 13, "Administrator:");
        newEmail =  v.addTextField(lblWidth, 200, 0, 80, "New Email:");
        newPassword = v.addTextField(lblWidth, ctlRoom, 0, 35, "New Password:");

        h = new HorizontalLayout();
        h.addLabel(lblWidth/2, "Status:");
        userStatus = new RadioGroup();
        h.addRadio(userStatus, "A", "Active");
        h.addRadio(userStatus, "D", "Deleted");
        h.addRadio(userStatus, "B", "Banned");
        v.add(h);
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();

        if (mode == NEWRECORD)
            ec.checkTextField(password, 1, 35, "Password");
        else
            ec.checkTextField(password, 0, 35, "Password");
//		ec.checkTextField(fname, 1, 20, "First name");
//        ec.checkTextField(lname, 1, 20, "Last name");
//        ec.checkTextField(email, 3, 80, "Email");

        return ec;
    }

    public String initAddPopup() {
        userID.setText("");
        password.setText("");
        fname.setText("");
        lname.setText("");
        email.setText("");
        birthDate.setValue(null);
        sex.setGroupValue('U');
        admin.setValue(false);
        newEmail.setText("");
        newPassword.setText("");
        userStatus.setGroupValue(" ");
        setInitialFocus(email);
        return "Add User";
    }

    public String getData(Record rec) {
        userID.setText(rec.getUserId() + "");
//		password.setText(rec.getPassword());
        password.setText("");
        fname.setText(rec.getFname());
        lname.setText(rec.getLname());
        email.setText(rec.getEmail());
        birthDate.setValue(DateUtils.toDate(rec.getBirthDate()));
        admin.setValue("Y".equals(rec.getAdmin()));
        sex.setGroupValue(rec.getSex());
        newEmail.setText(rec.getNewEmail());
        newPassword.setText("");
        userStatus.setGroupValue(rec.getUserStatus());
        setInitialFocus(email);
        return "Edit User";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        String pw = password.getText();
        if (pw != null  &&  pw.length() > 0)
            rec.setPassword(pw);
        rec.setFname(fname.getText());
        rec.setLname(lname.getText());
        rec.setEmail(email.getText());

        rec.setBirthDate(DateUtils.toInt(birthDate.getValue()));

        rec.setSex((Character) sex.getGroupValue());

        rec.setAdmin(admin.getValue() ? "Y" : "N");
        rec.setNewEmail(newEmail.getText());
        pw = newPassword.getText();
        if (pw != null  &&  pw.length() > 0)
            rec.setNewPassword(pw);
        rec.setUserStatus((String) userStatus.getGroupValue());
    }

}
