package chatzis.nikolas.chess.move;

import chatzis.nikolas.chess.pieces.Piece;

import java.util.Map;

/**
 * Class with interface, that gets called as soon as the piece moves.
 * Extends {@link Move}
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class SpecialMove extends Move {
    private final IMoved move;

    public SpecialMove(char name, byte from, byte to, IMoved move) {
        super(name, from, to);
        this.move = move;
    }

    public IMoved getMove() {
        return move;
    }

    public interface IMoved {

        /**
         * Method will be called as soon as the piece is moved.
         * @param pieces Map<Byte, Piece> - the updated piece set
         */
        void moved(Map<Byte, Piece> pieces);

    }
}
