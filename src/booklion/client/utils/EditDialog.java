package booklion.client.utils;

import booklion.client.login.Login;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;


/**
 * @author Blake McBride
 * Date: 11/3/13
 *
 * Dialog associated to a record and list of records.
 */
public abstract class EditDialog<REC> {

    public final static int NEWRECORD = 1;
    public final static int UPDATERECORD = 2;
    private int popupMode;
    private Dialog editDlg;
    private REC record;  //  record being edited
    private REC originalRec;  //  copy of record being edited in case we need to return to original values
    private IUpdate<REC> crudInstance;

    protected abstract void configureEditPopup(Dialog dlg, VerticalLayout v);
    protected abstract String getData(REC rec);
    protected abstract String initAddPopup();
    protected abstract void saveUpdatedData(REC rec, int mode);
    protected abstract REC newRecord();
    protected abstract REC copy(REC obj);
    protected abstract void updateRecord(String uuid, REC rec, AsyncCallback<StandardReturn> callback);
    protected abstract void addRecord(String uuid, REC rec, AsyncCallback<StandardReturn> callback);

    public EditDialog() {
        this(null);
    }

    public EditDialog(IUpdate<REC> crudInstance) {
        this.crudInstance = crudInstance;
        editDlg = new Dialog();
        editDlg.setModal(true);
        editDlg.setPredefinedButtons(Dialog.PredefinedButton.CANCEL, Dialog.PredefinedButton.OK);
//		dlg.setHeadingText("Edit User");
//		dlg.setSize("100", "200");
        editDlg.setBodyStyleName("background: none; padding: 5px");
        TextButton cancelBtn = editDlg.getButtonById(Dialog.PredefinedButton.CANCEL.name());
        TextButton okBtn = editDlg.getButtonById(Dialog.PredefinedButton.OK.name());
        okBtn.addSelectHandler(new LocalEditRecordHandler(editDlg, this));
        cancelBtn.addSelectHandler(new SelectEvent.SelectHandler(){
            @Override
            public void onSelect(SelectEvent event) {
                EditDialog.this.editDlg.hide();
            }});

        VerticalLayout v = new VerticalLayout();
        editDlg.add(v);
        v.setPosition(5, 10);

        configureEditPopup(editDlg, v);
    }

    public void show(int mode, REC rec) {
        record = rec;
        if (rec != null)
            originalRec = copy(rec);
        popupMode = mode;
        editDlg.setHeadingText(mode == NEWRECORD ? initAddPopup() : getData(rec));
        editDlg.show();
    }

    protected abstract ErrorCheck errorCheck(int mode);

    private class LocalEditRecordHandler extends EditRecordHandler {

        private EditDialog editDialog;

        protected LocalEditRecordHandler(Dialog dlg, EditDialog edlg) {
            internalDialog = dlg;
            editDialog = edlg;
        }

        protected void handleData() {
            if (popupMode == UPDATERECORD) {
                saveUpdatedData(record, popupMode);
                updateRecordInt(Login.getUUID(), record);
            } else if (popupMode == NEWRECORD) {
                REC rec = newRecord();
                addRecordInt(Login.getUUID(), rec);
            }
        }

        @Override
        protected ErrorCheck errorCheck() {
            return editDialog.errorCheck(popupMode);
        }

    }

    private void updateRecordInt(String uuid, REC rec) {
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
                if (result.getReturnCode() == StandardReturn.SUCCESS) {
                    if (crudInstance != null)
                        crudInstance.update(record);
                    Message.notice("Data updated.");
                } else {
                    if (crudInstance != null)
                        crudInstance.update(originalRec);
                    Message.msgOk(result.getMsg());
                }
            }
        };
        UserInput.disable();
        updateRecord(uuid, rec, callback);
    }

    private void addRecordInt(String uuid, REC rec) {
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
                if (result.getReturnCode() == StandardReturn.SUCCESS) {
                    if (crudInstance != null)
                        crudInstance.refresh(result);
                    Message.notice("Data added.");
                } else
                    Message.msgOk(result.getMsg());
            }
        };
        UserInput.disable();
        addRecord(uuid, rec, callback);
    }

    protected IUpdate<REC> getCrudInstance() {
        return crudInstance;
    }

    protected int getPopupMode() {
        return popupMode;
    }

}
