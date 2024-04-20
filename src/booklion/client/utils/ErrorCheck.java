package booklion.client.utils;

import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author Blake McBride
 */
public class ErrorCheck {

	private StringBuilder eMsg = new StringBuilder();

	public ErrorCheck() {
	}

	public void add(String msg) {
		if (eMsg.length() != 0)
			eMsg.append("<br>");
		eMsg.append(msg);
	}

	public String stringValue() {
		return eMsg.length() == 0 ? null : new String(eMsg);
	}
	
	public void checkTextField(TextField fld, int min, int max, String fldName) {
		String sval = fld.getText();
		int len = sval.trim().length();
		
		if (len == 0  && min > 0)
			add(fldName + " is required.");
		else if (len < min)
			add(fldName + " must be at least " + min + " characters long.");
		else if (max > 0  &&  len > max)
			add(fldName + " can not be more than " + max + " characters long.");
	}

}
