package booklion.client.characteredit;

import booklion.client.chapterlist.ChapterComboBox;
import booklion.client.species.SpeciesComboBox;
import booklion.client.utils.*;
import com.sencha.gxt.widget.core.client.Dialog;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 2/24/14
 */
public class CharacterListDialog  extends SpecificDialog {

    private long bookCharacterId;
    private TextControl lName;
    private TextControl fName;
    private TextControl suffix;
    private TextControl nickName;
    private TextControl affiliation;
    private TextControl occupation;
    private TextControl relationship;
    private RadioGroup gender;
    private RadioGroup relevance;
    private ChapterComboBox chapterComboBox;
    private SpeciesComboBox speciesComboBox;

    public CharacterListDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 155;

        dlg.setSize("400", "420");
        fName = v.addTextField(lblWidth, 200, 0, 50, "First name:");
        lName = v.addTextField(lblWidth, 200, 0, 60, "Last name:");


        suffix = v.addTextField(lblWidth, 200, 0, 5, "Suffix:");
        nickName = v.addTextField(lblWidth, 200, 0, 20, "Nickname:");

        HorizontalLayout h = new HorizontalLayout();
        h.addLabel(lblWidth/2, "Sex:");
        gender = new RadioGroup();
        h.addRadio(gender, 'M', "Male");
        h.addRadio(gender, 'F', "Female");
        h.addRadio(gender, 'N', "None");
        h.addRadio(gender, 'U', "Unspecified");
        v.add(h);

        h = new HorizontalLayout();
        h.addLabel(70, "Relevance:");
        relevance = new RadioGroup();
        h.addRadio(relevance, 'P', "Primary");
        h.addRadio(relevance, 'S', "Secondary");
        h.addRadio(relevance, 'I', "Incidental");
        h.addRadio(relevance, 'U', "Unspecified");
        v.add(h);

        affiliation = v.addTextField(lblWidth, 200, 0, 30, "Affiliation:");
        occupation = v.addTextField(lblWidth, 200, 0, 30, "Occupation:");
        relationship = v.addTextField(lblWidth, 200, 0, 30, "Relationship:");


        chapterComboBox = ChapterComboBox.createChapterComboBox();
        v.addComboBox(chapterComboBox, lblWidth, 200, "Chapter first appeared in:");
        speciesComboBox = SpeciesComboBox.createSpeciesComboBox();
        v.addComboBox(speciesComboBox, lblWidth, 200, "Species:");
    }

    private boolean isEmpty(String s) {
        return s == null  ||  s.isEmpty();
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();
        if (isEmpty(lName.getText())  &&  isEmpty(fName.getText())  &&  isEmpty(nickName.getText()))
            ec.add("First, last, or nickname must be specified.");
        if (chapterComboBox.getSelection() == null)
            ec.add("Valid chapter must be selected.");
        if (speciesComboBox.getSelection() == null)
            ec.add("Valid species must be selected.");
        return ec;
    }

    public String initAddPopup() {
        bookCharacterId = 0;
        lName.setText("");
        fName.setText("");
        suffix.setText("");
        nickName.setText("");
        gender.setGroupValue('U');
        relevance.setGroupValue('U');
        affiliation.setText("");
        occupation.setText("");
        relationship.setText("");
        chapterComboBox.clearInvalid();
        speciesComboBox.clearInvalid();
        setInitialFocus(fName);
        return "New Character";
    }

    public String getData(Record rec) {
        bookCharacterId = rec.getBookCharacterId();
        lName.setText(rec.getLname());
        fName.setText(rec.getFname());
        suffix.setText(rec.getSuffix());
        nickName.setText(rec.getNickname());
        gender.setGroupValue(rec.getGender().charAt(0));
        relevance.setGroupValue(rec.getRelevance().charAt(0));
        affiliation.setText(rec.getAffiliation());
        occupation.setText(rec.getOccupation());
        relationship.setText(rec.getRelationship());
        chapterComboBox.clearInvalid();
        chapterComboBox.setChapterId(rec.getChapterId());
        speciesComboBox.clearInvalid();
        speciesComboBox.setSpeciesId(rec.getSpeciesId());
        setInitialFocus(fName);
        return "Edit Character";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setBookCharacterId(bookCharacterId);
        rec.setLname(lName.getText());
        rec.setFname(fName.getText());
        rec.setSuffix(suffix.getText());
        rec.setNickname(nickName.getText());
        rec.setGender((Character)gender.getGroupValue() + "");
        rec.setRelevance((Character)relevance.getGroupValue() + "");
        rec.setAffiliation(affiliation.getText());
        rec.setOccupation(occupation.getText());
        rec.setRelationship(relationship.getText());
        rec.setChapterId(chapterComboBox.getChapterId());
        rec.setSpeciesId(speciesComboBox.getSpeciesId());
    }

    String getFname() {
        return fName == null ? null : fName.getText();
    }

    String getLname() {
        return lName == null ? null : lName.getText();
    }

}
