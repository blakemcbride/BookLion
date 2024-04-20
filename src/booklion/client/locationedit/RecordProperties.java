package booklion.client.locationedit;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public interface RecordProperties extends PropertyAccess<Record> {

    ValueProvider<Record, String> locName();

}
