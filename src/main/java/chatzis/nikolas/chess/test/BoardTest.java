package chatzis.nikolas.chess.test;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.utils.BoardUtils;
import chatzis.nikolas.chess.utils.FieldNameConverter;
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
        assertEquals("[c1,d1,f1,g1]", FieldNameConverter.fromFieldNumbers(board.getPieceOnBoard(4).getMoves(board)));
        assertEquals("[c8,d8,f8,g8]", FieldNameConverter.fromFieldNumbers(board.getPieceOnBoard(60).getMoves(board)));

        //assertTrue(board.makeMove(board.getPieceOnBoard(4).getMoves(board).get(2)).getPieceOnBoard(3) instanceof Rook);
        //assertTrue(board.makeMove(board.getPieceOnBoard(4).getMoves(board).get(3)).getPieceOnBoard(5) instanceof Rook);
    }

    @Test
    void checkMate() {
        assertTrue(Board.createNewBoard("K7/qq6/8/8/8/8/8/k7 w - - 0 1").getAllMovePositions().isEmpty());
        assertFalse(Board.createNewBoard("K7/q7/8/8/8/8/8/k7 w - - 0 1").getAllMovePositions().isEmpty());
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