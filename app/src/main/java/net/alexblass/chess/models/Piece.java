package net.alexblass.chess.models;

import static net.alexblass.chess.models.GameBoard.BOARD_LENGTH;

/**
 * A class to store data related to game pieces.
 */

public class Piece {

    // Piece names
    // Public so other classes can reference against a given Piece's name
    public static final String QUEEN = "queen";
    public static final String KING = "king";
    public static final String ROOK = "rook";
    public static final String BISHOP = "bishop";
    public static final String KNIGHT = "knight";
    public static final String PAWN = "pawn";

    // Color codes for each player's pieces
    // Public so other classes can reference against a Piece's color code
    public static final int WHITE = 0; // White goes first in the game
    public static final int BLACK = 1;

    // The name of the given piece
    private String mName;

    // Keeps track of the color of this individual piece
    private int mColorCode;

    // Points value of the piece
    private int mPointsValue;

    // The image file for the piece
    private int mImageResourceId;

    // The x and y row and column values for a Piece's location
    private int mRowX;
    private int mColY;

    // The Piece's position in a singular list 1-64
    private int mListPostion;

    // Whether or not the piece has been moved from it's original starting location
    // This is important for pawns, which can move 2 spaces on their first move
    // and for rooks and kings, which can use a special move called "castling" only when
    // they have not yet moved
    private boolean mHasMovedFromStart;

    // Is the piece still on the board
    // Should be false when pieces are captured, otherwise true
    private boolean mIsActive;

    public Piece(String name, int colorCode, int x, int y, int imageResourceId){
        // Information about the Piece type
        this.mName = name;
        this.mColorCode = colorCode;
        this.mImageResourceId = imageResourceId;

        // Information about the Piece's playability
        mHasMovedFromStart = false;
        mIsActive = true;

        // The Piece's coordinates on a 64 x 64 grid
        mRowX = x;
        mColY = y;

        mListPostion = (x * BOARD_LENGTH) + y;

        // The Piece's points value
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

    // Used for when a pawn reaches the other player's home row and can be changed to another piece
    public void setName(String name){
        this.mName = name;

        // The Piece's points value
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

    public int getImageResourceId() {
        return mImageResourceId;
    }

    // Used for when a pawn reaches the other player's home row and can be changed to another piece
    public void setImageResourceId(int imageResourceId) {
        this.mImageResourceId = imageResourceId;
    }

    public void setCoordinates(int x, int y){
        this.mRowX = x;
        this.mColY = y;

        if (x == -1 || y == -1){
            mListPostion = -1;
        } else {
            mListPostion = (x * BOARD_LENGTH) + y;
        }
    }

    public int getRowX(){
        return mRowX;
    }

    public int getColY(){
        return mColY;
    }

    public int getListPosition(){
        return mListPostion;
    }
}
