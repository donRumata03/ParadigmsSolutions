package bufferedScanning;

public class ScanningUtils {
    // Default delimiters:
    public static boolean isFromWord(int c) {
        return Character.isLetter(c) || Character.getType(c) == Character.DASH_PUNCTUATION || (char)c == '\'';
    }

    public static final char LF = 0x000A;
    public static final char CR = 0x000D;
    public static final char[] lineSeparators = { LF, 0x000B, 0x000C, CR, 0x0085, 0x2028, 0x2029 };

    public static boolean isLineSeparator(int c) {
        for (char sep : lineSeparators) {
            if (sep == c) {
                return true;
            }
        }
        return false;
    }

    static void copyForward(char[] array, int srcIndex, int dstIndex, int length) {
        assert srcIndex + length <= array.length;

        int srcPtr = srcIndex;
        int dstPtr = dstIndex;

        for (int i = 0; i < length; i++) {
            array[dstPtr] = array[srcPtr];
            srcPtr++;
            dstPtr++;
        }
    }

    static String bufferSubstring(char[] array, int startPos, int endPos) {
        return String.valueOf(array, startPos, endPos - startPos);
    }
}
