package net.alexblass.chess;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.PawnPiece;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

import static net.alexblass.chess.R.string.rook;
import static net.alexblass.chess.models.Piece.BISHOP;
import static net.alexblass.chess.models.Piece.BLACK;
import static net.alexblass.chess.models.Piece.KING;
import static net.alexblass.chess.models.Piece.KNIGHT;
import static net.alexblass.chess.models.Piece.PAWN;
import static net.alexblass.chess.models.Piece.QUEEN;
import static net.alexblass.chess.models.Piece.ROOK;
import static net.alexblass.chess.models.Piece.WHITE;

public class MainActivity extends AppCompatActivity {

    // A GridView to display each game tile ImageView
    private GridView mGridView;

    // Textviews for the player labels and score
    private TextView mPlayer1Lbl;
    private TextView mPlayer2Lbl;
    private TextView mPlayer1ScoreTv;
    private TextView mPlayer2ScoreTv;

    // An adapter to display images on the board correctly
    private TileAdapter mAdapter;

    // A board for a new chess game
    private GameBoard mBoard;

    // A boolean to keep track of turns
    // When true, it's player 1 (white)'s turn
    // When false, it's player 2 (black)'s turn
    private boolean mPlayer1Turn;

    // Keep track of the players' scores
    private int mPlayer1Score = 0;
    private int mPlayer2Score = 0;

    // Keep track of where the user has clicked
    // Is true by default until the user clicks the piece they want to move
    // Then is false until the user selects a valid square to move to
    // Upon successful move, it goes back to true
    private boolean mFirstClick = true;

    // The piece the user clicks on
    private Piece mPieceToMove;

    // The coordinates of the first and second clicks
    private int mFirstClickRow;
    private int mFirstClickCol;
    private int mSecondClickRow;
    private int mSecondClickCol;

    // Whether or not the current player can capture a piece on the tile selected
    boolean mCanCapture;

    // TODO: Implement on saved instance state for rotation and background state
    // TODO: Optimize layout for horizontal orientation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer1Lbl = (TextView) findViewById(R.id.player1_lbl);
        mPlayer2Lbl = (TextView) findViewById(R.id.player2_lbl);
        mPlayer1ScoreTv = (TextView) findViewById(R.id.player1_score);
        mPlayer2ScoreTv = (TextView) findViewById(R.id.player2_score);

        mPlayer1ScoreTv.setText(Integer.toString(mPlayer1Score));
        mPlayer2ScoreTv.setText(Integer.toString(mPlayer2Score));

        mGridView = (GridView) findViewById(R.id.boardGridView);

        mBoard = new GameBoard();
        mPlayer1Turn = true;
        setLabelStyle(mPlayer1Turn);

        mAdapter = new TileAdapter(this, mBoard.getGameBoardTiles());
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Piece[] piecesPlacement = mBoard.getGameBoardTiles();

                // If it's the first click, verify there's a valid piece on the square
                if (mFirstClick) {
                    if (piecesPlacement[position] != null) {
                        // Initialize to false by default before the second click
                        mCanCapture = false;

                        //Get the coordinate values for the item selected
                        mPieceToMove = piecesPlacement[position];

                        // if it's Player 1 (white)'s turn, verify white piece was clicked
                        if ((mPlayer1Turn == true && mPieceToMove.getColorCode() == WHITE) ||
                                // if it's Player 2 (black)'s turn, verify a black piece was clicked
                                mPlayer1Turn == false && mPieceToMove.getColorCode() == BLACK) {
                            mFirstClickRow = mPieceToMove.getRowX();
                            mFirstClickCol = mPieceToMove.getColY();

                            view.setBackgroundColor(
                                    getApplicationContext().getResources().getColor(R.color.selected));

                            // First click successfully ended
                            mFirstClick = false;
                        }
                    }
                } else { // This is not the first click
                    mSecondClickRow = position / 8;
                    mSecondClickCol = position % 8;

                    // If we click the same tile, deselect the piece
                    if (mSecondClickRow == mFirstClickRow && mSecondClickCol == mFirstClickCol) {
                        mAdapter.setGameBoard(mBoard.getGameBoardTiles());
                        mFirstClick = true;
                    } else { // To move, the second tile must be empty
                        boolean validMove = checkMoveValidity(mPieceToMove, mFirstClickRow, mFirstClickCol,
                                mSecondClickRow, mSecondClickCol);

                        // If the target tile is not empty, check if
                        // we can capture the piece on it
                        if (piecesPlacement[position] != null) {
                            mCanCapture = canCapturePiece(mPieceToMove, piecesPlacement[position]);
                        }

                        // If the second click move is valid for the piece type
                        // and if the second click tile is empty or has a piece
                        // we can capture, proceed.
                        if (validMove && (piecesPlacement[position] == null || mCanCapture)) {
                            if (mCanCapture) {
                                capturePiece(piecesPlacement[position]);
                            }

                            mBoard.movePieceTo(mPieceToMove, mSecondClickRow, mSecondClickCol);
                            mAdapter.setGameBoard(mBoard.getGameBoardTiles());

                            mFirstClick = true;

                            // Close turn by setting the next player's turn
                            if (mPlayer1Turn == true) {
                                mPlayer1Turn = false;
                            } else {
                                mPlayer1Turn = true;
                            }
                            setLabelStyle(mPlayer1Turn);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.invalid_move), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    private boolean checkMoveValidity(Piece piece, int oldRow, int oldCol, int newRow, int newCol) {
        // Since the user must click on an existing tile with pre-determined coordinates,
        // We do not need to check for out of bounds errors
        boolean validMove = false;

        int changeRow = newRow - oldRow;
        int changeCol = newCol - oldCol;

        // Check position validity
        switch (piece.getName()) {
            case PAWN:
                validMove = movePawn((PawnPiece) piece, newRow, newCol);

                // Trigger change piece type when a pawn reaches enemy home row.
                if (validMove &&
                        (piece.getColorCode() == WHITE && newRow == 0) ||
                        (piece.getColorCode() == BLACK && newRow == 7)) {
                    mPieceToMove = pawnPromotion(mPieceToMove);
                }
                break;
            case KNIGHT:
                validMove = moveKnight(changeRow, changeCol);
                break;
            case BISHOP:
                if (Math.abs(changeRow) == Math.abs(changeCol)) {

                    // Determine how to increment or decrement each row
                    // and column change when we move the Bishop.
                    int rowChange = 0;
                    int colChange = 0;
                    if (oldRow < newRow) {
                        rowChange = 1; // Moving downward
                    } else{
                        rowChange = -1; // Moving upward
                    }
                    if (oldCol < newCol) {
                        colChange = 1; // Moving right
                    } else {
                        colChange = -1; // Moving left
                    }

                    validMove = moveBishop(piece,
                            oldRow, oldCol,
                            newRow, newCol,
                            rowChange, colChange);
                }
                break;
            case ROOK:
                if ((changeRow == 0 && changeCol != 0) ||
                        (changeCol == 0 && changeRow != 0)) {
                    validMove = moveRook(piece, oldRow, newRow, oldCol, newCol);
                }
                break;
            case QUEEN:
                // Queen can move like a rook and bishop:
                // Any direction diagonal, vertical, and horizontal
                if ((changeRow == 0 && changeCol != 0) ||
                        (changeCol == 0 && changeRow != 0)) {
                    // Rook move logic
                    validMove = moveRook(piece, oldRow, newRow, oldCol, newCol);

                } else if (Math.abs(changeRow) == Math.abs(changeCol)) {
                    // Bishop move logic

                    // Determine how to increment or decrement each row
                    // and column change when we move the Bishop.
                    int rowChange = 0;
                    int colChange = 0;
                    if (oldRow < newRow) {
                        rowChange = 1; // Moving downward
                    } else{
                        rowChange = -1; // Moving upward
                    }
                    if (oldCol < newCol) {
                        colChange = 1; // Moving right
                    } else {
                        colChange = -1; // Moving left
                    }

                    validMove = moveBishop(piece,
                            oldRow, oldCol,
                            newRow, newCol,
                            rowChange, colChange);
                }
                break;
            case KING:
                // TODO: Check for king in check
                validMove = moveKing(piece, changeRow, newRow, changeCol, oldCol, newCol);
                break;
        }
        return validMove;
    }

    // Check if piece can be captured
    private boolean canCapturePiece(Piece selectedPiece, Piece secondPiece) {
        if (selectedPiece.getColorCode() != secondPiece.getColorCode()) {
            return true;
        }
        return false;
    }

    // Clean up to remove piece from the board and update the scores
    private void capturePiece(Piece pieceCaptured) {
        // Piece is no longer on the board
        pieceCaptured.setIsActive(false);
        pieceCaptured.setCoordinates(-1, -1);

        if (pieceCaptured.getColorCode() == WHITE) {
            mPlayer2Score += pieceCaptured.getPointsValue();
        } else {
            mPlayer1Score += pieceCaptured.getPointsValue();
        }

        mPlayer1ScoreTv.setText(Integer.toString(mPlayer1Score));
        mPlayer2ScoreTv.setText(Integer.toString(mPlayer2Score));
    }

    // Check for an complete a castling move
    private boolean castling(Piece king, Piece rook, int oldKingColPosition, int newKingColPosition) {
        int changeValue = 1;
        if (oldKingColPosition > newKingColPosition) {
            // if the old column is greater, we're moving down the board towards 0
            changeValue = -1;
        }

        // Check that all tiles between the king and the rook are empty
        for (int i = oldKingColPosition + changeValue; // Offset the column by +- 1 to avoid error
            // of detecting the current piece
             i > 0 && i < 7;
            // limit range to avoid the error of detecting the rook
             i = i + changeValue) {
            if (mBoard.getPieceAtCoordinates(king.getRowX(), i) != null) {
                return false;
            }
        }

        mBoard.movePieceTo(rook, king.getRowX(), newKingColPosition - changeValue);
        mAdapter.setGameBoard(mBoard.getGameBoardTiles());
        return true;
    }

    // Change player labels to indicate turns
    private void setLabelStyle(boolean player1Turn) {
        if (player1Turn == true) {
            if (Build.VERSION.SDK_INT < 23) {
                mPlayer1Lbl.setTextAppearance(this, R.style.playerLabelActiveTurn);
                mPlayer2Lbl.setTextAppearance(this, R.style.playerLabel);
            } else {
                mPlayer1Lbl.setTextAppearance(R.style.playerLabelActiveTurn);
                mPlayer2Lbl.setTextAppearance(R.style.playerLabel);
            }
        } else {
            if (Build.VERSION.SDK_INT < 23) {
                mPlayer1Lbl.setTextAppearance(this, R.style.playerLabel);
                mPlayer2Lbl.setTextAppearance(this, R.style.playerLabelActiveTurn);
            } else {
                mPlayer1Lbl.setTextAppearance(R.style.playerLabel);
                mPlayer2Lbl.setTextAppearance(R.style.playerLabelActiveTurn);
            }
        }
    }

    // The logic to move a Pawn
    private boolean movePawn(PawnPiece pawnToMove, int row, int col) {
        // Constants to hold the values for the amount of tiles a pawn may move.
        final int STANDARD_MOVE = 1;
        final int FIRST_MOVE = 2;

        int rowChange = row - pawnToMove.getRowX();
        int colChange = col - pawnToMove.getColY();

        boolean validResult = false;

        // Pawns can only move in one direction towards the enemy's home row.
        //// pawnDirection will be negative for white pawns, since they can
        //// only go upward on the board(towards row 0). pawnDirection will
        //// be positive for black pawns, since they can only go downwards
        //// on the board (towards row 7).
        int pawnDirection = STANDARD_MOVE;
        if (pawnToMove.getColorCode() == WHITE) {
            pawnDirection = STANDARD_MOVE * -1;
        }

        // Generally, pawns can only move 1 tile per move, but on their
        // first move, they may move 2 tiles forward.
        int firstMove = pawnDirection;
        if (!pawnToMove.hasMovedFromStart()) {
            firstMove *= FIRST_MOVE;
        }

        // En passant is only valid when the pawn has used it's 2 space move.
        if (Math.abs(rowChange) == FIRST_MOVE) {
            pawnToMove.setValidEnPassant(true);
        } else {
            pawnToMove.setValidEnPassant(false);
        }

        // On a standard, no-capture move, a pawn may move forward one tile, or
        // move forward two tiles on their very first move.  When a pawn moves
        // forward, it cannot capture or jump other pieces, so the tiles must
        // be clear.
        if (pawnToMove.getColY() == col &&
                (rowChange == pawnDirection || rowChange == firstMove)) {
            Piece checkForNullPiece;
            // Check for obstacles
            int i = pawnToMove.getRowX();
            while (i != row) {
                // Add or subtract 1 to traverse rows
                i += pawnDirection;
                checkForNullPiece = mBoard.getPieceAtCoordinates(i, col);
                if (checkForNullPiece != null) {
                    return false;
                } else {
                    validResult = true;
                }
            }

            // Pawns can capture pieces one row ahead and one column over.
        } else if (mBoard.getPieceAtCoordinates(row, col) != null &&
                Math.abs(rowChange) == STANDARD_MOVE &&
                Math.abs(colChange) == STANDARD_MOVE) {
            validResult = canCapturePiece(pawnToMove, mBoard.getPieceAtCoordinates(row, col));

            // Pawns can also capture pieces En Passant--when an enemy pawn moves 2
            // forward instead of 1, but moving 1 forward would have allowed
            // the other player to capture. To do this move, the target tile must
            // also be empty.
        } else if (mBoard.getPieceAtCoordinates(row, col) == null &&
                mBoard.getPieceAtCoordinates(pawnToMove.getRowX(), col) != null &&
                Math.abs(rowChange) == STANDARD_MOVE &&
                Math.abs(colChange) == STANDARD_MOVE) {
            validResult = enPassant(pawnToMove, col);
        }
        return validResult;
    }

    // Change a pawn when it reaches the enemy's home row
    private Piece pawnPromotion(final Piece oldPawn) {
        // Display an alert dialog so the user can select their new piece type
        AlertDialog.Builder promotionDialog = new AlertDialog.Builder(this);
        promotionDialog.setTitle(R.string.promo_dialog_title)
                .setItems(R.array.pawn_promotions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                oldPawn.setName(QUEEN);
                                if (oldPawn.getColorCode() == WHITE) {
                                    oldPawn.setImageResourceId(R.drawable.queen_w);
                                } else {
                                    oldPawn.setImageResourceId(R.drawable.queen_b);
                                }
                                break;
                            case 1:
                                oldPawn.setName(BISHOP);
                                if (oldPawn.getColorCode() == WHITE) {
                                    oldPawn.setImageResourceId(R.drawable.bishop_w);
                                } else {
                                    oldPawn.setImageResourceId(R.drawable.bishop_b);
                                }
                                break;
                            case 2:
                                oldPawn.setName(ROOK);
                                if (oldPawn.getColorCode() == WHITE) {
                                    oldPawn.setImageResourceId(R.drawable.rook_w);
                                } else {
                                    oldPawn.setImageResourceId(R.drawable.rook_b);
                                }
                                break;
                            case 3:
                                oldPawn.setName(KNIGHT);
                                if (oldPawn.getColorCode() == WHITE) {
                                    oldPawn.setImageResourceId(R.drawable.knight_w);
                                } else {
                                    oldPawn.setImageResourceId(R.drawable.knight_b);
                                }
                                break;
                        }
                        mAdapter.setGameBoard(mBoard.getGameBoardTiles());
                    }
                });
        promotionDialog.create().show();

        return oldPawn;
    }

    // Check for and complete an en passant move
    private boolean enPassant(Piece piece, int newCol) {
        // These are the rows in which you can find a pawn that
        // has made it's initial 2 space move
        int enemyRowPosition;
        if (piece.getColorCode() == WHITE) {
            // if my selected piece is white, the black enemy's row will be 3
            enemyRowPosition = 3;
        } else {
            // if my selected piece is black, the white enemy's row will be 4
            enemyRowPosition = 4;
        }

        // Already verified that this piece is not null before calling this method
        PawnPiece enemyPawn = (PawnPiece) mBoard.getPieceAtCoordinates(enemyRowPosition, newCol);

        // Verify the piece is a pawn of the opposite color
        // Verify an En Passant move is valid
        if (enemyPawn.getName().equals(PAWN) &&
                canCapturePiece(piece, enemyPawn) &&
                enemyPawn.getValidEnPassant()) {
            // Verify the selected piece is going from being besides the enemy pawn
            // To being one tile behind it
            if (enemyRowPosition == piece.getRowX() && newCol == enemyPawn.getColY()) {
                // Remove the piece from the board
                mBoard.setPieceAt(null, enemyPawn.getListPosition());
                mAdapter.setGameBoard(mBoard.getGameBoardTiles());
                return true;
            }
        }
        return false;
    }

    // The logic to move a Knight
    private boolean moveKnight(int rowChange, int colChange) {
        // Knights can move in an L shape in any direction.
        // Knights are the only piece that can jump other pieces.
        if ((Math.abs(rowChange) == 1 && Math.abs(colChange) == 2) ||
                (Math.abs(rowChange) == 2 && Math.abs(colChange) == 1)) {
            return true;
        }
        return false;
    }

    // The logic to move a Bishop
    private boolean moveBishop(Piece bishop,
                               int oldRow, int oldCol,
                               int newRow, int newCol,
                               int rowChange, int colChange) {
        // Bishops can move in any direction diagonally, but they cannot
        // jump over other pieces.

        // The row and column values we check need to be offset by +-1
        // so that we do not get a logic error where we pick up on
        // the piece that's being moved as an obstacle.
        int r = oldRow + rowChange;
        int c = oldCol + colChange;

        // If the Bishop is moving more than one square,
        // check the tiles between the start position and
        // the target position are empty.
        if (Math.abs(newRow - oldRow) > 1){
            while (r != newRow - rowChange) {
                // Check for obstructions between the start position and the target
                // position, but not on the final target tile.
                Piece checkForNullPiece = mBoard.getPieceAtCoordinates(r, c);
                if (checkForNullPiece != null) {
                    return false;
                }
                r += rowChange;
                c += colChange;
            }
        }

        // Check the target position for any pieces
        Piece checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, newCol);
        if (checkForNullPiece == null) {
            // If it's also empty, it's a valid move
            return true;
        } else {
            // If it's an enemy piece, it's also a valid move
            return canCapturePiece(bishop, checkForNullPiece);
        }
    }

    // The logic to move a Rook
    private boolean moveRook(Piece piece, int oldRow, int newRow, int oldCol, int newCol) {
        // Rooks can only move in a straight line either horizontally across a row
        // or vertically across a column.
        boolean result = false;
        Piece checkForNullPiece;

        // Check for obstructions
        if (oldRow < newRow){ // Moving downward
            // Check obstacles between start and target tiles
            // Use int i = oldRow + 1 to offset tile by one
            // to avoid a logic error reading the piece to be moved
            // as an obstacle.
            for (int i = oldRow + 1; i <= newRow - 1; i++){
                checkForNullPiece = mBoard.getPieceAtCoordinates(i, newCol);
                if (checkForNullPiece != null){
                    result = false;
                    return result;
                }
            }
            // Check the target tile for any pieces
            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, newCol);
            if (checkForNullPiece == null){
                // If it's also empty, it's a valid move
                result = true;
            } else {
                // If it's an enemy piece, it's also a valid move
                result = canCapturePiece(piece, checkForNullPiece);
            }
        } else if (oldRow > newRow){ // Moving upward
            // Check obstacles between start and target tiles
            // Use int i = oldRow - 1 to offset tile by one
            // to avoid a logic error reading the piece to be moved
            // as an obstacle.
            for (int i = oldRow - 1; i >= newRow + 1; i--){
                checkForNullPiece = mBoard.getPieceAtCoordinates(i, newCol);
                if (checkForNullPiece != null){
                    result = false;
                    return result;
                }
            }
            // Check the target tile for any pieces
            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, newCol);
            if (checkForNullPiece == null){
                // If it's also empty, it's a valid move
                result = true;
            } else {
                // If it's an enemy piece, it's also a valid move
                result = canCapturePiece(piece, checkForNullPiece);
            }
        } else if (oldCol < newCol){ // Moving right
            // Check obstacles between start and target tiles
            // Use int i = oldCol + 1 to offset tile by one
            // to avoid a logic error reading the piece to be moved
            // as an obstacle.
            for (int i = oldCol + 1; i <= newCol - 1; i++){
                checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, i);
                if (checkForNullPiece != null){
                    result = false;
                    return result;
                }
            }
            // Check the target tile for any pieces
            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, newCol);
            if (checkForNullPiece == null){
                // If it's also empty, it's a valid move
                result = true;
            } else {
                // If it's an enemy piece, it's also a valid move
                result = canCapturePiece(piece, checkForNullPiece);
            }
        } else if (oldCol > newCol){ // Moving left
            // Check obstacles between start and target tiles
            // Use int i = oldCol - 1 to offset tile by one
            // to avoid a logic error reading the piece to be moved
            // as an obstacle.
            for (int i = oldCol - 1; i >= newCol + 1; i--){
                checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, i);
                if (checkForNullPiece != null){
                    result = false;
                    return result;
                }
            }
            // Check the target tile for any pieces
            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, newCol);
            if (checkForNullPiece == null){
                // If it's also empty, it's a valid move
                result = true;
            } else {
                // If it's an enemy piece, it's also a valid move
                result = canCapturePiece(piece, checkForNullPiece);
            }
        }

            return result;
    }

    private boolean moveKing(Piece piece,
                             int changeRow, int newRow,
                             int changeCol, int oldCol, int newCol) {
        // Kings can move to any adjacent tile
        // Kings can move either one up or down and one or none left or right

        boolean result = false;

        if ((Math.abs(changeRow) == 1 && Math.abs(changeCol) <= 1)
                // Or kings can move one left or right and one or none up and down
                || (Math.abs(changeCol) == 1 && Math.abs(changeRow) <= 1)) {
            result = true;
        } else if (piece.hasMovedFromStart() == false
                // A king can do a Castling move when:
                // (A) They have not yet moved from their start position
                // (B) Not in check and will not move into a checked position
                // (C) The rook has not moved from start
                // (D) The spaces between the King and Rook are not occupied

                // In Castling, the King moves 2 tiles either left or right
                // And the Rook on that side of the board moves to the tile
                // on the opposite side of the King
                && (changeRow) == 0 && Math.abs(changeCol) == 2) {

            int rookColPosition;
            if (changeCol > 0) {
                // If the change row value is positive, the King is moving
                // right so we should use the right rook, which is at column 7
                rookColPosition = 7;
            } else {
                // If the change row value is negative, the King is moving
                // left so we should use the left rook, which is at column 0
                rookColPosition = 0;
            }

            // Verify there is a piece at the Rook's start position
            // Verify it is a Rook and has not yet moved from start
            Piece castlingRook = mBoard.getPieceAtCoordinates(newRow, rookColPosition);
            if (castlingRook != null
                    && castlingRook.getName() == ROOK
                    && castlingRook.hasMovedFromStart() == false) {
                result = castling(piece, castlingRook, oldCol, newCol);
            }
        }
        return result;
    }
}
