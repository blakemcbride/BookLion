package booklion.client.utils;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Radio;

/**
 * @author Blake McBride
 */
public class HorizontalLayout extends HorizontalLayoutContainer {

    public HorizontalLayout() {
    }

    /**
     * Returns a HorizontalLayout widget with a label moved to the right in it.
     *
     * @param leadingSpace the amount to move the label
     * @param text text in the label
     */
    public HorizontalLayout(int leadingSpace, String text) {
        Label lbl = new Label("");
        add(lbl, new HorizontalLayoutData(leadingSpace, -1));
        lbl = new Label(text);
        add(lbl);
    }

    /**
     * Adds a label to the HorizontalLayout.
     *
     * @param room width of the label
     * @param text
     * @return
     */
    public Label addLabel(int room, String text) {
		Label lbl = new Label(text);
		add(lbl, new HorizontalLayoutData(room, -1));
		return lbl;
	}
	
	public Radio addRadio(RadioGroup rg, Object value, String label) {
		Radio rb = rg.addRadio(label, value);
		add(rb);
		return rb;
	}

    public HorizontalLayout addSpace(String amt) {
        Label lbl = new Label("");
        lbl.setWidth(amt);
        add(lbl);
        return this;
    }

}
