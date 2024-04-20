package booklion.client.utils;

/**
 * @author Blake McBride
 * Date: 11/6/13
 */
public interface IUpdate<REC> {
    public void update(REC rec);
    public void refresh(StandardReturn ret);
}
