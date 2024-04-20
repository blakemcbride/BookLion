package booklion.client.utils;

import com.google.gwt.user.client.ui.*;

/**
 * @author Blake McBride
 * Date: 1/1/14
 */
public class LayoutUtils {

    public static VerticalPanel addSpace(VerticalPanel vp, String amt) {
        Label lbl = new Label("");
        lbl.setHeight(amt);
        vp.add(lbl);
        return vp;
    }

    public static HorizontalPanel addSpace(HorizontalPanel hp, String amt) {
        Label lbl = new Label("");
        lbl.setWidth(amt);
        hp.add(lbl);
        return hp;
    }

    public static Widget center(Widget w) {
        Grid grid = new Grid(1,1);
        grid.setWidget(0, 0, w);

        HTMLTable.CellFormatter formatter = grid.getCellFormatter();
        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

        grid.getElement().getStyle().setProperty("width", "100%");
        grid.getElement().getStyle().setProperty("height", "100%");
        return grid;
    }

    public static Widget topCenter(Widget w) {
        Grid grid = new Grid(1,1);
        grid.setWidget(0, 0, w);

        HTMLTable.CellFormatter formatter = grid.getCellFormatter();
        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

        grid.getElement().getStyle().setProperty("width", "100%");
        grid.getElement().getStyle().setProperty("height", "100%");
        return grid;
    }

}
