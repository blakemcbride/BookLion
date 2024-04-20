package booklion.client.locationedit;

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
public class LocationListDialog extends SpecificDialog {

    private long locationId;
    private TextControl locName;
    private ChapterComboBox chapterComboBox;

    public LocationListDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 150;

        dlg.setSize("380", "150");
        locName = v.addTextField(lblWidth, 200, 1, 60, "Location Name:");
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
        locationId = 0;
        locName.setText("");
        chapterComboBox.clearInvalid();
        setInitialFocus(locName);
        return "New Location";
    }

    public String getData(Record rec) {
        locationId = rec.getLocationId();
        locName.setText(rec.getLocName());
        chapterComboBox.clearInvalid();
        chapterComboBox.setChapterId(rec.getChapterId());
        setInitialFocus(locName);
        return "Edit Location";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setLocationId(locationId);
        rec.setLocName(locName.getText());
        rec.setChapterId(chapterComboBox.getChapterId());
    }

    String getLocationName() {
        return locName == null ? null : locName.getText();
    }

}
