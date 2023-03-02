package chatzis.nikolas.schachreader.pieces;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.Move;
import chatzis.nikolas.schachreader.move.PieceMoveList;
import chatzis.nikolas.schachreader.move.SpecialMove;
import chatzis.nikolas.schachreader.utils.BoardUtils;

public class King extends RememberMovePiece {

    public King(Player player, byte currentPosition) {
        this(player, false, currentPosition);
    }

    public King(Player player, boolean moved, byte currentPosition) {
        super(player, 'K', moved, currentPosition);
    }

    /**
     * Lists every possible move the piece can make with the given board.
     *
     * @param board           {@link Board} - the board.
     * @return List<{ @ link Move }> - every move the piece can make.
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

        if (!board.isSimulated() && hasntMoved() && BoardUtils.kingIsntChecked(board)) {

            byte queenSideRookPos;
            byte kingRookPos;
            if (belong.equals(Player.WHITE)) {
                queenSideRookPos = 0;
                kingRookPos = 7;
            } else {
                queenSideRookPos = 56;
                kingRookPos = 63;
            }

            // queen side rochade
            Piece rook = board.getPieceOnBoard(queenSideRookPos);
            if (rook instanceof Rook && ((Rook) rook).hasntMoved() &&
                    board.getPieceOnBoard(queenSideRookPos + 1) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 2) == null &&
                    board.getPieceOnBoard(queenSideRookPos + 3) == null) {

                // king will not be in check
                if (BoardUtils.kingIsntChecked(board, new Move(name, currentPosition, currentPosition - 1))) {
                    // method always checks for king check
                    moveList.add(new SpecialMove(name, currentPosition, (byte) (currentPosition - 2), (pieceSet, piece) -> {
                        pieceSet[queenSideRookPos + 3] = pieceSet[queenSideRookPos];
                        pieceSet[queenSideRookPos].setCurrentPosition((byte) (queenSideRookPos + 3));
                        pieceSet[queenSideRookPos] = null;
                    }));
                }
            }

            // king side rochade
            Piece kingSidedRook = board.getPieceOnBoard(kingRookPos);
            if (kingSidedRook instanceof Rook && ((Rook) kingSidedRook).hasntMoved() &&
                    board.getPieceOnBoard(kingRookPos - 1) == null &&
                    board.getPieceOnBoard(kingRookPos - 2) == null) {
                if (BoardUtils.kingIsntChecked(board, new Move(name, currentPosition, currentPosition + 1))) {
                    // method always checks for king check
                    moveList.add(new SpecialMove(name, currentPosition, (byte) (currentPosition + 2), (pieceSet, piece) -> {
                        pieceSet[kingRookPos - 2] = pieceSet[kingRookPos];
                        pieceSet[kingRookPos] = null;
                    }));
                }
            }
        }

        return moveList;
    }

    /**
     * Copies the instance of this class.
     *
     * @return {@link Piece} - copies instance
     */
    @Override
    public King copy() {
        return new King(belong, moved, currentPosition);
    }
}
