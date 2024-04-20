package booklion.client.utils;

/**
 * @author Blake McBride
 * Date: 4/30/14
 */
public class StringUtils {

    private static final String randomStr2 = "bo87I&6ru6dytrdy543yRCiuviy%yfjytvljhb";

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

    /**
     * Convert a unicode string into an encrypted hex string.
     *
     * @param inp the input string
     * @param sec the key
     * @return a hex string
     */
    public static String encode(String inp, final String sec) {
        char [] input = new char[inp.length()];
        char [] secret = new char[sec.length()];
        inp.getChars(0, inp.length(), input, 0);
        sec.getChars(0, sec.length(), secret, 0);
        StringBuffer sb = new StringBuffer();
        for (int pos=0 ; pos < input.length ; pos++)
            sb.append(Integer.toHexString(0x010000 + (input[pos] ^ secret[pos%secret.length])).substring(1));
        return sb.toString();
    }

    public static String encode(String inp) {
        return encode(inp, randomStr2);
    }

    /**
     * Convert an encrypted hex string into the original unicode string
     * @param inp the hex string
     * @param sec the key
     * @return the original string
     */
    public static String decode(String inp, final String sec) {
        StringBuffer sb = new StringBuffer();
        char [] secret = new char[sec.length()];
        sec.getChars(0, sec.length(), secret, 0);
        for (int i=0,p=0 ; i < inp.length() ; i+=4, p++)
            sb.append((char) (Integer.parseInt(inp.substring(i, i + 4), 16) ^ secret[p % secret.length]));
        return sb.toString();
    }

    public static String decode(String inp) {
        return decode(inp, randomStr2);
    }

}
