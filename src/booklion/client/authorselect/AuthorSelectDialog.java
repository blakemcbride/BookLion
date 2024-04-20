package booklion.client.authorselect;

import booklion.client.utils.*;
import com.sencha.gxt.widget.core.client.Dialog;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
class AuthorSelectDialog extends SpecificDialog {
    private long authorId;
    private TextControl lname;
    private TextControl fame;

    public AuthorSelectDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 120;

        dlg.setSize("340", "140");
        lname = v.addTextField(lblWidth, 200, 1, 20, "Last (or only) Name:");
        fame = v.addTextField(lblWidth, 200, 0, 80, "First Name, Middle:");
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();
        return ec;
    }

    public String initAddPopup() {
        authorId = 0;
        lname.setText("");
        fame.setText("");
        setInitialFocus(lname);
        return "Add New Author";
    }

    public String getData(Record rec) {
        authorId = rec.getAuthorId();
        lname.setText(rec.getLname());
        fame.setText(rec.getFname());
        setInitialFocus(lname);
        return "Edit Author";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setAuthorId(authorId);
        rec.setLname(lname.getText());
        rec.setFname(fame.getText());
    }

}
