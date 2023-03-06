package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.move.PieceMoveList;
import chatzis.nikolas.chess.move.SpecialMove;

public class King extends Piece {

    public King(Player player, byte currentPosition) {
        super(player, 'K', currentPosition);
    }

    /**
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{@link Move}> - every move the piece can make.
     */
    @Override
    public PieceMoveList getPossibleMoves(Board board) {
        PieceMoveList moveList = new PieceMoveList(board, this);
        for (int i = -1; i < 2; i++) {
            addMoveWhenMoveableOrAttackable(moveList, (byte) (currentPosition + (8 + i)));
            addMoveWhenMoveableOrAttackable(moveList, (byte) (currentPosition + (-8 + i)));
        }
        addMoveWhenMoveableOrAttackable(moveList, (byte) (currentPosition - 1));
        addMoveWhenMoveableOrAttackable(moveList, (byte) (currentPosition + 1));

        if (board.kingIsNotChecked()) {
            boolean[] castlingRights = board.getCastlingRights(getBelong());

            byte queenSideRookPos = belong.getQueenSidedRookStartingPosition();
            byte kingRookPos = belong.getKingSidedRookStartingPosition();

            // queen side rochade
            if (castlingRights[1] &&
                    board.getPieceOnBoard(queenSideRookPos + 1) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 2) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 3) == null) {

                // king will not be in check
                if (board.kingIsNotInCheckAfterMove(new Move(name, currentPosition, currentPosition - 1))) {
                    // method always checks for king check
                    moveList.add(new SpecialMove(name, currentPosition, (byte) (currentPosition - 2), pieceSet -> {
                        pieceSet[queenSideRookPos + 3] = pieceSet[queenSideRookPos];
                        pieceSet[queenSideRookPos + 3].setCurrentPosition((byte) (queenSideRookPos + 3));
                        pieceSet[queenSideRookPos] = null;
                    }));
                }
            }

            // king side rochade
            if (castlingRights[0] &&
                    board.getPieceOnBoard(kingRookPos - 1) == null &&
                    board.getPieceOnBoard(kingRookPos - 2) == null) {
                if (board.kingIsNotInCheckAfterMove(new Move(name, currentPosition, currentPosition + 1))) {
                    // method always checks for king check
                    moveList.add(new SpecialMove(name, currentPosition, (byte) (currentPosition + 2), pieceSet -> {
                        pieceSet[kingRookPos - 2] = pieceSet[kingRookPos];
                        pieceSet[kingRookPos - 2].setCurrentPosition((byte) (kingRookPos - 2));
                        pieceSet[kingRookPos] = null;
                    }));
                }
            }
        }

        return moveList;
    }

    /**
     * Copies the instance of this class.
     * @return {@link Piece} - copies instance
     */
    @Override
    public King clone() {
        return new King(belong, currentPosition);
    }
}
