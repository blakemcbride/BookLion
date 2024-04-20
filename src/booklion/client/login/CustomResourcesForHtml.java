package booklion.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;


public interface CustomResourcesForHtml extends ClientBundle {

    public static final CustomResourcesForHtml INSTANCE = GWT.create(CustomResourcesForHtml.class);

    public interface LayoutStyles extends CssResource {

        /* example Constant which is 100% */
        String wallTowall();

        /* basic style */
        String boxy();

        /* sprite */
//        String logoBox();

        String left();
        String right();

        String htmlFormatting();
    }

    // css file
//    @Source("customStyles.css")
    public LayoutStyles customStyles();

//    @Source("logo.jpg")
//    @ImageResource.ImageOptions(repeatStyle = RepeatStyle.None)
//    ImageResource logo();

}
