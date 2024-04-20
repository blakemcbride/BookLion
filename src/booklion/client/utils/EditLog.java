package booklion.client.utils;

import booklion.client.global.Information;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * @author Blake McBride
 * Date: 3/2/14
 */
public class EditLog {

    private TextArea textArea;
    private HorizontalPanel sectionMenu;
    private String originalText;
    private int border = 15;
    private int pc = 0;

    protected void runNoClear(DockLayoutPanelEx panel) {

        sectionMenu = createSectionMenuInt();
//        if (sectionMenu.getWidgetCount() > 0) {
            VerticalPanel vl = new VerticalPanel();
            SimplePanel sp = new SimplePanel();
            sp.setHeight(".75em");  // space above anchors
            vl.add(sp);
            vl.add(sectionMenu);
            panel.addNorth(vl, 4);
 //       }

        textArea = createResizableTextArea(panel);
        textArea.setEnabled(false);

        textArea.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!Information.isDirty()) {
                    createSaveMenu();
                    Information.setDirty(true);
                }

                /*  The remainder of the code in this function is used to insert two newlines whenever the user hits the enter key
                    in order to separate paragraphs by a blank line. We also have to delete consecutive newlines when the backspace
                    is hit.
                 */
                int c = event.getNativeKeyCode();
                int cp = textArea.getCursorPos();
                if (c == 13  ||  pc == 17  &&  c == 77) {  //  return or ^M
                    String t = textArea.getText();
                    String t1 = t.substring(0, cp);
                    String t2 = t.substring(cp);
                    String t3 = t1 + '\n' + t2;
                    textArea.setText(t3);
                    textArea.setCursorPos(cp+1);
                } else if (c == 8  &&  cp > 1) {  // backspace and at least 2 characters (possible 2 returns)
                    String t = textArea.getText();
                    char pChar = t.charAt(cp - 1);
                    char ppChar = t.charAt(cp - 2);
                    if (pChar == '\n' && ppChar == '\n') {
                        String t1 = t.substring(0, cp-1);
                        String t2 = t.substring(cp);
                        String t3 = t1 + t2;
                        textArea.setText(t3);
                        textArea.setCursorPos(cp-1);
                    }
                }
                pc = c;
            }
        });

    }

    //  to be overridden
    protected void createSaveMenu() {}

    protected void setEnabled(boolean flg) {
        textArea.setEnabled(flg);
    }

    private TextArea createResizableTextArea(DockLayoutPanel panel) {
        final FlowPanel wrapperDiv = new FlowPanel();

        wrapperDiv.setSize("100%", "100%");

        final TextArea textArea = new TextArea() {
            // provide style with color override
            private final static String styleClassName = "disabledStyle";
            private final static String styleClass = "." + styleClassName + " { color: black !important; }";

            {
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
                pc = 0;
            }

            @Override
            protected void onAfterFirstAttach() {
                super.onAfterFirstAttach();

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        int width = wrapperDiv.getOffsetWidth();
                        int height = wrapperDiv.getOffsetHeight();

                        width -= border * 2;
                        height -= border * 2;
                        // no calcs for borders...
                        setPixelSize(width, height);
                    }
                });
                getInputEl().getStyle().setProperty("whiteSpace", "pre-wrap"); // need to cause IE to wrap text
                getInputEl().getStyle().setPadding(border, Style.Unit.PX);
            }
        };

        wrapperDiv.add(textArea);
        panel.add(wrapperDiv);

        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                int width = wrapperDiv.getOffsetWidth();
                int height = wrapperDiv.getOffsetHeight();
                width -= border * 2;
                height -= border * 2;
                // no calcs for borders...
                textArea.setPixelSize(width, height);
            }
        });
        return textArea;
    }

    protected void saveText() {
        originalText = textArea.getText();
    }

    protected void restoreText() {
        if (originalText != null)
            textArea.setText(originalText);
        else
            textArea.clear();
        Information.setDirty(false);
        pc = 0;
    }

    protected boolean originalTextEmpty() {
        return originalText == null  ||  originalText.isEmpty();
    }

    protected void run(DockLayoutPanelEx panel) {
        panel.clear();
        runNoClear(panel);
    }

    private HorizontalPanel createSectionMenuInt() {
        sectionMenu = new HorizontalPanel();
        return sectionMenu;
    }

    public void clearSectionMenu() {
        sectionMenu.clear();
    }

    /**
     *
     * Intended to be overwritten if needed.
     */
    protected void createSectionMenu() {}

    protected void addMenuItem(String name,  ClickHandler handler) {
        Anchor a = new Anchor(name);
        a.setWordWrap(false);
        a.addClickHandler(handler);
        SimplePanel sp = new SimplePanel();
        sp.setWidth("2em");
        sectionMenu.add(sp);
        sectionMenu.add(a);
    }

    protected void addMenuItem(Widget w) {
        SimplePanel sp = new SimplePanel();
        sp.setWidth("2em");
        sectionMenu.add(sp);
        sectionMenu.add(w);
    }

    public void update(String txt) {
        if (txt == null)
            textArea.setText("");  // clear doesn't work on disabled controls
        else
            textArea.setText(txt);
        Information.setDirty(false);
        pc = 0;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public String getText() {
        return textArea.getText();
    }

}
