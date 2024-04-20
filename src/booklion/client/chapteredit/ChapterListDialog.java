package booklion.client.chapteredit;


import booklion.client.global.Information;
import booklion.client.utils.*;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.grid.Grid;

import static booklion.client.utils.ControlUtils.setInitialFocus;

/**
 * @author Blake McBride
 * Date: 1/4/14
 */
public class ChapterListDialog extends SpecificDialog {

    private long chapterId;
    private TextControl chapterDesignation;
    private TextControl chapterName;

    public ChapterListDialog(IUpdate<Record> crudInstance) {
        super(crudInstance);
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v) {
        int lblWidth = 130;

        dlg.setSize("360", "140");
        chapterDesignation = v.addTextField(lblWidth, 100, 1, 20, "Chapter Designation:");
        chapterName = v.addTextField(lblWidth, 200, 0, 80, "Chapter Title:");
    }

    protected ErrorCheck errorCheck(int mode) {
        ErrorCheck ec = new ErrorCheck();
        return ec;
    }

    public String initAddPopup() {
        chapterId = 0;
        chapterDesignation.setText("");
        chapterName.setText("");
        setInitialFocus(chapterDesignation);
        return "Add Chapter";
    }

    public String getData(Record rec) {
        chapterId = rec.getChapterId();
        chapterDesignation.setText(rec.getChapterDesignation());
        chapterName.setText(rec.getChapterName());
        setInitialFocus(chapterDesignation);
        return "Edit Chapter";
    }

    protected void saveUpdatedData(Record rec, int mode) {
        rec.setChapterId(chapterId);
        rec.setBookId(Information.getBook().getBookId());
        rec.setChapterDesignation(chapterDesignation.getText());
        rec.setChapterName(chapterName.getText());
        if (mode == EditDialog.NEWRECORD)
            rec.setSeqno(getNextSeqno());
    }

    private short getNextSeqno() {
        short seqno = 1;
        ChapterList chapterList = (ChapterList) getCrudInstance();
        Grid<Record> grid = chapterList.getGrid();
        ListStore<Record> recs = grid.getStore();
        for (Record r : recs.getAll()) {
            short t = r.getSeqno();
            if (t >= seqno)
                seqno = (short) (t + (short) 1);
        }
        return seqno;
    }

    String getChapterDesignation() {
        return chapterDesignation == null ? null : chapterDesignation.getText();
    }
}
