package com.example.android.greatflix;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by TRAVIS on 2017-05-22.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private String[] mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler{
        void onClick(String currentMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;

    }
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mImageView;

        public MovieAdapterViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.poster_imageview);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String imagePath = mMovieData[adapterPosition];
            mClickHandler.onClick(imagePath);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIDforMovieList = R.layout.list_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParents = false;

        View view = inflater.inflate(layoutIDforMovieList,parent,shouldAttachToParents);
        return new MovieAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String MoviePosterPath = mMovieData[position];
        //todo: prob broken
        holder.mImageView.setImageURI(Uri.parse(MoviePosterPath));
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();

    }
}
