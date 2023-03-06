package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.SpecialMove;

public class King extends Piece {

    public King(Player player, byte currentPosition) {
        super(player, 'K', currentPosition);
    }

    /**
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    @Override
    public void addPossibleMoves(Board board) {
        for (int i = -1; i < 2; i++) {
            addMoveWhenMoveableOrAttackable(board, (currentPosition + (8 + i)));
            addMoveWhenMoveableOrAttackable(board, (currentPosition + (-8 + i)));
        }
        addMoveWhenMoveableOrAttackable(board, (currentPosition - 1));
        addMoveWhenMoveableOrAttackable(board, (currentPosition + 1));

        if (board.kingIsNotChecked(getBelong())) {
            boolean[] castlingRights = board.getCastlingRights(getBelong());

            byte queenSideRookPos = belong.getQueenSidedRookStartingPosition();
            byte kingRookPos = belong.getKingSidedRookStartingPosition();

            // queen side rochade
            if (castlingRights[1] &&
                    board.getPieceOnBoard(queenSideRookPos + 1) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 2) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 3) == null) {

                // king will not be in check
                if (board.noneAttacks(getBelong().nextPlayer(), (byte) (currentPosition - 1))) {
                    // method always checks for king check
                    add(board, new SpecialMove(name, currentPosition, (byte) (currentPosition - 2), (pieceSet) -> {
                        Piece rook = pieceSet.get(queenSideRookPos);
                        pieceSet.put((byte) (queenSideRookPos + 3), rook);
                        rook.setCurrentPosition((byte) (queenSideRookPos + 3));
                        pieceSet.remove(queenSideRookPos);
                    }));
                }
            }

            // king side rochade
            if (castlingRights[0] &&
                    board.getPieceOnBoard(kingRookPos - 1) == null &&
                    board.getPieceOnBoard(kingRookPos - 2) == null) {
                if (board.noneAttacks(getBelong().nextPlayer(), (byte) (currentPosition + 1))) {
                    // method always checks for king check
                    add(board, new SpecialMove(name, currentPosition, (byte) (currentPosition + 2), (pieceSet)  -> {
                        Piece rook = pieceSet.get(kingRookPos);
                        pieceSet.put((byte) (kingRookPos - 2), rook);
                        rook.setCurrentPosition((byte) (kingRookPos - 2));
                        pieceSet.remove(kingRookPos);

                    }));
                }
            }
        }
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
