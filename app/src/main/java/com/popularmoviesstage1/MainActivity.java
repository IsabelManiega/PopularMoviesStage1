package com.popularmoviesstage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String API_KEY = "";//Put your API key here

    private MovieAdapter mAdapter;
    private RecyclerView mMovieGrid;

    private String currentQuery = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mMovieGrid = findViewById(R.id.movie_thumbs);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieGrid.setLayoutManager(layoutManager);
        mMovieGrid.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        mMovieGrid.setAdapter(mAdapter);

        executeWithCurrentQuery(currentQuery);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            case R.id.sort_by_popular:
                currentQuery = "popular";
                executeWithCurrentQuery(currentQuery);
                getSupportActionBar().setTitle("Popular Movies");
                return true;

            case R.id.sort_by_rating:
                currentQuery = "top_rated";
                executeWithCurrentQuery(currentQuery);
                getSupportActionBar().setTitle("Top Rated");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(JSONObject movieJsonObject) {
        Intent intent = new Intent(this, ChildActivity.class);
        // Movie information to be sent over in Intent
        String movieTitle = "";
        String posterPath = "";
        String plotSynopsis = "";
        String userRating = "";
        String releaseDate = "";
        try {
            movieTitle = movieJsonObject.getString("original_title");
            posterPath = movieJsonObject.getString("poster_path");
            plotSynopsis = movieJsonObject.getString("overview");
            userRating = movieJsonObject.getString("vote_average");
            releaseDate = movieJsonObject.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("original_title", movieTitle);
        intent.putExtra("poster_path", posterPath);
        intent.putExtra("overview", plotSynopsis);
        intent.putExtra("vote_average", userRating);
        intent.putExtra("release_date", releaseDate);

        startActivity(intent);

    }

    private class GetMoviesTask extends AsyncTask<String, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(String... params) {

            String baseUrl = "https://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + API_KEY + "&language=en-US&page=1";

            URL url;

            HttpURLConnection urlConnection = null;

            try {
                url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    JSONArray response = null;
                    JSONObject object = new JSONObject(scanner.next());
                    response = object.getJSONArray("results");

                    Log.v("JSONArray RESPONSE", response.toString());
                    return response;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            if (jsonArray != null) {
                mAdapter.setMovies(jsonArray);
            } else {
                mAdapter.setMovies(null);
            }
        }
    }

    private void executeWithCurrentQuery(String currentQuery) {
        new GetMoviesTask().execute(currentQuery);
    }

}
