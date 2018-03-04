package com.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ChildActivity extends AppCompatActivity {

    private static final String POSTER_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342/";

    private ImageView mMoviePoster;

    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieRating;
    private TextView mMovieOverview;

    private Intent fetchedIntent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mMoviePoster = findViewById(R.id.movie_thumbnail);
        mMovieTitle = findViewById(R.id.original_title);
        mMovieReleaseDate = findViewById(R.id.release_date);
        mMovieRating = findViewById(R.id.vote_average);
        mMovieOverview = findViewById(R.id.overview);

        fetchedIntent = getIntent();
        bundle = fetchedIntent.getExtras();

        if (bundle != null) {
            String posterPath = bundle.getString("poster_path");
            String movieTitle = bundle.getString("original_title");
            String plotSynopsis = bundle.getString("overview");
            String userRating = bundle.getString("vote_average");
            String releaseDate = bundle.getString("release_date");
            String year = releaseDate.substring(0, 4);
            String month = releaseDate.substring(5, 7);
            String day = releaseDate.substring(8, 10);

            Picasso.with(this).load(POSTER_IMAGE_BASE_URL + posterPath).into(mMoviePoster);

            mMovieTitle.setText(movieTitle);
            mMovieReleaseDate.setText("Release Date: " + month + "/" + day + "/" + year +"\n");
            mMovieRating.setText("MovieDB Rating: " + userRating + "/10");
            mMovieOverview.setText(plotSynopsis);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("title"));
        }
        return super.onCreateOptionsMenu(menu);
    }
}