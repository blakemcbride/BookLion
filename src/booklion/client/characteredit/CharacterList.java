package booklion.client.characteredit;

import booklion.client.global.Information;
import booklion.client.login.Login;
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
import com.sencha.gxt.state.client.GridStateHandler;
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
 * Date: 2/24/14
 */
public class CharacterList extends SpecificCrud implements EntryPoint {

    private CharacterEditServiceAsync service = CharacterEditService.App.getInstance();
    private CharacterListDialog editDlg;
    private EditData editData = new EditData();
    private CharacterLog characterLog;
    private Record prevSelectedRecord;
    private boolean updateSelection = false;


    public void onModuleLoad() {
        final DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        final CharacterList cl = this;

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
                Record [] characterList = result.getRecords();
                updateGrid(characterList);
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
        editDlg = new CharacterListDialog(cl);
        characterLog = new CharacterLog().init(this);
    }

    protected void configureGrid(FramedPanel cp) {
        cp.setHeadingText("Characters");
        getGrid().setStateful(true);
        getGrid().setStateId("characterListGrid");
        GridStateHandler state = new GridStateHandler(getGrid());
        state.loadState();
    }

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
        addStringColumn(collst, props.name(), 300, "Name").setSortable(true);
        addStringColumn(collst, props.priority(), 100, "Significance").setSortable(true);
        addStringColumn(collst, props.speciesDisplayName(), 100, "Species").setSortable(true);
    }

    private static final String addCharacterMenu = "Add Character";
    private static final String editCharacterMenu = "Edit Character";
    private static final String deleteCharacterMenu = "Delete Character";

    @Override
    protected void createSectionMenu() {
        addMenuItem(addCharacterMenu, new LocalClickHandler(addCharacterMenu));
        addMenuItem(editCharacterMenu, new LocalClickHandler(editCharacterMenu));
        addMenuItem(deleteCharacterMenu, new LocalClickHandler(deleteCharacterMenu));
    }

    private class LocalClickHandler implements ClickHandler {

        private String selector;

        LocalClickHandler(String s) {
            selector = s;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (selector == addCharacterMenu) {
                if (Message.notForGuest())
                    return;
                deselectAll();
                refreshLogs(null);
                updateSelection = true;
                editDlg.show(EditDialog.NEWRECORD, null);
            } else if (selector == editCharacterMenu) {
                editData();
            } else if (selector == deleteCharacterMenu) {
                if (Message.notForGuest())
                    return;
                int n = getNumSelectedRecords();
                if (n < 1)
                    Message.msgOk("The record to be deleted must be selected first.");
                else if (n > 1)
                    Message.msgOk("Only one record may be deleted at a time.");
                else {
                    ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected character?");
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
        long chapterId = characterLog.getChapterId();
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
            characterLog.update(null);
            characterLog.clearSectionMenu();
            characterLog.setEnabled(false);
            prevSelectedRecord = null;
            return;
        }
        Record rec = getSelectedRecord();
//            Information.setChapter(rec.getChapterId(), rec.getChapterName());
        characterLog.update(getLogs(rec));
        characterLog.clearSectionMenu();
        characterLog.setEnabled(false);
        characterLog.createSectionMenu();
        characterLog.updateDetail(rec);
        prevSelectedRecord = rec;
    }

    private class SelectData implements SelectionHandler {

        private boolean okAsked = false;  // prevent the system from asking the question again while in internal code

        @Override
        public void onSelection(SelectionEvent event) {
            if (!okAsked  &&  prevSelectedRecord != null  &&  Information.isDirty()) {
                okAsked = true;
                final String currentText = characterLog.getText();
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
                        characterLog.setText(currentText);  // restore the text they had edited
                        Information.setDirty(true);
                        characterLog.clearSectionMenu();
                        characterLog.setEnabled(true);
                        characterLog.createSaveMenu();
                    }
                };
            } else
                changeSelection();
        }
    }

    public void refreshLogs(String logs) {
        if (getNumSelectedRecords() != 1) {
            characterLog.update(null);
            return;
        }
        Record rec = getSelectedRecord();
        if (logs == null)
            characterLog.update(getLogs(rec));
        else {
//            rec.setLogs(logs);
            characterLog.update(logs);
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

    // cause the character summary info to update
    protected void updateAdditional(Record rec) {
        characterLog.updateDetail(rec);
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
                    String fname = editDlg.getFname();
                    String lname = editDlg.getLname();
                    if (fname != null  ||  lname != null) {
                        for (Record r : records) {
                            String fst = r.getFname();
                            String lst = r.getLname();
                            if (fst == null && fname != null || fst != null && fname == null)
                                continue;
                            if (fst != null && !fst.equals(fname))
                                continue;
                            if (lst == null && lname != null || lst != null && lname == null)
                                continue;
                            if (lst != null && !lst.equals(lname))
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
}
