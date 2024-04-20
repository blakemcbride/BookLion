package booklion.client.chapterlist;

import booklion.client.global.Information;
import booklion.client.login.Login;
import booklion.client.utils.Message;
import booklion.client.utils.UserInput;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Blake McBride
 * Date: 1/10/14
 *
 * This class is used by several other packages in order to present a list of book chapters.
 */
public class ChapterComboBox extends ComboBox<Record> {
    private ChapterListServiceAsync chapterService = ChapterListService.App.getInstance();
    private Record selection;
    private Long selectedId;  // handle case where selection made before combobox has been filled
    private boolean showAll = false;  //  show all rows
    private boolean showAllOption = false;  //  add "All Chapters" row
    private SelectionHandler selectionChangeHandler;

    public static ChapterComboBox createChapterComboBox() {
        RecordProperties props = GWT.create(RecordProperties.class);
        ListStore<Record> store = new ListStore<Record>(props.chapterId());
        ChapterComboBox cb = new ChapterComboBox(store, props.chapterComboBoxLabel(), false);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return cb;
    }

    public static ChapterComboBox createChapterComboBox(int wth) {
        RecordProperties props = GWT.create(RecordProperties.class);
        ListStore<Record> store = new ListStore<Record>(props.chapterId());
        ChapterComboBox cb = new ChapterComboBox(store, props.chapterComboBoxLabel(), false);
        cb.setWidth(wth);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return cb;
    }

    public static ChapterComboBox createChapterComboBoxWithAllOption(int wth) {
        RecordProperties props = GWT.create(RecordProperties.class);
        ListStore<Record> store = new ListStore<Record>(props.chapterId());
        ChapterComboBox cb = new ChapterComboBox(store, props.chapterComboBoxLabel(), true);
        cb.setWidth(wth);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return cb;
    }

    private ChapterComboBox(ListStore<Record> store, LabelProvider<Record> props, boolean showAll) {
        super(store, props);
        showAllOption = showAll;
        setAllowBlank(true);
        setForceSelection(true);
        setEditable(false);
        addSelectionHandler(selectionChangeHandler = new SelectionChanged());
        fill();
    }

    public SelectionHandler getSelectionChangeHandler() {
        return selectionChangeHandler;
    }

    public Record getSelection() {
        return selection;
    }

    public long getChapterId() {
        return selection == null ? 0 : selection.getChapterId();
    }

    public void setChapterId(long id) {
        boolean foundSome = false;
        selectedId = null;
        ListStore<Record> store = getStore();
        for (Record rec : store.getAll()) {
            foundSome = true;
            if (rec.getChapterId() == id) {
//             select(rec);
               setValue(rec);
               selection = rec;
               return;
            }
        }
        if (!foundSome)
            selectedId = id;
    }

    public void next() {
        selectedId = null;
        if (selection == null)
            return;
        boolean next = false;
        ListStore<Record> store = getStore();
        for (Record rec : store.getAll()) {
            if (next) {
                setValue(rec);
                selection = rec;
                return;
            }
            if (rec.getChapterId() == selection.getChapterId())
                next = true;
        }
    }

    public void previous() {
        selectedId = null;
        if (selection == null)
            return;
        Record prev = null;
        ListStore<Record> store = getStore();
        for (Record rec : store.getAll()) {
            if (rec.getChapterId() == selection.getChapterId()) {
                if (prev != null) {
                    setValue(prev);
                    selection = prev;
                }
                return;
            }
            prev = rec;
        }
    }

    public void setShowAll(boolean val) {
        showAll = val;
    }

    private class SelectionChanged implements SelectionHandler<Record> {

        @Override
        public void onSelection(SelectionEvent<Record> event) {
            selection = event.getSelectedItem();
        }
    }

    private static List<Record> recordCache;

    private void fill() {
        if (recordCache != null) {
            fill2(recordCache);
            return;
        }
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
                fill2(Arrays.asList(result.getRecords()));
            }
        };
        UserInput.disable();
        chapterService.getRecords(Login.getUUID(), Information.getBook().getBookId(), callBack);
    }

    private void fill2(List<Record> records) {
        ListStore<Record> store = getStore();
        store.clear();
        if (showAllOption) {
            Record r = new Record();
            r.setChapterId(-1L);
            r.setChapterDesignation("All Chapters");
            store.add(r);
        }
        recordCache = records;
        long lastId = Information.getChapterLimitId();
        Record lastRec = null;
        for (Record rec : records) {
            store.add(lastRec=rec);
            if (!showAll  &&  lastId == rec.getChapterId())
                break;
        }
        if (selectedId != null) {
            setChapterId(selectedId);
            selectedId = null;
        } else if (lastRec != null)
            setChapterId(lastRec.getChapterId());
    }

    public static void clearCache() {
        recordCache = null;
    }

    public LinkedList<Long> getValidChapterIds(Record selectedRec) {
        if (selectedRec == null)
            return null;
        long selectedId = selectedRec.getChapterId();
        LinkedList<Long> validChapterIds = new LinkedList<Long>();
        ListStore<Record> records = getStore();
        for (Record rec : records.getAll()) {
            long cid = rec.getChapterId();
            if (cid != -1L) {
                validChapterIds.add(cid);
                if (rec.getChapterId() == selectedId)
                    break;
            }
        }
        return validChapterIds;
    }

    public LinkedList<Long> getAllChapterIds() {
        LinkedList<Long> validChapterIds = new LinkedList<Long>();
        ListStore<Record> records = getStore();
        for (Record rec : records.getAll()) {
            long cid = rec.getChapterId();
            if (cid != -1L)
                validChapterIds.add(cid);
        }
        return validChapterIds;
    }

    public Record getRecord(long chapterId) {
        ListStore<Record> records = getStore();
        for (Record rec : records.getAll()) {
            long cid = rec.getChapterId();
            if (cid == chapterId)
                return rec;
        }
        return null;
    }

}
