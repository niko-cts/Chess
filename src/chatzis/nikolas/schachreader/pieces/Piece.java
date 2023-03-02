package chatzis.nikolas.schachreader.pieces;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.PieceMoveList;
import chatzis.nikolas.schachreader.utils.BoardUtils;

import java.util.Objects;

/**
 * Abstract piece class which holds necessary information about owner, name and current position.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public abstract class Piece {

    protected final Player belong;
    protected final char name;
    protected byte currentPosition;

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
    }

    /**
     * Lists every possible move the piece can make with the given board.
     * @param board {@link Board} - the board.
     * @return List<{@link Move}> - every move the piece can make.
     */
    public abstract PieceMoveList getPossibleMoves(Board board);

    /**
     * Checks if given toPosition is movable or stackable then adds to the moveList.
     * @param pieceMoveList {@link PieceMoveList} - move list to add.
     * @param toPosition byte - position to check and move to.
     */
    protected void addMoveWhenMoveableOrAttackable(PieceMoveList pieceMoveList, byte toPosition) {
        if (BoardUtils.staysOnBoard(currentPosition, toPosition)) {
            Piece pieceOnBoard = pieceMoveList.getBoard().getPieceOnBoard(toPosition);
            if (pieceOnBoard == null || pieceOnBoard.isntSamePlayer(belong))
                pieceMoveList.add(toPosition); // checks if owned king would be under attack
        }
    }

    /**
     * Adds a move to the given list, if the fromPosition can attack the toPosition
     * @param pieceMoveList {@link PieceMoveList} - the piece move list instance
     * @param attackPosition int - the attackPosition.
     */
    protected void addMoveIfAttackable(PieceMoveList pieceMoveList, int attackPosition) {
        if (BoardUtils.staysOnBoard(currentPosition, attackPosition) && pieceMoveList.getBoard().isEnemyPiece(belong, (byte) attackPosition))
            pieceMoveList.add(attackPosition);
    }

    /**
     * Gets a repeating pattern from the current position till the piece cannot move further.
     * @param board {@link Board} - the board
     * @param pattern int[] - the moving pattern.
     * @return List<{@link Move}> - all moves
     */
    protected PieceMoveList getRepeatingMove(Board board, int[] pattern) {
        PieceMoveList pieceMoveList = new PieceMoveList(board, this);

        for (int i : pattern) {
            for (byte m : new byte[]{1, -1}) {
                i *= m;
                byte copiedPosition = currentPosition;
                byte movingPosition = (byte) (currentPosition + i);
                while (board.canMove(copiedPosition, movingPosition)) {
                    pieceMoveList.add(movingPosition);
                    copiedPosition = movingPosition;
                    movingPosition += i;
                }

                if (BoardUtils.staysOnBoard(copiedPosition, movingPosition) && board.isEnemyPiece(belong, movingPosition))
                    pieceMoveList.add(movingPosition);
            }
        }

        return pieceMoveList;
    }

    /**
     * Check if the piece belongs to the give player.
     * @return boolean - piece belongs to the same player.
     */
    public boolean isntSamePlayer(Player player) {
        return !belong.equals(player);
    }

    @Override
    public String toString() {
        return this.name + "(" + currentPosition + ")";
    }

    public byte getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(byte currentPosition) {
        this.currentPosition = currentPosition;
    }

    public char getName() {
        return name;
    }

    /**
     * Copies the instance of this class.
     * @return {@link Piece} - copies instance
     */
    public abstract Piece copy();


    public Player getBelong() {
        return belong;
    }

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