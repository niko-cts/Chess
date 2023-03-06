package chatzis.nikolas.chess.game;

public enum Player {

    WHITE('W', (byte) 7, (byte) 0),
    BLACK('B', (byte) 63, (byte) 56);

    private final char name;
    private final byte kingSidedRookStartingPosition;
    private final byte queenSidedRookStartingPosition;


    Player(char name, byte kingSidedRookStartingPosition, byte queenSidedRookStartingPosition) {
        this.name = name;
        this.kingSidedRookStartingPosition = kingSidedRookStartingPosition;
        this.queenSidedRookStartingPosition = queenSidedRookStartingPosition;
    }

    public char getName() {
        return name;
    }

    /**
     * Returns the starting position of the king sided rook.
     * @return byte - starting pos of king sided rook.
     */
    public byte getKingSidedRookStartingPosition() {
        return kingSidedRookStartingPosition;
    }

    /**
     * Returns the starting position of the queen sided rook.
     * @return byte - starting pos of queen sided rook.
     */
    public byte getQueenSidedRookStartingPosition() {
        return queenSidedRookStartingPosition;
    }

    /**
     * Returns the next player.
     * @return Player - the next player.
     */
    public Player nextPlayer() {
        return this == WHITE ? BLACK : WHITE;
    }

    @Override
    public String toString() {
        return "Player " + this.name;
    }
}
