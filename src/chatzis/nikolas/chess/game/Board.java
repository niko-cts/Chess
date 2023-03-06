package chatzis.nikolas.chess.game;

import chatzis.nikolas.chess.move.Move;
import chatzis.nikolas.chess.move.SpecialMove;
import chatzis.nikolas.chess.pieces.King;
import chatzis.nikolas.chess.pieces.Piece;
import chatzis.nikolas.chess.pieces.Rook;
import chatzis.nikolas.chess.utils.BoardUtils;

import java.util.*;

/**
 * Board class to store every piece.
 *
 * @author Nikolas Chatzis
 * @since 1.0-Snapshot
 */
public class Board {

    private final Player currentPlayer;
    private final Piece[] pieceSet;
    private final Map<Player, Set<Byte>> boardPieces;
    private final boolean[][] castlingRights; // white?: [king-sided?, queen-sided?]
    private final Map<Player, List<Move>> possibleMoves;
    private final byte whiteKing;
    private final byte blackKing;
    private final Move lastMove;
    private Boolean kingInCheck;
    private final boolean simulated;

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieceSet      {@link Piece}[] - a 8*8 piece array (nullable)
     * @param boardPieces   Map<Player, Set<Byte>> - every piece position of both players for quick player pieces access
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param lastMove      {@link Move} - the last move made
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Piece[] pieceSet, Map<Player, Set<Byte>> boardPieces, byte whiteKing, byte blackKing, Move lastMove, boolean[][] castleRights) {
        this (currentPlayer, pieceSet, boardPieces, whiteKing, blackKing, lastMove, castleRights, false);
    }

    /**
     * Instantiates the class
     *
     * @param currentPlayer Player - the current player (white, black)
     * @param pieceSet      {@link Piece}[] - a 8*8 piece array (nullable)
     * @param boardPieces   Map<Player, Set<Byte>> - every piece position of both players for quick player pieces access
     * @param whiteKing     {@link King} - the white king piece
     * @param blackKing     {@link King} - the black king piece
     * @param lastMove      {@link Move} - the last move made
     * @param castleRights boolean[][] - castle rights like this: [[W-king-sided?, W-queen-sided?], [B-king-sided?, B-queen-sided?]]
     * @param simulate Board - this board is simulated
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Piece[] pieceSet, Map<Player, Set<Byte>> boardPieces, Byte whiteKing, Byte blackKing, Move lastMove, boolean[][] castleRights, boolean simulate) {
        this.currentPlayer = currentPlayer;
        this.pieceSet = pieceSet;
        this.boardPieces = boardPieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.lastMove = lastMove;
        this.castlingRights = castleRights;
        this.possibleMoves = new EnumMap<>(Player.class);
        this.simulated = simulate;
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
        if (simulated)
            return new ArrayList<>();
        if (possibleMoves.containsKey(player))
            return possibleMoves.get(player);
        List<Move> moves = new ArrayList<>();
        Board newBoard = new Board(currentPlayer.nextPlayer(), pieceSet, boardPieces, whiteKing, blackKing, lastMove, castlingRights, true);
        for (Byte piecePosition : this.boardPieces.get(player))
            moves.addAll(pieceSet[piecePosition].getMoves(newBoard));
        possibleMoves.put(player, moves);
        return moves;
    }

    /**
     * Makes a move and returns a new board.
     * Could be null, if the king would be in check with the given move.
     *
     * @param move      {@link Move} - the move to make.
     * @return Board - the new board.
     */
    public Board makeMove(Move move) {
        return makeMove(move, simulated);
    }

    /**
     * Makes a move and returns a new board.
     * Could be null, if the king would be in check with the given move.
     *
     * @param move      {@link Move} - the move to make.
     * @param simulateBoard boolean - simulate the next board.
     * @return Board - the new board.
     */
    public Board makeMove(Move move, boolean simulateBoard) {
        byte newWhiteKing = whiteKing;
        byte newBlackKing = blackKing;

        // king position update
        if (move.from() == whiteKing || move.from() == blackKing) {
            if (move.from() == whiteKing)
                newWhiteKing = move.to();
            else if (move.from() == blackKing)
                newBlackKing = move.to();
        }

        Piece[] newPieceSet = clonePieceSet();
        Map<Player, Set<Byte>> newBoardPieces = clonePlayerPiecePositions();

        if (newPieceSet[move.to()] != null) {
            Set<Byte> enemyPieces = newBoardPieces.get(currentPlayer.nextPlayer());
            enemyPieces.remove(move.to());
        }

        Set<Byte> playerPiecePositions = newBoardPieces.get(currentPlayer);
        playerPiecePositions.remove(move.from());
        playerPiecePositions.add(move.to());

        Piece movingPiece = newPieceSet[move.from()];
        movingPiece.setCurrentPosition(move.to());
        newPieceSet[move.from()] = null;
        newPieceSet[move.to()] = movingPiece;

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

        return new Board(currentPlayer.nextPlayer(), newPieceSet, newBoardPieces, newWhiteKing, newBlackKing, move, newCastleRights, simulateBoard);
    }

    /**
     * Clones all player pieces.
     *
     * @return Map<Player, Set < Piece>> - new player piece set
     */
    private Map<Player, Set<Byte>> clonePlayerPiecePositions() {
        Map<Player, Set<Byte>> pieceMap = new EnumMap<>(Player.class);
        for (Player player : new Player[]{Player.WHITE, Player.BLACK})
            pieceMap.put(player, new HashSet<>(this.boardPieces.get(player)));
        return pieceMap;
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
     * Creates a default chess board.
     *
     * @return Board - default board.
     */
    public static Board createNewBoard() {
        return createNewBoard("RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr w KQkq - 0 1");
    }

    /**
     * Creates a chess board with the given board data.
     *
     * @param fen String - the Forsyth-Edwards Notation
     * @return Board - default board.
     */
    public static Board createNewBoard(String fen) {
        String[] fenData = fen.split(" ");

        String[] board = fenData[0].split("/");

        Piece[] pieces = new Piece[64];
        int x = 0;
        for (String s : board) {
            for (char chessPieceChar : s.toCharArray()) {
                try {
                    x += Integer.parseInt(chessPieceChar + ""); // Is number
                } catch (NumberFormatException exception) {
                    pieces[x] = BoardUtils.getPieceByChar(chessPieceChar, (byte) x);
                    x++;
                }
            }
        }

        Map<Player, Set<Byte>> map = new EnumMap<>(Player.class);
        byte whiteKing = 0;
        byte blackKing = 0;

        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] != null) {
                Piece piece = pieces[i];
                Set<Byte> playerPieces = map.getOrDefault(piece.getBelong(), new HashSet<>());
                playerPieces.add((byte) i);
                map.put(piece.getBelong(), playerPieces);

                if (piece instanceof King) {
                    if (piece.getBelong().equals(Player.WHITE)) {
                        whiteKing = (byte) i;
                    } else {
                        blackKing = (byte) i;
                    }
                }
            }
        }

        return new Board(fenData[1].equals("w") ? Player.WHITE : Player.BLACK,
                pieces, map, whiteKing, blackKing, null,
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
                .append(currentPlayer).append(" - ")
                .append(castlingRights[0][0] ? "K" : "")
                .append(castlingRights[0][1] ? "Q" : "")
                .append(castlingRights[1][0] ? "k" : "")
                .append(castlingRights[1][1] ? "q" : "")
                .append(!castlingRights[0][0] && !castlingRights[0][1] && !castlingRights[1][0] && !castlingRights[1][1] ? "-" : "")
                .append("\n\n").toString();
    }

    /**
     * Get the last move
     * @return Move - the last move.
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Check if the king is NOT currently checked.
     * @return boolean - king not checked
     * @since 1.1-SNAPSHOT
     */
    public boolean kingIsNotChecked() {
        if (kingInCheck == null) {
            byte kingPosition = currentPlayer == Player.WHITE ? whiteKing : blackKing;
            this.kingInCheck = getAllMoves(currentPlayer.nextPlayer()).stream().anyMatch(m -> m.to() == kingPosition);
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
        return makeMove(move, true).kingIsNotChecked();
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
}
