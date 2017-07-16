package net.alexblass.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

import static net.alexblass.chess.models.Piece.X_INDEX;
import static net.alexblass.chess.models.Piece.Y_INDEX;

public class MainActivity extends AppCompatActivity implements TileAdapter.ItemClickListener{

    // Chess boards are 8 x 8
    private final int BOARD_COLS = 8;

    // A RecyclerView to display each game tile ImageView
    private RecyclerView mRecyclerView;

    // A GridLayoutManager to display our RecyclerView of images as a game board
    private GridLayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.boardRecyclerView);

        // Set up an 8 column grid for our board
        mLayoutManager = new GridLayoutManager(this, BOARD_COLS);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mBoard = new GameBoard(this);

        mAdapter = new TileAdapter(this, mBoard);
        mAdapter.setClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position, ImageView imageView) {

        Piece[] piecesPlacement = mBoard.getGameBoardTiles();

        // If it's the first click, verify there's a valid piece on the square
        if (mFirstClick){
            if (piecesPlacement[position] != null){
                //Get the coordinate values for the item selected
                mPieceToMove = piecesPlacement[position];
                int[] coordinates = mPieceToMove.getCoordinates();
                mFirstClickRow = coordinates[X_INDEX];
                mFirstClickCol = coordinates[Y_INDEX];

                imageView.setBackgroundColor(
                        getApplicationContext().getResources().getColor(R.color.selected));

                // First click successfully ended
                mFirstClick = false;
            }
        } else { // If this is not the first click, the next square must be empty
            if (piecesPlacement[position] == null){
                mSecondClickRow = position / 8;
                mSecondClickCol = position % 8;

                mBoard.movePieceTo(mPieceToMove, mSecondClickRow, mSecondClickCol);
                mPieceToMove.setCoordinates(new int[]{mSecondClickRow, mSecondClickCol});

                mAdapter.setGameBoard(mBoard);

                mFirstClick = true;
            }
        }
    }
}
