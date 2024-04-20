package booklion.client.speciesedit;

import booklion.client.chapterlist.ChapterComboBox;
import booklion.client.utils.ErrorCheck;
import booklion.client.utils.IUpdate;
import booklion.client.utils.TextControl;
import booklion.client.utils.VerticalLayout;
import com.sencha.gxt.widget.core.client.Dialog;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class SpeciesDialog extends SpecificDialog {

    private long speciesId;
    private TextControl speciesName;
    private ChapterComboBox chapterComboBox;

    public SpeciesDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 150;

        dlg.setSize("380", "150");
        speciesName = v.addTextField(lblWidth, 200, 1, 30, "Species Name:");
        chapterComboBox = ChapterComboBox.createChapterComboBox();
        v.addComboBox(chapterComboBox, lblWidth, 200, "Chapter first appeared in:");
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();
        if (chapterComboBox.getSelection() == null)
            ec.add("Valid chapter must be selected.");
        return ec;
    }

    public String initAddPopup() {
        speciesId = 0;
        speciesName.setText("");
        chapterComboBox.clearInvalid();
        setInitialFocus(speciesName);
        return "New Species";
    }

    public String getData(Record rec) {
        speciesId = rec.getSpeciesId();
        speciesName.setText(rec.getSpeciesName());
        chapterComboBox.clearInvalid();
        chapterComboBox.setChapterId(rec.getChapterId());
        setInitialFocus(speciesName);
        return "Edit Species";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setSpeciesId(speciesId);
        rec.setSpeciesName(speciesName.getText());
        rec.setChapterId(chapterComboBox.getChapterId());
    }

    String getSpeciesName() {
        return speciesName == null ? null : speciesName.getText();
    }


}
