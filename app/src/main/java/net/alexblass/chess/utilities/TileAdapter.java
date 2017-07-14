package net.alexblass.chess.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.alexblass.chess.R;

/**
 * A class to correctly display the game tiles on the board.
 */

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.ViewHolder> {

    private int[] mGameBoard = { 0, 1, 2, 3, 4, 5, 6, 7,
                                8, 9, 10, 11, 12, 13, 14, 15,
                                16, 17, 18, 19, 20, 21, 22, 23,
                                24, 25, 26, 27, 28, 29, 30, 31,
                                32, 33, 34, 35, 36, 37, 38, 39,
                                40, 41, 42, 43, 44, 45, 46, 47,
                                48, 49, 50, 51, 51, 53, 54, 55,
                                56, 57, 58, 59, 60, 61, 62, 64 };

    // Context of the activity so we can access resources
    private Context mContext;

    // An inflater so we can set our tiles to the right layout
    private LayoutInflater mInflater;

    // A click listener so that when the image is tapped, it becomes selected
    private ItemClickListener mClickListener;

    public TileAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
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

        /*
                 A B C D E F G H
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

        int row = (position) / 8;

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
            switch (position % 8){
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

            switch (position % 8){
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
        return mGameBoard.length;
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
