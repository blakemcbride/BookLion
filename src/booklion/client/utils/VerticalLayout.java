package booklion.client.utils;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.*;

/**
 * @author Blake McBride
 */
public class VerticalLayout extends VerticalLayoutContainer {
	
	private static int additionalSpace = 30;

    /**
     *  Add a text control to a vertical layout.
     *
     * @param lblRoom width of the label
     * @param ctlRoom width of the control
     * @param lbl the text of the label
     * @return the control
     */
    public TextControl addTextField(double lblRoom, double ctlRoom, int min, int max, String lbl) {
        HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
        TextControl txtCtl = new TextControl(min, max, lbl);
        Label lblCtl = new Label(lbl);
        txtCtl.setLabel(lblCtl);
        hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
        hc.add(txtCtl, new HorizontalLayoutData(ctlRoom, -1));
        hc.setWidth((int)(lblRoom+(int)ctlRoom));
        add(hc);
        return txtCtl;
    }

    /**
     *  Add a password control to a vertical layout.
     *
     * @param lblRoom width of the label
     * @param ctlRoom width of the control
     * @param lbl the text of the label
     * @return the control
     */
    public PasswordControl addPasswordField(double lblRoom, double ctlRoom, int min, int max, String lbl) {
        HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
        PasswordControl pwCtl = new PasswordControl(min, max, lbl);
        Label lblCtl = new Label(lbl);
        pwCtl.setLabel(lblCtl);
        hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
        hc.add(pwCtl, new HorizontalLayoutData(ctlRoom, -1));
        hc.setWidth((int) (lblRoom + (int) ctlRoom));
        add(hc);
        return pwCtl;
    }

    /**
     *  Add a text area control to a vertical layout.
     *
     * @param width
     * @param height
     * @param min
     * @param max
     * @param lbl the name of the control for error messaging purposes
     *
     * @return the control
     */
    public TextAreaControl addTextArea(String width, String height, int min, int max, String lbl) {
        Label lblCtl = new Label(lbl);
        add(lblCtl);
        TextAreaControl txtCtl = new TextAreaControl(min, max, lbl);
        txtCtl.setSize(width, height);
        add(txtCtl);
        return txtCtl;
    }

    /**
     *  Add a numeric control to a vertical layout.
     *
     * @param lblRoom width of the label
     * @param ctlRoom width of the control
     * @param lbl the text of the label
     * @return the control
     */
    public NumericControl addNumberField(double lblRoom, double ctlRoom, int dp, int min, int max, String lbl) {
        HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
        NumericControl ctl = new NumericControl(dp, min, max, lbl);
        Label lblCtl = new Label(lbl);
        ctl.setLabel(lblCtl);
        hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
        hc.add(ctl.getField(), new HorizontalLayoutData(ctlRoom, -1));
        hc.setWidth((int)(lblRoom+(int)ctlRoom));
        add(hc);
        return ctl;
    }

    /**
     * Add a checkbox to a vertical layout
     *
     * @param lblRoom
     * @param ctlRoom
     * @param lbl
     * @return
     */
	public CheckBox addCheckBox(double lblRoom, double ctlRoom, String lbl) {
		HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
		CheckBox cb = new CheckBox();
		Label lblCtl = new Label(lbl);
		hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
		hc.add(cb, new HorizontalLayoutData(ctlRoom, -1));
        hc.setWidth((int)(lblRoom+(int)ctlRoom));
		add(hc);
		return cb;
	}

    private static final DateTimeFormat dateFmt = DateTimeFormat.getFormat("MM/dd/yyyy");

	/**
	 * Add a date control to a vertical layout.
	 *
	 * @param lblRoom width of the label
	 * @param ctlRoom width of the control
	 * @param lbl the text of the label
	 * @return the control
	 */
	public DateField addDateField(double lblRoom, double ctlRoom, String lbl) {
		HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
		DateField dateCtl = new DateField(new DateTimePropertyEditor(dateFmt));
		Label lblCtl = new Label(lbl);
		hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
		hc.add(dateCtl, new HorizontalLayoutData(ctlRoom, -1));
        SimplePanel sp = new SimplePanel();
        sp.setWidth("1em");
        hc.add(sp);
        Label elbl = new Label("(mm/dd/yyyy)");
        hc.add(elbl);
        hc.setWidth((int)(lblRoom+(int)ctlRoom)+elbl.getOffsetWidth());
		add(hc);
		return dateCtl;
	}

    public ComboBox addComboBox(ComboBox comboBox, double lblRoom, double ctlRoom, String lbl) {
        HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
        Label lblCtl = new Label(lbl);
        hc.add(lblCtl, new HorizontalLayoutData(lblRoom, -1));
        hc.add(comboBox, new HorizontalLayoutData(ctlRoom, -1));
        hc.setWidth((int)(lblRoom+(int)ctlRoom));
        add(hc);
        return comboBox;
    }

	public void add(HorizontalLayoutContainer h) {
		add(h, new VerticalLayoutData(-1, additionalSpace));
	}


    public VerticalLayout addSpace(String amt) {
        Label lbl = new Label("");
        lbl.setHeight(amt);
        add(lbl);
        return this;
    }


}
