package booklion.client.utils;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.LinkedList;

/**
 * @author Blake McBride
 * Date: 11/6/13
 */
public abstract class EditRecordHandler implements SelectEvent.SelectHandler  {

    protected LinkedList<Widget> widgetList;  // a flattened list of widgets on the dialog
    protected Container internalDialog;
    protected ComplexPanel complexPanel;

    protected abstract ErrorCheck errorCheck();
    protected abstract void handleData();

    public EditRecordHandler() {
    }

    public EditRecordHandler(Container dlg) {
        internalDialog = dlg;
    }

    public EditRecordHandler(ComplexPanel panel) {
        complexPanel = panel;
    }

    @Override
    public void onSelect(SelectEvent event) {
        if (widgetList == null) {
            widgetList = getAllWidgets(internalDialog);
            widgetList = getAllWidgets(complexPanel);
        }
        ErrorCheck ec = errorCheck();
        if (ec == null)
            ec = new ErrorCheck();
        checkControls(ec, widgetList);
        if (ec != null) {
            String emsg = ec.stringValue();
            if (emsg != null) {
                Message.msgOk(emsg);
                return;
            }
        }
        handleData();
        if (internalDialog != null)
            internalDialog.hide();
    }

    protected LinkedList<Widget> getAllWidgets(Container dlg) {
        if (dlg == null)
            return widgetList;
        LinkedList<Widget> wlst = new LinkedList<Widget>();
        int n = dlg.getWidgetCount();
        for (int i=0 ; i < n ; i++) {
            Widget w = dlg.getWidget(i);
            if (w instanceof VerticalLayoutContainer)
                enumerateVert(wlst, (VerticalLayoutContainer) w);
            else if (w instanceof HorizontalLayoutContainer)
                enumerateHorz(wlst, (HorizontalLayoutContainer) w);
            else if (!(w instanceof Label))
                wlst.add(w);
        }
        return wlst;
    }

    protected LinkedList<Widget> getAllWidgets(ComplexPanel panel) {
        if (panel == null)
            return widgetList;
        LinkedList<Widget> wlst = new LinkedList<Widget>();
        int n = panel.getWidgetCount();
        for (int i=0 ; i < n ; i++) {
            Widget w = panel.getWidget(i);
            if (w instanceof VerticalLayoutContainer)
                enumerateVert(wlst, (VerticalLayoutContainer) w);
            else if (w instanceof HorizontalLayoutContainer)
                enumerateHorz(wlst, (HorizontalLayoutContainer) w);
            else if (!(w instanceof Label))
                wlst.add(w);
        }
        return wlst;
    }

    private void enumerateVert(LinkedList<Widget> wlst, VerticalLayoutContainer v) {
        int n = v.getWidgetCount();
        for (int i=0 ; i < n ; i++) {
            Widget w = v.getWidget(i);
            if (w instanceof VerticalLayoutContainer)
                enumerateVert(wlst, (VerticalLayoutContainer) w);
            else if (w instanceof HorizontalLayoutContainer)
                enumerateHorz(wlst, (HorizontalLayoutContainer) w);
            else if (!(w instanceof Label))
                wlst.add(w);
        }
    }

    private void enumerateHorz(LinkedList<Widget> wlst, HorizontalLayoutContainer h) {
        int n = h.getWidgetCount();
        for (int i=0 ; i < n ; i++) {
            Widget w = h.getWidget(i);
            if (w instanceof VerticalLayoutContainer)
                enumerateVert(wlst, (VerticalLayoutContainer) w);
            else if (w instanceof HorizontalLayoutContainer)
                enumerateHorz(wlst, (HorizontalLayoutContainer) w);
            else if (!(w instanceof Label))
                wlst.add(w);
        }
    }

    protected void checkControls(ErrorCheck ec, LinkedList<Widget> wlst) {
        for (Widget w : wlst)
            if (w instanceof HandleErrorProcessing)
                ((HandleErrorProcessing) w).errorCheck(ec);
    }

}
