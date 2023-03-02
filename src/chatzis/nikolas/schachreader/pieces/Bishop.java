package chatzis.nikolas.schachreader.pieces;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.PieceMoveList;

public class Bishop extends Piece {

    public Bishop(Player player, byte position) {
        super(player, 'B', position);
    }

    /**
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        return getRepeatingMove(board, new int[]{9, 7});
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece copy() {
        return new Bishop(belong, currentPosition);
    }

}
