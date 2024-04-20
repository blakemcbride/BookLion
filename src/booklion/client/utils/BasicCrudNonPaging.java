package booklion.client.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.*;
import com.sencha.gxt.widget.core.client.grid.Grid;


/**
 * @author Blake McBride
 * @param <REC>
 */
public abstract class BasicCrudNonPaging<REC> implements IUpdate<REC>, IButtons {

    private Grid<REC> grid;
    protected int numDeletesLeft;
    protected int totalDeletes;
    protected int successfulDeletes;
    private Widget mainGrid;
    private HorizontalPanel sectionMenu;
    private HorizontalLayoutContainer buttons;
    protected Dialog searchDlg;
    protected String searchText1;
    protected String searchText2;

    protected abstract void defineGridColumns(List<ColumnConfig<REC, ?>> collst);
    protected abstract String getMainKey(REC item);
    protected abstract void configureGrid(FramedPanel cp);
    protected abstract void deleteRecord(String uuid, REC rec, AsyncCallback<StandardReturn> callback);

    protected void runNoClear(DockLayoutPanelEx panel, REC[] records) {
        mainGrid = createGrid(records);
        searchDlg = createSearchDialog();
        sectionMenu = createSectionMenuInt();
        if (sectionMenu.getWidgetCount() > 0) {
            VerticalPanel vl = new VerticalPanel();
            SimplePanel sp = new SimplePanel();
            sp.setHeight(".75em");  // space above anchors
            vl.add(sp);
            sectionMenu.setWidth("3.7in");
            vl.add(sectionMenu);
            panel.addNorth(vl, 4);
        }
        buttons = createButtonsInt();
        if (buttons.getWidgetCount() > 0) {
            buttons.setLayoutData(new VerticalLayoutData());
            buttons.setHeight("30px");
            panel.addSouth(buttons, 4);
        }
        panel.add(mainGrid);
 //       panel.forceLayout();  forceLayout cannot be here for IE 8-10
    }

    protected void run(DockLayoutPanelEx panel, REC[] records) {
        panel.clear();
        runNoClear(panel, records);
    }

    /**
     *
     * @return the selected record in the grid
     */
    protected REC getSelectedRecord() {
        GridSelectionModel<REC> sm = grid.getSelectionModel();
        List<REC> recs = sm.getSelectedItems();
        if (recs.size() < 1) {
            return null;
        } else if (recs.size() > 1) {
            return null;
        } else
            return recs.get(0);
    }

    protected void deselectAll() {
        GridSelectionModel<REC> sm = grid.getSelectionModel();
        sm.deselectAll();
    }

    /**
     *
     * @return a List of the selected records in the grid
     */
    protected List<REC> getSelectedRecords() {
        GridSelectionModel<REC> sm = grid.getSelectionModel();
        return sm.getSelectedItems();
    }

    /**
     *
     * @return the number of rows selected
     */
    public int getNumSelectedRecords() {
        GridSelectionModel<REC> sm = grid.getSelectionModel();
        List<REC> recs = sm.getSelectedItems();
        return recs.size();
    }

    protected ColumnConfig<REC, String> addStringColumn(List<ColumnConfig<REC, ?>> collst, ValueProvider<REC, String> vp, int width, String title) {
        ColumnConfig<REC, String> col = new ColumnConfig<REC, String>(vp, width, title);
        collst.add(col);
        col.setSortable(false);
        return col;
    }

    protected ColumnConfig<REC, Long> addLongColumn(List<ColumnConfig<REC, ?>> collst, ValueProvider<REC, Long> vp, int width, String title) {
        ColumnConfig<REC, Long> col = new ColumnConfig<REC, Long>(vp, width, title);
        collst.add(col);
        col.setSortable(false);
        return col;
    }

    private RowDoubleClickHandler doubleClickHandler;

    protected void setDoubleClickHandler(RowDoubleClickHandler dch) {
        doubleClickHandler = dch;
        grid.addRowDoubleClickHandler(dch);
    }

    protected RowDoubleClickHandler getDoubleClickHandler() {
        return doubleClickHandler;
    }

    protected void setSelectionHandler(SelectionHandler sh) {
        grid.getSelectionModel().addSelectionHandler(sh);
    }

    private Widget createGrid(REC [] records) {
        ListStore<REC> store = new ListStore<REC>(
                new ModelKeyProvider<REC>() {
                    @Override
                    public String getKey(REC item) {
                        return getMainKey(item);
                    }
                });
        if (records != null)
            for (REC rec : records)
                if (acceptRecord(rec))
                    store.add(rec);

        List<ColumnConfig<REC, ?>> collst = new ArrayList<ColumnConfig<REC, ?>>();
        defineGridColumns(collst);

        ColumnModel<REC> cm = new ColumnModel<REC>(collst);

        grid = new Grid<REC>(store, cm);
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
//		grid.getView().setAutoFill(true);
//        grid.getView().setAutoExpandColumn(collst.get(1));

//        grid.addRowDoubleClickHandler(doubleClickHandler);

        FramedPanel cp = new FramedPanel();
        // cp.setCollapsible(true);
//        cp.addStyleName("margin-10");
        configureGrid(cp);

        VerticalLayoutContainer con = new VerticalLayout();
        con.setBorders(true);
        con.add(grid, new VerticalLayoutData(1, 1));
//		toolBar.setHeight(200);
//		toolBar.setLayoutData(new VerticalLayoutData(-1, -1));

        cp.setWidget(con);

//		NorthSouthContainer con = new NorthSouthContainer();
//		con.setBorders(true);
//		con.setNorthWidget(grid);
//		con.setSouthWidget(toolBar);
//		cp.setWidget(con);

        return cp;
    }

    /**
     * This should be overridden if you want to limit what is displayed.
     *
     * @param rec
     * @return true if record should be displayed
     */
    protected boolean acceptRecord(REC rec) {
        return true;
    }

    public void updateGrid(REC [] records) {
        ListStore<REC> store = grid.getStore();
        store.clear();
        if (records != null)
            for (REC rec : records)
                if (acceptRecord(rec))
                    store.add(rec);
    }

    private HorizontalLayoutContainer buttonLayout;

    public void addButton(String lbl, SelectHandler handler) {
        TextButton btn = new TextButton(lbl);
        btn.addSelectHandler(handler);
//        buttonLayout.add(btn, buttonLayoutData);
        buttonLayout.add(btn, new HorizontalLayoutData(btn.getMinWidth(), 20, new Margins(10, 0, 0, 20)));
    }

    protected void addMenuItem(String name,  ClickHandler handler) {
        Anchor a = new Anchor(name);
        a.setWordWrap(false);
        a.addClickHandler(handler);
        SimplePanel sp = new SimplePanel();
        sp.setWidth("2em");
        sectionMenu.add(sp);
        sectionMenu.add(a);
    }

    private HorizontalPanel createSectionMenuInt() {
        sectionMenu = new HorizontalPanel();
        createSectionMenu();
        return sectionMenu;
    }

    private HorizontalLayoutContainer createButtonsInt() {
        buttonLayout = new HorizontalLayoutContainer();
        createButtons();
        return buttonLayout;
    }

    /**
     *
     * Intended to be overwritten if needed.
     */
    protected void createButtons() {}

    /**
     *
     * Intended to be overwritten if needed.
     */
    protected void createSectionMenu() {}

    /**
     * Intended to be overridden if needed.
     *
     * @param dlg
     */
    protected void setupSearchPopup(Dialog dlg) {
        //  do nothing
    }

    public void setSelection(REC record) {
        List<REC> lst = new LinkedList<REC>();
        if (record != null)
            lst.add(record);
        grid.getSelectionModel().setSelection(lst);
        // now make sure the row is not scrolled out of view
        if (record != null) {
            GridView view = grid.getView();
            view.ensureVisible(view.findRowIndex(view.getRow(record)), 0, false);
        }
    }

    /**
     * Returns the name of the column currently acting as the sort column.  Null is returned if the user
     * never selected a column.  In this case the default sort order can be assumed.
     *
     * @return name of the sort column
     */
    protected String sortField() {
        List<?> lst = grid.getLoader().getSortInfo();
        if (lst.isEmpty())
            return null;  // no user selected sort column, assume default
        SortInfo si = (SortInfo) lst.get(0);
        return si.getSortField();
    }

    protected void deleteRecordInt(String uuid, REC rec) {
        AsyncCallback<StandardReturn> callback = new AsyncCallback<StandardReturn>() {

            @Override
            public void onFailure(Throwable caught) {
                UserInput.enable();
                String msg = caught.getMessage();
                if (msg == null)
                    Message.systemError("Error communicating with the server.");
                else
                    Message.systemError(msg);
                refresh(null);
            }

            @Override
            public void onSuccess(StandardReturn result) {
                UserInput.enable();
                if (result.getReturnCode() == StandardReturn.SUCCESS)
                    successfulDeletes++;
                if (--numDeletesLeft <= 0) {
                    if (successfulDeletes != 0)
                        refresh(result);
                    if (successfulDeletes == totalDeletes)
                        Message.notice("UserData(s) deleted successfully.");
                    else if (totalDeletes == 1)
//                        Message.systemError(result.getMsg());
                        Message.msgOk("The record was not deleted.  This is probably because other records are using this record.");
                    else if (successfulDeletes == 0)
                        Message.msgOk("None of the selected records were deleted.");
                    else
                        Message.msgOk("Some, but not all, of the selected records were deleted.");
                }
            }
        };
        UserInput.disable();
        deleteRecord(uuid, rec, callback);
    }


    /**
     * Configure search dialog.  Should be overridden
     *
     * @param dlg
     * @param v
     */
    protected void configureSearchPopup(Dialog dlg, VerticalLayout v) {
    }

    private Dialog createSearchDialog() {
        Dialog dlg = new Dialog();
        dlg.setModal(true);
        dlg.setPredefinedButtons(PredefinedButton.CANCEL, PredefinedButton.OK);
//		dlg.setHeadingText("Edit User");
//		dlg.setSize("100", "200");
        dlg.setBodyStyleName("background: none; padding: 5px");
        TextButton cancelBtn = dlg.getButtonById(PredefinedButton.CANCEL.name());
        TextButton okBtn = dlg.getButtonById(PredefinedButton.OK.name());
        okBtn.addSelectHandler(new SearchRecordHandler());
        cancelBtn.addSelectHandler(new SelectHandler(){
            @Override
            public void onSelect(SelectEvent event) {
                searchDlg.hide();
            }});

        VerticalLayout v = new VerticalLayout();
        dlg.add(v);
        v.setPosition(5, 10);

        configureSearchPopup(dlg, v);
        return dlg;
    }

    private class SearchRecordHandler implements SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {
            searchDlg.hide();
            searchText1 = null;
            searchText2 = null;
            setSearchFields();
            refreshFromBeginning();  // update grid from first record
        }

    }

    /**
     * Methods should be overridden
     */
    protected void setSearchFields() {
        //  do nothing
    }

    protected void updateAdditional(REC record) {
        // do nothing
    }

    public void update(REC record) {
 //       refresh(null);
        /* in cases where what is being displayed includes something that has to be read, we must
           re-read rather than update the record in place.
         */
        grid.getStore().update(record);  // update grid row display
        updateAdditional(record);
    }

    public abstract void refresh(StandardReturn ret);

    public void refreshFromBeginning() {
        refresh(null);
    }

    public Grid<REC> getGrid() {
        return grid;
    }
}
