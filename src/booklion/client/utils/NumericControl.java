package booklion.client.utils;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

/**
 * @author Blake McBride
 * Date: 11/16/13
 */
public class NumericControl implements HandleErrorProcessing {
    
    private NumberField nf;
    private  NumberPropertyEditor editor;
    private int dp;
    private int min;
    private int max;
    private String ctlName;
    private boolean errorParametersSpecified = false;
    private Label label;


    public NumericControl(int dp, int min, int max, String name) {
        if (dp == 0) {
            editor = new NumberPropertyEditor.IntegerPropertyEditor();
            nf = new NumberField(editor);
        } else {
            editor = new NumberPropertyEditor.DoublePropertyEditor();
            editor.setIncrement(Math.pow(10.0, -dp));
            nf = new NumberField(editor);
        }
        ctlName = name.replaceAll(":", "");
        errorParametersSpecified = true;
    }

    public NumberField getField() {
        return nf;
    }

    public void errorCheck(ErrorCheck ec) {
        if (!errorParametersSpecified)
            return;
        String sval = nf.getText();
        int len = sval.trim().length();

        if (len == 0) {
            if  (min > 0  ||  max < 0)
                ec.add(ctlName + " is required.");
        } else if (dp > 0) {
            double n = getDoubleValue();
            if (n < min  ||  n > max)
                ec.add(ctlName + " must be between " + min + " and " + max);
        } else {
            int n = getIntValue();
            if (n < min  ||  n > max)
                ec.add(ctlName + " must be between " + min + " and " + max);
        }
    }

    private boolean isEmpty(String sval) {
        return sval == null  ||  sval.isEmpty();
    }

    private boolean isNumber() {
        String sval = nf.getText();
        if (isEmpty(sval))
            return min <= 0  &&  max >= 0;
        try {
            if (dp > 0)
                Double.parseDouble(sval);
            else
                Integer.parseInt(sval);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getIntValue() {
        String sval = nf.getText();
        if (isEmpty(sval))
            return 0;
        int val;
        try {
            val= Integer.parseInt(sval);
        } catch (NumberFormatException e) {
            val = 0;
        }
        return val;
    }

    public double getDoubleValue() {
        String sval = nf.getText();
        if (isEmpty(sval))
            return 0.0;
        double val;
        try {
            val= Double.parseDouble(sval);
        } catch (NumberFormatException e) {
            val = 0.0;
        }
        return val;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setValue(int val) {
        nf.setValue(val);
    }

    public void setValue(double val) {
        nf.setValue(val);
    }

}
