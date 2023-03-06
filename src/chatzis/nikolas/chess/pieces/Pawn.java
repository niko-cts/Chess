package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.PieceMoveList;
import chatzis.nikolas.chess.move.SpecialMove;

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
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        PieceMoveList pieceMoveList = new PieceMoveList(board, this);

        // en passant
        if (board.getLastMove() != null && board.getLastMove().getName() == Pawn.PAWN_NAME) {
            int currentRow = currentPosition % 8;
            if ((currentRow != 0 && currentRow != 7) ||
                    (currentRow == 7 && board.getLastMove().to() % 8 == 6) ||
                    (currentRow == 0 && board.getLastMove().to() % 8 == 1)) {
                if (belong.equals(Player.WHITE) && currentPosition / 8 == 4 && board.getLastMove().to() / 8 == 4) {
                    if (board.getLastMove().to() == currentPosition - 1 && board.getLastMove().from() == currentPosition + 15) {
                        byte lastTo = board.getLastMove().to();
                        pieceMoveList.add(new SpecialMove(name, currentPosition,
                                (byte) (board.getLastMove().to() + 8), ((pieceSet) -> pieceSet[lastTo] = null)));
                    } else if (board.getLastMove().to() == currentPosition + 1 && board.getLastMove().from() == currentPosition + 17) {
                        byte lastTo = board.getLastMove().to();
                        pieceMoveList.add(new SpecialMove(name, currentPosition,
                                (byte) (board.getLastMove().to() + 8),  (pieceSet -> pieceSet[lastTo] = null)));
                    }
                } else if (belong.equals(Player.BLACK) && currentPosition / 8 == 3 && board.getLastMove().to() / 8 == 3) {
                    if (board.getLastMove().to() == currentPosition - 1 && board.getLastMove().from() == currentPosition - 17) {
                        byte lastTo = board.getLastMove().to();
                        pieceMoveList.add(new SpecialMove(name, currentPosition,
                                (byte) (board.getLastMove().to() - 8),  (pieceSet -> pieceSet[lastTo] = null)));
                    } else if (board.getLastMove().to() == currentPosition + 1 && board.getLastMove().from() == currentPosition - 15) {
                        byte lastTo = board.getLastMove().to();
                        pieceMoveList.add(new SpecialMove(name, currentPosition,
                                (byte) (board.getLastMove().to() - 8), (pieceSet -> pieceSet[lastTo] = null)));
                    }
                }
            }
        }

        if (belong.equals(Player.WHITE)) {
            if (board.canMove(currentPosition, currentPosition + 8)) {
                pieceMoveList.add(currentPosition + 8);

                if (currentPosition < 16 && board.canMove(currentPosition, currentPosition + 16))
                    pieceMoveList.add(currentPosition + 16);
            }

            addMoveIfAttackable(pieceMoveList, currentPosition + 7);
            addMoveIfAttackable(pieceMoveList, currentPosition + 9);

            return pieceMoveList;
        }

        if (board.canMove(currentPosition, currentPosition - 8)) {
            pieceMoveList.add(currentPosition - 8);

            if (currentPosition > 47 && board.canMove(currentPosition, currentPosition - 16))
                pieceMoveList.add(currentPosition - 16);
        }

        addMoveIfAttackable(pieceMoveList, currentPosition - 7);
        addMoveIfAttackable(pieceMoveList, currentPosition - 9);

        return pieceMoveList;
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
