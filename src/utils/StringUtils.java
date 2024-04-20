package utils;

/**
 * @author Blake McBride
 * Date: 3/12/14
 */
public class StringUtils {

    public static String normalize(String str) {
        if (str == null)
            return null;
        str = str.trim();
        if (str.isEmpty())
            return null;
        str = str.trim();
        str = str.replace("\r", "");         //  ignore
        str = str.replaceAll("\n +", "\n");  //  remove spaces after newlines
        str = str.replaceAll(" +\n", "\n");  //  remove spaces before newlines
        str = str.replaceAll("\n+", "\r");   //  signify paragraph break
        str = str.replace("\r", "\n\n");     //  split into paragraphs
        return str;
    }

    public static boolean different(String s1, String s2) {
        if (s1 == null)
            s1 = "";
        if (s2 == null)
            s2 = "";
        return !s1.equals(s2);
    }

    public static boolean same(String s1, String s2) {
        if (s1 == null)
            s1 = "";
        if (s2 == null)
            s2 = "";
        return s1.equals(s2);
    }

    public static int length(String s) {
        return s == null ? 0 : s.length();
    }

    public static boolean different(Long a, Long b) {
        if (a == null  &&  b == null)
            return false;
        if (a == null  ||  b == null)
            return true;
        return a.longValue() != b.longValue();
    }

    public static String rightStrip(String s) {
        int i;
        for (i = s.length() - 1; i >= 0 && Character.isSpaceChar(s.charAt(i));)
            i--;
        if (i == -1)
            return s;
        return s.substring(0, i + 1);
    }

}
