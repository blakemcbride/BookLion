package booklion.client.characteredit;

import booklion.client.login.Login;
import booklion.client.utils.BasicCrudNonPaging;
import booklion.client.utils.StandardReturn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
public abstract class SpecificCrud extends BasicCrudNonPaging<Record> {

    protected void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        CharacterEditService.App.getInstance().deleteRecord(Login.getUUID(), rec, callback);
    }

    protected String getMainKey(Record item) {
        return item.getBookCharacterId() + "";
    }

    protected abstract void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props);

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst) {
        RecordProperties props = GWT.create(RecordProperties.class);
        defineGridColumns(collst, props);
    }

}
