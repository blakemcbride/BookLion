package booklion.client.booklist;

import booklion.client.bookmain.BookMain;
import booklion.client.global.Information;
import booklion.client.login.Login;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.List;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public class Booklist extends SpecificCrud implements EntryPoint {
    protected TextControl searchField1;
    protected TextControl searchField2;
    private BooklistDialog editDlg;

    public void onModuleLoad() {
        Information.clearBookContext();
        SupervisorMain.updateMenu();
        editDlg = new BooklistDialog(this);
        setDoubleClickHandler(new EnterBook());
        run(false);
        new KeyNav(getGrid()) {
            @Override
            public void onEnter(NativeEvent evt) {
                bookSelect();  //  when the user hits the enter key
            }
        };
    }

    protected void configureGrid(FramedPanel cp) {
        cp.setHeadingText("Book List");
//		cp.setPixelSize(Settings.width, Settings.height - 100);
//		cp.setSize("100%", "100%");
//		cp.setHeight(Settings.height - 100);
//		cp.setWidth("100%");
    }

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
//		addStringColumn(collst, props.email(), 300, "Email").setSortable(true);
        ColumnConfig<Record, String> col = addStringColumn(collst, props.bookTitle(), 350, "Title");
        col.setResizable(true);
        col.setFixed(false);
        col.setSortable(true);

        addStringColumn(collst, props.authorsName(), 200, "Author(s)");
        addStringColumn(collst, props.genreName(), 200, "Genre");
    }

    protected void configureSearchPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 100;
        int ctlRoom = 150;

        dlg.setSize("300", "150");
        searchField1 = v.addTextField(lblWidth, ctlRoom,  1, 80, "Search Field 1:");
        searchField2 =  v.addTextField(lblWidth, ctlRoom, 1, 80, "Search Field 2:");
    }

    protected void setupSearchPopup(final Dialog dlg) {
        searchField1.setText("");
        searchField2.setText("");
        String sortOrder = sortField();
        if (sortOrder == null  ||  "email".equals(sortOrder)) {
            dlg.setHeadingText("Book Search");
            Label lbl = searchField1.getLabel();
            lbl.setText("Book Title:");
            lbl = searchField2.getLabel();
            lbl.setVisible(false);
            searchField2.setVisible(false);

            searchField1.addKeyPressHandler(new KeyPressHandler() {
                @Override
                public void onKeyPress(KeyPressEvent event) {
                    if (ControlUtils.isEnterKey(event.getCharCode()))
                        dlg.getButtonById("OK").fireEvent(new SelectEvent());
                }
            });

        } else {
            dlg.setHeadingText("Search by Name");
            Label lbl = searchField1.getLabel();
            lbl.setText("Last Name:");
            lbl = searchField2.getLabel();
            lbl.setText("First Name:");
            lbl.setVisible(true);
            searchField2.setVisible(true);

            searchField1.addKeyPressHandler(new KeyPressHandler() {
                @Override
                public void onKeyPress(KeyPressEvent event) {
                    if (ControlUtils.isEnterKey(event.getCharCode()))
                        searchField2.focus();
                }
            });

            searchField2.addKeyPressHandler(new KeyPressHandler() {
                @Override
                public void onKeyPress(KeyPressEvent event) {
                    if (ControlUtils.isEnterKey(event.getCharCode()))
                        dlg.getButtonById("OK").fireEvent(new SelectEvent());
                }
            });
        }
        setInitialFocus(searchField1);
    }

    protected void setSearchFields() {
        String sortOrder = sortField();
        searchText1 = searchField1.getText();
        if (sortOrder != null  &&  "lname".equals(sortOrder))
            searchText2 = searchField2.getText();
    }

    protected void createButtons() {
    }

    public void bookAdd() {
        if (Message.notForGuest())
            return;
        editDlg.show(EditDialog.NEWRECORD, null);
    }

    public void bookEdit() {
        if (Message.notForGuest())
            return;
        int n = getNumSelectedRecords();
        if (n < 1)
            Message.msgOk("The record to be edited must be selected first.");
        else if (n > 1)
            Message.msgOk("Only one record may be selected at a time.");
        else {
            Record rec = getSelectedRecord();
            editDlg.show(EditDialog.UPDATERECORD, rec);
        }
    }

    public void bookDelete() {
        if (Message.notForGuest())
            return;
        int n = getNumSelectedRecords();
        if (n < 1)
            Message.msgOk("The book to be deleted must be selected first.");
            //		else if (n > 1)
            //			Message.msgOk("Only one record may be selected at a time.");
        else {
            ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected book"
                    + (n > 1 ? "s?" : "?"));
            final HideEvent.HideHandler hideHandler = new HideEvent.HideHandler() {
                @Override
                public void onHide(HideEvent event) {
                    Dialog btn = (Dialog) event.getSource();
                    String btnLbl = btn.getHideButton().getText();
                    if (btnLbl.equals("Yes")) {
                        List<Record> recs = getSelectedRecords();
                        totalDeletes = numDeletesLeft = recs.size();
                        successfulDeletes = 0;
                        for (Record rec : recs)
                            deleteRecordInt(Login.getUUID(), rec);
                    }
                }
            };

            box.addHideHandler(hideHandler);
            box.show();
        }
    }

    public void bookSelect() {
        int n = getNumSelectedRecords();
        if (n < 1)
            Message.msgOk("The book to be entered must be selected in the list above first.");
        else if (n > 1)
            Message.msgOk("Only one book may be entered at a time.  Please select only one book above.");
        else {
            Record rec = getSelectedRecord();
            Information.setBook(rec);
            SupervisorMain.setBookHeader();
            SupervisorMain.updateMenu();
            new BookMain().onModuleLoad();
        }
    }

    private class EnterBook implements RowDoubleClickEvent.RowDoubleClickHandler, SelectEvent.SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {
            bookSelect();
        }

        @Override
        public void onRowDoubleClick(RowDoubleClickEvent event) {
            bookSelect();
        }
    }

    public void bookSearch() {
        setupSearchPopup(searchDlg);
        searchDlg.show();
    }

    public void bookShowAll() {
        searchText1 = searchText2 = "";
        refreshFromBeginning();  // update grid from first record
    }

}
