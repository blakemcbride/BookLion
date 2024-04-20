package booklion.client.authorselect;

import booklion.client.utils.EditDialog;
import booklion.client.utils.IUpdate;
import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public abstract class SpecificDialog extends EditDialog<Record> {

    public SpecificDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected Record copy(Record obj) {
        return obj.copy();
    }

    protected void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        AuthorSelectService.App.getInstance().addRecord(uuid, rec, callback);
    }

    protected void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        AuthorSelectService.App.getInstance().updateRecord(uuid, rec, callback);
    }

    protected Record newRecord() {
        Record rec = new Record();
        saveUpdatedData(rec, getPopupMode());
        return rec;
    }

}
