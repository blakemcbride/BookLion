package booklion.client.edituser;

import java.util.List;

import booklion.client.global.Information;
import booklion.client.login.Login;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 */
public class EditUser extends SpecificCrud implements EntryPoint {

    protected TextControl searchField1;
    protected TextControl searchField2;
    private EditUserDialog editDlg;

    public void onModuleLoad() {
        editDlg = new EditUserDialog(this);
        setDoubleClickHandler(new EditData());

        Information.clearBookContext();
        SupervisorMain.updateMenu();

        run();
    }
	
	protected void configureGrid(FramedPanel cp) {
		cp.setHeadingText("Edit User");
//		cp.setPixelSize(Settings.width, Settings.height - 100);
//		cp.setSize("100%", "100%");
//		cp.setHeight(Settings.height - 100);
//		cp.setWidth("100%");
	}
	
	protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
		addLongColumn(collst, props.userId(), 125, "User ID");
		
//		addStringColumn(collst, props.email(), 300, "Email").setSortable(true);
		ColumnConfig<Record, String> col = addStringColumn(collst, props.email(), 300, "Email");
		col.setResizable(true);
		col.setFixed(false);
		col.setSortable(true);
		
		
		addStringColumn(collst, props.admin(), 75, "Admin");
		addStringColumn(collst, props.lname(), 150, "Last Name").setSortable(true);
		addStringColumn(collst, props.fname(), 150, "First Name");
	}

	protected void configureSearchPopup(Dialog dlg, VerticalLayout v) {
		int lblWidth = 100;
		int ctlRoom = 150;
		
		dlg.setSize("300", "150");
		searchField1 = v.addTextField(lblWidth, ctlRoom, 1, 20, "Search Field 1:");
		searchField2 =  v.addTextField(lblWidth, ctlRoom, 0, 20, "Search Field 2:");
	}
	
	protected void setupSearchPopup(final Dialog dlg) {
		searchField1.setText("");
		searchField2.setText("");
		String sortOrder = sortField();
		if (sortOrder == null  ||  "email".equals(sortOrder)) {
			dlg.setHeadingText("Search Email");
			Label lbl = searchField1.getLabel();
			lbl.setText("Email Address:");
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
        addButton("Add", new AddData());
        addButton("Edit", (SelectEvent.SelectHandler) getDoubleClickHandler());
        addButton("Delete", new DeleteData());
        addButton("Search", new SearchData());
        addButton("Show All", new ClearSearchData());
    }

    private class AddData implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            editDlg.show(EditDialog.NEWRECORD, null);
        }
    }

    private class EditData implements RowDoubleClickEvent.RowDoubleClickHandler, SelectEvent.SelectHandler {

        private void editData() {
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

        @Override
        public void onRowDoubleClick(RowDoubleClickEvent event) {
            editData();
        }

        @Override
        public void onSelect(SelectEvent event) {
            editData();
        }
    }

    private class DeleteData implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            int n = getNumSelectedRecords();
            if (n < 1)
                Message.msgOk("The record to be deleted must be selected first.");
                //		else if (n > 1)
                //			Message.msgOk("Only one record may be selected at a time.");
            else {
                ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected record"
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
    }

    private class SearchData implements SelectEvent.SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {
            setupSearchPopup(searchDlg);
            searchDlg.show();
        }
    }

    private class ClearSearchData implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            searchText1 = searchText2 = "";
            refreshFromBeginning();  // update grid from first record
        }
    }

}
