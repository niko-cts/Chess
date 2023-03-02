package chatzis.nikolas.schachreader.move;

import chatzis.nikolas.schachreader.utils.FieldNameConverter;

/**
 * Move class stores from -> to position
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class Move {

    private final byte from;
    private final byte to;
    private final char name;

    public Move(char name, int from, int to) {
        this(name, (byte) from, (byte) to);
    }

    public Move(char name, byte from, byte to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public byte from() {
        return from;
    }

    public byte to() {
        return to;
    }

    public char getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(FieldNameConverter.fromFieldNumber(from)).append("(").append(name).append(") ").append(FieldNameConverter.fromFieldNumber(to)).toString();
    }
}
