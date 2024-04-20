package booklion.client.chapterlist;

import booklion.client.utils.StandardReturn;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Blake McBride
 * Date: 1/4/14
 */
public interface ChapterListServiceAsync {
    void getRecords(String uuid, long bookId, AsyncCallback<RecordReturn> async);
}
