package chatzis.nikolas.chess.utils;

import java.util.Iterator;
import java.util.Set;

public class FieldNameConverter {

    private FieldNameConverter() {
        throw new UnsupportedOperationException("FieldNameConverter is an utility class");
    }

    public static byte fromFieldName(String rowCol) {
        char[] chars = rowCol.toCharArray();
        if (chars.length != 2)
            return -1;
        final int col = (Byte.parseByte(chars[1] + "") - 1) * 8;
        return switch (chars[0]) {
            case 'a' -> (byte) col;
            case 'b' -> (byte) (col + 1);
            case 'c' -> (byte) (col + 2);
            case 'd' -> (byte) (col + 3);
            case 'e' -> (byte) (col + 4);
            case 'f' -> (byte) (col + 5);
            case 'g' -> (byte) (col + 6);
            case 'h' -> (byte) (col + 7);
            default -> -1;
        };
    }

    public static char[] fromFieldNumber(byte move) {
        String row = "" + (move / 8 + 1);
        char r = row.charAt(0);

        return switch (move % 8) {
            case 0 -> new char[]{'a', r};
            case 1 -> new char[]{'b', r};
            case 2 -> new char[]{'c', r};
            case 3 -> new char[]{'d', r};
            case 4 -> new char[]{'e', r};
            case 5 -> new char[]{'f', r};
            case 6 -> new char[]{'g', r};
            case 7 -> new char[]{'h', r};
            default -> new char[]{'n', 'n'};
        };
    }

    public static String fromFieldNumbers(Set<Byte> moves) {
        StringBuilder builder = new StringBuilder().append("[");
        Iterator<Byte> iterator = moves.iterator();
        while (iterator.hasNext()) {
            builder.append(fromFieldNumber(iterator.next()));
            if (iterator.hasNext()) builder.append(",");
        }
        return builder.append("]").toString();
    }
}
