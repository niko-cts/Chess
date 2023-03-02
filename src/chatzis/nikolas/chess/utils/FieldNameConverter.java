package chatzis.nikolas.chess.utils;

public class FieldNameConverter {

    private FieldNameConverter() {
        throw new UnsupportedOperationException("FieldNameConverter should not be instantiated.");
    }

    /**
     * Translates a field number to the field name.
     * @param fieldNumber byte - the field number
     * @return String - the field name (e.g. 28 -> d4)
     */
    public static String fromFieldNumber(byte fieldNumber) {
        int y = fieldNumber / 8 + 1;
        return switch (fieldNumber % 8) {
            case 0 -> "a" + y;
            case 1 -> "b" + y;
            case 2 -> "c" + y;
            case 3 -> "d" + y;
            case 4 -> "e" + y;
            case 5 -> "f" + y;
            case 6 -> "g" + y;
            default -> "h" + y;
        };
    }
    /**
     * Translates a field name to the field number.
     * @param fieldName String - the field name
     * @return byte - the field number (e.g. d4 -> 28)
     */
    public static byte fromFieldName(String fieldName) {
        if (fieldName.length() != 2)
            throw new IllegalStateException("The field name needs to be a size of 2");
        char[] chars = fieldName.toCharArray();
        int number = 8 * (Byte.parseByte("" + chars[1]) - 1);
        return switch (chars[0]) {
            case 'a' -> (byte) (number);
            case 'b' -> (byte) (number + 1);
            case 'c' -> (byte) (number + 2);
            case 'd' -> (byte) (number + 3);
            case 'e' -> (byte) (number + 4);
            case 'f' -> (byte) (number + 5);
            case 'g' -> (byte) (number + 6);
            case 'h' -> (byte) (number + 7);
            default -> throw new IllegalStateException("The field name needs to between a-h");
        };
    }
}
