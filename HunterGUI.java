import javax.swing.*;
import java.awt.*;

public class HunterGUI {

    private final JFrame frame;
    private BoardGUI boardGUI;

    private static final int INITIAL_BOARD_SIZE = 5;
    private int currentBoardSize = INITIAL_BOARD_SIZE;

    public HunterGUI(){

        frame = new JFrame("Hunter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMenuStrip();
        initialiseBoard(currentBoardSize);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void startNewGame(int boardSize) {

        currentBoardSize = boardSize;

        frame.getContentPane().remove(boardGUI.getBoardPanel());
        frame.getContentPane().remove(boardGUI.getStatusLabel());
        boardGUI = new BoardGUI(boardSize);
        boardGUI.setOnGameEnd(() -> startNewGame(currentBoardSize));
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.CENTER);
        frame.getContentPane().add(boardGUI.getStatusLabel(), BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
        frame.pack();

    }

    private void initialiseBoard(int boardSize) {

        currentBoardSize = boardSize;

        if(boardGUI != null) {
            frame.getContentPane().remove(boardGUI.getBoardPanel());
            frame.getContentPane().remove(boardGUI.getStatusLabel());
        }

        boardGUI = new BoardGUI(boardSize);
        boardGUI.setOnGameEnd(() -> startNewGame(currentBoardSize));
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.CENTER);
        frame.getContentPane().add(boardGUI.getStatusLabel(), BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
        frame.pack();

    }

    private void createMenuStrip() {

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenu newMenu = new JMenu("New");
        gameMenu.add(newMenu);
        int[] boardSizes = new int[]{3, 5, 7};

        for (int boardSize : boardSizes) {

            JMenuItem sizeMenuItem = new JMenuItem(boardSize + "x" + boardSize);
            newMenu.add(sizeMenuItem);
            sizeMenuItem.addActionListener(e -> startNewGame(boardSize));

        }

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitMenuItem);

    }

}
