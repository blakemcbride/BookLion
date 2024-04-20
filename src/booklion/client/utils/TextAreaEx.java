package booklion.client.utils;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * @author Blake McBride
 * Date: 3/8/14
 */
public class TextAreaEx extends TextArea {

    private final static String styleClassName = "disabledStyle";
    private final static String styleClass = "." + styleClassName + " { color: black !important; -webkit-text-fill-color: rgba(0, 0, 0, 1); -webkit-opacity: 1; }";
    private final static int border = 10;

    static {
        // inject style on instantiation
        StyleInjector.inject(styleClass);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        // main element that wraps the others, opacity does most of the gray effect
        getElement().getStyle().setOpacity(1);

        XElement textAreaElement = getElement().select("textarea").getItem(0).cast();

        // text area element
        XElement textAreaWrapper = getElement().getChild(0).cast();

        if (enabled) {
            textAreaElement.getStyle().setBackgroundColor("#fff");
            textAreaElement.removeClassName(styleClassName);
            textAreaWrapper.removeClassName(styleClassName);
        } else {
            textAreaElement.getStyle().setBackgroundColor("#eee");
            textAreaElement.addClassName(styleClassName);
            textAreaWrapper.addClassName(styleClassName);
        }
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();

        getInputEl().getStyle().setProperty("whiteSpace", "pre-wrap"); // need to cause IE to wrap text
        getInputEl().getStyle().setPadding(border, Style.Unit.PX);
    }

}
