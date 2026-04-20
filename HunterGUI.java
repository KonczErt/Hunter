import javax.swing.*;
import java.awt.*;

public class HunterGUI {

    private final JFrame frame;
    private BoardGUI boardGUI;

    private final int INITIAL_BOARD_SIZE = 5;
    private final int currentBoardSize = INITIAL_BOARD_SIZE;

    public HunterGUI(){

        frame = new JFrame("Hunter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardGUI = new BoardGUI(INITIAL_BOARD_SIZE);
        boardGUI.setOnGameEnd(() -> startNewGame(currentBoardSize));
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.CENTER);
        frame.getContentPane().add(boardGUI.getStatusLabel(), BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenu newMenu = new JMenu("New");
        gameMenu.add(newMenu);
        int[] boardSizes = new int[]{3, 5, 7};

        for (int boardSize : boardSizes) {

            JMenuItem sizeMenuItem = new JMenuItem(boardSize + "x" + " (max" + (4 * boardSize) + " moves)");
            newMenu.add(sizeMenuItem);
            sizeMenuItem.addActionListener(e -> startNewGame(boardSize));

        }

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(e -> System.exit(0));

        frame.pack();
        frame.setVisible(true);

    }

    private void startNewGame(int boardSize) {

        frame.getContentPane().remove(boardGUI.getBoardPanel());
        frame.getContentPane().remove(boardGUI.getStatusLabel());
        boardGUI = new BoardGUI(boardSize);
        boardGUI.setOnGameEnd(() -> startNewGame(currentBoardSize));
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.CENTER);
        frame.getContentPane().add(boardGUI.getStatusLabel(), BorderLayout.SOUTH);
        frame.pack();

    }

}
