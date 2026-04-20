import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BoardGUI {

    private static final Color COLOR_FUGITIVE = new Color(50, 180, 80);
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

        button.setBackground(getButtonBackground(x, y, field));
        button.setText(getButtonText(x, y, field));

    }

    private void refreshAll() {
        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                refresh(i, j);
            }
        }
    }

    private Color getButtonBackground(int x, int y, Field field) {

        if (selectedX == x && selectedY == y) {
            return COLOR_SELECTED;
        } else if (isHint[x][y]) {
            return COLOR_HINT;
        } else if (field.getPieceType() == Field.PieceType.FUGITIVE) {
            return COLOR_FUGITIVE;
        } else if (field.getPieceType() == Field.PieceType.HUNTER) {
            return COLOR_HUNTER;
        } else {
            return null;
        }

    }

    private String getButtonText(int x, int y, Field field) {
        if (field.getPieceType() == Field.PieceType.FUGITIVE) {
            return "F";
        } else if (field.getPieceType() == Field.PieceType.HUNTER) {
            return "H";
        } else {
            return isHint[x][y] ? "+" : "";
        }
    }

    private void updateStatus() {
        String turn = (board.getCurrentTurn() == Board.Turn.FUGITIVE) ? "Fugitive (F)" : "Hunter (H)";
        int remaining = board.getMaxMoves() - board.getMoveCount();
        statusLabel.setText("Turn: " + turn + "   |   Moves used: " + board.getMoveCount() + " / " + board.getMaxMoves() + "   |   Remaining: " + remaining);
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

            if (selectedX != -1 && isHint[x][y]) {
                moveSelectedPiece(x, y);
                return;
            }

            clearSelection();
            selectPiece(x, y);

        }
    }

    private boolean isCurrentPlayerPiece(Field field) {

        Field.PieceType pieceType = field.getPieceType();
        return (board.getCurrentTurn() == Board.Turn.FUGITIVE && pieceType == Field.PieceType.FUGITIVE) ||
                (board.getCurrentTurn() == Board.Turn.HUNTER && pieceType == Field.PieceType.HUNTER);

    }

    private void gameEnd() {

        Board.GameState state = board.getGameState();
        String title, message;

        if (state == Board.GameState.HUNTER_WIN) {
            title = "Hunter Wins!";
            message = "The Hunter surrounded the Fugitive!";
        } else {
            title = "Fugitive Wins!";
            message = "The Fugitive survived all " + board.getMaxMoves() + " moves!";
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

    private void moveSelectedPiece(int toX, int toY) {

        int fromX = selectedX;
        int fromY = selectedY;

        clearSelection();
        board.movePiece(fromX, fromY, toX, toY);
        refresh(fromX, fromY);
        refresh(toX, toY);
        updateStatus();

        if (board.isOver()) {
            gameEnd();
        }

    }

    private void selectPiece(int x, int y) {

        Field field = board.get(x, y);
        List<Point> moves = board.getValidMoves(x, y);

        if (!isCurrentPlayerPiece(field) || moves.isEmpty()) {
            return;
        }

        selectedX = x;
        selectedY = y;

        for (Point p : moves) {
            isHint[p.x][p.y] = true;
            refresh(p.x, p.y);
        }

        refresh(x, y);

    }

}
