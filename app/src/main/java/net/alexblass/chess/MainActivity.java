package net.alexblass.chess;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

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

    // Textviews for the player labels
    private TextView mPlayer1Lbl;
    private TextView mPlayer2Lbl;

    // An adapter to display images on the board correctly
    private TileAdapter mAdapter;

    // A board for a new chess game
    private GameBoard mBoard;

    // A boolean to keep track of turns
    // When true, it's player 1 (white)'s turn
    // When false, it's player 2 (black)'s turn
    private boolean mPlayer1Turn;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer1Lbl = (TextView) findViewById(R.id.player1_lbl);
        mPlayer2Lbl = (TextView) findViewById(R.id.player2_lbl);

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
                if (mFirstClick){
                    if (piecesPlacement[position] != null){
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
                    if (mSecondClickRow == mFirstClickRow && mSecondClickCol == mFirstClickCol){
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
                            mBoard.movePieceTo(mPieceToMove, mSecondClickRow, mSecondClickCol);

                            mAdapter.setGameBoard(mBoard.getGameBoardTiles());

                            mFirstClick = true;

                            // Close turn by setting the next player's turn
                            if (mPlayer1Turn == true){
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

    private boolean checkMoveValidity(Piece piece, int oldRow, int oldCol, int newRow, int newCol){
        // Since the user must click on an existing tile with pre-determined coordinates,
        // We do not need to check for out of bounds errors
        // TODO: Check for king in check
        // TODO: Capture piece functionality
        boolean validMove = false;

        int changeRow = newRow - oldRow;
        int changeCol = newCol - oldCol;

        Piece checkForNullPiece;
        int r, c;

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
                // Evaluate both statements: changeRow == rowChangeValue (either 1 or -1)
                // because a Pawn can move 1 on a regular turn,
                // And changeRow == firstMoveRowChange (2 or -2) can move 2 on a first
                // move but does not HAVE to and can also move 1 instead.
                if((changeRow == rowChangeValue || changeRow == firstMoveRowChange)
                        && changeCol == 0){
                    // Check for obstacles
                    int i = oldRow;
                    // Check if the target tile or the tile between (if 2 space move) is
                    // occupied by another piece. Pawns cannot capture straight on.
                    while (i != newRow){
                        // Add or subtract 1 to traverse rows
                        i += rowChangeValue;
                        checkForNullPiece = mBoard.getPieceAtCoordinates(i, newCol);
                        if (checkForNullPiece != null){
                            validMove = false;
                            return validMove;
                        }
                    }
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
                        r = oldRow + 1;
                        c = oldCol + 1;
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
                        r = oldRow - 1;
                        c = oldCol + 1;
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
                        r = oldRow - 1;
                        c = oldCol - 1;
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
                        r = oldRow + 1;
                        c = oldCol - 1;
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
                if ((changeRow == 0 && changeCol != 0) ||
                        (changeCol == 0 && changeRow != 0)){
                    // Rook move logic
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
                } else if (Math.abs(changeRow) == Math.abs(changeCol)){
                    // Bishop move logic
                    if (oldRow < newRow && oldCol < newCol){ // Moving down and right
                        // Offset tile by one to avoid a logic error
                        // reading the piece to be moved as an obstacle.
                        r = oldRow + 1;
                        c = oldCol + 1;
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
                        r = oldRow - 1;
                        c = oldCol + 1;
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
                        r = oldRow - 1;
                        c = oldCol - 1;
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
                        r = oldRow + 1;
                        c = oldCol - 1;
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

    // Check if piece can be captured
    private boolean canCapturePiece(Piece selectedPiece, Piece secondPiece){
        if (selectedPiece.getColorCode() != secondPiece.getColorCode()){
            return true;
        }
        return false;
    }

    // Change player labels to indicate turns
    private void setLabelStyle(boolean player1Turn){
        if (player1Turn == true){
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
}
