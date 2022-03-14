package bufferedScanning;


public class HexAbcParser {
    public static int parseHexAbcInt(String token) {
        String stringToParse;
        int radix;
        if (token.length() > 2 && startsWithIgnoreCase(token, "0x")) {
            // Hex number:
            stringToParse = token.substring(2);
            radix = 16;
            return Integer.parseUnsignedInt(stringToParse, radix);

        } else if (Character.isDigit(token.charAt(token.length() - 1))) {
            // Regular number:
            stringToParse = token;
            radix = 10;
        } else {
            // Abc number:
            stringToParse = toDecimalString(token);
            radix = 10;
        }

        return Integer.parseInt(stringToParse, radix);
    }

    private static boolean startsWithIgnoreCase(String targetString, String prefix) {
        return targetString.length() >= prefix.length() &&
            targetString.substring(0, prefix.length()).toLowerCase().equals(prefix.toLowerCase());
//        return targetString.toLowerCase().startsWith(prefix.toLowerCase());
    }

    private static String toDecimalString(String abcString) {
        StringBuilder resultBuilder = new StringBuilder();

        for (char c: abcString.toCharArray()) {
            resultBuilder.append(Character.isLetter(c) ? (char)(c + '0' - 'a') : c);
        }

        return resultBuilder.toString();
    }
}
