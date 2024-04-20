package utils;

/**
 * User: Blake McBride
 * Date: 7/16/14
 */

import java.security.MessageDigest;
import java.security.SecureRandom;


public class Crypto {

    private static SecureRandom rand;
    private static SecureRandom seedGen;
    private static int nused = 0;
    private static final String randomStr = "5ee35f159aec404b80992f683b79aaf9";
    private static final String randomStr2 = "bo87I&6ru6dytrdy543yRCiuviy%yfjytvljhb";

    static {
        try {
            rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
            seedGen = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String getHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte hashBytes[] = md.digest(str.getBytes());
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer s = new StringBuffer();
        for (byte element : bytes)
            s.append(Integer.toHexString(0x0100 + (element & 0x00FF)).substring(1));
        return s.toString();
    }

    private static String makeSalt(int len) {
        StringBuffer str = new StringBuffer();
        nused += len;
        if (nused > 30000) { // can't securely use the same seed forever
            rand.setSeed(seedGen.generateSeed(16));  // numbers above 16 start to really take a long time
            nused = 0;
        }
        while (len-- > 0)
            str.append(Integer.toHexString(0x0100 + rand.nextInt(256)).substring(1));
        return str.toString();
    }

    /**
     * Create a new password hash (80 characters long)
     *
     * @param pw
     * @return its hash
     */
    public static String hashPW(String pw) {
        String salt = makeSalt(8);
        return salt + getHash(salt + pw + randomStr);
    }

    /**
     * Check to see if a password matches its hash
     *
     * @param pw
     * @param hash
     * @return
     */
    public static boolean hashCheck(String pw, String hash) {
        String salt = hash.substring(0, 16);
        return (salt + getHash(salt + pw + randomStr)).equals(hash);
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

    private static void test(String str) {
        System.out.println("");
        System.out.println(str);
        String t = encode(str, randomStr2);
        System.out.println(t);
        System.out.println(decode(t, randomStr2));
    }

    public static void main(final String args[]) throws Exception {
        String pw = "hhasdfhdfliuh";
        String hash = hashPW(pw);
        System.out.println("hash = (" + hash.length() + ") " + hash);
        System.out.println(hashCheck(pw, hash));
        System.out.println(hashCheck("asfasf", hash));

        System.out.println("");
        test("abc");
        test("Hello there!");

    }
}


