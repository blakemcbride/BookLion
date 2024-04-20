package booklion.client.speciesedit;

import booklion.client.chapterlist.*;
import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class SpeciesLog extends EditLog {

    private SpeciesList speciesList;
    private ChapterComboBox chapterComboBox;
    private static final String logInstructions = "INSTRUCTIONS\n\n" +
            "Use the area in this gray box to specify things you know about the species.  Events that occur to the species during the course of the book " +
            "should not be recorded here, the chapter logs are for that purpose.\n\n" +
            "For example, you could record things like Elves are fast, or Orcs are large and slow.  You would not typically include events that transpire " +
            "to a species over the course of a chapter.\n\n" +
            "To add to this area, use the chapter selector above to select the chapter that you found the information in, and then click on edit.";

    public SpeciesLog init(SpeciesList speciesList) {
        this.speciesList = speciesList;
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

        final SpeciesList list = speciesList;
        chapterComboBox = ChapterComboBox.createChapterComboBoxWithAllOption(250);
        chapterComboBox.setChapterId(-1L);  //  init to "All Chapters"
        final SelectionHandler vch = chapterComboBox.getSelectionChangeHandler();
        chapterComboBox.addSelectionHandler(new SelectionHandler<booklion.client.chapterlist.Record>() {
            @Override
            public void onSelection(SelectionEvent<booklion.client.chapterlist.Record> event) {
                vch.onSelection(event);  // chain to the previous value change handler
                list.changeSelection();
            }
        });

        return this;
    }

    /**
     * This limits records displayed to only those related to the restricted view
     *
     * @param rec
     * @return
     */
    protected boolean acceptRecord(LogRecord rec) {
        return Information.acceptChapterId(rec.getChapterId());
    }

    private static final String next = "Next";
    private static final String previous = "Previous";
    private static final String editLogMenu = "Edit";
    private static final String saveLog = "Save";
    private static final String undoChanges = "Undo";


    @Override
    protected void createSectionMenu() {
        addMenuItem(chapterComboBox);
        addMenuItem(previous, new LocalClickHandler(previous));
        addMenuItem(next, new LocalClickHandler(next));
        addMenuItem(editLogMenu, new LocalClickHandler(editLogMenu));
    }

    @Override
    protected void createSaveMenu() {
        clearSectionMenu();
        addMenuItem(chapterComboBox);
        addMenuItem(previous, new LocalClickHandler(previous));
        addMenuItem(next, new LocalClickHandler(next));
        addMenuItem(saveLog, new LocalClickHandler(saveLog));
        if (!originalTextEmpty())
            addMenuItem(undoChanges, new LocalClickHandler(undoChanges));
    }

    public void setEnabled(boolean flg) {
        super.setEnabled(flg);
    }

    long getChapterId() {
        return chapterComboBox.getChapterId();
    }

    private void setLog(Record rec, String str) {
        str = StringUtils.normalize(str);
        long chapterId = chapterComboBox.getChapterId();
        List<Long> allChapterIds = chapterComboBox.getAllChapterIds();
        LogRecord [] logs = rec.getLogs();
        ArrayList<LogRecord> newLogs = new ArrayList<LogRecord>();
        for (long cid : allChapterIds) {
            LogRecord log = null;
            for (LogRecord r : logs)
                if (r.getChapterId() == cid) {
                    log = r;
                    break;
                }
            if (log != null) {
                if (chapterId == cid)
                    log.setLog(str);
                newLogs.add(log);
            } else {
                if (chapterId == cid) {
                    log = new LogRecord();
                    log.setChapterId(cid);
                    log.setLog(str);
                    newLogs.add(log);
                }
            }
        }
        rec.setLogs(newLogs.toArray(new LogRecord[newLogs.size()]));
    }

    private boolean isChapterPrior() {
        List<Long> allChapterIds = chapterComboBox.getAllChapterIds();
        long selectedChapterId = getChapterId();
        long firstIntroduced = speciesList.getGrid().getSelectionModel().getSelectedItem().getChapterId();
        if (selectedChapterId == firstIntroduced)
            return false;
        for (long chapterId : allChapterIds) {
            if (chapterId == firstIntroduced)
                return false;
            if (chapterId == selectedChapterId)
                return true;
        }
        return true;  //  should never happen
    }

    private class LocalClickHandler implements ClickHandler {

        private String selector;

        LocalClickHandler(String s) {
            selector = s;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (selector == previous) {
                chapterComboBox.previous();
                speciesList.changeSelection();
            } else if (selector == next) {
                chapterComboBox.next();
                speciesList.changeSelection();
            } else if (selector == editLogMenu) {
                if (Message.notForGuest())
                    return;
                if (getChapterId() == -1L) {  //  all chapters selected
                    Message.msgOk("Species log cannot be edited when All Chapters is selected.  You must first select the chapter the log is to be associated with.");
                } else if (isChapterPrior()) {
                    Message.msgOk("You can't add a species log prior to when the species was introduced.  You can edit the species to change when they were introduced.");
                } else {
                    String txt = getText();
                    if (txt != null  &&  txt.equals(logInstructions))
                        setText("");
                    saveText();
                    clearSectionMenu();
                    addMenuItem(chapterComboBox);
                    addMenuItem(previous, new LocalClickHandler(previous));
                    addMenuItem(next, new LocalClickHandler(next));
                    setEnabled(true);
                }
            } else if (selector == saveLog) {
                Record rec = speciesList.getGrid().getSelectionModel().getSelectedItem();
                setLog(rec, getText());
                UserInput.disable();
                SpeciesEditService.App.getInstance().updateRecord(Information.getUserUuid(), rec, new AsyncCallback<StandardReturn>() {
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
                            addMenuItem(chapterComboBox);
                            addMenuItem(previous, new LocalClickHandler(previous));
                            addMenuItem(next, new LocalClickHandler(next));
                            Information.setDirty(false);
                            Message.notice("Data updated.");
                        } else {
                            Message.msgOk(result.getMsg());
                        }
                    }
                });
            } else if (selector == undoChanges) {
                restoreText();
                clearSectionMenu();
                addMenuItem(chapterComboBox);
                addMenuItem(previous, new LocalClickHandler(previous));
                addMenuItem(next, new LocalClickHandler(next));
            }
        }
    }

    public void update(String txt) {
        if ((txt == null  ||  txt.isEmpty()) && getChapterId() == -1)
            super.update(logInstructions);
        else
            super.update(txt);
    }

}
