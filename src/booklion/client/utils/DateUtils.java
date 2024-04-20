package booklion.client.utils;

import java.util.Date;

/**
 * @author Blake McBride
 */
public class DateUtils {
	
	@SuppressWarnings("deprecation")
	public static int toInt(Date dt) {
		if (dt == null)
			return 0;
		int ret = 10000 * (dt.getYear() + 1900) + (100 * (dt.getMonth() + 1)) + dt.getDate();
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	public static Date toDate(int dt) {
		if (dt == 0)
			return null;
		Date ret = new Date();
		ret.setYear(getYear(dt)-1900);
		ret.setMonth(getMonth(dt)-1);
		ret.setDate(getDay(dt));
		return ret;
	}
	
	public static int getYear(int dt) {
		return dt / 10000;
	}
	
	public static int getMonth(int dt) {
		return (dt % 10000) / 100;
	}

	public static int getDay(int dt) {
		return dt % 100;
	}

    public static int now() {
        return toInt(new Date());
    }

}
