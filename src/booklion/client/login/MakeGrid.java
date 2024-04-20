package booklion.client.login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.GridStateHandler;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MakeGrid  implements IsWidget, EntryPoint {

	  private static final StockProperties props = GWT.create(StockProperties.class);
	 
	  private ContentPanel root;
	  private Grid<Stock> grid;
    DockLayoutPanel con = null;

	  @Override
	  public Widget asWidget() {
	    if (root == null) {
	      final NumberFormat number = NumberFormat.getFormat("0.00");
	 
	      ColumnConfig<Stock, String> nameCol = new ColumnConfig<Stock, String>(props.name(), 50, "Company");
	      ColumnConfig<Stock, String> symbolCol = new ColumnConfig<Stock, String>(props.symbol(), 100, "Symbol");
	      ColumnConfig<Stock, Double> lastCol = new ColumnConfig<Stock, Double>(props.last(), 75, "Last");
	 
	      ColumnConfig<Stock, Double> changeCol = new ColumnConfig<Stock, Double>(props.change(), 100, "Change");
	      changeCol.setCell(new AbstractCell<Double>() {
	 
	        @Override
	        public void render(Context context, Double value, SafeHtmlBuilder sb) {
	          String style = "style='color: " + (value < 0 ? "red" : "green") + "'";
	          String v = number.format(value);
	          sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
	        }
	      });
	 
	      ColumnConfig<Stock, Date> lastTransCol = new ColumnConfig<Stock, Date>(props.lastTrans(), 100, "Last Updated");
	      lastTransCol.setCell(new DateCell(DateTimeFormat.getFormat("MM/dd/yyyy")));
	 
	      List<ColumnConfig<Stock, ?>> l = new ArrayList<ColumnConfig<Stock, ?>>();
	      l.add(nameCol);
	      l.add(symbolCol);
	      l.add(lastCol);
	      l.add(changeCol);
	      l.add(lastTransCol);
	      ColumnModel<Stock> cm = new ColumnModel<Stock>(l);
	 
	      ListStore<Stock> store = new ListStore<Stock>(props.key());
//	      store.addAll(TestData.getStocks());
	 
	      root = new ContentPanel();
	      root.setHeadingText("Basic Grid");
//	      root.getHeader().setIcon(ExampleImages.INSTANCE.table());
	      root.setPixelSize(600, 300);
	      root.addStyleName("margin-10");
	       
	      ToolButton info = new ToolButton(ToolButton.QUESTION);
	      ToolTipConfig config = new ToolTipConfig("Example Info", "This examples includes resizable panel, reorderable columns and grid state.");
	      config.setMaxWidth(225);
	      info.setToolTipConfig(config);
	      root.addTool(info);
	       
	      new Resizable(root, Dir.E, Dir.SE, Dir.S);
	 
	      grid = new Grid<Stock>(store, cm);
	      grid.getView().setAutoExpandColumn(nameCol);
	      grid.getView().setStripeRows(true);
	      grid.getView().setColumnLines(true);
	      grid.setBorders(false);
	 
	      grid.setColumnReordering(true);
	      grid.setStateful(true);
	      grid.setStateId("gridExample");
	 
	      GridStateHandler<Stock> state = new GridStateHandler<Stock>(grid);
	      state.loadState();
	 
	      ToolBar toolBar = new ToolBar();
	      toolBar.add(new LabelToolItem("Selection Mode: "));
	 
	      SimpleComboBox<String> type = new SimpleComboBox<String>(new StringLabelProvider<String>());
	      type.setTriggerAction(TriggerAction.ALL);
	      type.setEditable(false);
	      type.setWidth(100);
	      type.add("Row");
	      type.add("Cell");
	      type.setValue("Row");
	      // we want to change selection model on select, not value change which fires on blur
	      type.addSelectionHandler(new SelectionHandler<String>() {
	 
	        @Override
	        public void onSelection(SelectionEvent<String> event) {
	          boolean cell = event.getSelectedItem().equals("Cell");
	          if (cell) {
	            CellSelectionModel<Stock> c = new CellSelectionModel<Stock>();
	            c.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Stock>() {
	 
	              @Override
	              public void onCellSelectionChanged(CellSelectionChangedEvent<Stock> event) {
	 
	              }
	            });
	 
	            grid.setSelectionModel(c);
	          } else {
	            grid.setSelectionModel(new GridSelectionModel<Stock>());
	          }
	        }
	      });
	      type.addValueChangeHandler(new ValueChangeHandler<String>() {
	 
	        @Override
	        public void onValueChange(ValueChangeEvent<String> event) {
	 
	        }
	      });
	      toolBar.add(type);

            con = new DockLayoutPanel(Style.Unit.EM);

	      root.setWidget(con);
	 
	      con.addSouth(toolBar, 4);
	      con.add(grid);
	 
	      // needed to enable quicktips (qtitle for the heading and qtip for the
	      // content) that are setup in the change GridCellRenderer
	      new QuickTip(grid);
	    }
	 
//	    return root;
	    return con;
	  }
	 
	  @Override
	  public void onModuleLoad() {
	    StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));
	    RootPanel.get().add(asWidget());
	  }
}
