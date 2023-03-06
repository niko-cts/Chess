package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;

public class Bishop extends Piece {

    public Bishop(Player player, byte position) {
        super(player, 'B', position);
    }

    /**
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        addRepeatingMove(board, new int[]{9, 7});
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece clone() {
        return new Bishop(belong, currentPosition);
    }

}
