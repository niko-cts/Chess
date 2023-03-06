package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.PieceMoveList;

public class Rook extends Piece {

    /**
     * Instantiates the class.
     *
     * @param player Player - the player the piece belongs to.
     * @param position byte - the piece position
     */
    public Rook(Player player, byte position) {
        super (player, 'R', position);
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
    public Piece clone() {
        return new Rook(belong, currentPosition);
    }
}
