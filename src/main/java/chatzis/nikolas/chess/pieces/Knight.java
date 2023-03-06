package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;

public class Knight extends Piece {

    /**
     * Instantiates the class.
     *
     * @param player Player - the player the piece belongs to.
     */
    public Knight(Player player, byte position) {
        super(player, 'N', position);
    }

    /**
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        for (int i : new int[]{6, 10, 15, 17}) {
            for (byte m : new byte[]{1, -1})
                addMoveWhenMoveableOrAttackable(board, (byte) (currentPosition + i * m));
        }
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece clone() {
        return new Knight(belong, currentPosition);
    }
}
