package chatzis.nikolas.schachreader.pieces;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.PieceMoveList;

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
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        PieceMoveList pieceMoveList = new PieceMoveList(board, this);
        for (int i : new int[]{6, 10, 15, 17}) {
            for (byte m : new byte[]{1, -1})
                addMoveWhenMoveableOrAttackable(pieceMoveList, (byte) (currentPosition + i * m));
        }
        return pieceMoveList;
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece copy() {
        return new Knight(belong, currentPosition);
    }
}