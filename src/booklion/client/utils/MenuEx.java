package booklion.client.utils;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * @author Blake McBride
 */
public class MenuEx extends Menu {
	
	public void addMenuItem(String title, String id, MenuSelectionHandler handler) {
		MenuItem mi = new MenuItem(title);
		mi.addSelectionHandler(handler);
		mi.setItemId(id);
		add(mi);		
	}

}
