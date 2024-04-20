package booklion.client.genre;

/**
 * User: blake
 * Date: 10/28/13
 */
public class TestData {

    static FolderDto getRootFolder() {
        FolderDto root = new FolderDto();
        FolderDto child, child2;
        int id = 1;

        child = root.addChildFolder(id++, "Beethoven");

        child2 = child.addChildFolder(id++, "Quartets");
        child2.addChild(id++, "Six String Quartets");
        child2.addChild(id++, "Three String Quartets");
        child2.addChild(id++, "Grosse Fugue for String Quartets");

        child2 = child.addChildFolder(id++, "Sonatas");
        child2.addChild(id++, "Sonata in A Minor");
        child2.addChild(id++, "Sonata in F Major");

        child2 = child.addChildFolder(id++, "Concertos");
        child2.addChild(id++, "No. 1 - C");

        child = root.addChildFolder(id++, "Brahms");

        child2 = child.addChildFolder(id++, "Concertos");
        child2.addChild(id++, "Violin Concerto");
        child2.addChild(id++, "Double Concerto - A Minor");



        return root;
    }
}
