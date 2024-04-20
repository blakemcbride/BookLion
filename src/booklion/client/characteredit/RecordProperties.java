package booklion.client.characteredit;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
public interface RecordProperties extends PropertyAccess<Record> {

    ValueProvider<Record, String> name();

    ValueProvider<Record, String> priority();

    ValueProvider<Record, String> speciesDisplayName();
}
