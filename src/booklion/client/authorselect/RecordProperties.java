package booklion.client.authorselect;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 1/31/14
 */
public interface RecordProperties extends PropertyAccess<Record> {
    ModelKeyProvider<Record> authorId();

    //    @Path("chapterName")
//    LabelProvider<Record> authorComboBoxLabel();

    ValueProvider<Record, String> lname();

    ValueProvider<Record, String> fname();

    ValueProvider<Record, String> name();

}
