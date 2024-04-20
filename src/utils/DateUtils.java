/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	private static final String DATE_FORMAT_MM_DD_YY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/(\\d\\d)";
    private static final String DATE_FORMAT_MM_DD_YYYY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
    private static final String DATE_FORMAT_DOT_MM_DD_YY = "(0?[1-9]|1[012]).(0?[1-9]|[12][0-9]|3[01]).(\\d\\d)";
    private static final String DATE_FORMAT_DASH_MM_DD_YY = "(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(\\d\\d)";
	
	public static int getDate(String date) {
        try {

            //date format does not like dash, so convert it to dot if found
            date = date.replaceAll("-", ".");

            if (date.matches(DATE_FORMAT_MM_DD_YY)) {
                date = normalizeDate(date);
                return getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_MM_DD_YYYY)) {
                return getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_DOT_MM_DD_YY)) {
                date = normalizeDate(date);
                return getDate(new SimpleDateFormat("MM.dd.yyyy").parse(date));

            } else if (date.matches(DATE_FORMAT_DASH_MM_DD_YY)) {
                date = normalizeDate(date);
                return getDate(new SimpleDateFormat("MM-dd-yyyy").parse(date));
            }

        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }
	
	public static Date getDate(final int datint) {
        if (datint <= 0) {
            return new java.sql.Date(new java.util.Date().getTime());
        }

        final int day = datint % 100;
        final int month = (datint % 10000 - day) / 100;

        final int year = datint / 10000;

        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        return new java.sql.Date(c.getTime().getTime());
    }
	
	public static String dateFormat(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }
	
	private static String normalizeDate(String date) {

        int length = date.length();

        String dateYear = date.substring(length - 2);
        if (Integer.parseInt(dateYear) > 20) {
            date = date.substring(0, length - 2) + "19" + dateYear;
        } else {
            date = date.substring(0, length - 2) + "20" + dateYear;
        }
        return date;
    }
	
	public static int getDate(final java.util.Date dat) {
        if (dat == null) {
            return 0;
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTime(dat);

        return cal.get(Calendar.DAY_OF_MONTH) + ((cal.get(Calendar.MONTH) + 1) * 100) + ((cal.get(Calendar.YEAR)) * 10000);
    }

    public static Timestamp now() {
        return new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

}
