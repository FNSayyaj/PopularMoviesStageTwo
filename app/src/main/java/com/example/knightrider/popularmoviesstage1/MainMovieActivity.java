package com.example.knightrider.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.knightrider.popularmoviesstage1.Adapters.MoviesAdapter;
import com.example.knightrider.popularmoviesstage1.DataBase.FavoriteMoviesContract;
import com.example.knightrider.popularmoviesstage1.Models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.android.volley.Request.Method.GET;

public class MainMovieActivity extends AppCompatActivity implements MoviesAdapter.ListMovieClickListener{

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private final String URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=PUT YOUR API KEY HERE";
    private final String URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=PUT YOUR API KEY HERE";
    private String CHOSEN_URL = URL_TOP_RATED;
    @BindView(R.id.nointernettextview) TextView NO_INTERNET;
    @BindView(R.id.refreshbutton) Button REFRESH;
    private final Context context = this;
    private boolean POPULATED_UI_SUCCESSFULLY = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movie);

        LoadPreferences();

        recyclerView = findViewById(R.id.recyclerview);

        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        movieList = new ArrayList<>();

        if(isNetworkAvailable()){

            hideConnectionErrors();
            loadMovieData(CHOSEN_URL);
            POPULATED_UI_SUCCESSFULLY = true;}
        else {

            showConnectionErrors();
        }

        REFRESH.setOnClickListener(v -> {
            if(isNetworkAvailable()) {

                hideConnectionErrors();
                loadMovieData(CHOSEN_URL);
                POPULATED_UI_SUCCESSFULLY = true;
            }
            else{
                Toast.makeText(MainMovieActivity.this,
                        getResources().getText(R.string.no_connectivity_error_with_refresh_button),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    private void loadMovieData(String url) {

            StringRequest stringRequest = new StringRequest(GET, url, response -> {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.optJSONArray(getResources().getString(R.string.KEY_RESULTS));
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject jo = results.optJSONObject(i);
                        Movie movies = new Movie(
                                jo.optString(getResources().getString(R.string.KEY_MOVIE_ID)),
                                jo.optString(getResources().getString(R.string.KEY_TITLE)),
                                jo.optString(getResources().getString(R.string.KEY_RELEASE_DATE)),
                                jo.optDouble(getResources().getString(R.string.KEY_RATE)),
                                jo.optString(getResources().getString(R.string.KEY_POSTER_PATH)),
                                jo.optString(getResources().getString(R.string.KEY_PLOT)));
                        movieList.add(movies);
                    }
                    adapter = new MoviesAdapter(movieList, getApplicationContext(), this);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(MainMovieActivity.this,
                    getResources().getText(R.string.no_connectivity_error),
                    Toast.LENGTH_LONG).show()
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.popular:
                if(isNetworkAvailable()){

                    CHOSEN_URL = URL_POPULAR;
                    setActivityName();
                    SavePreferences();
                    loadMovieData(CHOSEN_URL);
                    if (POPULATED_UI_SUCCESSFULLY){adapter.refreshEvents(movieList);}
                    hideConnectionErrors();
                }else {
                    Toast.makeText(MainMovieActivity.this,
                            getResources().getText(R.string.no_connectivity_error_with_refresh_button),
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.top_rate:
                if(isNetworkAvailable()) {

                    CHOSEN_URL = URL_TOP_RATED;
                    setActivityName();
                    SavePreferences();
                    loadMovieData(CHOSEN_URL);
                    if (POPULATED_UI_SUCCESSFULLY){adapter.refreshEvents(movieList);}
                    hideConnectionErrors();
                }else {
                    Toast.makeText(MainMovieActivity.this,
                            getResources().getText(R.string.no_connectivity_error_with_refresh_button),
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.favorite:
                setTitle(getResources().getText(R.string.favorites_movie_title));
                if(isNetworkAvailable()) {
                    loadFavoriteMovies();
                    SavePreferences();
                    hideConnectionErrors();
                }else {
                    Toast.makeText(MainMovieActivity.this,
                            getResources().getText(R.string.no_connectivity_error_with_refresh_button),
                            Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setActivityName(){

        if (CHOSEN_URL.equals(URL_POPULAR)){ setTitle(getResources().getText(R.string.popular_movie_title));}
        if (CHOSEN_URL.equals(URL_TOP_RATED)) {setTitle(getResources().getText(R.string.top_rated_movies_title));}
    }

    private void SavePreferences(){

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sorting", CHOSEN_URL);
        editor.apply();
    }
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        CHOSEN_URL = sharedPreferences.getString("sorting", CHOSEN_URL);
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void defineErrorViews(){
        ButterKnife.bind(this);
    }

    private void showConnectionErrors(){
        defineErrorViews();
        NO_INTERNET.setVisibility(View.VISIBLE);
        REFRESH.setVisibility(View.VISIBLE);
    }
    private void hideConnectionErrors(){
        defineErrorViews();
        NO_INTERNET.setVisibility(View.INVISIBLE);
        REFRESH.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Movie movie1 = movieList.get(clickedItemIndex);
        Intent intent = new Intent(context, DetailedMovieActivity.class);
        intent.putExtra(getResources().getString(R.string.KEY_MOVIE_ID), movie1.getId());
        intent.putExtra(getResources().getString(R.string.KEY_POSTER_PATH), movie1.getPosterPath());
        intent.putExtra(getResources().getString(R.string.KEY_TITLE), movie1.getTitle());
        intent.putExtra(getResources().getString(R.string.KEY_RELEASE_DATE), movie1.getReleaseDate());
        intent.putExtra(getResources().getString(R.string.KEY_RATE), movie1.getVote());
        intent.putExtra(getResources().getString(R.string.KEY_PLOT), movie1.getOverview());

        context.startActivity(intent);

    }

    public void loadFavoriteMovies() {
        adapter.refreshEvents(movieList);
        Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            if (!cursor.moveToNext()) {
                Toast.makeText(this, getResources().getText(R.string.no_movies_liked), Toast.LENGTH_SHORT).show();

            } else {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Movie movie = new Movie(
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)),
                            cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_AVERAGE_RATE)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW))
                    );
                    movieList.add(movie);
                }
                adapter = new MoviesAdapter(movieList, getApplicationContext(), this);
                recyclerView.setAdapter(adapter);
                POPULATED_UI_SUCCESSFULLY = true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

}
