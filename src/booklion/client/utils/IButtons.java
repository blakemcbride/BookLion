package booklion.client.utils;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author Blake McBride
 * Date: 2/5/14
 */
public interface IButtons {
    public void addButton(String lbl, SelectEvent.SelectHandler handler);
}
