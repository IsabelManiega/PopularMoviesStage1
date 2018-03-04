package com.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {

    private JSONArray movies;

    private MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(JSONObject movieJsonObject);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        super();
        movieAdapterOnClickHandler = clickHandler;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_grid_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;


        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {

        String url = "http://image.tmdb.org/t/p/w342/";

        try {
            Picasso.with(holder.mMoviePoster.getContext()).load(url + movies.getJSONObject(position).getString("poster_path")).into(holder.mMoviePoster);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.length();
        } else return 0;
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePoster;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            JSONObject movieObject = null;
            try {
                movieObject = movies.getJSONObject(adapterPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieAdapterOnClickHandler.onClick(movieObject);
        }
    }

    public void setMovies(JSONArray movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}