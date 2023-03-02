package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.PieceMoveList;

public class Rook extends RememberMovePiece {

    /**
     * Instantiates the class.
     *
     * @param player Player - the player the piece belongs to.
     */
    public Rook(Player player, byte position) {
        this(player, false, position);
    }

    public Rook(Player player, boolean moved, byte position) {
        super(player, 'R', moved, position);
    }

    /**
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        return getRepeatingMove(board, new int[]{1, 8});
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece copy() {
        return new Rook(belong, moved, currentPosition);
    }
}
