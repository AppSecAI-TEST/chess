package net.alexblass.chess.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.alexblass.chess.R;
import net.alexblass.chess.models.Piece;

/**
 * A class to correctly display the game tiles on the board.
 */

public class TileAdapter extends ArrayAdapter{

    // Context of the activity so we can access resources
    private Context mContext;

    // An inflater so we can set our tiles to the right layout
    private LayoutInflater mInflater;

    // The tiles on the gameboard
    private Piece[] mGameBoardTiles;

    public TileAdapter(Context context, Piece[] pieces) {
        super(context, R.layout.item_tile, pieces);

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mGameBoardTiles = pieces;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gameTile = convertView;
        ViewHolder holder;

        Piece thisGamePiece = mGameBoardTiles[position];

        // Inflate the layout for the tile
        if (gameTile == null) {
            gameTile = mInflater.inflate(R.layout.item_tile, parent, false);
            holder = new ViewHolder();
            holder.tileImageView = (ImageView) gameTile.findViewById(R.id.tileImageView);
            gameTile.setTag(holder);
        } else {
            holder = (ViewHolder) gameTile.getTag();
        }

        // Set the Piece image on the tile if there is a Piece on this tile
        if (thisGamePiece != null) {
            holder.tileImageView.setImageResource(thisGamePiece.getImageResourceId());
        } else { // Reset empty tiles for pieces that have been moved
            holder.tileImageView.setImageResource(0);
        }

        // Set the background color to the checker pattern
        int backgroundColor = setBackgroundColor(position);
        holder.tileImageView.setBackgroundColor(mContext.getResources().getColor(backgroundColor));

        return gameTile;
    }

    // Determine which color the tile should be
    public int setBackgroundColor(int position){
        int row = position / 8;
        int col = position % 8;

        // Determine which row color pattern is starting
        // 0 and even rows start with white
        // Odd rows start with black
        boolean startsWhite;
        if (row % 2 == 0) {
            startsWhite = true;
        } else {
            startsWhite = false;
        }

        // Once determined the first tile color, apply black and white color pattern appropriately
        if (startsWhite) {
            if (col % 2 == 0) {
                return R.color.white;
            } else {
                return R.color.black;
            }
        } else {
            if (col % 2 == 0) {
                return R.color.black;
            } else {
                return R.color.white;
            }
        }
    }

    public void setGameBoard(Piece[] boardPlacement) {
        this.mGameBoardTiles = boardPlacement;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView tileImageView;
    }
}