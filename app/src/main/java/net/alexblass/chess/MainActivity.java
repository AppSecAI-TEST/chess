package net.alexblass.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;
import net.alexblass.chess.utilities.TileAdapter;

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
                    if (piecesPlacement[position] == null){
                        mSecondClickRow = position / 8;
                        mSecondClickCol = position % 8;

                        mBoard.movePieceTo(mPieceToMove, mSecondClickRow, mSecondClickCol);

                        mAdapter.setGameBoard(mBoard.getGameBoardTiles());

                        mFirstClick = true;
                    }
                }
            }
        });

    }
}
