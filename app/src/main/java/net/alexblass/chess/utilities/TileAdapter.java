package net.alexblass.chess.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.alexblass.chess.R;
import net.alexblass.chess.models.GameBoard;
import net.alexblass.chess.models.Piece;

import static net.alexblass.chess.models.Piece.X_INDEX;
import static net.alexblass.chess.models.Piece.Y_INDEX;

/**
 * A class to correctly display the game tiles on the board.
 */

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.ViewHolder> {

    // Our chess game's current board
    private GameBoard mBoard;

    // The tiles on the gameboard
    private Piece[] mGameBoardTiles;

    // Context of the activity so we can access resources
    private Context mContext;

    // An inflater so we can set our tiles to the right layout
    private LayoutInflater mInflater;

    // A click listener so that when the image is tapped, it becomes selected
    private ItemClickListener mClickListener;

    public TileAdapter (Context context, GameBoard board) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBoard = board;

        mGameBoardTiles = mBoard.getGameBoardTiles();
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

        int row, col, imageResourceId;
        if (mGameBoardTiles[position] != null){
            Piece thisPiece = mGameBoardTiles[position];
            int[] coordinates = thisPiece.getCoordinates();
            row = coordinates[X_INDEX];
            col = coordinates[Y_INDEX];
            imageResourceId = thisPiece.getImageResourceId();
            holder.tileImageView.setImageResource(imageResourceId);
        } else {
            row = position / 8;
            col = position % 8;
        }

        // First, color the tiles
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
    public Piece getItem(int index){
        return mGameBoardTiles[index];
    }

    public void changeBackgroundColor(ImageView imageView, int position){
        if(mGameBoardTiles[position] == null){
            return;
        }
        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.selected));
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
                changeBackgroundColor(tileImageView, getAdapterPosition());
            }
        }
    }

    // MainActivity.java will respond by implementing this
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
