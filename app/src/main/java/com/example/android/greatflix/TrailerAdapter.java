package com.example.android.greatflix;

import android.content.Context;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.greatflix.objects.Trailer;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by TRAVIS on 2017-10-25.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerData;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler{

        //todo: implement click function
        void onClick(String trailerKey);
    }
    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, Context context){

        mClickHandler = clickHandler;

        mContext = context;
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_recycler_view_layout,parent,false);

        return new TrailerAdapterViewHolder(trailerView);
    }


    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mImageView;
        public final TextView mTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);


            mTextView = (TextView) itemView.findViewById(R.id.tv_trailer_one_vh) ;
            mImageView = (ImageView) itemView.findViewById(R.id.iv_play_button_vh);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String trailerKey = mTrailerData.get(adapterPosition).getKey();
            mClickHandler.onClick(trailerKey);
        }
    }
    @Override
    public void onBindViewHolder(final TrailerAdapterViewHolder holder, final int position) {
        final String selectedTrailerKey = mTrailerData.get(position).getKey();
        final String youtubeBaseUrl = mContext.getString(R.string.url_youtube_video_id);
        Log.d(TAG, "onBindViewHolder: trailerKey = " + selectedTrailerKey);
        Log.d(TAG, "onBindViewHolder: full youtube = "+ mContext.getString(R.string.url_youtube_video_id) + selectedTrailerKey);
        holder.mImageView.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        holder.mTextView.setText("Trailer #"+(position+1));
        //todo: implement play button in detail activity/ trailer recyclerview to get
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(youtubeBaseUrl +selectedTrailerKey)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });

    }
    @Override
    public int getItemCount() {
        if (null==mTrailerData) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData(List<Trailer> data) {
        mTrailerData = data;
        notifyDataSetChanged();
    }
}
