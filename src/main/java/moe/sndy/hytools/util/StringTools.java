package moe.sndy.hytools.util;

public class StringTools {

    public static int occurrencesOf(String in, char match) {
        int count = 0;
        for (char c : in.toCharArray()) {
            if (c == match) {
                count++;
            }
        }
        return count;
    }

    public static boolean isEscaped(String val) {
        if (val.length() - 2 < 0) {
             return false;
        }
        if (val.charAt(val.length() - 2) == '\\') {
            return !isEscaped(val.substring(0, val.length() - 1));
        }
        return false;
    }

}
