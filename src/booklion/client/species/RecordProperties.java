package booklion.client.species;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 1/10/14
 */
public interface RecordProperties extends PropertyAccess<Record> {

    ModelKeyProvider<Record> speciesId();

    //    @Path("chapterName")
    LabelProvider<Record> speciesComboBoxLabel();

    ValueProvider<Record, String> speciesName();

}
