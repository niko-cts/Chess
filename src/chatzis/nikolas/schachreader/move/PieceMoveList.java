package chatzis.nikolas.schachreader.move;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.pieces.Piece;
import chatzis.nikolas.schachreader.pieces.RememberMovePiece;
import chatzis.nikolas.schachreader.utils.BoardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Class, which stores every possible move a piece can make on the given board.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class PieceMoveList {

    private final Board board;
    private final Piece movingPiece;
    private final List<Move> moves;

    public PieceMoveList(Board board, Piece piece) {
        this.board = board;
        this.movingPiece = piece;
        this.moves = new ArrayList<>();
    }

    /**
     * Adds the given move if the board is not simulated and the owners king isn't checked.
     * @param move Move - the move to add
     */
    public void add(Move move) {
        if (board.isSimulated() || BoardUtils.kingIsntChecked(board, move))
           moves.add(move);
    }

    /**
     * Creates a {@link Move} or {@link SpecialMove} instance based on the extending {@link Piece} class.
     * @param to int - the position to move.
     */
    public void add(int to) {
        if (movingPiece instanceof RememberMovePiece && ((RememberMovePiece) movingPiece).hasntMoved())
            add(new SpecialMove(movingPiece.getName(), movingPiece.getCurrentPosition(), (byte) to, ((pieces, piece) -> ((RememberMovePiece) piece).moved())));
        else
            add(new Move(movingPiece.getName(), movingPiece.getCurrentPosition(), (byte) to));
    }


    public Board getBoard() {
        return board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Optional<Move> findByDestination(byte to) {
        return moves.stream().filter(m -> m.to() == to).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMoveList that = (PieceMoveList) o;
        return Objects.equals(board, that.board) && Objects.equals(movingPiece, that.movingPiece) && Objects.equals(moves, that.moves);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("[ ");
        moves.forEach(m -> builder.append(m.toString()).append(","));
        builder.append("]");
        return movingPiece.toString() + "\n" + builder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, movingPiece, moves);
    }
}
