package booklion.client.speciesedit;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public interface RecordProperties extends PropertyAccess<Record> {

    ModelKeyProvider<Record> speciesId();

    ValueProvider<Record, String> speciesName();

}
