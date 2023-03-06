package chatzis.nikolas.chess.test;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.pieces.Piece;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class will evaluate every possible moves from depth 1 - 4.
 * The amount of moves was found in: <a href="https://youtu.be/Km024eldY1A?t=345">How many chatzis.nikolas.chess games are possible? by Numberphilie</a>
 * @author Nikolas C.
 * @since 1.1-SNAPSHOT
 */
public class PossibleMovesTest {

    @Test
    void depthTest() {
        assertEquals(20, numberOfMoves(Board.createNewBoard(), 1));
        assertEquals(400, numberOfMoves(Board.createNewBoard(), 2));
        assertEquals(8902, numberOfMoves(Board.createNewBoard(), 3));
        assertEquals(197303, numberOfMoves(Board.createNewBoard(), 4));
    }

    @Test
    void performanceTest() {
        long l = System.currentTimeMillis();
        System.out.println(numberOfMoves(Board.createNewBoard(), 5) + " moves in " + (System.currentTimeMillis()-l) + "ms");
    }

    /**
     * Will call itself till depth 0 is reached.
     * Counts the amount of moves per depth.
     * @param board Board - the starting board
     * @param depth int - the depth to look for
     * @return int - all possible moves
     */
    private int numberOfMoves(Board board, int depth) {
        if (depth == 0)
            return 1;
        int i = 0;
        for (Map.Entry<Piece, Set<Byte>> entry : board.getAllMovePositions().entrySet()) {
            for (Byte toPos : entry.getValue()) {
                i += numberOfMoves(board.makeMove(entry.getKey().getMove(toPos)), depth -1);
            }
        }
        return i;
    }
}
