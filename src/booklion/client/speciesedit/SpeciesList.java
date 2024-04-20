package booklion.client.speciesedit;

import booklion.client.global.Information;
import booklion.client.login.Login;
import booklion.client.species.SpeciesComboBox;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class SpeciesList extends SpecificCrud implements EntryPoint {

    private SpeciesEditServiceAsync service = SpeciesEditService.App.getInstance();
    private SpeciesDialog editDlg;
    private EditData editData = new EditData();
    private SpeciesLog speciesLog;
    private Record prevSelectedRecord;
    private boolean updateSelection = false;

    public void onModuleLoad() {
        final DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        SpeciesComboBox.clearCache();

        final SpeciesList cl = this;

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
                Record [] speciesList = result.getRecords();
                updateGrid(speciesList);
                Scheduler.get().scheduleDeferred(new Command() {
                    public void execute () {
                        panel.forceLayout();
                    }
                });
            }
        };
        UserInput.disable();
        service.getRecords(Login.getUUID(), Information.getBook().getBookId(), callBack);
        run(SupervisorMain.getPanel(), null);
        setDoubleClickHandler(editData);
        setSelectionHandler(new SelectData());
        editDlg = new SpeciesDialog(cl);
        speciesLog = new SpeciesLog().init(this);
    }

    /**
     * This limits records displayed to only those related to the restricted view
     *
     * @param rec
     * @return
     */
    protected boolean acceptRecord(Record rec) {
        return Information.acceptChapterId(rec.getChapterId());
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
                updateSelection = false;
            }

            @Override
            public void onSuccess(RecordReturn result) {
                UserInput.enable();
                Record [] records = result.getRecords();
                updateGrid(records);
                if (updateSelection) {
                    updateSelection = false;
                    String speciesname = editDlg.getSpeciesName();
                    if (speciesname != null) {
                        for (Record r : records) {
                            String sn = r.getSpeciesName();
                            if (sn == null && speciesname != null || sn != null && speciesname == null)
                                continue;
                            if (sn != null && !sn.equals(speciesname))
                                continue;
                            setSelection(r);
                            changeSelection();
                            break;
                        }
                    }
                }
            }
        };
        UserInput.disable();
        service.getRecords(Login.getUUID(), Information.getBook().getBookId(), callBack);
    }

    protected void configureGrid(FramedPanel cp) {
        cp.setHeadingText("Species");
    }

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
        addStringColumn(collst, props.speciesName(), 150, "Species").setSortable(true);
    }

    private static final String addSpeciesMenu = "Add Species";
    private static final String editSpeciesMenu = "Edit Species";
    private static final String deleteSpeciesMenu = "Delete Species";

    @Override
    protected void createSectionMenu() {
        addMenuItem(addSpeciesMenu, new LocalClickHandler(addSpeciesMenu));
        addMenuItem(editSpeciesMenu, new LocalClickHandler(editSpeciesMenu));
        addMenuItem(deleteSpeciesMenu, new LocalClickHandler(deleteSpeciesMenu));
    }

    private class LocalClickHandler implements ClickHandler {

        private String selector;

        LocalClickHandler(String s) {
            selector = s;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (selector == addSpeciesMenu) {
                if (Message.notForGuest())
                    return;
                deselectAll();
                refreshLogs(null);
                updateSelection = true;
                editDlg.show(EditDialog.NEWRECORD, null);
            } else if (selector == editSpeciesMenu) {
                if (Message.notForGuest())
                    return;
                editData();
            } else if (selector == deleteSpeciesMenu) {
                if (Message.notForGuest())
                    return;
                int n = getNumSelectedRecords();
                if (n < 1)
                    Message.msgOk("The record to be deleted must be selected first.");
                else if (n > 1)
                    Message.msgOk("Only one record may be deleted at a time.");
                else {
                    ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected species?");
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
    }

    String getLogs(Record rec) {
        StringBuilder sb = new StringBuilder();
        long chapterLimit = Information.getChapterLimitId();
        long chapterId = speciesLog.getChapterId();
        LogRecord [] logs = rec.getLogs();
        for (LogRecord log : logs) {
            String s = log.getLog();
            long cid = log.getChapterId();
            if ((chapterId == -1L  ||  cid == chapterId)  &&  s != null  &&  !s.isEmpty()) {
                if (sb.length() != 0)
                    sb.append("\n\n");
                sb.append(s);
            }
            if (chapterLimit == cid)
                break;
        }
        return sb.toString();
    }

    void changeSelection() {
        if (getNumSelectedRecords() != 1) {
            speciesLog.update(null);
            speciesLog.clearSectionMenu();
            speciesLog.setEnabled(false);
            prevSelectedRecord = null;
            return;
        }
        Record rec = getSelectedRecord();
//            Information.setChapter(rec.getChapterId(), rec.getChapterName());
        speciesLog.update(getLogs(rec));
        speciesLog.clearSectionMenu();
        speciesLog.setEnabled(false);
        speciesLog.createSectionMenu();
        prevSelectedRecord = rec;
    }

    public void refreshLogs(String logs) {
        if (getNumSelectedRecords() != 1) {
            speciesLog.update(null);
            return;
        }
        Record rec = getSelectedRecord();
        if (logs == null)
            speciesLog.update(getLogs(rec));
        else {
//            rec.setLogs(logs);
            speciesLog.update(logs);
        }
    }

    private class SelectData implements SelectionHandler {

        private boolean okAsked = false;  // prevent the system from asking the question again while in internal code

        @Override
        public void onSelection(SelectionEvent event) {
            if (!okAsked  &&  prevSelectedRecord != null  &&  Information.isDirty()) {
                okAsked = true;
                final String currentText = speciesLog.getText();
                new YesNoMessageBox("Summary changed.  Do you want to leave without saving?") {
                    @Override
                    public void yes() {
                        changeSelection();
                        okAsked = false;
                    }

                    @Override
                    public void no() {
                        setSelection(prevSelectedRecord);
                        okAsked = false;
                        // now we have to restore everything as if we are still in the middle of editing
                        speciesLog.setText(currentText);  // restore the text they had edited
                        Information.setDirty(true);
                        speciesLog.clearSectionMenu();
                        speciesLog.setEnabled(true);
                        speciesLog.createSaveMenu();
                    }
                };
            } else
                changeSelection();
        }
    }

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

    private class EditData implements RowDoubleClickEvent.RowDoubleClickHandler, SelectEvent.SelectHandler {

        @Override
        public void onRowDoubleClick(RowDoubleClickEvent event) {
            editData();
        }

        @Override
        public void onSelect(SelectEvent event) {
            editData();
        }
    }

}
