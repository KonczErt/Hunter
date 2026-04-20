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
    private int moveCnt;
    private Turn crntTurn;

    public Board(int boardSize){

        this.boardSize = boardSize;
        this.maxMoves = 4 * boardSize;
        this.moveCnt = 0;
        this.crntTurn = Turn.HUNTED;

        board = new Field[this.boardSize][this.boardSize];
        for( int i = 0; i < boardSize; i++ ){
            for( int j = 0; j < boardSize; j++ ){
                board[i][j] = new Field();
            }
        }

        int center = boardSize / 2;
        board[center][center].setPieceType(Field.PieceType.HUNTED);
        board[0][0].setPieceType(Field.PieceType.HUNTER);
        board[0][boardSize - 1].setPieceType(Field.PieceType.HUNTER);
        board[boardSize - 1][0].setPieceType(Field.PieceType.HUNTER);
        board[boardSize - 1][boardSize - 1].setPieceType(Field.PieceType.HUNTER);

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

    public int getMoveCnt() {

        return moveCnt;

    }

    public int getMaxMoves() {

        return maxMoves;

    }

    public Turn getCrntTurn(){

        return crntTurn;

    }

    public List<Point> getValidMoves(int x, int y) {

        List<Point> moves = new ArrayList<>();
        for (int[] d : DIRECTIONS ) {

            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < boardSize && ny >= 0 && ny < boardSize && board[nx][ny].isEmpty()){

                moves.add(new Point(nx, ny));

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

        if (moveCnt >= maxMoves) {
            return GameState.HUNTED_WIN;
        }
        return GameState.PLAYING;

    }

    public void movePiece(int fromX, int fromY, int toX, int toY){

        board[toX][toY].setPieceType(board[fromX][fromY].getPieceType());
        board[fromX][fromY].setPieceType(null);
        ++moveCnt;
        crntTurn = (crntTurn == Turn.HUNTED) ? Turn.HUNTER : Turn.HUNTED;

    }

}