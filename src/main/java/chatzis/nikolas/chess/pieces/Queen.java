package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;

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
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        addRepeatingMove(board, new int[]{1, 7, 8, 9});
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece clone() {
        return new Queen(belong, currentPosition);
    }
}
