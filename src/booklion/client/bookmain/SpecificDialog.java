package booklion.client.bookmain;

import booklion.client.booklist.BooklistService;
import booklion.client.booklist.Record;
import booklion.client.utils.EditDialog;
import booklion.client.utils.ErrorCheck;
import booklion.client.utils.IUpdate;
import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *@author Blake McBride
 * Date: 3/27/14
 */
public abstract class SpecificDialog extends EditDialog<Record> {

    public SpecificDialog(IUpdate<Record> main) {
        super(main);
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
