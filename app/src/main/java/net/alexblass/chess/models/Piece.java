package net.alexblass.chess.models;

/**
 * A class to store data related to game pieces.
 */

public class Piece {

    // Color codes for each player's pieces
    private final int WHITE = 0; // White goes first in the game
    private final int BLACK = 1;

    // The name of the piece
    private String mName;

    // Whether or not the piece has been moved from it's original starting location
    // This is important for pawns, which can move 2 spaces on their first move
    // and for rooks and kings, which can use a special move called "castling" only when
    // they have not yet moved
    private boolean mHasMovedFromStart;

    // Is the piece still on the board
    private boolean mIsActive;

    // Points value of the piece
    private int mPointsValue;

    // Keeps track of the color of this individual piece
    private int mColorCode;

    // Keep track of where on the board the piece is
    private int[] mCoordinates;

    public Piece(String name, int colorCode, int[] coordinates){
        this.mName = name;
        mHasMovedFromStart = false;
        mIsActive = true;
        this.mColorCode = colorCode;
        this.mCoordinates = coordinates;

        switch(name){
            case "pawn":
                mPointsValue = 1;
                break;
            case "knight":
            case "bishop":
                mPointsValue = 3;
                break;
            case "rook":
                mPointsValue = 5;
                break;
            case "queen":
                mPointsValue = 9;
                break;
            default:
                mPointsValue = 0;
        }
    }

    public String getName() {
        return mName;
    }

    public boolean hasMovedFromStart() {
        return mHasMovedFromStart;
    }

    public void setHasMovedFromStart(boolean hasMovedFromStart) {
        this.mHasMovedFromStart = hasMovedFromStart;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setIsActive(boolean isActive) {
        this.mIsActive = isActive;
    }

    public int getPointsValue() {
        return mPointsValue;
    }

    public int getColorCode() {
        return mColorCode;
    }

    public int[] getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.mCoordinates = coordinates;
    }
}
