package booklion.client.booklist;

import booklion.client.login.Login;
import booklion.client.utils.BasicCrudPaging;
import booklion.client.utils.EditDialog;
import booklion.client.utils.IUpdate;
import booklion.client.utils.StandardReturn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.List;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public abstract class SpecificCrud extends BasicCrudPaging<Record> {

    protected String getMainKey(Record item) {
        return item.getBookId() + "";
    }

    protected void deleteRecord(String uuid, Record rec, AsyncCallback<StandardReturn> callback) {
        BooklistService.Util.getInstance().deleteRecord(Login.getUUID(), rec, callback);
    }

    protected void getPosts(String uuid, PagingLoadConfig config, AsyncCallback<PagingLoadResult<Record>> callback) {
        final BooklistServiceAsync service = GWT.create(BooklistService.class);
        service.getPosts(Login.getUUID(), config,
                PageData.firstRecord(), PageData.lastRecord(),
                PageData.totalLength(), searchText1, searchText2, callback);
    }

    protected abstract void defineGridColumns(List<ColumnConfig<Record, ?>> collst, RecordProperties props);

    protected void defineGridColumns(List<ColumnConfig<Record, ?>> collst) {
        RecordProperties props = GWT.create(RecordProperties.class);
        defineGridColumns(collst, props);
    }

}
