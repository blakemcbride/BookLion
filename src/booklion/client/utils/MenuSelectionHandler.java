package booklion.client.utils;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * @author Blake McBride
 */
public abstract class MenuSelectionHandler implements SelectionHandler<Item> {
	
	@Override
	public void onSelection(SelectionEvent<Item> event) {
		MenuItem item = (MenuItem) event.getSelectedItem();
		executeRequest(item.getItemId());
	}
	
	public abstract void executeRequest(String selection);

}
