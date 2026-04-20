public class Field {

    private PieceType pieceType;

    public enum PieceType {
        HUNTER, HUNTED
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

    public boolean isEmpty() {
        return pieceType == null;
    }

}
