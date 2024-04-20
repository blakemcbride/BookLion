package booklion.client.utils;

import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;

/**
 * @author Blake McBride
 */
public class MenuBarEx extends MenuBar {
	
	public MenuBarEx() {
//		addStyleName(ThemeStyles.getStyle().borderBottom());
	}
	
	public MenuEx addMenuBarItem(String title) {
		MenuEx menu = new MenuEx();
		add(new MenuBarItem(title, menu));
		return menu;
	}

	public MenuBarItem addMenuBarDirectItem(String id, MenuBarItem mbi) {
		mbi.setItemId(id);
		add(mbi);
        return mbi;
	}

}
