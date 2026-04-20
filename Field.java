public class Field {

    private PieceType pieceType;

    public enum PieceType {
        HUNTER, FUGITIVE
    }

    public Field() {
        pieceType = null;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType){
        this.pieceType = pieceType;
    }

    public void clear() {
        pieceType = null;
    }

    public boolean isEmpty() {
        return pieceType == null;
    }

}
