package booklion.client.booklist;

import booklion.client.authorselect.AuthorList;
import booklion.client.genre.BaseDto;
import booklion.client.genre.GenreSelectionDialog;
import booklion.client.genre.IGenreSelection;
import booklion.client.utils.*;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 11/11/13
 */
public class BooklistDialog extends SpecificDialog {

    private TextControl bookTitle;
    private NumericControl yearPublished;
    private TextAreaControl description;
    private int genreId;
    private TextControl genreTxtCtl;
    private GenreSelectionDialog genreDialog;
    private AuthorGroup author1;
    private AuthorGroup author2;
    private AuthorGroup author3;

    private static class AuthorGroup {
        Long authorId;
        TextControl authorName;

        AuthorGroup(boolean required, String name) {
            authorName = new TextControl(required?1:0, 180, name);
            authorName.disable();
        }

    }

    public BooklistDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
        genreDialog = new GenreSelectionDialog();
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 90;
        int ctlRoom = 270;

        dlg.setSize("470", "580");
        bookTitle = v.addTextField(lblWidth, ctlRoom, 1, 80, "Book Title:");

        HorizontalLayout h = new HorizontalLayout();
        h.addLabel(lblWidth, "Genre:");
        genreTxtCtl = new TextControl(1, 180, "Genre");
        genreTxtCtl.disable();
        h.add(genreTxtCtl, new HorizontalLayoutContainer.HorizontalLayoutData(ctlRoom, -1));
        TextButton selectBtn = new TextButton("Select");
        selectBtn.addSelectHandler(new SelectGenre());
        h.add(selectBtn);
        v.add(h);

        h = new HorizontalLayout();
        h.addLabel(lblWidth, "Author 1:");
        author1 = new AuthorGroup(true, "Author 1");
        h.add(author1.authorName, new HorizontalLayoutContainer.HorizontalLayoutData(ctlRoom, -1));
        selectBtn = new TextButton("Select");
        selectBtn.addSelectHandler(new SelectAuthor(author1));
        h.add(selectBtn);
        v.add(h);

        h = new HorizontalLayout();
        h.addLabel(lblWidth, "Author 2:");
        author2 = new AuthorGroup(false, "Author 2");
        h.add(author2.authorName, new HorizontalLayoutContainer.HorizontalLayoutData(ctlRoom, -1));
        selectBtn = new TextButton("Select");
        selectBtn.addSelectHandler(new SelectAuthor(author2));
        h.add(selectBtn);
        selectBtn = new TextButton("Erase");
        selectBtn.addSelectHandler(new ClearAuthor(author2));
        h.add(selectBtn);
        v.add(h);

        h = new HorizontalLayout();
        h.addLabel(lblWidth, "Author 3:");
        author3 = new AuthorGroup(false, "Author 3");
        h.add(author3.authorName, new HorizontalLayoutContainer.HorizontalLayoutData(ctlRoom, -1));
        selectBtn = new TextButton("Select");
        selectBtn.addSelectHandler(new SelectAuthor(author3));
        h.add(selectBtn);
        selectBtn = new TextButton("Erase");
        selectBtn.addSelectHandler(new ClearAuthor(author3));
        h.add(selectBtn);
        v.add(h);


        yearPublished =  v.addNumberField(lblWidth, 50, 0, 0, 2100, "Year Published:");
        description = v.addTextArea("430px", "300px", 1, 2000, "Book Description:");
    }

    private class SelectGenre implements SelectEvent.SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {
            genreDialog.show(new GenreSelectionReturn());
        }
    }

    private class UpdateRecord implements IUpdate<booklion.client.authorselect.Record>  {

        private AuthorGroup authorGroup;

        UpdateRecord(AuthorGroup author) {
            this.authorGroup = author;
        }

        @Override
        public void update(booklion.client.authorselect.Record record) {
            authorGroup.authorId = record.getAuthorId();
            String fname = record.getFname();
            if (fname != null && !fname.isEmpty())
                authorGroup.authorName.setText(record.getLname() + ", " + record.getFname());
            else
                authorGroup.authorName.setText(record.getLname());
        }

        @Override
        public void refresh(StandardReturn ret) {

        }

    }

    private class SelectAuthor implements SelectEvent.SelectHandler {

        private AuthorGroup authorGroup;

        SelectAuthor(AuthorGroup author) {
            this.authorGroup = author;
        }

        @Override
        public void onSelect(SelectEvent event) {
            new AuthorList(new UpdateRecord(authorGroup));
        }
    }

    private class ClearAuthor implements SelectEvent.SelectHandler {

        private AuthorGroup authorGroup;

        ClearAuthor(AuthorGroup author) {
            this.authorGroup = author;
        }

        @Override
        public void onSelect(SelectEvent event) {
            authorGroup.authorId = null;
            authorGroup.authorName.setText("");
        }
    }

    private class GenreSelectionReturn implements IGenreSelection {

        @Override
        public void itemSelected(BaseDto selectedItem) {
            genreTxtCtl.setText(selectedItem.getName());
            genreId = selectedItem.getId();
        }
    }

    protected String getData(Record record) {
        bookTitle.setText(record.getBookTitle());
        yearPublished.setValue(record.getYearPublished());
        genreId = record.getGenreId();
        genreTxtCtl.setValue(record.getGenreName());

        author1.authorId = record.getAuthor1Id();
        author1.authorName.setValue(record.getAuthor1Name());
        author2.authorId = record.getAuthor2Id();
        author2.authorName.setValue(record.getAuthor2Name());
        author3.authorId = record.getAuthor3Id();
        author3.authorName.setValue(record.getAuthor3Name());

        description.setText(record.getDescription());
        setInitialFocus(bookTitle);
        return "Edit Book";
    }

    protected String initAddPopup() {
        bookTitle.clear();
        yearPublished.setValue(0);
        genreId = 0;
        genreTxtCtl.setText("");
//        description.clear();
        description.setText("");
        author1.authorId = null;
        author1.authorName.setText("");
        author2.authorId = null;
        author2.authorName.setText("");
        author3.authorId = null;
        author3.authorName.setText("");
        setInitialFocus(bookTitle);
        return "Add Book";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setBookTitle(bookTitle.getText());
        rec.setYearPublished(yearPublished.getIntValue());
        rec.setDescription(description.getText());
        rec.setGenreId(genreId);
        rec.setGenreName(genreTxtCtl.getText());
        rec.setAuthor1Id(author1.authorId);
        rec.setAuthor1Name(author1.authorName.getText());
        rec.setAuthor2Id(author2.authorId);
        rec.setAuthor2Name(author2.authorName.getText());
        rec.setAuthor3Id(author3.authorId);
        rec.setAuthor3Name(author3.authorName.getText());
    }
}
