package net.alexblass.chess;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

import static net.alexblass.chess.models.GameBoard.BOARD_LENGTH;
import static net.alexblass.chess.models.Piece.BISHOP;
import static net.alexblass.chess.models.Piece.KING;
import static net.alexblass.chess.models.Piece.KNIGHT;
import static net.alexblass.chess.models.Piece.PAWN;
import static net.alexblass.chess.models.Piece.QUEEN;
import static net.alexblass.chess.models.Piece.ROOK;

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

        // Check position validity
        switch (piece.getName()){
            case PAWN:
                // Pawns can only move one up or down
                // TODO: White and black pawns can only move in one direction
                if(Math.abs(changeRow) == 1 && changeCol == 0){
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
                    validMove = true;
                }
                break;
            case ROOK:
                // Rooks can only move in a straight line either horizontally across a row
                if((changeRow == 0 && changeCol != 0) ||
                        // Or vertically across a column
                        (changeCol == 0 && changeRow != 0)){
                    validMove = true;
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
                // Kings can move one up or down or left or right
                if(Math.abs(changeRow) == 1 || Math.abs(changeCol) == 1){
                    validMove = true;
                }
                break;
        }
        return validMove;
    }
}
