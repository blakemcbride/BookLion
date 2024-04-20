package booklion.client.edituser;

import java.util.List;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public class PageData implements PagingLoadResult<Record> {

	private static final long serialVersionUID = 1L;
	private static PageData lastInstance = null;  //  only used on the client side
	
	private int offset=0;
	private int totalLength=0;
	private List<Record> data;
	
	public static int totalLength() {
		return lastInstance == null ? 0 : lastInstance.totalLength;
	}
	
	public static Record firstRecord() {
		if (lastInstance == null  ||  lastInstance.data == null  ||  lastInstance.data.size() == 0)
			return null;
		return lastInstance.data.get(0);
	}
	
	public static Record lastRecord() {
		int size;
		if (lastInstance == null  ||  lastInstance.data == null  ||  (size=lastInstance.data.size()) == 0)
			return null;
		return lastInstance.data.get(size-1);
	}

	@Override
	public List<Record> getData() {
		lastInstance = this;
		return data;
	}
	
	public void setData(List<Record> data) {
		this.data = data;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getTotalLength() {
		return totalLength;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

}
