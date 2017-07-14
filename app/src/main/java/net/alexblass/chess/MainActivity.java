package net.alexblass.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.alexblass.chess.utilities.TileAdapter;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    // Chess boards are 8 x 8
    private final int BOARD_COLS = 8;

    // A RecyclerView to display each game tile ImageView
    private RecyclerView mRecyclerView;

    // A GridLayoutManager to display our RecyclerView of images as a game board
    private GridLayoutManager mLayoutManager;

    // An adapter to display images on the board correctly
    private TileAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.boardRecyclerView);

        // Set up an 8 column grid for our board
        mLayoutManager = new GridLayoutManager(this, BOARD_COLS);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TileAdapter(this);
        //mAdapter.setClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
    }
}
