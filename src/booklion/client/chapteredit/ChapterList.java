package booklion.client.chapteredit;

import booklion.client.chapterlist.ChapterComboBox;
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
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.*;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 1/4/14
 */
public class ChapterList extends SpecificCrud implements EntryPoint {

    private ChapterEditServiceAsync service = ChapterEditService.App.getInstance();
    private ChapterListDialog editDlg;
    private EditData editData = new EditData();
    private ChapterLog chapterLog;
    private Record prevSelectedRecord;
    private boolean updateSelection = false;

    public void onModuleLoad() {
        final DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        ChapterComboBox.clearCache();

        final ChapterList cl = this;

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
                Record [] chapterList = result.getRecords();
                updateGrid(chapterList);
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
        editDlg = new ChapterListDialog(cl);
        chapterLog = new ChapterLog().init(this);
    }

    /**
     * This limits records displayed to only those related to the restricted view
     *
     * @param rec
     * @return
     */
    protected boolean acceptRecord(Record rec) {
        if (!Information.isChapterLimit())
            return true;
        return rec.getSeqno() <= Information.getChapterLimitSeqno();
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
                    String chapname = editDlg.getChapterDesignation();
                    if (chapname != null) {
                        for (Record r : records) {
                            String chap = r.getChapterDesignation();
                            if (chap == null && chapname != null || chap != null && chapname == null)
                                continue;
                            if (chap != null && !chap.equals(chapname))
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
        cp.setHeadingText("Chapters");
    }

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props) {
        addStringColumn(collst, props.chapterDesignation(), 30, "Chapter").setSortable(true);
        addStringColumn(collst, props.chapterName(), 150, "Title");
    }

    private static final String addChapterMenu = "Add Chapter";
    private static final String editChapterMenu = "Edit Chapter";
    private static final String deleteChapterMenu = "Delete Chapter";

    @Override
    protected void createSectionMenu() {
        addMenuItem(addChapterMenu, new LocalClickHandler(addChapterMenu));
        addMenuItem(editChapterMenu, new LocalClickHandler(editChapterMenu));
        addMenuItem(deleteChapterMenu, new LocalClickHandler(deleteChapterMenu));
    }

    private class LocalClickHandler implements ClickHandler {

        private String selector;

        LocalClickHandler(String s) {
            selector = s;
        }

        @Override
        public void onClick(ClickEvent event) {
            new ExecuteIfNotDirty("Unsaved data exists; Leave without saving?") {
                @Override
                public void execute() {
                    if (selector == addChapterMenu) {
                        if (Message.notForGuest())
                            return;
                        deselectAll();
                        refreshLogs(null);
                        updateSelection = true;
                        editDlg.show(EditDialog.NEWRECORD, null);

                    } else if (selector == editChapterMenu) {
                        if (Message.notForGuest())
                            return;
                        editData();
                    } else if (selector == deleteChapterMenu) {
                        if (Message.notForGuest())
                            return;
                        int n = getNumSelectedRecords();
                        if (n < 1)
                            Message.msgOk("The record to be deleted must be selected first.");
                        else if (n > 1)
                            Message.msgOk("Only one record may be deleted at a time.");
                        else {
                            ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected chapter?");
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
            };
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

    private void changeSelection() {
        if (getNumSelectedRecords() != 1) {
            chapterLog.update(null);
            chapterLog.clearSectionMenu();
            chapterLog.setEnabled(false);
            prevSelectedRecord = null;
            return;
        }
        Record rec = getSelectedRecord();
        Information.setChapter(rec.getChapterId(), rec.getChapterName());
        chapterLog.update(rec.getLogs());
        chapterLog.clearSectionMenu();
        chapterLog.setEnabled(false);
        chapterLog.createSectionMenu();
        prevSelectedRecord = rec;
    }

     private class SelectData implements SelectionHandler {

        private boolean okAsked = false;  // prevent the system from asking the question again while in internal code

        @Override
        public void onSelection(SelectionEvent event) {
            if (!okAsked  &&  prevSelectedRecord != null  &&  Information.isDirty()) {
                okAsked = true;
                final String currentText = chapterLog.getText();
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
                        chapterLog.setText(currentText);  // restore the text they had edited
                        Information.setDirty(true);
                        chapterLog.clearSectionMenu();
                        chapterLog.setEnabled(true);
                        chapterLog.createSaveMenu();
                    }
                };

            } else
                changeSelection();
        }
    }

    public void refreshLogs(String logs) {
        if (getNumSelectedRecords() != 1) {
            chapterLog.update(null);
            return;
        }
        Record rec = getSelectedRecord();
        if (logs == null)
            chapterLog.update(rec.getLogs());
        else {
            rec.setLogs(logs);
            chapterLog.update(logs);
        }
    }

}
