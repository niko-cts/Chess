package chatzis.nikolas.chess.utils;

import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.pieces.*;

public class BoardUtils {

    private BoardUtils() {
        throw new UnsupportedOperationException("BoardUtils is a utility class and should not be instantiated.");
    }

    /**
     * Checks if the from and to position jumps over the border of the board.
     *
     * @param from int - from position
     * @param to   int - to position
     * @return boolean - stays on board
     */
    public static boolean staysOnBoard(int from, int to) {
        if (to < 0 || to > 63)
            return false;

        int fromMod = from % 8;
        int toMod = to % 8;

        if (fromMod == toMod)
            return true;

        if (fromMod < toMod)
            return toMod - fromMod < 3;
        return fromMod - toMod < 3;
    }

    /**
     * Returns the piece instance.
     * @param name char - the name character.
     * @param i byte - the position of the piece.
     * @return {@link Piece} - the piece.
     * @since 1.0-SNAPSHOT
     */
    public static Piece getPieceByChar(char name, byte i) {
        switch (name) {
            case 'B' -> {
                return new Bishop(Player.WHITE, i);
            }
            case 'b' -> {
                return new Bishop(Player.BLACK, i);
            }
            case 'K' -> {
                return new King(Player.WHITE, i);
            }
            case 'k' -> {
                return new King(Player.BLACK, i);
            }
            case 'N' -> {
                return new Knight(Player.WHITE, i);
            }
            case 'n' -> {
                return new Knight(Player.BLACK, i);
            }
            case 'P' -> {
                return new Pawn(Player.WHITE, i);
            }
            case 'p' -> {
                return new Pawn(Player.BLACK, i);
            }
            case 'Q' -> {
                return new Queen(Player.WHITE, i);
            }
            case 'q' -> {
                return new Queen(Player.BLACK, i);
            }
            case 'R' -> {
                return new Rook(Player.WHITE, i);
            }
            case 'r' -> {
                return new Rook(Player.BLACK, i);
            }
            default -> throw new IllegalStateException(name + " is not a piece");
        }
    }
}
