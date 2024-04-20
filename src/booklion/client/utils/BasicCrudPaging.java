package booklion.client.utils;

import java.util.ArrayList;
import java.util.List;

import booklion.client.login.Login;

import booklion.client.supervisormain.SupervisorMain;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
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
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

/**
 * @author Blake McBride
 * @param <REC>
 */
public abstract class BasicCrudPaging<REC> implements IUpdate<REC> {

    private Grid<REC> grid;
    private PagingToolBar toolBar;
    protected int numDeletesLeft;
    protected int totalDeletes;
    protected int successfulDeletes;
    private Widget mainGrid;
    protected Dialog searchDlg;
    protected String searchText1;
    protected String searchText2;

    protected abstract void defineGridColumns(List<ColumnConfig<REC, ?>> collst);
    protected abstract String getMainKey(REC item);
    protected abstract void configureGrid(FramedPanel cp);
    protected abstract void getPosts(String uuid, PagingLoadConfig config, AsyncCallback<PagingLoadResult<REC>> callback);
    protected abstract void deleteRecord(String uuid, REC rec, AsyncCallback<StandardReturn> callback);
    protected abstract void createButtons();

    protected void run(boolean hasButtons) {
        DockLayoutPanelEx panel = SupervisorMain.getPanel();
        panel.clear();

        mainGrid = createGrid();
        searchDlg = createSearchDialog();
        if (hasButtons) {
            Widget buttons = createButtonsInt();
            buttons.setLayoutData(new VerticalLayoutData());
            buttons.setHeight("30px");
            panel.addSouth(buttons, 4);
        }
        panel.add(mainGrid);
        panel.forceLayout();
    }

    protected void run() {
        run(true);
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
    protected int getNumSelectedRecords() {
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
    }

    protected RowDoubleClickHandler getDoubleClickHandler() {
        return doubleClickHandler;
    }

    private Widget createGrid() {
        RpcProxy<PagingLoadConfig, PagingLoadResult<REC>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<REC>>() {

            @Override
            public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<REC>> callback) {
                getPosts(Login.getUUID(), loadConfig, callback);
            }
        };


        ListStore<REC> store = new ListStore<REC>(
                new ModelKeyProvider<REC>() {
                    @Override
                    public String getKey(REC item) {
                        return getMainKey(item);
                    }
                });

        final PagingLoader<PagingLoadConfig, PagingLoadResult<REC>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<REC>>(proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, REC, PagingLoadResult<REC>>(store));

        toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);

        List<ColumnConfig<REC, ?>> collst = new ArrayList<ColumnConfig<REC, ?>>();
        defineGridColumns(collst);

        ColumnModel<REC> cm = new ColumnModel<REC>(collst);

        grid = new Grid<REC>(store, cm) {
            @Override
            protected void onAfterFirstAttach() {
                super.onAfterFirstAttach();
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        loader.load();
                    }
                });
            }
        };
        grid.getView().setForceFit(true);
        grid.setLoadMask(true);
        grid.setLoader(loader);
//		grid.getView().setAutoFill(true);
        grid.getView().setAutoExpandColumn(collst.get(1));

        grid.addRowDoubleClickHandler(doubleClickHandler);

        FramedPanel cp = new FramedPanel();
        // cp.setCollapsible(true);
        cp.addStyleName("margin-10");
        configureGrid(cp);

        VerticalLayoutContainer con = new VerticalLayout();
        con.setBorders(true);
        con.add(grid, new VerticalLayoutData(1, 1));
//		toolBar.setHeight(200);
//		toolBar.setLayoutData(new VerticalLayoutData(-1, -1));
        con.add(toolBar, new VerticalLayoutData(1, -1));
        cp.setWidget(con);

//		NorthSouthContainer con = new NorthSouthContainer();
//		con.setBorders(true);
//		con.setNorthWidget(grid);
//		con.setSouthWidget(toolBar);
//		cp.setWidget(con);

        return cp;
    }

    private HorizontalLayoutContainer buttonLayout;

    protected void addButton(String lbl, SelectHandler handler) {
        TextButton btn = new TextButton(lbl);
        btn.addSelectHandler(handler);
        buttonLayout.add(btn, new HorizontalLayoutData(btn.getMinWidth(), 20, new Margins(10, 0, 0, 20)));
    }

    private Widget createButtonsInt() {
        buttonLayout = new HorizontalLayoutContainer();
        createButtons();
        return buttonLayout;
    }

    /**
     * Intended to be overridden if needed.
     *
     * @param dlg
     */
    protected void setupSearchPopup(Dialog dlg) {
        //  do nothing
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
            }

            @Override
            public void onSuccess(StandardReturn result) {
                UserInput.enable();
                if (result.getReturnCode() == StandardReturn.SUCCESS)
                    successfulDeletes++;
                if (--numDeletesLeft <= 0) {
                    if (successfulDeletes != 0)
                        toolBar.refresh();
                    if (successfulDeletes == totalDeletes)
                        Message.notice("UserData(s) deleted successfully.");
                    else if (totalDeletes == 1)
                        Message.msgOk(result.getMsg());
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
     * Method should be overridden
     */
    protected void setSearchFields() {
        //  do nothing
    }

    public void update(REC record) {
        grid.getStore().update(record);  // update grid row display
    }

    public void refresh(StandardReturn ret) {
        toolBar.refresh();
    }

    public void refreshFromBeginning() {
        toolBar.first();  // update grid from first record
    }

    protected Grid getGrid() {
        return grid;
    }
}
