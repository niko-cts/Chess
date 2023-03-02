package chatzis.nikolas.schachreader.utils;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.Move;
import chatzis.nikolas.schachreader.pieces.*;

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
     * Checks if the king is in check after doing the given move.
     * @param currentBoard Board - the current board.
     * @param move Move - the move to do
     * @return boolean - king is not in check.
     */
    public static boolean kingIsntChecked(Board currentBoard, Move move) {
        Board board = currentBoard.makeMove(move, true);

        int kingPos = board.getKingPosition(currentBoard.getCurrentPlayer());
        return board.getAllMoves().stream().noneMatch(m -> m.to() == kingPos);
    }

    /**
     * Checks if the king is in check.
     * @param board Board - the current board.
     * @return boolean - king is not in check.
     */
    public static boolean kingIsntChecked(Board board) {
        return kingIsntChecked(board, null);
    }

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
