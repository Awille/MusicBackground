package utils;

public class TextUtils {
    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }
}
