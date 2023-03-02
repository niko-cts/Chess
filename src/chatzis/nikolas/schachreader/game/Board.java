package chatzis.nikolas.schachreader.game;

import chatzis.nikolas.schachreader.move.Move;
import chatzis.nikolas.schachreader.move.SpecialMove;
import chatzis.nikolas.schachreader.pieces.*;
import chatzis.nikolas.schachreader.utils.BoardUtils;

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
    private final byte whiteKing;
    private final byte blackKing;
    private final Move lastMove;
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
     * @param simulated     boolean - board is only simulated
     * @since 1.0-Snapshot
     */
    private Board(Player currentPlayer, Piece[] pieceSet, Map<Player, Set<Byte>> boardPieces, Byte whiteKing, Byte blackKing, Move lastMove, boolean simulated) {
        this.currentPlayer = currentPlayer;
        this.pieceSet = pieceSet;
        this.boardPieces = boardPieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.lastMove = lastMove;
        this.simulated = simulated;
    }

    /**
     * Returns the position of the king.
     *
     * @param player Player - the player.
     * @return int - the king pos
     */
    public int getKingPosition(Player player) {
        return player.equals(Player.WHITE) ? whiteKing : blackKing;
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
        return BoardUtils.staysOnBoard(from, to) && pieceSet[to] == null;
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
        List<Move> moves = new ArrayList<>();
        for (Byte piecePosition : this.boardPieces.get(player)) {
            moves.addAll(pieceSet[piecePosition].getPossibleMoves(this).getMoves());
        }
        return moves;
    }

    /**
     * Makes a move and returns a new board.
     *
     * @param move {@link Move} - the move to make.
     * @return Board - the new board.
     */
    public Board makeMove(Move move) {
        return makeMove(move, simulated);
    }

    /**
     * Makes a move and returns a new board.
     *
     * @param move      {@link Move} - the move to make.
     * @param simulated boolean - if board is only simulated.
     * @return Board - the new board.
     */
    public Board makeMove(Move move, boolean simulated) {
        Piece[] newPieceSet = clonePieceSet();
        Map<Player, Set<Byte>> newBoardPieces = clonePlayerPiecePositions();
        byte newWhiteKing = whiteKing;
        byte newBlackKing = blackKing;

        if (move != null) {
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

            if (move instanceof SpecialMove specialMove) {
                specialMove.getMove().moved(newPieceSet, movingPiece);
            }

            if (move.from() == whiteKing)
                newWhiteKing = move.to();
            else if (move.from() == blackKing)
                newBlackKing = move.to();
        }

        return new Board(currentPlayer.nextPlayer(), newPieceSet, newBoardPieces, newWhiteKing, newBlackKing, move, simulated);
    }

    /**
     * Clones all player pieces.
     *
     * @return Map<Player, Set < Piece>> - new player piece set
     */
    private Map<Player, Set<Byte>> clonePlayerPiecePositions() {
        Map<Player, Set<Byte>> pieceMap = new HashMap<>();
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
            copied[i] = pieceSet[i] != null ? pieceSet[i].copy() : null;
        }
        return copied;
    }

    /**
     * Creates a default chess board.
     *
     * @return Board - default board.
     */
    public static Board createNewBoard() {
        return createNewBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Creates a chess board with the given board data.
     *
     * @param boardData String - the board data like "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
     * @return Board - default board.
     */
    public static Board createNewBoard(String boardData) {
        String[] boards = boardData.split(" ");

        String[] board = boards[0].split("/");

        Piece[] pieces = new Piece[64];
        int x = 63;
        for (int i = 0; i < board.length; i++) {
            char[] row = board[i].toCharArray();
            for (char chessPieceChar : row) {
                try {
                    x -= Integer.parseInt(chessPieceChar + ""); // Is number
                } catch (NumberFormatException exception) {
                    pieces[x] = BoardUtils.getPieceByChar(chessPieceChar, (byte) x);
                    x--;
                }
            }
        }

        Player currentPlayer = boards[1].equals("w") ? Player.WHITE : Player.BLACK;

        Map<Player, Set<Byte>> map = new HashMap<>();
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

        return new Board(currentPlayer, pieces, map, whiteKing, blackKing, null, false);
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
        return piece != null && piece.isntSamePlayer(player);
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

        return builder.append("\nA B C D E F G H\n").append(currentPlayer).append("\n\n").toString();
    }

    public Move getLastMove() {
        return lastMove;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Check if this board is simulated.
     * Simulation is used to decide, if a king is in check e.g. and needs only all moves from the players.
     * This is necessary to prevent an endless repetition.
     *
     * @return boolean - board is simulated
     */
    public boolean isSimulated() {
        return simulated;
    }

}
