package booklion.client.utils;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Overrides the default behavior, making sure it doesn't show an empty menu
 * 
 * 
 */
public class MenuNoSubMenu extends Menu {
	
	@Override
	public void show(Element elem, AnchorAlignment alignment, int[] offsets) {
		// don't show a submenu
	}

}
