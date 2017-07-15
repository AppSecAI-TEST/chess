package net.alexblass.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.utilities.TileAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.boardRecyclerView);

        // Set up an 8 column grid for our board
        mLayoutManager = new GridLayoutManager(this, BOARD_COLS);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mBoard = new GameBoard();

        mAdapter = new TileAdapter(this, mBoard);
        mAdapter.setClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        // Get the coordinate values for the item selected
        int[] coorinates = mAdapter.getItem(position);
        int row = coorinates[0];
        int col = coorinates[1];

        // TODO check if a piece is on the tile and respond accordingly
    }
}
