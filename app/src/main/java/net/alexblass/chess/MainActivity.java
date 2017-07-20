package net.alexblass.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

import static net.alexblass.chess.models.Piece.BISHOP;
import static net.alexblass.chess.models.Piece.KING;
import static net.alexblass.chess.models.Piece.KNIGHT;
import static net.alexblass.chess.models.Piece.PAWN;
import static net.alexblass.chess.models.Piece.QUEEN;
import static net.alexblass.chess.models.Piece.ROOK;
import static net.alexblass.chess.models.Piece.WHITE;

public class MainActivity extends AppCompatActivity {

    // A GridView to display each game tile ImageView
    private GridView mGridView;

    // An adapter to display images on the board correctly
    private TileAdapter mAdapter;

    // A board for a new chess game
    private GameBoard mBoard;

    // Keep track of where the user has clicked
    // Is true by default until the user clicks the piece they want to move
    // Then is false until the user selects a valid square to move to
    // Upon successful move, it goes back to true
    private boolean mFirstClick = true;

    private Piece mPieceToMove;
    int mFirstClickRow;
    int mFirstClickCol;
    int mSecondClickRow;
    int mSecondClickCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.boardGridView);

        mBoard = new GameBoard();

        mAdapter = new TileAdapter(this, mBoard.getGameBoardTiles());
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Piece[] piecesPlacement = mBoard.getGameBoardTiles();

                // If it's the first click, verify there's a valid piece on the square
                if (mFirstClick){
                    if (piecesPlacement[position] != null){
                        //Get the coordinate values for the item selected
                        mPieceToMove = piecesPlacement[position];
                        mFirstClickRow = mPieceToMove.getRowX();
                        mFirstClickCol = mPieceToMove.getColY();

                        view.setBackgroundColor(
                                getApplicationContext().getResources().getColor(R.color.selected));

                        // First click successfully ended
                        mFirstClick = false;
                    }
                } else { // If this is not the first click, the next square must be empty
                    mSecondClickRow = position / 8;
                    mSecondClickCol = position % 8;

                    boolean validMove = checkMoveValidity(mPieceToMove, mFirstClickRow, mFirstClickCol,
                            mSecondClickRow, mSecondClickCol);

                    if (piecesPlacement[position] == null && validMove){
                        mBoard.movePieceTo(mPieceToMove, mSecondClickRow, mSecondClickCol);

                        mAdapter.setGameBoard(mBoard.getGameBoardTiles());

                        mFirstClick = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.invalid_move), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean checkMoveValidity(Piece piece, int oldRow, int oldCol, int newRow, int newCol){
        // Since the user must click on an existing tile with pre-determined coordinates,
        // We do not need to check for out of bounds errors
        // TODO: Check obstructions
        // TODO: Check for king in check
        // TODO: Capture piece functionality
        boolean validMove = false;

        int changeRow = newRow - oldRow;
        int changeCol = newCol - oldCol;

        Piece checkForNullPiece;

        // Check position validity
        switch (piece.getName()){
            case PAWN:
                // TODO: Pawns can capture diagonal pieces
                int rowChangeValue;
                if(piece.getColorCode() == WHITE){
                    // White pieces can only move up on the board (towards row 0)
                    rowChangeValue = -1;
                } else { // Black pieces can only move down on the board (towards row 7)
                    rowChangeValue = 1;
                }

                // Pawns can move 2 squares on the first move
                int firstMoveRowChange = rowChangeValue;
                if(piece.hasMovedFromStart() == false){
                    firstMoveRowChange = rowChangeValue * 2;
                }

                // Pawns can only move one up or down
                if((changeRow == rowChangeValue || changeRow == firstMoveRowChange)
                        && changeCol == 0){
                    validMove = true;
                }
                break;
            case KNIGHT:
                // Knights can move in an L shape in any direction
                // Knights are the only piece that can jump other pieces
                if ((Math.abs(changeRow) == 1 && Math.abs(changeCol) == 2) ||
                        (Math.abs(changeRow) == 2 && Math.abs(changeCol) == 1)){
                    validMove = true;
                }
                break;
            case BISHOP:
                // Bishops can move in any direction diagonally
                if (Math.abs(changeRow) == Math.abs(changeCol)){
                    if (oldRow < newRow && oldCol < newCol){ // Moving down and right
                        // Offset tile by one to avoid a logic error
                        // reading the piece to be moved as an obstacle.
                        int r = oldRow + 1;
                        int c = oldCol + 1;
                        while (r <= newRow) {
                            // Check for obstructions
                            checkForNullPiece = mBoard.getPieceAtCoordinates(r, c);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                            r++;
                            c++;
                        }
                    } else if (oldRow > newRow && oldCol < newCol){ // Moving up and right
                        // Offset tile by one to avoid a logic error
                        // reading the piece to be moved as an obstacle.
                        int r = oldRow - 1;
                        int c = oldCol + 1;
                        while (r >= newRow) {
                            // Check for obstructions
                            checkForNullPiece = mBoard.getPieceAtCoordinates(r, c);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                            r--;
                            c++;
                        }
                    } else if (oldRow > newRow && oldCol > newCol){ // Moving up and left
                        // Offset tile by one to avoid a logic error
                        // reading the piece to be moved as an obstacle.
                        int r = oldRow - 1;
                        int c = oldCol - 1;
                        while (r >= newRow) {
                            // Check for obstructions
                            checkForNullPiece = mBoard.getPieceAtCoordinates(r, c);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                            r--;
                            c--;
                        }
                    } else if (oldRow < newRow && oldCol > newCol){ // Moving down and left
                        // Offset tile by one to avoid a logic error
                        // reading the piece to be moved as an obstacle.
                        int r = oldRow + 1;
                        int c = oldCol - 1;
                        while (r <= newRow) {
                            // Check for obstructions
                            checkForNullPiece = mBoard.getPieceAtCoordinates(r, c);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                            r++;
                            c--;
                        }
                    }
                }
                break;
            case ROOK:
                // Rooks can only move in a straight line either horizontally across a row
                if((changeRow == 0 && changeCol != 0) ||
                        // Or vertically across a column
                        (changeCol == 0 && changeRow != 0)){

                    // Check for obstructions
                    if (oldRow < newRow){ // Moving downward
                        // Use int i = oldRow + 1 to offset tile by one
                        // to avoid a logic error reading the piece to be moved
                        // as an obstacle.
                        for (int i = oldRow + 1; i <= newRow; i++){
                            checkForNullPiece = mBoard.getPieceAtCoordinates(i, newCol);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                        }
                    } else if (oldRow > newRow){ // Moving upward
                        // Use int i = oldRow - 1 to offset tile by one
                        // to avoid a logic error reading the piece to be moved
                        // as an obstacle.
                        for (int i = oldRow - 1; i >= newRow; i--){
                            checkForNullPiece = mBoard.getPieceAtCoordinates(i, newCol);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                        }
                    } else if (oldCol < newCol){ // Moving right
                        // Use int i = oldCol + 1 to offset tile by one
                        // to avoid a logic error reading the piece to be moved
                        // as an obstacle.
                        for (int i = oldCol + 1; i <= newCol; i++){
                            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, i);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                        }
                    } else if (oldCol > newCol){ // Moving left
                        // Use int i = oldCol - 1 to offset tile by one
                        // to avoid a logic error reading the piece to be moved
                        // as an obstacle.
                        for (int i = oldCol - 1; i >= newCol; i--){
                            checkForNullPiece = mBoard.getPieceAtCoordinates(newRow, i);
                            if (checkForNullPiece != null){
                                validMove = false;
                                return validMove;
                            } else {
                                validMove = true;
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                // Queen can move like a rook and bishop:
                // Any direction diagonal, vertical, and horizontal
                if ((Math.abs(changeRow) == Math.abs(changeCol)) ||
                        (changeRow == 0 && changeCol != 0) ||
                        (changeCol == 0 && changeRow != 0)){
                    validMove = true;
                }
                break;
            case KING:
                // Kings can move to any adjacent tile
                // Kings can move either one up or down and one or none left or right
                if((Math.abs(changeRow) == 1 && Math.abs(changeCol) <= 1)
                        // Or kings can move one left or right and one or none up and down
                        || (Math.abs(changeCol) == 1 && Math.abs(changeRow) <= 1)){
                    validMove = true;
                }
                break;
        }
        return validMove;
    }
}
