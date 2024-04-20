package booklion.client.authorselect;

import booklion.client.login.Login;
import booklion.client.utils.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.List;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public class AuthorList extends SpecificCrud {
    private Dialog dlg;
    private AuthorSelectServiceAsync service = AuthorSelectService.App.getInstance();
    private TextControl lname = new TextControl(0, 300, "searchLname");
    private AuthorSelectDialog editDlg;
    private SelectData selectData = new SelectData();
    private IUpdate updateRec;

    public AuthorList(IUpdate updateRec) {
        final int topMargin = 10;

        this.updateRec = updateRec;
        dlg = new Dialog();
        dlg.setHeadingText("Author Selection");
        dlg.setSize("400", "400");
        dlg.setModal(true);
        dlg.setBodyStyleName("background: none; padding: 5px");
        dlg.setPredefinedButtons();

        DockLayoutPanelEx panel = new DockLayoutPanelEx(Style.Unit.EM);
        HorizontalLayout h = new HorizontalLayout();
        h.add(new Label("Last (or only) name:"), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(topMargin, 5, 0, 0)));
        lname.setWidth(200);
        h.add(lname, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(topMargin, 5, 0, 0)));
        TextButton searchBtn = new TextButton("Search");
        searchBtn.addSelectHandler(new Search());
        h.add(searchBtn, new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(topMargin, 0, 0, 0)));
        panel.addNorth(h, 4.0);
        dlg.add(panel);
        runNoClear(panel, null);
        editDlg = new AuthorSelectDialog(this);
        setDoubleClickHandler(selectData);
        setInitialFocus(lname);
        dlg.forceLayout();  // forceLayout must be here for IE 8-10 and not elsewhere
        dlg.show();

        // show initial list
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute () {
                refresh(null);
            }
        });

    }

    private class Search implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            refresh(null);
        }
    }

    public void refresh(StandardReturn ret) {
        AsyncCallback<RecordReturn> callBack = new AsyncCallback<RecordReturn>() {
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
            public void onSuccess(RecordReturn result) {
                UserInput.enable();
                Record [] records = result.getRecords();
                updateGrid(records);
            }
        };
        UserInput.disable();
        service.getRecords(Login.getUUID(), lname.getText(), callBack);
    }

    protected void configureGrid(FramedPanel cp) {
        cp.setHeadingText("Authors");
    }

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
        addStringColumn(collst, props.name(), 30, "Name").setSortable(true);
    }

    protected void createButtons() {
        addButton("Select", selectData);
        addButton("New", new AddData());
        addButton("Edit", new EditData());
        addButton("Delete", new DeleteData());
        addButton("Cancel", new Cancel());
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
            else if (n > 1)
                Message.msgOk("Only one record may be deleted at a time.");
            else {
                ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected author?");
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

    private class SelectData implements RowDoubleClickEvent.RowDoubleClickHandler, SelectEvent.SelectHandler {

        private void selectData() {
            int n = getNumSelectedRecords();
            if (n < 1)
                Message.msgOk("The record to be used must be selected first.");
            else if (n > 1)
                Message.msgOk("Only one record may be selected at a time.");
            else {
                Record rec = getSelectedRecord();
                dlg.hide();
                updateRec.update(rec);
            }
        }

        @Override
        public void onRowDoubleClick(RowDoubleClickEvent event) {
            selectData();
        }

        @Override
        public void onSelect(SelectEvent event) {
            selectData();
        }
    }

    private class Cancel implements SelectEvent.SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {
            dlg.hide();
        }
    }

}
