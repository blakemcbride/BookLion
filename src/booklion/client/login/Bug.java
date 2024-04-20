package booklion.client.login;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.menu.*;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * @author Blake McBride ( blake@mcbride.name )
 * Date: 10/1/13
 */
public class Bug {
    private static final String userMaint = "userMaint";
    private DockLayoutPanel dataPanel;
    private Viewport vp;


    public void onModuleLoad() {
        RootLayoutPanel vp = RootLayoutPanel.get();
        vp.clear();

        DockLayoutPanel dp = new DockLayoutPanel(Style.Unit.EM);
        vp.add(dp);

        dp.addNorth(makeMenu(), 2);

        dataPanel = dp;
    }

    private MenuBar makeMenu()  {
        Menu menu;
        MenuBar bar = new MenuBar();
        LocalSelectionHandler handler = new LocalSelectionHandler();

        bar.setBorders(false);

        // ///////////////
        menu = new Menu();
        bar.add(new MenuBarItem("File", menu));

        MenuItem mi = new MenuItem("Grid");
        mi.addSelectionHandler(handler);
        mi.setItemId(userMaint);
        menu.add(mi);

        return bar;
    }

    private void addGrid() {
        FramedPanel cp = new FramedPanel();

        Widget grid = new MakeGrid().asWidget();

        dataPanel.addSouth(new Label("Bottom of grid"), 2);

        dataPanel.add(grid);

        vp.forceLayout();
    }

    private class LocalSelectionHandler implements SelectionHandler<Item> {

        @Override
        public void onSelection(SelectionEvent<Item> event) {
            MenuItem item = (MenuItem) event.getSelectedItem();
            String selection = item.getItemId();
            if (selection == userMaint)
                addGrid();
        }
    }
}
