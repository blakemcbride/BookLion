package booklion.client.booklist;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public interface RecordProperties extends PropertyAccess<Record> {

    ValueProvider<Record, Integer> recNo();

    ValueProvider<Record, Long> bookId();

    ValueProvider<Record, String> bookTitle();

    ValueProvider<Record, Integer> genreId();

    ValueProvider<Record, String> genreName();

    ValueProvider<Record, Long> author1Id();

    ValueProvider<Record, String> author1Name();

    ValueProvider<Record, Long> author2Id();

    ValueProvider<Record, String> author2Name();

    ValueProvider<Record, Long> author3Id();

    ValueProvider<Record, String> author3Name();

    ValueProvider<Record, Integer> yearPublished();

    ValueProvider<Record, String> description();

    ValueProvider<Record, String> authorsName();
}
