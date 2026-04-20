import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BoardGUI {

    private static final Color COLOR_HUNTED = new Color(50, 180, 80);
    private static final Color COLOR_HUNTER = new Color(200, 55, 55);
    private static final Color COLOR_SELECTED = new Color(131, 146, 220);
    private static final Color COLOR_HINT = new Color(180, 230, 180);

    private final JButton[][] buttons;
    private final Board board;
    private final JPanel boardPanel;
    private final JLabel statusLabel;

    private int selectedX = -1;
    private int selectedY = -1;
    private final boolean[][] isHint;

    private Runnable onGameEnd;

    BoardGUI(int boardSize) {

        board = new Board(boardSize);
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];
        isHint = new boolean[board.getBoardSize()][board.getBoardSize()];
        for ( int i = 0; i < board.getBoardSize(); i++ ){
            for ( int j = 0; j < board.getBoardSize(); j++ ){
                JButton button = new JButton();
                button.addActionListener(new ButtonListener(i, j));
                button.setPreferredSize(new Dimension(60, 50));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        refreshAll();
        updateStatus();

    }

    public void refresh(int x, int y) {

        JButton button = buttons[x][y];
        Field field = board.get(x, y);

        if (selectedX == x && selectedY == y) {
            button.setBackground(COLOR_SELECTED);
        } else if (isHint[x][y]) {
            button.setBackground(COLOR_HINT);
        } else if (field.getPieceType() == Field.PieceType.HUNTED) {
            button.setBackground(COLOR_HUNTED);
        } else if (field.getPieceType() == Field.PieceType.HUNTER) {
            button.setBackground(COLOR_HUNTER);
        } else {
            button.setBackground(null);
        }

        if (field.getPieceType() == Field.PieceType.HUNTED) {
            button.setText("F");
        } else if (field.getPieceType() == Field.PieceType.HUNTER) {
            button.setText("A");
        } else {
            button.setText(isHint[x][y] ? "·" : "");
        }

    }

    private void refreshAll() {
        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                refresh(i, j);
            }
        }
    }

    private void updateStatus() {
        String turn = (board.getCrntTurn() == Board.Turn.HUNTED) ? "Fugitive (F)" : "Attacker (A)";
        int remaining = board.getMaxMoves() - board.getMoveCnt();
        statusLabel.setText("Turn: " + turn + "   |   Moves used: " + board.getMoveCnt() + " / " + board.getMaxMoves() + "   |   Remaining: " + remaining);
    }

    private void clearSelection() {
        if (selectedX == -1) {
            return;
        }
        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                if (isHint[i][j]) {
                    isHint[i][j] = false;
                    refresh(i, j);
                }
            }
        }
        int ox = selectedX;
        int oy = selectedY;
        selectedX = -1;
        selectedY = -1;
        refresh(ox, oy);
    }

    public void setOnGameEnd(Runnable onGameEnd) {
        this.onGameEnd = onGameEnd;
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    class ButtonListener implements ActionListener {

        private final int x, y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.isOver()) {
                return;
            }

            // Move the selected piece to this hint cell
            if (selectedX != -1 && isHint[x][y]) {
                int fromX = selectedX;
                int fromY = selectedY;
                clearSelection();
                board.movePiece(fromX, fromY, x, y);
                refresh(fromX, fromY);
                refresh(x, y);
                updateStatus();

                if (board.isOver()) {
                    Board.GameState state = board.getGameState();
                    String title, message;
                    if (state == Board.GameState.HUNTER_WIN) {
                        title = "Attacker Wins!";
                        message = "The Attacker surrounded the Fugitive!\nA new game will start automatically.";
                    } else {
                        title = "Fugitive Wins!";
                        message = "The Fugitive survived all " + board.getMaxMoves() + " moves!\nA new game will start automatically.";
                    }
                    Timer delay = new Timer(150, ev -> {
                        ((Timer) ev.getSource()).stop();
                        JOptionPane.showMessageDialog(boardPanel, message, title, JOptionPane.PLAIN_MESSAGE);
                        if (onGameEnd != null) {
                            onGameEnd.run();
                        }
                    });
                    delay.setRepeats(false);
                    delay.start();
                }
                return;
            }

            // Clear any previous selection
            clearSelection();

            // Select a piece belonging to the current player
            Field field = board.get(x, y);
            Board.Turn turn = board.getCrntTurn();
            boolean isFugitiveTurn = (turn == Board.Turn.HUNTED && field.getPieceType() == Field.PieceType.HUNTED);
            boolean isAttackerTurn = (turn == Board.Turn.HUNTER && field.getPieceType() == Field.PieceType.HUNTER);

            if (isFugitiveTurn || isAttackerTurn) {

                List<Point> moves = board.getValidMoves(x, y);
                if (!moves.isEmpty()) {
                    selectedX = x;
                    selectedY = y;
                    for (Point p : moves) {
                        isHint[p.x][p.y] = true;
                        refresh(p.x, p.y);
                    }
                    refresh(x, y);
                }

            }
        }
    }

}
