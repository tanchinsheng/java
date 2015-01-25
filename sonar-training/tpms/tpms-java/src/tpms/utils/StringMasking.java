package tpms.utils;

import it.txt.general.utils.Base64;
import it.txt.general.utils.GeneralStringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 8-nov-2005
 * Time: 11.40.26
 * To change this template use File | Settings | File Templates.
 */
public class StringMasking {


    private static int seed = TpmsConfiguration.getInstance().getSeed();


    public static String encriptString(String source) {
        String result = source;
        if (!GeneralStringUtils.isEmptyString(result)) {
            for (int i = 0; i < seed; i ++) {
                String cripted = Base64.encodeBytes(result.getBytes());
                if (!GeneralStringUtils.isEmptyString(cripted)) {
                    result = cripted;
                }
            }
        }
        return result;
    }

    public static String decriptString(String criptedString) {

        String result = criptedString;
        if (!GeneralStringUtils.isEmptyString(result)) {
            for (int i = 0; i < seed; i++) {
                byte[] clear = Base64.decode(result);
                if (clear != null && clear.length > 0)
                    result = new String(clear);
            }
        }
        return result;
    }

    public static String decriptString(String criptedString, int seed) {
        String result = criptedString;
        if (!GeneralStringUtils.isEmptyString(result)) {
            for (int i = 0; i < seed; i++) {
                byte[] clear = Base64.decode(result);
                if (clear != null && clear.length > 0)
                    result = new String(clear);
            }
        }
        return result;
    }

    public static String encriptString(String source, int seed) {
        String result = source;
        if (!GeneralStringUtils.isEmptyString(result)) {
            for (int i = 0; i < seed; i ++) {
                String cripted = Base64.encodeBytes(result.getBytes());
                if (!GeneralStringUtils.isEmptyString(cripted))
                    result = cripted;
            }
        }
        return result;
    }
}
