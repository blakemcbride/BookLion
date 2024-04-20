package booklion.client.utils;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author Blake McBride
 */
public class TextControl extends TextField implements HandleErrorProcessing {
	private int minLength = 0;
	private int maxLength = 10000;
	private String ctlName;
	private boolean errorParametersSpecified = false;
	private Label label;

    public TextControl(int min, int max, String name) {
        minLength = min;
        maxLength = max;
        ctlName = name.replaceAll(":", "");
        errorParametersSpecified = true;
    }
	
//	public TextControl setRange(int min, int max, String name) {
//		minLength = min;
//		maxLength = max;
//		ctlName = name.replaceAll(":", "");
//		errorParametersSpecified = true;
//		return this;
//	}
	
	public void errorCheck(ErrorCheck ec) {
		if (!errorParametersSpecified)
			return;
		String sval = getText();
		int len = sval.trim().length();
		
		if (len == 0  && minLength > 0)
			ec.add(ctlName + " is required.");
		else if (len < minLength)
			ec.add(ctlName + " must be at least " + minLength + " characters long.");
		else if (maxLength > 0  &&  len > maxLength)
			ec.add(ctlName + " can not be more than " + maxLength + " characters long.");
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
}
