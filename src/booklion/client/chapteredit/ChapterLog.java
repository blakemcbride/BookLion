package booklion.client.chapteredit;

import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;


/**
 * @author Blake McBride
 * Date: 2/19/14
 */
public class ChapterLog extends EditLog {

    private ChapterList chapterList;
    private static final String logInstructions = "INSTRUCTIONS\n\n" +
            "Use the area in this gray box to specify events that occur during the course of the chapter.  Qualities associated to characters " +
            "should not be recorded here, the character logs are for that purpose.\n\n" +
            "To add to this area, simply click on edit above.";

    public ChapterLog init(ChapterList list) {
        chapterList = list;
        final DockLayoutPanelEx panel = SupervisorMain.getPanel();

        Widget leftTop = panel.getNorth();
        Widget leftCenter = panel.getCenter();

        run(panel);

        Widget rightTop = panel.getNorth();
        Widget rightCenter = panel.getCenter();

        LayoutPanel layout = new LayoutPanel();

        layout.add(leftTop);
        layout.setWidgetLeftWidth(leftTop, 0, Style.Unit.PCT, 50, Style.Unit.PCT);

        layout.add(leftCenter);
        layout.setWidgetLeftWidth(leftCenter, 0, Style.Unit.PCT, 50, Style.Unit.PCT);
        layout.setWidgetTopBottom(leftCenter, 30, Style.Unit.PT, 0, Style.Unit.PT);

        layout.add(rightTop);
        layout.setWidgetRightWidth(rightTop, 0, Style.Unit.PCT, 50, Style.Unit.PCT);

        layout.add(rightCenter);
        layout.setWidgetRightWidth(rightCenter, 0, Style.Unit.PCT, 50, Style.Unit.PCT);
        layout.setWidgetTopBottom(rightCenter, 30, Style.Unit.PT, 0, Style.Unit.PT);

        panel.clear();
        panel.add(layout);

        return this;
    }

    private static final String editLogMenu = "Edit Summary";
    private static final String saveLog = "Save Summary";
    private static final String undoChanges = "Undo Changes";

    @Override
    protected void createSectionMenu() {
        addMenuItem(editLogMenu, new LocalClickHandler(editLogMenu));
    }

    @Override
    protected void createSaveMenu() {
        addMenuItem(saveLog, new LocalClickHandler(saveLog));
        if (!originalTextEmpty())
            addMenuItem(undoChanges, new LocalClickHandler(undoChanges));
    }

    public void setEnabled(boolean flg) {
        super.setEnabled(flg);
    }

    private class LocalClickHandler implements ClickHandler {

        private String selector;

        LocalClickHandler(String s) {
            selector = s;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (selector == editLogMenu) {
                if (Message.notForGuest())
                    return;
                String txt = getText();
                if (txt != null  &&  txt.equals(logInstructions))
                    setText("");
                saveText();
                clearSectionMenu();
                setEnabled(true);
            } else if (selector == saveLog) {
                Record rec = chapterList.getGrid().getSelectionModel().getSelectedItem();
                rec.setLogs(getText());
                UserInput.disable();
                ChapterEditService.App.getInstance().updateRecord(Information.getUserUuid(), chapterList.getGrid().getSelectionModel().getSelectedItem(), new AsyncCallback<StandardReturn>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        UserInput.enable();
                        String msg = caught.getMessage();
                        if (msg == null)
                            Message.systemError("Error communicating with the server.");
                        else
                            Message.systemError(msg);
                    }

                    @Override
                    public void onSuccess(StandardReturn result) {
                        UserInput.enable();
                        if (result.getReturnCode() == StandardReturn.SUCCESS) {
                            clearSectionMenu();
                            Information.setDirty(false);
                            Message.notice("Summary updated.");
                        } else {
                            Message.msgOk(result.getMsg());
                        }
                    }
                });
            } else if (selector == undoChanges) {
                restoreText();
                clearSectionMenu();
            }
        }
    }

    public void update(String txt) {
        if ((txt == null  ||  txt.isEmpty()))
            super.update(logInstructions);
        else
            super.update(txt);
    }

}
