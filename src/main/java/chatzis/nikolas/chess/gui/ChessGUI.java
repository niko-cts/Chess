package chatzis.nikolas.chess.gui;

import chatzis.nikolas.chess.game.Board;
import chatzis.nikolas.chess.pieces.King;
import chatzis.nikolas.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The GUI class to display the Jframe.
 * @author Nikolas C.
 * @since 1.1-SNAPSHOT
 */
public class ChessGUI extends JFrame {


    // gui
    private final JButton[] fields;
    private Map<String, ImageIcon> pieceIcons;

    // game related
    private Board currentBoard;
    private int selected;
    private Piece piece;
    private Set<Byte> moves;
    private boolean checkMate;

    /**
     * Instantiates the gui.
     * @param board Board - board to start with.
     * @since 1.1-SNAPSHOT
     */
    public ChessGUI(Board board) {
        loadPieceImgs();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(512, 512));
        setMaximumSize(getPreferredSize());
        setTitle("Chess");
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(getJMenu());
        setJMenuBar(jMenuBar);


        this.fields = new JButton[64];
        this.moves = new HashSet<>();
        JPanel panel = new JPanel(new GridLayout(8, 8));
        panel.setSize(new Dimension(512, 512));
        this.selected = -1;

        for (int i = 0; i < fields.length; i++) {
            JButton button = new JButton("");
            button.setOpaque(true);

            int finalI = i;
            button.addActionListener((e) -> fieldPressed(finalI));
            this.fields[i] = button;
            panel.add(button);
        }

        getContentPane().add(panel);

        loadBoard(board);
        setVisible(true);
        pack();
    }

    /**
     * Loads the new board into the jframe.
     * @param board Board - new board.
     * @since 1.1-SNAPSHOT
     */
    private void loadBoard(Board board) {
        this.currentBoard = board;
        this.moves.clear();
        this.selected = -1;
        this.piece = null;
        this.checkMate = currentBoard.getAllMovePositions().isEmpty();
        if (checkMate)
            System.out.println("Checkmate!");
        paintButtons();
    }

    /**
     * Returns the Menu item.
     * @return JMenu - the jmenu.
     * @since 1.1-SNAPSHOT
     */
    private JMenu getJMenu() {
        JMenu jMenu = new JMenu("Game");
        JMenuItem jMenuItem = new JMenuItem("Restart game");
        jMenuItem.addActionListener(e -> loadBoard(Board.createNewBoard()));
        jMenu.add(jMenuItem);
        return jMenu;
    }

    /**
     * Will be called, when a button was pressed.
     * @param i int - the pressed button
     * @since 1.1-SNAPSHOT
     */
    private void fieldPressed(int i) {
        if (checkMate)
            return;

        if (i == selected) {
            moves.clear();
            selected = -1;
            paintButtons();
            System.out.println("Unselected");
            return;
        }

        Piece clickingPiece = currentBoard.getPieceOnBoard(i);
        if (clickingPiece != null && clickingPiece.getBelong() == currentBoard.getCurrentPlayer()) {
            this.piece = clickingPiece;
            moves = clickingPiece.getMoves(currentBoard);
            this.selected = i;
            paintButtons();
            System.out.println("Selected: " + piece);
            return;
        }

        moves.stream().filter(m -> m == i).findFirst().ifPresent(m -> {
            System.out.println(currentBoard.getCurrentPlayer() + " Moved: " + piece);
            loadBoard(currentBoard.makeMove(piece.getMove(m)));
        });
    }

    /**
     * Paints the buttons.
     * @since 1.0-SNAPSHOT
     */
    private void paintButtons() {
        for (int i = 0; i < fields.length; i++) {
            Piece pieceOnBoard = currentBoard.getPieceOnBoard(i);
            JButton button = fields[i];
            if (pieceOnBoard != null)
                button.setIcon(pieceIcons.get(pieceOnBoard.getName() + ""));
            else
                button.setIcon(null);

            button.setBackground((i / 8 + i) % 2 == 0 ? Color.WHITE : new Color(125, 125, 125));

            final int pos = i;
            if (!checkMate) {
                if (selected == i) {
                    button.setBackground(Color.YELLOW);
                } else if (moves.stream().anyMatch(m -> m == pos)) {
                    button.setBackground(currentBoard.getPieceOnBoard(i) == null ? Color.GREEN : Color.ORANGE);
                }
            } else if (pieceOnBoard instanceof King && pieceOnBoard.getBelong() == currentBoard.getCurrentPlayer()) {
                button.setBackground(Color.RED);
            }
        }
        validate();
    }

    /**
     * Loads the piece images and puts it into the hashmap.
     * @since 1.1-SNAPSHOT
     */
    private void loadPieceImgs() {
        this.pieceIcons = new HashMap<>();

        for (String player : new String[]{"w", "b"}) {
            for (String name : new String[]{"b", "k", "n", "p", "q", "r"}) {
                String finalName = player.equals("w") ? name.toUpperCase() : name;
                this.pieceIcons.put(finalName, new ImageIcon("src/main/resources/pieces/" + player + name + ".png"));
            }
        }
    }

}
