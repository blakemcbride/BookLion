package booklion.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * @author Blake McBride
 * Date: 3/27/14
 */
public class IntroText {
    interface IntroTextUiBinder extends UiBinder<DivElement, IntroText> {
    }

    private static IntroTextUiBinder ourUiBinder = GWT.create(IntroTextUiBinder.class);

    private DivElement rootElement;

    public IntroText() {
        rootElement = ourUiBinder.createAndBindUi(this);

        // be sure its injected on app load or widget load
        CustomResourcesForHtml.INSTANCE.customStyles().ensureInjected();

        // get the class name to use on the element
        String htmlStyle = CustomResourcesForHtml.INSTANCE.customStyles().htmlFormatting();
        rootElement.setClassName(htmlStyle);
    }

    public Element getElement() {
        return rootElement;
    }
}