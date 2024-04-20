package booklion.client.utils;

import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;

/**
 * @author Blake McBride
 *
 * This allows for observation of clicks on the MenuBarItem
 * 
 */
public class MenuBarItemClickOnly extends MenuBarItem {
	
	private MenuSelectionHandler handler;

	public MenuBarItemClickOnly(String text, MenuSelectionHandler handler) {
		super(text, new MenuNoSubMenu());
		this.handler = handler;
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONMOUSEDOWN:
			event.preventDefault();
			event.stopPropagation();
			handler.executeRequest(getItemId());
			break;
		}
	}

}
