package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.SpecialMove;
import chatzis.nikolas.chess.utils.BoardUtils;

public class Pawn extends Piece {

    public static final char PAWN_NAME = 'P';

    /**
     * Instantiates the class.
     *
     * @param player Player - the player the piece belongs to.
     */
    public Pawn(Player player, byte currentPosition) {
        super(player, PAWN_NAME, currentPosition);
    }

    /**
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        if (belong.equals(Player.WHITE)) {
            if (board.canMove(currentPosition, currentPosition + 8)) {
                add(board, currentPosition + 8);

                if (currentPosition < 16 && board.canMove(currentPosition, currentPosition + 16))
                    add(board, currentPosition + 16);
            }

            addMoveIfAttackableOrEnpassant(board, currentPosition + 7);
            addMoveIfAttackableOrEnpassant(board, currentPosition + 9);
        } else {
            if (board.canMove(currentPosition, currentPosition - 8)) {
                add(board, currentPosition - 8);

                if (currentPosition > 47 && board.canMove(currentPosition, currentPosition - 16))
                    add(board, currentPosition - 16);
            }

            addMoveIfAttackableOrEnpassant(board, currentPosition - 7);
            addMoveIfAttackableOrEnpassant(board, currentPosition - 9);
        }
    }


    /**
     * Adds a move to the given list, if the fromPosition can attack the toPosition or the toPosition is the enPassant position
     * @param board {@link Board} - the board
     * @param attackPosition int - the attackPosition.
     */
    protected void addMoveIfAttackableOrEnpassant(Board board, int attackPosition) {
        if (BoardUtils.staysOnBoard(currentPosition, attackPosition)) {
            if (board.isEnemyPiece(belong, (byte) attackPosition))
                add(board, attackPosition);
            else if (attackPosition == board.getEnPassant()) {
                add(board, new SpecialMove(name, currentPosition, (byte) attackPosition, (pieceSet) ->
                        pieceSet.remove((byte) (attackPosition + (belong == Player.WHITE ? -8 : 8)))));
            }
        }
    }


    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public Piece clone() {
        return new Pawn(belong, currentPosition);
    }

}
