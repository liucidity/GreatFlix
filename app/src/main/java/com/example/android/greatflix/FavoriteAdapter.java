package com.example.android.greatflix;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.greatflix.objects.Movies;
import com.example.android.greatflix.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TRAVIS on 2017-09-09.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {


    private List<Movies> mFavoritesData;
    private final FavoriteAdapterOnClickHandler mClickHandler;



    public interface FavoriteAdapterOnClickHandler{
        void onClick(String currentMovie, String titlePath, String idPath, String releaseDatePath,String ratingPath, String overviewPath, boolean isFavorite);
    }
    public FavoriteAdapter(FavoriteAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }
    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mImageView;

        public FavoriteAdapterViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.poster_imageview);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String imagePath = mFavoritesData.get(adapterPosition).getPosterPath();
            String titlePath = mFavoritesData.get(adapterPosition).getTitle();
            String idPath = mFavoritesData.get(adapterPosition).getId();
            String releaseDatePath = mFavoritesData.get(adapterPosition).getReleaseDate();
            String ratingPath = mFavoritesData.get(adapterPosition).getRating();
            String overviewPath = mFavoritesData.get(adapterPosition).getOverview();
            boolean isFavorite = true;
            mClickHandler.onClick(imagePath,titlePath,idPath,releaseDatePath,ratingPath,overviewPath,isFavorite);
        }

    }

    @Override
    public FavoriteAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForFavoritesList = R.layout.list_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParents = false;
        View view = inflater.inflate(layoutIdForFavoritesList,parent,shouldAttachToParents);
        return new FavoriteAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FavoriteAdapterViewHolder holder, int position) {
        String MoviePosterPath = mFavoritesData.get(position).getPosterPath();
        Uri uri = Uri.parse(NetworkUtils.buildMoviePosterPath(MoviePosterPath).toString());
        ImageView ivBasicImage = holder.mImageView;
        Context context = ivBasicImage.getContext();
        Picasso.with(context).load(uri).resize(342,514).centerInside().into(ivBasicImage);
    }
    @Override
    public int getItemCount() {
        if (null == mFavoritesData) return 0;
        return mFavoritesData.size();
    }


    public void setFavoritesData(List<Movies> movieData) {
        mFavoritesData = movieData;
        notifyDataSetChanged();

    }
}
