package chatzis.nikolas.chess.pieces;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.game.Player;
import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.utils.BoardUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract piece class which holds necessary information about owner, name and current position.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public abstract class Piece implements Cloneable {

    protected final Player belong;
    protected final char name;
    protected byte currentPosition;
    private final Move[] possibleMoves;
    private Set<Byte> toMoves;


    /**
     * Instantiates the class.
     * @param player Player - the player the piece belongs to.
     * @param name char - the abbreviation of the piece
     * @param currentPosition byte - the starting position of the piece
     */
    public Piece(Player player, char name, byte currentPosition) {
        this.belong = player;
        this.name = player.equals(Player.BLACK) ? (name + "").toLowerCase().charAt(0) :  name;
        this.currentPosition = currentPosition;
        this.possibleMoves = new Move[64];
    }

    /**
     * Adds every possible move the piece can make to hashset.
     * @param board {@link Board} - the board.
     * @since 1.1-SNAPSHOT
     */
    protected abstract void addPossibleMoves(Board board);

    protected void add(Board board, Move move) {
        if (board.kingIsNotInCheckAfterMove(move)) {
            possibleMoves[move.to()] = move;
            toMoves.add(move.to());
        }
    }

    protected void add(Board board, int to) {
        add(board, (byte) to);
    }

    protected void add(Board board, byte to) {
        if (board.kingIsNotInCheckAfterMove(new Move(name, currentPosition, to))) {
            toMoves.add(to);
        }
    }

    public Move getMove(byte toPos) {
        Move move = possibleMoves[toPos];
        return move != null ? move : new Move(name, currentPosition, toPos);
    }

    /**
     * Get all the legal move positions of the piece.
     * @param board Board - the board to get the positions.
     * @return Set<Move> - all the to-positions
     */
    public Set<Byte> getMoves(Board board) {
        if (toMoves == null) {
            toMoves = new HashSet<>();
            addPossibleMoves(board);
        }
        return toMoves;
    }

    /**
     * Checks if given toPosition is movable or stackable then adds to the moveList.
     * @param board {@link Board} - the board to add.
     * @param toPosition byte - position to check and move to.
     */
    protected void addMoveWhenMoveableOrAttackable(Board board, int toPosition) {
        if (BoardUtils.staysOnBoard(currentPosition, toPosition)) {
            Piece pieceOnBoard = board.getPieceOnBoard(toPosition);
            if (pieceOnBoard == null || pieceOnBoard.isNotSamePlayer(belong))
                add(board, toPosition); // checks if owned king would be under attack
        }
    }


    /**
     * Gets a repeating pattern from the current position till the piece cannot move further.
     * @param board {@link Board} - the board
     * @param pattern int[] - the moving pattern.
     */
    protected void addRepeatingMove(Board board, int[] pattern) {
        for (int i : pattern) {
            for (byte m : new byte[]{1, -1}) {
                i *= m;
                byte copiedPosition = currentPosition;
                byte movingPosition = (byte) (currentPosition + i);
                while (board.canMove(copiedPosition, movingPosition)) {
                    add(board, movingPosition);
                    copiedPosition = movingPosition;
                    movingPosition += i;
                }

                if (BoardUtils.staysOnBoard(copiedPosition, movingPosition) && board.isEnemyPiece(belong, movingPosition))
                    add(board, movingPosition);
            }
        }
    }

    /**
     * Check if the piece belongs to the given player.
     * @return boolean - piece belongs to the same player.
     */
    public boolean isNotSamePlayer(Player player) {
        return belong != player;
    }

    @Override
    public String toString() {
        return this.name + "(" + currentPosition + ")";
    }

    public void setCurrentPosition(byte currentPosition) {
        this.currentPosition = currentPosition;
    }

    public char getName() {
        return name;
    }

    public Player getBelong() {
        return belong;
    }

    /**
     * Copies the instance of this class.
     * @return {@link Piece} - copied instance
     */
    public abstract Piece clone();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return name == piece.name && currentPosition == piece.currentPosition && Objects.equals(belong, piece.belong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(belong, name, currentPosition);
    }

}
