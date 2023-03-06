package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;

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
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        addRepeatingMove(board, new int[]{1, 8});
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
