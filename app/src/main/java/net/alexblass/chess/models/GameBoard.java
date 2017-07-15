package net.alexblass.chess.models;

import android.content.Context;

import net.alexblass.chess.R;

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
    private Piece[] mGameBoardTiles = new Piece[BOARD_LENGTH * BOARD_LENGTH];

    // Pieces for white and black players
    private Piece mQueenW, mKingW, mRookW1, mRookW2, mBishopW1, mBishopW2,
            mKnightW1, mKnightW2, mPawnW1, mPawnW2, mPawnW3, mPawnW4, mPawnW5, mPawnW6,
            mPawnW7, mPawnW8;
    private Piece mQueenB, mKingB, mRookB1, mRookB2, mBishopB1, mBishopB2,
            mKnightB1, mKnightB2, mPawnB1, mPawnB2, mPawnB3, mPawnB4, mPawnB5, mPawnB6,
            mPawnB7, mPawnB8;

    // Create a new gameboard for a new game of chess
    public GameBoard(Context context){
        // Initialize the chess pieces
        // WHITE:
        mPawnW1 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,0}, R.drawable.pawn_w);
        mPawnW2 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,1}, R.drawable.pawn_w);
        mPawnW3 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,2}, R.drawable.pawn_w);
        mPawnW4 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,3}, R.drawable.pawn_w);
        mPawnW5 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,4}, R.drawable.pawn_w);
        mPawnW6 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,5}, R.drawable.pawn_w);
        mPawnW7 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,6}, R.drawable.pawn_w);
        mPawnW8 = new Piece(Piece.PAWN, Piece.WHITE, new int[]{6,7}, R.drawable.pawn_w);

        mRookW1 = new Piece(Piece.ROOK, Piece.WHITE, new int[]{7,0}, R.drawable.rook_w);
        mKnightW1 = new Piece(Piece.KNIGHT, Piece.WHITE, new int[]{7,1}, R.drawable.knight_w);
        mBishopW1 = new Piece(Piece.BISHOP, Piece.WHITE, new int[]{7,2}, R.drawable.bishop_w);
        mQueenW = new Piece(Piece.QUEEN, Piece.WHITE, new int[]{7,3}, R.drawable.queen_w);
        mKingW = new Piece(Piece.KING, Piece.WHITE, new int[]{7,4}, R.drawable.king_w);
        mBishopW2 = new Piece(Piece.BISHOP, Piece.WHITE, new int[]{7,5}, R.drawable.bishop_w);
        mKnightW2 = new Piece(Piece.KNIGHT, Piece.WHITE, new int[]{7,6}, R.drawable.knight_w);
        mRookW2 = new Piece(Piece.ROOK, Piece.WHITE, new int[]{7,7}, R.drawable.rook_w);

        // BLACK:
        mRookB1 = new Piece(Piece.ROOK, Piece.BLACK, new int[]{0,0}, R.drawable.rook_b);
        mKnightB1 = new Piece(Piece.KNIGHT, Piece.BLACK, new int[]{0,1}, R.drawable.knight_b);
        mBishopB1 = new Piece(Piece.BISHOP, Piece.BLACK, new int[]{0,2}, R.drawable.bishop_b);
        mQueenB = new Piece(Piece.QUEEN, Piece.BLACK, new int[]{0,3}, R.drawable.queen_b);
        mKingB = new Piece(Piece.KING, Piece.BLACK, new int[]{0,4}, R.drawable.king_b);
        mBishopB2 = new Piece(Piece.BISHOP, Piece.BLACK, new int[]{0,5}, R.drawable.bishop_b);
        mKnightB2 = new Piece(Piece.KNIGHT, Piece.BLACK, new int[]{0,6}, R.drawable.knight_b);
        mRookB2 = new Piece(Piece.ROOK, Piece.BLACK, new int[]{0,7}, R.drawable.rook_b);

        mPawnB1 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,0}, R.drawable.pawn_b);
        mPawnB2 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,1}, R.drawable.pawn_b);
        mPawnB3 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,2}, R.drawable.pawn_b);
        mPawnB4 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,3}, R.drawable.pawn_b);
        mPawnB5 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,4}, R.drawable.pawn_b);
        mPawnB6 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,5}, R.drawable.pawn_b);
        mPawnB7 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,6}, R.drawable.pawn_b);
        mPawnB8 = new Piece(Piece.PAWN, Piece.BLACK, new int[]{1,7}, R.drawable.pawn_b);

        // Place pieces on the board
        // We use a single array so we can pass this array to the TileAdapter
        // And the array will be read to populate the RecyclerView with images

        mGameBoardTiles[0] = mRookB1;
        mGameBoardTiles[1] = mKnightB1;
        mGameBoardTiles[2] = mBishopB1;
        mGameBoardTiles[3] = mQueenB;
        mGameBoardTiles[4] = mKingB;
        mGameBoardTiles[5] = mBishopB2;
        mGameBoardTiles[6] = mKnightB2;
        mGameBoardTiles[7] = mRookB2;

        mGameBoardTiles[8] = mPawnB1;
        mGameBoardTiles[9] = mPawnB2;
        mGameBoardTiles[10] = mPawnB3;
        mGameBoardTiles[11] = mPawnB4;
        mGameBoardTiles[12] = mPawnB5;
        mGameBoardTiles[13] = mPawnB6;
        mGameBoardTiles[14] = mPawnB7;
        mGameBoardTiles[15] = mPawnB8;

        // Squares 16 - 48 are empty

        mGameBoardTiles[48] = mPawnW1;
        mGameBoardTiles[49] = mPawnW2;
        mGameBoardTiles[50] = mPawnW3;
        mGameBoardTiles[51] = mPawnW4;
        mGameBoardTiles[52] = mPawnW5;
        mGameBoardTiles[53] = mPawnW6;
        mGameBoardTiles[54] = mPawnW7;
        mGameBoardTiles[55] = mPawnW8;

        mGameBoardTiles[56] = mRookW1;
        mGameBoardTiles[57] = mKnightW1;
        mGameBoardTiles[58] = mBishopW1;
        mGameBoardTiles[59] = mQueenW;
        mGameBoardTiles[60] = mKingW;
        mGameBoardTiles[61] = mBishopW2;
        mGameBoardTiles[62] = mKnightW2;
        mGameBoardTiles[63] = mRookW2;

        // Set the empty tiles
        for(int i = 16; i < 48; i++){
                mGameBoardTiles[i] = null;
                i++;
        }
    }

    public Piece[] getGameBoardTiles() {
        return mGameBoardTiles;
    }

    public void getPieceAtCoordinates(int x, int y){
        // TODO find piece at teh coordinates given
    }
}
