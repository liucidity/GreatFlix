package com.example.android.greatflix;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by TRAVIS on 2017-05-22.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movies> mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler{
        void onClick(String currentMovie, String titlePath, String idPath, String releaseDatePath,String ratingPath, String overviewPath);
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
            String imagePath = mMovieData.get(adapterPosition).getPosterPath();
            String titlePath = mMovieData.get(adapterPosition).getTitle();
            String idPath = mMovieData.get(adapterPosition).getId();
            String releaseDatePath = mMovieData.get(adapterPosition).getReleaseDate();
            String ratingPath = mMovieData.get(adapterPosition).getRating();
            String overviewPath = mMovieData.get(adapterPosition).getOverview();
            mClickHandler.onClick(imagePath,titlePath,idPath,releaseDatePath, ratingPath, overviewPath);
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
        String MoviePosterPath = mMovieData.get(position).getPosterPath();
        Log.d(TAG, "onBindViewHolder: "+MoviePosterPath);
        //todo: prob broken
        Uri uri = Uri.parse(NetworkUtils.buildMoviePosterPath(MoviePosterPath).toString());
        ImageView ivBasicImage = holder.mImageView;
        Context context = ivBasicImage.getContext();
        Picasso.with(context).load(uri).resize(342,514).centerInside().into(ivBasicImage);


    }

    public List<Movies> getMovieData() {
        return mMovieData;
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }


    public void setMovieData(List<Movies> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();

    }

}

