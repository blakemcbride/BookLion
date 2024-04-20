package booklion.client.bookmain;

import booklion.client.booklist.Record;
import booklion.client.chapterlist.ChapterComboBox;
import booklion.client.global.Information;
import booklion.client.species.SpeciesComboBox;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.TextArea;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.Radio;

import static booklion.client.utils.LayoutUtils.addSpace;
import static booklion.client.utils.LayoutUtils.*;

/**
 * @author Blake McBride
 * Date: 1/1/14
 */
public class BookMain implements EntryPoint, IUpdate<Record> {

    private ChapterComboBox chapterComboBox;
    private RadioGroup chapterRadioGroup;
    private static final String leftmargin = "2em";
    private BookMainDialog bookMainDialog;
    private static boolean showMessage = true;

    private Label bookTitle;
    private Label authors;
    private Label genre;
    private Label year;
    private TextAreaEx description;

    public void onModuleLoad() {
        DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        bookMainDialog = new BookMainDialog(this);

        ChapterComboBox.clearCache();
        SpeciesComboBox.clearCache();

        VerticalPanel vp = new VerticalPanel();

        HorizontalPanel hp = new HorizontalPanel();

        hp.add(createLeftSide());

        addSpace(hp, "5em");

        hp.add(createRightSide());

        vp.add(hp);

        HorizontalPanel hp2 = new HorizontalPanel();
        addSpace(hp2, leftmargin);
        Label lbl = new Label("(Please note that what you see on this page is just generic and very brief information.  Now that you've gone into the book, you can get to the real data by clicking on Chapters, Characters, Locations, or Species above.)");
        hp2.setWidth("50em");
        hp2.add(lbl);
        vp.add(hp2);
        addSpace(vp, "2em");


        vp.add(createDescription());


        panel.add(topCenter(vp));

        update(Information.getBook());

        panel.forceLayout();

        if (showMessage) {
            Message.msgOk("Please note that what you see on this page is just generic and very brief information.  Now that you've gone into the book, you can get to the real data by clicking on Chapters, Characters, Locations, or Species above.");
            showMessage = false;
        }
    }

    private VerticalPanel createLeftSide() {
        HorizontalPanel hp;
        final String vspace = "2em";

        VerticalPanel vp = new VerticalPanel();

        addSpace(vp, vspace);
        hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        hp.add(new Label("Book title:"));
        addSpace(hp, "1em");
        hp.add(bookTitle=new Label());
        vp.add(hp);

        addSpace(vp, vspace);
        hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        hp.add(new Label("Author(s):"));
        addSpace(hp, "1em");
        hp.add(authors=new Label());
        vp.add(hp);

        addSpace(vp, vspace);
        hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        hp.add(new Label("Genre:"));
        addSpace(hp, "1em");
        hp.add(genre=new Label());
        vp.add(hp);

        addSpace(vp, vspace);
        hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        hp.add(new Label("Year:"));
        addSpace(hp, "1em");
        hp.add(year=new Label());
        vp.add(hp);

        addSpace(vp, vspace);
        return vp;
    }

    private String combineAuthors(Record bookRec) {
        String authorsStr = bookRec.getAuthor1Name();
        String tmp = bookRec.getAuthor2Name();
        if (tmp != null  && !tmp.isEmpty())
            authorsStr += ", " + tmp;
        tmp = bookRec.getAuthor3Name();
        if (tmp != null  && !tmp.isEmpty())
            authorsStr += ", " + tmp;
        return authorsStr;
    }

    private VerticalPanel createRightSide() {
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp;
        final String vspace = "1em";

        chapterComboBox = ChapterComboBox.createChapterComboBox(300);
        chapterComboBox.setShowAll(true);

        addSpace(vp, vspace);
        chapterRadioGroup = new RadioGroup();
        Radio showAll = chapterRadioGroup.addRadio("show all chapters", false);
        showAll.addChangeHandler(new ShowAllHandler());
        vp.add(showAll);
        Radio showSome = chapterRadioGroup.addRadio("only show information up to and", true);
        if (!Information.isChapterLimit()) {
            chapterRadioGroup.setGroupValue(false);
            chapterComboBox.disable();
            chapterComboBox.setChapterId(-1); // nothing selected
        } else {
            chapterRadioGroup.setGroupValue(true);
            chapterComboBox.setChapterId(Information.getChapterLimitId());
        }
        showSome.addChangeHandler(new ShowSomeHandler());
        vp.add(showSome);

        hp = new HorizontalPanel();
        Label lbl = new Label();
        lbl.setWidth("1.1em");
        hp.add(lbl);
        hp.add(new Label("including a specific chapter:"));
        vp.add(hp);

        chapterComboBox.addSelectionHandler(new ChapterChangeHandler());
        vp.add(chapterComboBox);

        addSpace(vp, "2em");
        Anchor a = new Anchor("Edit this information");
        a.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Message.notForGuest())
                    return;
                bookMainDialog.show(EditDialog.UPDATERECORD, Information.getBook());
            }
        });
        vp.add(a);

        return vp;
    }

    private VerticalPanel createDescription() {
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        hp.add(new Label("Description:"));
        vp.add(hp);

        hp = new HorizontalPanel();
        addSpace(hp, leftmargin);
        description = new TextAreaEx();
 //       description.setText(bookRec.getDescription());
        description.setEnabled(false);
        description.setSize("50em", "15em");
        hp.add(description);
        vp.add(hp);
        return vp;
    }

    @Override
    public void update(Record bookRec) {
        bookTitle.setText(bookRec.getBookTitle());
        authors.setText(combineAuthors(bookRec));
        genre.setText(bookRec.getGenreName());
        year.setText("" + bookRec.getYearPublished());
        description.setText(bookRec.getDescription());
    }

    @Override
    public void refresh(StandardReturn ret) {

    }

    private class ShowAllHandler implements ChangeHandler {
        @Override
        public void onChange(ChangeEvent event) {
            Information.clearChapterLimit();
            chapterComboBox.disable();
            SupervisorMain.setBookHeader();
        }
    }

    private class ShowSomeHandler implements ChangeHandler {
        @Override
        public void onChange(ChangeEvent event) {
            booklion.client.chapterlist.Record rec = chapterComboBox.getCurrentValue();
            if (rec != null) {
                Information.setChapterLimit(rec.getChapterId(), rec.getSeqno(), rec.getChapterDesignation(), rec.getChapterName());
                Information.setValidChapterIds(chapterComboBox.getValidChapterIds(rec));
                SupervisorMain.setBookHeader();
            }
            chapterComboBox.enable();
        }
    }

    private class ChapterChangeHandler implements SelectionHandler<booklion.client.chapterlist.Record> {
        @Override
        public void onSelection(SelectionEvent<booklion.client.chapterlist.Record> event) {
            booklion.client.chapterlist.Record rec = event.getSelectedItem();
            Information.setChapterLimit(rec.getChapterId(), rec.getSeqno(), rec.getChapterDesignation(), rec.getChapterName());
            Information.setValidChapterIds(chapterComboBox.getValidChapterIds(rec));
            SupervisorMain.setBookHeader();
        }
    }

}
