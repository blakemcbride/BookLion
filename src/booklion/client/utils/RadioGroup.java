package booklion.client.utils;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.form.Radio;

/**
 * @author Blake McBride
 */
public class RadioGroup extends ToggleGroup {
	
	public Radio addRadio(String lbl, Object val) {
	    Radio radio = new Radio();
	    radio.setBoxLabel(lbl);
	    radio.setData("value", val);
	    add(radio);
		return radio;
	}
	
	public Object getGroupValue() {
		Iterator<HasValue<Boolean>> it = iterator();
		while (it.hasNext()) {
			Radio r = (Radio) it.next();
			if (r.getValue())
				return r.getData("value");
		}
		return null;
	}
	
	/**
	 * Spin through all the radio buttons in a group, find the one that has the passed in value, select it
	 *
	 * @param val Character, String, or Integer only
	 */
	public void setGroupValue(Object val) {
		Iterator<HasValue<Boolean>> it = iterator();
		while (it.hasNext()) {
			Radio r = (Radio) it.next();
			Object v = r.getData("value");
			if (val instanceof Character)
				r.setValue(((Character) v).charValue() == ((Character) val).charValue());
			else if (val instanceof String)
				r.setValue(((String) v).equals((String) val));
			else if (val instanceof Integer)
				r.setValue(((Integer) v).intValue() == ((Integer) val).intValue());
            else if (val instanceof Boolean)
                r.setValue(((Boolean) v).booleanValue() == ((Boolean) val).booleanValue());
		}
	}
	

}
