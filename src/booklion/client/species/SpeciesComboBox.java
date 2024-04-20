package booklion.client.species;

import booklion.client.global.Information;
import booklion.client.login.Login;
import booklion.client.utils.Message;
import booklion.client.utils.UserInput;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import java.util.Arrays;
import java.util.List;

/**
 * @author Blake McBride
 * Date: 1/12/14
 */
public class SpeciesComboBox extends ComboBox<Record> {
    private SpeciesServiceAsync speciesService = SpeciesService.App.getInstance();
    private Record selection;
    private Long selectedId;  // handle case where selection made before combobox has been filled

    public static SpeciesComboBox createSpeciesComboBox() {
        RecordProperties props = GWT.create(RecordProperties.class);
        ListStore<Record> store = new ListStore<Record>(props.speciesId());
        SpeciesComboBox cb = new SpeciesComboBox(store, props.speciesComboBoxLabel());
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return cb;
    }

    public static SpeciesComboBox createSpeciesComboBox(int wth) {
        RecordProperties props = GWT.create(RecordProperties.class);
        ListStore<Record> store = new ListStore<Record>(props.speciesId());
        SpeciesComboBox cb = new SpeciesComboBox(store, props.speciesComboBoxLabel());
        cb.setWidth(wth);
        cb.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return cb;
    }

    public SpeciesComboBox(ListStore<Record> store, LabelProvider<Record> props) {
        super(store, props);
        setAllowBlank(true);
        setForceSelection(true);
        setEditable(false);
        addValueChangeHandler(new ValueChanged());
        fill();
    }

    public Record getSelection() {
        return selection;
    }

    public long getSpeciesId() {
        return selection == null ? 0 : selection.getSpeciesId();
    }

    public void setSpeciesId(long id) {
        boolean foundSome = false;
        selectedId = null;
        ListStore<Record> store = getStore();
        for (Record rec : store.getAll()) {
            foundSome = true;
            if (rec.getSpeciesId() == id) {
//             select(rec);
                setValue(rec);
                selection = rec;
                return;
            }
        }
        if (!foundSome)
            selectedId = id;
    }

    private class ValueChanged implements ValueChangeHandler<Record> {

        @Override
        public void onValueChange(ValueChangeEvent<Record> event) {
            selection = event.getValue();
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
        speciesService.getRecords(Login.getUUID(), Information.getBook().getBookId(), callBack);
    }

    private void fill2(List<Record> records) {
        Long humanId = null;
        ListStore<Record> store = getStore();
        store.clear();
        recordCache = records;
        for (Record rec : records)
            if (Information.acceptChapterId(rec.getChapterId())) {
                store.add(rec);
                if ("Human".equals(rec.getSpeciesName()))
                    humanId = rec.getSpeciesId();
            }
        if (selectedId != null) {
            setSpeciesId(selectedId);
            selectedId = null;
        } else if (recordCache.size() == 1)
            setSpeciesId(recordCache.get(0).getSpeciesId());
        else if (humanId != null)
            setSpeciesId(humanId);
    }

    public static void clearCache() {
        recordCache = null;
    }
}
