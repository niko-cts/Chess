package chatzis.nikolas.chess.test;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.pieces.Rook;
import chatzis.nikolas.chess.utils.BoardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to check different moves and special moves.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class BoardTest {

    @Test
    void rochadeNormal() {
        Board board = Board.createNewBoard("R3K2R/PPPPPPPP/8/8/8/8/pppppppp/r3k2r w KQkq - 0 1");
        assertEquals("[e1(K) d1, e1(K) f1, e1(K) c1, e1(K) g1]", board.getPieceOnBoard(4).getMoves(board).toString());
        assertEquals("[e8(k) d8, e8(k) f8, e8(k) c8, e8(k) g8]", board.getPieceOnBoard(60).getMoves(board).toString());

        assertTrue(board.makeMove(board.getPieceOnBoard(4).getMoves(board).get(2)).getPieceOnBoard(3) instanceof Rook);
        assertTrue(board.makeMove(board.getPieceOnBoard(4).getMoves(board).get(3)).getPieceOnBoard(5) instanceof Rook);
    }

    @Test
    void rochadeBlocking() {
        Board board = Board.createNewBoard("R3K2R/PPrPPrPP/8/8/8/8/pppppppp/r3k2r w KQkq - 0 1");
        System.out.println(board.getPieceOnBoard(4).getMoves(board));
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



}