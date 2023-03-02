package chatzis.nikolas.schachreader.pieces;

import chatzis.nikolas.schachreader.game.Player;

/**
 * Special abstract class for pieces,
 * which should remeber that they moved.
 * @see King
 * @see Rook
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public abstract class RememberMovePiece extends Piece {

    protected boolean moved;

    /**
     * Instantiates the class.
     * @param player Player - the player the piece belongs to.
     * @param name   char - the abbreviation of the piece
     */
    public RememberMovePiece(Player player, char name, boolean moved, byte position) {
        super(player, name, position);
        this.moved = moved;
    }

    public void moved() {
        moved = true;
    }

    public boolean hasntMoved() {
        return !moved;
    }

}
