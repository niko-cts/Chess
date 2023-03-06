package chatzis.nikolas.chess;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.gui.ChessGUI;
import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.pieces.Piece;
import chatzis.nikolas.chess.utils.FieldNameConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;

/**
 * Main class to play chatzis.nikolas.chess in the console.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class Main {

    /**
     * Starts default chess in {@link chatzis.nikolas.chess.gui.ChessGUI}
     * @param args String[] - arguments
     */
    public static void main(String[] args) {
        new ChessGUI(args.length == 0 ? Board.createNewBoard() : Board.createNewBoard(Arrays.toString(args).replace("[", "").replace("]", "")));
    }

    /**
     * Instantiate this class to enable console playing.
     * @throws IOException - Because of console reading
     */
    private Main() throws IOException {
        Board board = Board.createNewBoard();
        System.out.println(board);

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String name;
        Piece fromPiece = null;
        Set<Byte> moves;
        while (!(name = reader.readLine()).isEmpty()) {
            if (name.equalsIgnoreCase("exit")) {
                board = Board.createNewBoard();
                continue;
            }
            try {

                if (fromPiece == null) {
                    byte fieldNr = FieldNameConverter.fromFieldName(name);
                    fromPiece = board.getPieceOnBoard(fieldNr);
                    if (fromPiece == null) {
                        System.err.println("No piece");
                        continue;
                    }
                    moves = fromPiece.getMoves(board);
                    printMoves(moves);

                    System.out.println("Selected " + fromPiece);
                } else {
                    byte to = FieldNameConverter.fromFieldName(name);
                    Move move = fromPiece.getMove(to);
                    fromPiece = null;
                    if (move == null) {
                        System.err.println("No possible move.");
                        System.err.println("Selected new starting point.");
                        continue;
                    }
                    board = board.makeMove(move);

                    System.out.println(board);

                    if (board.getAllMovePositions().isEmpty()) {
                        System.out.println("Checkmate");
                        return;
                    }
                    System.out.println("No checkmate");

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void printMoves(Set<Byte> possibleMoves) {
        StringBuilder builder = new StringBuilder();
        builder.append(possibleMoves.size()).append(" Moves: ");
        for (Byte possibleMove : possibleMoves) {
            builder.append(FieldNameConverter.fromFieldNumber(possibleMove)).append(", ");
        }
        System.out.println(builder);
    }
}