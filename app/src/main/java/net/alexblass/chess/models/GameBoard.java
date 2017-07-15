package net.alexblass.chess.models;

/**
 * The GameBoard keeps track of the available spaces and what pieces are on what space.
 */

public class GameBoard {

    // Square board has 8 rows and 8 columns
    public static final int BOARD_LENGTH = 8;

    // Constant values to keep track of the availability of a tile
    public static final int EMPTY_TILE = 0;
    public static final int TAKEN_BY_WHITE = 1;
    public static final int TAKEN_BY_BLACK = 2;

    // Public variables to access the index for the correct column in our array
    public static final int X_INDEX = 0;
    public static final int Y_INDEX = 1;
    public static final int AVAILABILITY_INDEX = 2;

    // All game boards will have 64 squares so there's no need to get or set a customized array
    /* Board should look like:
                 0 1 2 3 4 5 6 7
                 _ _ _ _ _ _ _ _
              0 |_|_|_|_|_|_|_|_| // Black home row
              1 |_|_|_|_|_|_|_|_| // Black home row
              2 |_|_|_|_|_|_|_|_|
              3 |_|_|_|_|_|_|_|_|
              4 |_|_|_|_|_|_|_|_|
              5 |_|_|_|_|_|_|_|_|
              6 |_|_|_|_|_|_|_|_| // White home row
              7 |_|_|_|_|_|_|_|_| // White home row
         */
    // Coordinate board in double array
    // There are 64 rows for each tile space in the board
    // and each row contains 1 column that holds an int array with the x,y coordinates (0-7)
    private int[][] mGameBoardTiles = new int[BOARD_LENGTH * BOARD_LENGTH][];

    // Pieces for white and black players
    private Piece mQueenW, mKingW, mRookW1, mRookW2, mBishopW1, mBishopW2,
            mKnightW1, mKnightW2, mPawnW1, mPawnW2, mPawnW3, mPawnW4, mPawnW5, mPawnW6,
            mPawnW7, mPawnW8;
    private Piece mQueenB, mKingB, mRookB1, mRookB2, mBishopB1, mBishopB2,
            mKnightB1, mKnightB2, mPawnB1, mPawnB2, mPawnB3, mPawnB4, mPawnB5, mPawnB6,
            mPawnB7, mPawnB8;

    // Create a new gameboard for a new game of chess
    public GameBoard(){
        // Initialize the chess pieces
        // WHITE:
        mPawnW1 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,0});
        mPawnW2 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,1});
        mPawnW3 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,2});
        mPawnW4 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,3});
        mPawnW5 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,4});
        mPawnW6 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,5});
        mPawnW7 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,6});
        mPawnW8 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,7});

        mRookW1 = new Piece(Piece.ROOK, Piece.WHITE, new int[]{7,0});
        mKnightW1 = new Piece(Piece.KNIGHT, Piece.WHITE, new int[]{7,1});
        mBishopW1 = new Piece(Piece.BISHOP, Piece.WHITE, new int[]{7,2});
        mQueenW = new Piece(Piece.QUEEN, Piece.WHITE, new int[]{7,3});
        mKingW = new Piece(Piece.KING, Piece.WHITE, new int[]{7,4});
        mBishopW2 = new Piece(Piece.BISHOP, Piece.WHITE, new int[]{7,5});
        mKnightW2 = new Piece(Piece.KNIGHT, Piece.WHITE, new int[]{7,6});
        mRookW2 = new Piece(Piece.ROOK, Piece.WHITE, new int[]{7,7});

        // BLACK:
        mRookB1 = new Piece(Piece.ROOK, Piece.BLACK, new int[]{0,0});
        mKnightB1 = new Piece(Piece.KNIGHT, Piece.BLACK, new int[]{0,1});
        mBishopB1 = new Piece(Piece.BISHOP, Piece.BLACK, new int[]{0,2});
        mQueenB = new Piece(Piece.QUEEN, Piece.BLACK, new int[]{0,3});
        mKingB = new Piece(Piece.KING, Piece.BLACK, new int[]{0,4});
        mBishopB2 = new Piece(Piece.BISHOP, Piece.BLACK, new int[]{0,5});
        mKnightB2 = new Piece(Piece.KNIGHT, Piece.BLACK, new int[]{0,6});
        mRookB2 = new Piece(Piece.ROOK, Piece.BLACK, new int[]{0,7});

        mPawnB1 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,0});
        mPawnB2 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,1});
        mPawnB3 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,2});
        mPawnB4 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,3});
        mPawnB5 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,4});
        mPawnB6 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,5});
        mPawnB7 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,6});
        mPawnB8 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,7});

        // Initialize our empty board array and set our coordinates to our tiles
        int i = 0; // The tile space number

        // Number each row
        for(int row = 0; row < BOARD_LENGTH; row++){
            // Number each column
            for(int col = 0; col < BOARD_LENGTH; col++){
                int[] coordinate;

                // Determine which spaces are filled with a piece
                switch (row){
                    case 0:
                    case 1:
                        coordinate = new int[]{row, col, TAKEN_BY_BLACK};
                        break;
                    case 6:
                    case 7:
                        coordinate = new int[]{row, col, TAKEN_BY_WHITE};
                        break;
                    default:
                        coordinate = new int[]{row, col, EMPTY_TILE};
                }

                mGameBoardTiles[i] = coordinate;
                i++;
            }
        }
        /* Final array should look like:
                 mGameboardTiles = { {0,0,2}, {0,1,2}, {0,2,2}, ..., {7,6,1}, {7,7,1} };
         */
    }

    public int[][] getGameBoardTiles() {
        return mGameBoardTiles;
    }
}
