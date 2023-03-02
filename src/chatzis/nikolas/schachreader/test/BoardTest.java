package chatzis.nikolas.schachreader.test;

import chatzis.nikolas.schachreader.game.Board;
import chatzis.nikolas.schachreader.game.Player;
import chatzis.nikolas.schachreader.move.Move;
import chatzis.nikolas.schachreader.move.PieceMoveList;
import chatzis.nikolas.schachreader.utils.BoardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to check different moves and special moves.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class BoardTest {

    @Test
    void rochadeBlack() {
        Board board = Board.createNewBoard().makeMove(new Move('s', (byte) 57, (byte) 41)).makeMove(new Move('s', (byte) 58, (byte) 42))
                .makeMove(new Move('s', (byte) 59, (byte) 40));
        assertEquals("[e8(K) d8, e8(K) c8]", board.getPieceOnBoard(60).getPossibleMoves(board).getMoves().toString());

        Board kingSide = Board.createNewBoard().makeMove(new Move('s', (byte) 61, (byte) 41)).makeMove(new Move('s', (byte) 62, (byte) 42));
        assertEquals("[e8(K) f8, e8(K) g8]", kingSide.getPieceOnBoard(60).getPossibleMoves(kingSide).getMoves().toString());

        // no rochade if check
        Board newKingSide = kingSide.makeMove(new Move('n', (byte) 1, (byte) 55));
        assertEquals("[]", newKingSide.getPieceOnBoard(60).getPossibleMoves(newKingSide).getMoves().toString());
    }

    @Test
    void rochadeWhite() {
        Board board = Board.createNewBoard().makeMove(new Move('s', (byte) 1, (byte) 18)).makeMove(new Move('s', (byte) 2, (byte) 8)).makeMove(new Move('s', (byte) 3, (byte) 9));
        assertEquals("[e1(K) d1, e1(K) c1]", board.getPieceOnBoard(4).getPossibleMoves(board).getMoves().toString());

        Board kingSide = Board.createNewBoard().makeMove(new Move('s', (byte) 6, (byte) 18)).makeMove(new Move('s', (byte) 5, (byte) 8));
        assertEquals("[e1(K) f1, e1(K) g1]", kingSide.getPieceOnBoard(4).getPossibleMoves(kingSide).getMoves().toString());
    }

    /**
     * To check if the {@link BoardUtils#staysOnBoard(int, int)} works properly.
     * Will try to position a piece from "one side to the other" over the edge of the board.
     */
    @Test
    void staysOnBoard() {
        for (int i = 0; i < 64; i += 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i + 11));
            assertFalse(BoardUtils.staysOnBoard(i, i + 12));
            assertFalse(BoardUtils.staysOnBoard(i, i + 13));
            assertFalse(BoardUtils.staysOnBoard(i, i + 14));
            assertFalse(BoardUtils.staysOnBoard(i, i + 15));
        }

        for (int i = 1; i < 64; i += 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i + 11));
            assertFalse(BoardUtils.staysOnBoard(i, i + 12));
            assertFalse(BoardUtils.staysOnBoard(i, i + 13));
            assertFalse(BoardUtils.staysOnBoard(i, i + 14));

            assertTrue(BoardUtils.staysOnBoard(i, i - 1));
            assertTrue(BoardUtils.staysOnBoard(i, i + 1));
        }

        for (int i = 6; i < 64; i += 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i + 2));
            assertFalse(BoardUtils.staysOnBoard(i, i + 3));
            assertFalse(BoardUtils.staysOnBoard(i, i + 4));
            assertFalse(BoardUtils.staysOnBoard(i, i + 5));
        }

        for (int i = 7; i < 56; i += 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i + 1));
            assertFalse(BoardUtils.staysOnBoard(i, i + 2));
            assertFalse(BoardUtils.staysOnBoard(i, i + 3));
            assertFalse(BoardUtils.staysOnBoard(i, i + 4));
            assertFalse(BoardUtils.staysOnBoard(i, i + 5));

            assertTrue(BoardUtils.staysOnBoard(i, i + 6));
        }


        for (int i=56; i > 7;i -= 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i - 1));
            assertFalse(BoardUtils.staysOnBoard(i, i - 2));
            assertFalse(BoardUtils.staysOnBoard(i, i - 3));
            assertFalse(BoardUtils.staysOnBoard(i, i - 4));
            assertFalse(BoardUtils.staysOnBoard(i, i - 5));

            assertTrue(BoardUtils.staysOnBoard(i, i - 6));
        }

        for (int i=57; i > 8;i -= 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i - 2));
            assertFalse(BoardUtils.staysOnBoard(i, i - 3));
            assertFalse(BoardUtils.staysOnBoard(i, i - 4));
            assertFalse(BoardUtils.staysOnBoard(i, i - 5));

            assertTrue(BoardUtils.staysOnBoard(i, i - 1));
            assertTrue(BoardUtils.staysOnBoard(i, i - 6));
        }

        for (int i = 63; i > 0; i -= 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i - 11));
            assertFalse(BoardUtils.staysOnBoard(i, i - 12));
            assertFalse(BoardUtils.staysOnBoard(i, i - 13));
            assertFalse(BoardUtils.staysOnBoard(i, i - 14));
            assertFalse(BoardUtils.staysOnBoard(i, i - 15));
        }

        for (int i = 62; i > 0; i -= 8) {
            assertFalse(BoardUtils.staysOnBoard(i, i - 11));
            assertFalse(BoardUtils.staysOnBoard(i, i - 12));
            assertFalse(BoardUtils.staysOnBoard(i, i - 13));
            assertFalse(BoardUtils.staysOnBoard(i, i - 14));

            assertTrue(BoardUtils.staysOnBoard(i, i - 1));
        }
    }

    @Test
    void possibleMoveAmounts() {
        Board board = Board.createNewBoard();
        assertEquals(20, numberOfMoves(board, 1));
        assertEquals(400, numberOfMoves(board, 2));
        assertEquals(8902, numberOfMoves(board, 3));
        // according to https://youtu.be/Km024eldY1A?t=345 depth 4 needs to be 197742
        assertEquals(197742, numberOfMoves(board, 4));
    }

    private int numberOfMoves(Board board, int depth) {
        if (depth == 0)
            return 1;
        int i = 0;
        for (Move move : board.getAllMoves()) {
            i += numberOfMoves(board.makeMove(move), depth - 1);
        }
        return i;
    }

}