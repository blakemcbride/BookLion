package booklion.client.booklist;

import booklion.client.utils.EditDialog;
import booklion.client.utils.ErrorCheck;
import booklion.client.utils.IUpdate;
import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 11/17/13
 */
public abstract class SpecificDialog extends EditDialog<Record> {

    public SpecificDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected Record newRecord() {
        Record rec = new Record();
        saveUpdatedData(rec, getPopupMode());
        return rec;
    }

    protected Record copy(Record obj) {
        return obj.copy();
    }

    protected void updateRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        BooklistService.Util.getInstance().updateRecord(uuid, rec, callback);
    }

    protected void addRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        BooklistService.Util.getInstance().addRecord(uuid, rec, callback);
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();
        return ec;
    }

}
