package booklion.client.genre;

import booklion.client.global.Information;
import booklion.client.utils.Message;
import booklion.client.utils.UserInput;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Blake McBride
 *
 */
@SuppressWarnings("serial")
public class FolderDto extends BaseDto {

    private List<BaseDto> children = new LinkedList<BaseDto>();

    protected FolderDto() {
    }

    public FolderDto(Integer id, String name) {
        super(id, name);
    }

    public FolderDto clear() {
        children.clear();
        return this;
    }

    public List<BaseDto> getChildren() {
        return children;
    }

    public void setChildren(List<BaseDto> children) {
        this.children = children;
    }

    public void addChild(BaseDto child) {
        if (children == null)
            children = new LinkedList<BaseDto>();
        children.add(child);
    }

    public BaseDto addChild(Integer id, String name) {
        BaseDto child = new BaseDto(id, name);
//        if (children == null)
//            children = new LinkedList<BaseDto>();
        children.add(child);
        return child;
    }

    public FolderDto addChildFolder(Integer id, String name) {
        FolderDto child = new FolderDto(id, name);
        if (children == null)
            children = new LinkedList<BaseDto>();
        children.add(child);
        return child;
    }

    static void getData(final TreeStore<BaseDto> store) {
        GenreServiceAsync service = GenreService.App.getInstance();
        //       FolderDto root = TestData.getRootFolder();
        final FolderDto root = new FolderDto();
        AsyncCallback<GenreItem []> callback = new AsyncCallback<GenreItem []>() {

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
            public void onSuccess(GenreItem[] result) {
                UserInput.enable();
                store.clear();
                add(root, result, 0, null);
                //FolderDto root = TestData.getRootFolder();
                for (BaseDto base : root.getChildren()) {
                    store.add(base);
                    if (base instanceof FolderDto)
                        processFolder(store, (FolderDto) base);
                }
                //               tree.expandAll();
            }

            private void add(FolderDto node, GenreItem[] result, int i, Integer parentId) {
                for ( ; i < result.length ; ++i) {
                    Integer parentId2 = result[i].getParentId();
                    if (parentId == parentId2  ||  parentId != null  &&  parentId2 != null  &&  parentId.equals(parentId2)) {
                        FolderDto parentFolder = node.addChildFolder((int) result[i].getId(), result[i].getName());
                        add(parentFolder, result, i+1, (int) result[i].getId());
                    }
                }
            }

            private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
                for (BaseDto child : folder.getChildren()) {
                    store.add(folder, child);
                    if (child instanceof FolderDto) {
                        processFolder(store, (FolderDto) child);
                    }
                }
            }

        };
        UserInput.disable();
        service.loadGenre(Information.getUserUuid(), callback);
    }

}

