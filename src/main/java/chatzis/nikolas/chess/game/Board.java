package chatzis.nikolas.chess.game;

import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.move.SpecialMove;
import chatzis.nikolas.chess.pieces.King;
import chatzis.nikolas.chess.pieces.Pawn;
import chatzis.nikolas.chess.pieces.Piece;
import chatzis.nikolas.chess.pieces.Rook;
import chatzis.nikolas.chess.utils.BoardUtils;
import chatzis.nikolas.chess.utils.FieldNameConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Board class to store every piece.
 *
 * @author Nikolas Chatzis
 * @since 1.0-Snapshot
 */
public class Board {

    private final Player currentPlayer;
    private final Piece[] pieceSet;
    private final boolean[][] castlingRights; // white?: [king-sided?, queen-sided?]
    private final byte whiteKing;
    private final byte blackKing;
    private final byte enPassant;
    private final int simulationDepth;
    private Boolean kingInCheck;

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieceSet      {@link Piece}[] - a 8*8 piece array (nullable)
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param enPassant      Byte - a possible en passant position
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Piece[] pieceSet, byte whiteKing, byte blackKing, byte enPassant, boolean[][] castleRights) {
        this (currentPlayer, pieceSet, whiteKing, blackKing, enPassant, castleRights, 0);
    }

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieceSet      {@link Piece}[] - a 8*8 piece array (nullable)
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param enPassant      Byte - a possible en passant position
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @param simulationDepth int - the simulated depth (until 2)
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Piece[] pieceSet, Byte whiteKing, Byte blackKing, byte enPassant, boolean[][] castleRights, int simulationDepth) {
        this.currentPlayer = currentPlayer;
        this.pieceSet = pieceSet;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.enPassant = enPassant;
        this.castlingRights = castleRights;
        this.simulationDepth = simulationDepth;
    }

    /**
     * Gets the piece on the field number.
     *
     * @param fieldNumber byte - the field number.
     * @return Piece - the piece on the field (nullable)
     */
    public Piece getPieceOnBoard(int fieldNumber) {
        if (fieldNumber < 0 || fieldNumber > 63)
            throw new IllegalStateException("Field number needs to be between 0 - 63.");
        return pieceSet[fieldNumber];
    }

    /**
     * Checks if the field is not occupied and the piece doesn't move out of the border.
     *
     * @param from int - from position
     * @param to   int - to position
     * @return boolean - can move from position to position.
     */
    public boolean canMove(int from, int to) {
        return BoardUtils.staysOnBoard(from, to) && getPieceOnBoard(to) == null;
    }

    /**
     * Returns a list of all moves the player has.
     *
     * @return List<Move> - all moves the current player can do.
     */
    public List<Move> getAllMoves() {
        return getAllMoves(currentPlayer);
    }


    /**
     * Returns a list of all moves the given player has.
     *
     * @param player Player - player to get moves
     * @return List<Move> - all moves the player can do.
     */
    public List<Move> getAllMoves(Player player) {
        if (this.simulationDepth == 2)
            return new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        Board newBoard = new Board(currentPlayer, pieceSet, whiteKing, blackKing, enPassant, castlingRights, this.simulationDepth + 1);
        for (Piece piece : pieceSet) {
            if (piece != null && piece.getBelong() == player)
                moves.addAll(piece.getMoves(newBoard));
        }
        return moves;
    }

    /**
     * Makes a move and returns a new board.
     * @param move      {@link Move} - the move to make.
     * @return Board - the new board.
     */
    public Board makeMove(Move move) {
        byte newWhiteKing = whiteKing;
        byte newBlackKing = blackKing;
        byte newEnPassant = -1;

        // king position update
        if (move.from() == whiteKing || move.from() == blackKing) {
            if (move.from() == whiteKing)
                newWhiteKing = move.to();
            else if (move.from() == blackKing)
                newBlackKing = move.to();
        }

        Piece[] newPieceSet = clonePieceSet();
        Piece movingPiece = newPieceSet[move.from()];
        newPieceSet[move.from()] = null;
        newPieceSet[move.to()] = movingPiece;
        movingPiece.setCurrentPosition(move.to());

        if (movingPiece instanceof Pawn && Math.abs(move.from() - move.to()) == 16) {
            // pawn jumped two rows
            newEnPassant = (byte) (move.to() + (currentPlayer == Player.WHITE ? -8 : 8));
        }

        if (move instanceof SpecialMove)
            ((SpecialMove) move).getMove().moved(newPieceSet);

        // castle rights
        boolean[][] newCastleRights = new boolean[][]{castlingRights[0].clone(), castlingRights[1].clone()};
        if (movingPiece instanceof King) {
            newCastleRights[currentPlayer.ordinal()][0] = false;
            newCastleRights[currentPlayer.ordinal()][1] = false;
        } else if (movingPiece instanceof Rook) {
            if (move.from() == currentPlayer.getQueenSidedRookStartingPosition())
                newCastleRights[currentPlayer.ordinal()][1] = false;
            else if (move.from() == currentPlayer.getKingSidedRookStartingPosition())
                newCastleRights[currentPlayer.ordinal()][0] = false;
        }

        return new Board(currentPlayer.nextPlayer(), newPieceSet, newWhiteKing, newBlackKing, newEnPassant, newCastleRights, this.simulationDepth);
    }

    /**
     * Clones all pieces.
     *
     * @return {@link Piece}[] - copied piece set
     */
    private Piece[] clonePieceSet() {
        Piece[] copied = new Piece[pieceSet.length];
        for (int i = 0; i < copied.length; i++) {
            copied[i] = pieceSet[i] != null ? pieceSet[i].clone() : null;
        }
        return copied;
    }

    /**
     * Creates a default chatzis.nikolas.chess board.
     *
     * @return Board - default board.
     */
    public static Board createNewBoard() {
        return createNewBoard("RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr w KQkq - 0 1");
    }

    /**
     * Creates a chatzis.nikolas.chess board with the given board data.
     *
     * @param fen String - the Forsyth-Edwards Notation
     * @return Board - default board.
     */
    public static Board createNewBoard(String fen) {
        String[] fenData = fen.split(" ");

        String[] board = fenData[0].split("/");

        Piece[] pieces = new Piece[64];

        byte whiteKing = 0;
        byte blackKing = 0;

        int x = 0;
        for (String s : board) {
            for (char chessPieceChar : s.toCharArray()) {
                try {
                    x += Integer.parseInt(chessPieceChar + ""); // Is number
                } catch (NumberFormatException exception) {
                    Piece piece = BoardUtils.getPieceByChar(chessPieceChar, (byte) x);
                    pieces[x] = piece;
                    if (piece instanceof King) {
                        if (piece.getBelong().equals(Player.WHITE)) {
                            whiteKing = (byte) x;
                        } else {
                            blackKing = (byte) x;
                        }
                    }
                    x++;
                }
            }
        }
        return new Board(fenData[1].equals("w") ? Player.WHITE : Player.BLACK,
                pieces, whiteKing, blackKing, FieldNameConverter.fromFieldName(fenData[3]),
                new boolean[][]{new boolean[]{fenData[2].contains("K"), fenData[2].contains("Q")}, new boolean[]{fenData[2].contains("k"), fenData[2].contains("q")}});
    }

    /**
     * Checks if the given position is an enemy piece to the given player.
     *
     * @param player   Player - friendly player
     * @param position byte - the position to check
     * @return boolean - position is not null and an enemy piece
     */
    public boolean isEnemyPiece(Player player, byte position) {
        Piece piece = getPieceOnBoard(position);
        return piece != null && piece.isNotSamePlayer(player);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pieceSet.length; i++) {
            Piece pieceOnBoard = getPieceOnBoard(i);
            builder.append(pieceOnBoard != null ? pieceOnBoard : "_").append(" ");
            if (i % 8 == 7)
                builder.append("   ").append(i / 8 + 1).append("\n");
        }

        return builder.append("\nA B C D E F G H\n")
                .append(currentPlayer).append(" ")
                .append(castlingRights[0][0] ? "K" : "")
                .append(castlingRights[0][1] ? "Q" : "")
                .append(castlingRights[1][0] ? "k" : "")
                .append(castlingRights[1][1] ? "q" : "")
                .append(!castlingRights[0][0] && !castlingRights[0][1] && !castlingRights[1][0] && !castlingRights[1][1] ? "-" : "")
                .append("\n\n").toString();
    }

    /**
     * Get possible en passant move.
     * @return Byte - the position the enemey pawn could attack
     * @since 1.1-SNAPSHOT
     */
    public byte getEnPassant() {
        return enPassant;
    }

    /**
     * Check if the king is NOT currently checked.
     * @return boolean - king not checked
     * @since 1.1-SNAPSHOT
     */
    public boolean kingIsNotChecked() {
        return kingIsNotChecked(currentPlayer);
    }

    /**
     * Check if the king is NOT currently checked.
     * @param player Player - the player to check.
     * @return boolean - king not checked
     * @since 1.1-SNAPSHOT
     */
    private boolean kingIsNotChecked(Player player) {
        if (this.simulationDepth > 1)
            return true;
        if (kingInCheck == null) {
            byte kingPosition = player == Player.WHITE ? whiteKing : blackKing;
            this.kingInCheck = getAllMoves(player.nextPlayer()).stream().anyMatch(m -> m.to() == kingPosition);
        }
        return !kingInCheck;
    }

    /**
     * Check if the king is not checked after the given move.
     * @param move Move - the move to do first.
     * @return boolean - king will not be checked
     * @since 1.1-SNAPSHOT
     */
    public boolean kingIsNotInCheckAfterMove(Move move) {
        return this.simulationDepth > 1 || makeMove(move).kingIsNotChecked(currentPlayer);
    }

    /**
     * Get the castle rights of one player: [kingSided, queenSided]
     * @param player Player - the player to get.
     * @return boolean[] - [kingSided, queenSided] rochade
     * @since 1.1-SNAPSHOT
     */
    public boolean[] getCastlingRights(Player player) {
        return castlingRights[player.ordinal()];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
