package booklion.client.chapteredit;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 1/3/14
 */
public interface RecordProperties extends PropertyAccess<booklion.client.chapterlist.Record> {

    ModelKeyProvider<Record> chapterId();

    ValueProvider<Record, String> chapterDesignation();

    ValueProvider<Record, String> chapterName();

}
