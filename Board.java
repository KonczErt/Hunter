import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public enum Turn { HUNTER, HUNTED }
    public enum GameState { PLAYING, HUNTER_WIN, HUNTED_WIN }

    private static final int[][] DIRECTIONS = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    private final Field[][] board;
    private final int boardSize;
    private final int maxMoves;
    private int moveCount;
    private Turn currentTurn;

    public Board(int boardSize){

        this.boardSize = boardSize;
        this.maxMoves = 4 * boardSize;
        this.moveCount = 0;
        this.currentTurn = Turn.HUNTED;

        board = new Field[this.boardSize][this.boardSize];
        initializeBoard();
        placePieces();

    }

    public boolean isOver() {

        return getGameState() != GameState.PLAYING;

    }

    public Field get(int x, int y){

        return board[x][y];

    }

    public int getBoardSize(){

        return boardSize;

    }

    public int getMoveCount() {

        return moveCount;

    }

    public int getMaxMoves() {

        return maxMoves;

    }

    public Turn getCurrentTurn(){

        return currentTurn;

    }

    public List<Point> getValidMoves(int x, int y) {

        List<Point> moves = new ArrayList<>();
        for (int[] direction : DIRECTIONS ) {

            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isInsideBoard(newX, newY) && board[newX][newY].isEmpty()){

                moves.add(new Point(newX, newY));

            }

        }
        return moves;

    }

    public GameState getGameState() {

        for ( int i = 0; i < boardSize; i++ ){
            for ( int j = 0; j < boardSize; j++){
                if( board[i][j].getPieceType() == Field.PieceType.HUNTED ){
                    if ( getValidMoves(i, j).isEmpty()) {
                        return GameState.HUNTER_WIN;
                    }
                }

            }

        }

        if (moveCount >= maxMoves) {
            return GameState.HUNTED_WIN;
        }
        return GameState.PLAYING;

    }

    public void movePiece(int fromX, int fromY, int toX, int toY){

        if (!getValidMoves(fromX, fromY).contains(new Point(toX, toY))) {
            throw new IllegalArgumentException("Invalid Move");
        }

        board[toX][toY].setPieceType(board[fromX][fromY].getPieceType());
        board[fromX][fromY].clear();
        ++moveCount;
        currentTurn = (currentTurn == Turn.HUNTED) ? Turn.HUNTER : Turn.HUNTED;

    }

    private boolean isInsideBoard(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    private void initializeBoard() {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Field();
            }
        }

    }

    private void placePieces() {

        int center = boardSize / 2;
        board[center][center].setPieceType(Field.PieceType.HUNTED);
        board[0][0].setPieceType(Field.PieceType.HUNTER);
        board[0][boardSize - 1].setPieceType(Field.PieceType.HUNTER);
        board[boardSize - 1][0].setPieceType(Field.PieceType.HUNTER);
        board[boardSize - 1][boardSize - 1].setPieceType(Field.PieceType.HUNTER);

    }


}