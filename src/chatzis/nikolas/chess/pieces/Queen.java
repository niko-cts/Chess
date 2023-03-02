package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.PieceMoveList;

public class Queen extends Piece {

    /**
     * Instantiates the class.
     *
     * @param player Player - the player the piece belongs to.
     */
    public Queen(Player player, byte position) {
        super(player, 'Q', position);
    }

    /**
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        return getRepeatingMove(board, new int[]{1, 7, 8, 9});
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece copy() {
        return new Queen(belong, currentPosition);
    }
}
