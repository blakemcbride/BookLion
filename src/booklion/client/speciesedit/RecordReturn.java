package booklion.client.speciesedit;

import booklion.client.utils.StandardReturn;

/**
 * @author Blake McBride
 * Date: 2/25/14
 */
public class RecordReturn extends StandardReturn {
    private Record[] records;

    public RecordReturn() {
    }

    public RecordReturn(String msg) {
        super(msg);
    }

    public RecordReturn(int err) {
        super(err);
    }

    public Record[] getRecords() {
        return records;
    }

    public void setRecords(Record[] records) {
        this.records = records;
    }
}
