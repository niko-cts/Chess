package chatzis.nikolas.schachreader.game;

import java.util.Objects;

public class Player {

    public static final Player WHITE = new Player('W');
    public static final Player BLACK = new Player('B');

    private final char name;

    private Player(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    /**
     * Returns the next player.
     * @return Player - the next player.
     */
    public Player nextPlayer() {
        return this.name == 'W' ? BLACK : WHITE;
    }

    @Override
    public String toString() {
        return "Player " + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name == player.name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
