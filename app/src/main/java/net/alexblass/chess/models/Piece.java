package net.alexblass.chess.models;

/**
 * A class to store data related to game pieces.
 */

public class Piece {

    // Color codes for each player's pieces
    public static final int WHITE = 0; // White goes first in the game
    public static final int BLACK = 1;

    // Public variables to access the index for the correct column in our array
    public static final int X_INDEX = 0;
    public static final int Y_INDEX = 1;

    // Piece names
    public static final String QUEEN = "queen";
    public static final String KING = "king";
    public static final String ROOK = "rook";
    public static final String BISHOP = "bishop";
    public static final String KNIGHT = "knight";
    public static final String PAWN = "pawn";

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

    // The image file for the piece
    private int mImageResourceId;

    public Piece(String name, int colorCode, int[] coordinates, int imageResourceId){
        this.mName = name;
        mHasMovedFromStart = false;
        mIsActive = true;
        this.mColorCode = colorCode;
        this.mCoordinates = coordinates;
        this.mImageResourceId = imageResourceId;

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

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.mImageResourceId = imageResourceId;
    }
}
