package booklion.client.utils;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Blake McBride
 * Date: 2/19/14
 */
public class DockLayoutPanelEx extends DockLayoutPanel {

    public DockLayoutPanelEx(Style.Unit unit) {
        super(unit);
    }

    public Widget getCenter() {
        for (int i=0, m=getWidgetCount() ; i < m ; i++) {
            Widget w = getWidget(i);
            if (getWidgetDirection(w) == Direction.CENTER)
                return w;
        }
        return null;
    }

    public Widget getSouth() {
        for (int i=0, m=getWidgetCount() ; i < m ; i++) {
            Widget w = getWidget(i);
            if (getWidgetDirection(w) == Direction.SOUTH)
                return w;
        }
        return null;
    }

    public Widget getNorth() {
        for (int i=0, m=getWidgetCount() ; i < m ; i++) {
            Widget w = getWidget(i);
            if (getWidgetDirection(w) == Direction.NORTH)
                return w;
        }
        return null;
    }

    public Widget getEast() {
        for (int i=0, m=getWidgetCount() ; i < m ; i++) {
            Widget w = getWidget(i);
            if (getWidgetDirection(w) == Direction.EAST)
                return w;
        }
        return null;
    }

    public Widget getWest() {
        for (int i=0, m=getWidgetCount() ; i < m ; i++) {
            Widget w = getWidget(i);
            if (getWidgetDirection(w) == Direction.WEST)
                return w;
        }
        return null;
    }

}
