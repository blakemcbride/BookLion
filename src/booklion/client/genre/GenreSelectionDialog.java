package booklion.client.genre;

import booklion.client.utils.Message;
import booklion.client.utils.VerticalLayout;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author Blake McBride
 * Date: 1/29/14
 */
public class GenreSelectionDialog extends Dialog {

    private TreeStore<BaseDto> store;
    private IGenreSelection returnMethod;
    private Tree<BaseDto, String> tree;

    public GenreSelectionDialog() {
        store = new TreeStore<BaseDto>(new KeyProvider());
        tree = new Tree<BaseDto, String>(store, new ValueProvider<BaseDto, String>() {

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
        setModal(true);
        setPredefinedButtons(Dialog.PredefinedButton.CANCEL, Dialog.PredefinedButton.OK);
        setHeadingText("Genre Selection");
        setSize("300", "300");
 //       setBodyStyleName("background: none; padding: 5px;");
        final TextButton cancelBtn = getButtonById(Dialog.PredefinedButton.CANCEL.name());
        TextButton okBtn = getButtonById(Dialog.PredefinedButton.OK.name());
        okBtn.addSelectHandler(new SelectEvent.SelectHandler(){
            @Override
            public void onSelect(SelectEvent event) {
                BaseDto selectedGenre = tree.getSelectionModel().getSelectedItem();
                if (selectedGenre == null)
                    Message.msgOk("Please select the desired genre before clicking OK.");
                else {
                    hide();
                    returnMethod.itemSelected(selectedGenre);
                }
            }});
        cancelBtn.addSelectHandler(new SelectEvent.SelectHandler(){
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }});

        VerticalLayout v = new VerticalLayout();
        v.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        addGenres(v);
        add(v, new MarginData(2));
//        forceLayout();
    }

    public void show(IGenreSelection returnMeth) {
        returnMethod = returnMeth;
        show();
    }

    private void addGenres(VerticalLayout v) {
        FolderDto.getData(store);
        tree.setWidth(300);
        tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        tree.setAutoExpand(true);
        v.add(tree);
    }

    private class KeyProvider implements ModelKeyProvider<BaseDto> {
        @Override
        public String getKey(BaseDto item) {
            return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
        }
    }

}
