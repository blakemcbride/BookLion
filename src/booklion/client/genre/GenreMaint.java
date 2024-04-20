package booklion.client.genre;

import booklion.client.global.Information;
import booklion.client.supervisormain.SupervisorMain;
import booklion.client.utils.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;


/**
 * @author Blake McBride
 * Date: 10/28/13
 */
public class GenreMaint implements EntryPoint {

    private GenreServiceAsync service = GenreService.App.getInstance();
    private final TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());
    private Dialog editDlg;
    private TextControl folderLabel;
    protected final static int NEWRECORD = 1;
    protected final static int UPDATERECORD = 2;
    private int popupMode;

    private final Tree<BaseDto, String> tree = new Tree<BaseDto, String>(store, new ValueProvider<BaseDto, String>() {

        @Override
        public String getValue(BaseDto object) {
            return object.getName();
        }

        @Override
        public void setValue(BaseDto object, String value) {
        }

        @Override
        public String getPath() {
            return "name";
        }
    });

    public void onModuleLoad() {
       DockLayoutPanelEx panel = SupervisorMain.getPanel();
       panel.clear();

       Information.clearBookContext();
       SupervisorMain.updateMenu();

       editDlg = createEditDialog();

       FlowLayoutContainer con = new FlowLayoutContainer();
       con.addStyleName("margin-10");

       FolderDto.getData(store);

       tree.setWidth(300);
 //      tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

       tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);

       ButtonBar buttonBar = new ButtonBar();

       buttonBar.add(new TextButton("Expand All", new SelectEvent.SelectHandler() {

           @Override
           public void onSelect(SelectEvent event) {
               tree.expandAll();
           }
       }));
       buttonBar.add(new TextButton("Collapse All", new SelectEvent.SelectHandler() {
           @Override
           public void onSelect(SelectEvent event) {
               tree.collapseAll();
           }

       }));
       buttonBar.add(new TextButton("Add Sub", new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                BaseDto selected = tree.getSelectionModel().getSelectedItem();
                popupMode = NEWRECORD;
                editDlg.setHeadingText("Add Genre");
                folderLabel.clear();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        folderLabel.focus();
                    }
                });
                editDlg.show();
            }
       }));

        buttonBar.add(new TextButton("Rename", new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                BaseDto selected = tree.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    Message.msgOk("You must select a node before you can rename it.");
                } else {
                    popupMode = UPDATERECORD;
                    editDlg.setHeadingText("Edit Genre");
                    folderLabel.setText(selected.getName());
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            folderLabel.focus();
                        }
                    });
                    editDlg.show();
                }
            }
        }));
        buttonBar.add(new TextButton("Remove", new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                final BaseDto selected = tree.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    Message.msgOk("You must select a node before you can remove it.");
                } else if (!selected.getChildren().isEmpty()) {
                    Message.msgOk("Entry cannot be deleted because there are sub-entries.");
                } else {
                    ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "Are you sure you want to delete the selected record?");
                    final HideEvent.HideHandler hideHandler = new HideEvent.HideHandler() {
                        @Override
                        public void onHide(HideEvent event) {
                            Dialog btn = (Dialog) event.getSource();
                            String btnLbl = btn.getHideButton().getText();
                            if (btnLbl.equals("Yes")) {
                                removeGenre(selected.getId());
                            }
                        }
                    };
                    box.addHideHandler(hideHandler);
                    box.show();
                }
            }
        }));

        buttonBar.setLayoutData(new MarginData(4));
        con.add(buttonBar);
        con.add(tree);

        panel.add(con);

        panel.forceLayout();
    }

    private class KeyProvider implements ModelKeyProvider<BaseDto> {
        @Override
        public String getKey(BaseDto item) {
            return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
        }
    }

    private void addGenre(String name, Integer parent) {
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
                FolderDto.getData(store);
            }
        };
        UserInput.disable();
        service.addGenre(Information.getUserUuid(), name, parent, callback);
    }

    private void renameGenre(int id, String name) {
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
                FolderDto.getData(store);
            }
        };
        UserInput.disable();
        service.renameGenre(Information.getUserUuid(), id, name, callback);
    }

    private void removeGenre(int id) {
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
                FolderDto.getData(store);
            }
        };
        UserInput.disable();
        service.removeGenre(Information.getUserUuid(), id, callback);
    }

    private Dialog createEditDialog() {
        Dialog dlg = new Dialog();
        dlg.setModal(true);
        dlg.setPredefinedButtons(Dialog.PredefinedButton.CANCEL, Dialog.PredefinedButton.OK);
//		dlg.setHeadingText("Edit User");
//		dlg.setSize("100", "200");
        dlg.setBodyStyleName("background: none; padding: 5px");
        TextButton cancelBtn = dlg.getButtonById(Dialog.PredefinedButton.CANCEL.name());
        TextButton okBtn = dlg.getButtonById(Dialog.PredefinedButton.OK.name());
        okBtn.addSelectHandler(new LocalEditRecordHandler(dlg));
        cancelBtn.addSelectHandler(new SelectEvent.SelectHandler(){
            @Override
            public void onSelect(SelectEvent event) {
                editDlg.hide();
            }});

        VerticalLayout v = new VerticalLayout();
        dlg.add(v);
        v.setPosition(5, 10);

        configureEditPopup(dlg, v, okBtn);
        return dlg;
    }

    protected void configureEditPopup(Dialog dlg, VerticalLayout v, final TextButton okBtn) {
        int lblWidth = 45;

        dlg.setSize("340", "80");
        folderLabel =  v.addTextField(lblWidth, 240, 1, 80, "Genre:");
        folderLabel.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (ControlUtils.isEnterKey(event.getCharCode()))
                    okBtn.fireEvent(new SelectEvent());
            }
        });

    }

    private class LocalEditRecordHandler extends EditRecordHandler {

        protected LocalEditRecordHandler(Dialog dlg) {
            internalDialog = dlg;
        }

        protected void handleData() {
            if (popupMode == UPDATERECORD) {
                BaseDto selected = tree.getSelectionModel().getSelectedItem();
                renameGenre(selected.getId(), folderLabel.getText());
            } else if (popupMode == NEWRECORD) {
                BaseDto selected = tree.getSelectionModel().getSelectedItem();
                if (selected == null)
                    addGenre(folderLabel.getText(), null);
                else
                    addGenre(folderLabel.getText(), selected.getId());
            }
        }

        protected ErrorCheck errorCheck() {
            ErrorCheck ec = new ErrorCheck();

            ec.checkTextField(folderLabel, 1, 80, "Genre");

            return ec;
        }

    }


}
