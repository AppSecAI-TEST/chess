package net.alexblass.chess.models;

/**
 * Created by ajbla_000 on 7/24/2017.
 */

public class PawnPiece extends Piece {

    // The En Passant move is invalid UNLESS:
    // (A) The pawn to be captured has moved 2 spaces from start
    // (B) Only IMMEDIATELY after the pawn moves 2 spaces forward
    // (C) The pawn to capture is directly left or right of the
    // pawn to be captured
    private boolean mValidEnPassant;

    PawnPiece(String name, int colorCode, int x, int y, int imageResourceId){
        super(name, colorCode, x, y, imageResourceId);
        mValidEnPassant = false;
    }

    public void setValidEnPassant(boolean value) {
        this.mValidEnPassant = value;
    }

    public boolean getValidEnPassant(){
        return mValidEnPassant;
    }
}
