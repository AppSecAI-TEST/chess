package net.alexblass.chess.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.alexblass.chess.R;

/**
 * A class to correctly display the game tiles on the board.
 */

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.ViewHolder> {

    // Square board has 8 rows and 8 columns
    private final int BOARD_LENGTH = 8;

    // All game boards will have 64 squares so there's no need to get or set a customized array
    /* Board should look like:
                 0 1 2 3 4 5 6 7
                 _ _ _ _ _ _ _ _
              0 |_|_|_|_|_|_|_|_|
              1 |_|_|_|_|_|_|_|_|
              2 |_|_|_|_|_|_|_|_|
              3 |_|_|_|_|_|_|_|_|
              4 |_|_|_|_|_|_|_|_|
              5 |_|_|_|_|_|_|_|_|
              6 |_|_|_|_|_|_|_|_|
              7 |_|_|_|_|_|_|_|_|
         */
    // Coordinate board in double array
    // There are 64 rows for each tile space in the board
    // and each row contains 1 column that holds an int array with the x,y coordinates (0-7)
    private int[][] mGameBoardTiles = new int[BOARD_LENGTH * BOARD_LENGTH][];

    // Context of the activity so we can access resources
    private Context mContext;

    // An inflater so we can set our tiles to the right layout
    private LayoutInflater mInflater;

    // A click listener so that when the image is tapped, it becomes selected
    private ItemClickListener mClickListener;

    public TileAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        // Initialize our board array and set our coordinates to our tiles
        int i = 0; // The tile space number

        // Number each row
        for(int row = 0; row < BOARD_LENGTH; row++){
            // Number each column
            for(int col = 0; col < BOARD_LENGTH; col++){
                int[] coordinate = {row, col};
                mGameBoardTiles[i] = coordinate;
                i++;
            }
        }
        /* Final array should look like:
                 mGameboardTiles = { {0,0}, {0,1}, {0,2}, ..., {7, 6}, {7,7} };
         */
    }


    // Creates the ViewHolder layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_tile, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binds the ViewHolder to the RecyclerView with the specified colors and images
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int[] coordinates = mGameBoardTiles[position];
        int row = coordinates[0];
        int col = coordinates[1];

        // Rows 1, 3, 5, and 7 start with black tiles
        // Rows 0, 2, 4, and 6 start with white tiles
        // Determine which row color pattern is starting
        boolean startsWhite;
        switch (row)
        {
            case 0:
            case 2:
            case 4:
            case 6:
                startsWhite = true;
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            default:
                startsWhite = false;
                break;
        }

        // Once determined the first tile color, apply black and white color pattern appropriately
        if (startsWhite) {
            switch (col){
                case 0:
                case 2:
                case 4:
                case 6:
                    holder.tileImageView.setBackgroundColor(mContext.getResources().getColor(
                            R.color.white));
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                    holder.tileImageView.setBackgroundColor(mContext.getResources().getColor(
                            R.color.black));
                    break;
            }
        } else {

            switch (col){
                case 0:
                case 2:
                case 4:
                case 6:
                    holder.tileImageView.setBackgroundColor(mContext.getResources().getColor(
                            R.color.black));
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                    holder.tileImageView.setBackgroundColor(mContext.getResources().getColor(
                            R.color.white));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGameBoardTiles.length;
    }

    // Get the coordinates of a tile
    public int[] getItem(int index){
        return mGameBoardTiles[index];
    }

    public void setClickListener(ItemClickListener itemClickListener){
        mClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView tileImageView;

        public ViewHolder(View itemView){
            super(itemView);
            tileImageView = (ImageView) itemView.findViewById(R.id.tileImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener != null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    // MainActivity.java will respond by implementing this
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
