package chatzis.nikolas.chess;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.pieces.Piece;
import chatzis.nikolas.chess.utils.FieldNameConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main class to play chess in the console.
 * @author Nikolas Chatzis
 * @since 1.0-SNAPSHOT
 */
public class Main {

    /**
     * Starts default chess in the console.
     * Type in line by line to move pieces:
     * e.g:
     * b1
     * c3
     * @param args String[] - arguments
     * @throws IOException - exception with console reader.
     */
    public static void main(String[] args) throws IOException {
        Board board = Board.createNewBoard();
        System.out.println(board);

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String name;
        String from = "";
        List<Move> moves = new ArrayList<>();
        while (!(name = reader.readLine()).isEmpty()) {
            if (name.equalsIgnoreCase("exit")) {
                from = "";
                board = Board.createNewBoard();
                continue;
            }
            try {

                if (from.isEmpty()) {
                    byte fieldNr = FieldNameConverter.fromFieldName(name);
                    from = name;
                    Piece fromPiece = board.getPieceOnBoard(fieldNr);
                    if (fromPiece == null) {
                        System.err.println("No piece");
                        continue;
                    }
                    moves = fromPiece.getMoves(board);
                    printMoves(moves);

                    System.out.println("Selected " + fromPiece);
                } else {
                    byte to = FieldNameConverter.fromFieldName(name);
                    Optional<Move> move = moves.stream().filter(m -> m.to() == to).findFirst();
                    from = "";
                    if (move.isEmpty()) {
                        System.err.println("No possible move.");
                        System.err.println("Selected new starting point.");
                        continue;
                    }
                    board = board.makeMove(move.get());

                    System.out.println(board);

                    if (board.getAllMoves().isEmpty()) {
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

    private static void printMoves(List<Move> possibleMoves) {
        StringBuilder builder = new StringBuilder();
        builder.append(possibleMoves.size()).append(" Moves: ");
        for (Move possibleMove : possibleMoves) {
            builder.append(possibleMove).append(", ");
        }
        System.out.println(builder);
    }
}