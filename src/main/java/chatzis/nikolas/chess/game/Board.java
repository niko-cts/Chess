package chatzis.nikolas.chess.game;

import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.move.SpecialMove;
import chatzis.nikolas.chess.pieces.King;
import chatzis.nikolas.chess.pieces.Pawn;
import chatzis.nikolas.chess.pieces.Piece;
import chatzis.nikolas.chess.pieces.Rook;
import chatzis.nikolas.chess.utils.BoardUtils;
import chatzis.nikolas.chess.utils.FieldNameConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Board class to store every piece.
 *
 * @author Nikolas Chatzis
 * @since 1.0-Snapshot
 */
public class Board {

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

        Map<Byte, Piece> pieceSet = new HashMap<>();

        byte whiteKing = 0;
        byte blackKing = 0;

        int x = 0;
        for (String s : board) {
            for (char chessPieceChar : s.toCharArray()) {
                try {
                    x += Integer.parseInt(chessPieceChar + ""); // Is number
                } catch (NumberFormatException exception) {
                    Piece piece = BoardUtils.getPieceByChar(chessPieceChar, (byte) x);
                    pieceSet.put((byte) x, piece);
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
                pieceSet, whiteKing, blackKing, FieldNameConverter.fromFieldName(fenData[3]),
                new boolean[][]{new boolean[]{fenData[2].contains("K"), fenData[2].contains("Q")}, new boolean[]{fenData[2].contains("k"), fenData[2].contains("q")}});
    }


    private final Player currentPlayer;
    private final Map<Byte, Piece> pieceSet;
    private final boolean[][] castlingRights; // white?: [king-sided?, queen-sided?]
    private final byte whiteKing;
    private final byte blackKing;
    private final byte enPassant;
    private final byte simulationDepth;
    private Boolean kingNotInCheck;

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieces      {@link Piece} - the piece list
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param enPassant      Byte - a possible en passant position
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Map<Byte, Piece> pieces,  byte whiteKing, byte blackKing, byte enPassant, boolean[][] castleRights) {
        this (currentPlayer, pieces, whiteKing, blackKing, enPassant, castleRights, (byte) 0);
    }

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieces      {@link Piece} - the piece list
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param enPassant      Byte - a possible en passant position
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @param simulationDepth int - the simulated depth (until 2)
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Map<Byte, Piece> pieces, Byte whiteKing, Byte blackKing, byte enPassant, boolean[][] castleRights, byte simulationDepth) {
        this.currentPlayer = currentPlayer;
        this.pieceSet = pieces;
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
        if (!pieceSet.containsKey((byte) fieldNumber))
            return null;
        return pieceSet.get((byte) fieldNumber);
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
     * @return List<Move> - all moves the current player can do.
     */
    public Map<Piece, Set<Byte>> getAllMovePositions() {
        Map<Piece, Set<Byte>> allPieceMoves = new HashMap<>();
        for (Piece piece : pieceSet.values()) {
            if (piece != null && piece.getBelong() == currentPlayer) {
                Set<Byte> pieceMoves = piece.getMoves(this);
                if(!pieceMoves.isEmpty()) allPieceMoves.put(piece, pieceMoves);
            }
        }
        return allPieceMoves;
    }

    /**
     * Makes a move and returns a new board.
     * @param move      {@link Move} - the move to make.
     * @return Board - the new board.
     */
    public Board makeMove(Move move) {
        return makeMove(move, simulationDepth);
    }

    /**
     * Makes a move and returns a new board.
     * @param move      {@link Move} - the move to make.
     * @return Board - the new board.
     */
    private Board makeMove(Move move, byte depth) {
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



        Map<Byte, Piece> newPieceSet = clonePieceSet();
        Piece movingPiece = newPieceSet.get(move.from());
        newPieceSet.remove(move.from());
        newPieceSet.put(move.to(), movingPiece);
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

        return new Board(currentPlayer.nextPlayer(), newPieceSet, newWhiteKing, newBlackKing, newEnPassant, newCastleRights, depth);
    }

    /**
     * Clones all pieces.
     *
     * @return {@link Piece}[] - copied piece set
     */
    private Map<Byte, Piece> clonePieceSet() {
        Map<Byte, Piece> copied = new HashMap<>();
        for (Map.Entry<Byte, Piece> entry : pieceSet.entrySet()) {
            copied.put(entry.getKey(), entry.getValue().clone());
        }
        return copied;
    }


    /**
     * Checks if the given position is an enemy piece to the given player.
     * @param player   Player - friendly player
     * @param position byte - the position to check
     * @return boolean - position is not null and an enemy piece
     */
    public boolean isEnemyPiece(Player player, byte position) {
        Piece piece = getPieceOnBoard(position);
        return piece != null && piece.isNotSamePlayer(player);
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
    public boolean kingIsNotChecked(Player player) {
        if (this.simulationDepth > 1)
            return true;
        if (this.kingNotInCheck == null) {
            byte kingPosition = player == Player.WHITE ? whiteKing : blackKing;
            this.kingNotInCheck = noneAttacks(player.nextPlayer(), kingPosition);
        }
        return this.kingNotInCheck;
    }

    /**
     * Check if the king is not checked after the given move.
     * @param move Move - the move to do first.
     * @return boolean - king will not be checked
     * @since 1.1-SNAPSHOT
     */
    public boolean kingIsNotInCheckAfterMove(Move move) {
        return this.simulationDepth > 0 || makeMove(move, (byte) (simulationDepth + 1)).kingIsNotChecked(currentPlayer);
    }

    /**
     * Checks if the position is attacked by the given player.
     * @param player Player - the attacker.
     * @param attackingPosition byte - the attacking position
     * @return boolean - is attacked.
     */
    public boolean noneAttacks(Player player, byte attackingPosition) {
        Board newBoard = new Board(currentPlayer, pieceSet, whiteKing, blackKing, enPassant, castlingRights, (byte) (this.simulationDepth + 1));
        for (Piece piece : pieceSet.values()) {
            if (piece != null && piece.getBelong() == player && piece.getMoves(newBoard).contains(attackingPosition))
                return false;
        }
        return true;
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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
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
}
