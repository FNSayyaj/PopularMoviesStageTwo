package com.example.knightrider.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.knightrider.popularmoviesstage1.Adapters.TrailersAdapter;
import com.example.knightrider.popularmoviesstage1.DataBase.FavoriteMoviesContract;
import com.example.knightrider.popularmoviesstage1.Models.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.android.volley.Request.Method.GET;
import static com.example.knightrider.popularmoviesstage1.R.color.red_button;

public class DetailedMovieActivity extends AppCompatActivity implements TrailersAdapter.ListTrailerClickListener {

    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    String TRAILER_API_KEY = "/videos?api_key=PUT YOUR API KEY HERE";
    String FULL_VIDEO_URL = "";
    String YouTubeBaseUrl = "https://www.youtube.com/watch?v=";
    String FirstTrailerKEY = "";
    String voteAverage;
    String movieReleaseDate;
    String movieTitle;
    String movieID;
    String moviePlotSynopsis;
    String moviePosterPath = "";
    private final String ADDED_TAG = "favorited";
    private final String REMOVED_TAG = "unfavorited";
    private final Context context = this;
    TrailersAdapter trailersAdapter;
    List<String> movieId = new ArrayList<>();
    List<Trailer> trailerList;

    @BindView(R.id.movieTitle_tv)
    TextView title_tv;
    @BindView(R.id.releaseDate_tv)
    TextView releaseDate_tv;
    @BindView(R.id.plot_tv)
    TextView plot_tv;
    @BindView(R.id.vote_iv)
    TextView vote_iv;
    @BindView(R.id.progressBar_iv)
    ProgressBar progressBar;
    @BindView(R.id.moviePoster_iv)
    ImageView poster_iv;
    @BindView(R.id.trailersRecyclerView)
    RecyclerView trailerRecyclerView;
    @BindView(R.id.reviews)
    Button review;
    @BindView(R.id.movieTrailers)
    TextView movieTrailer;
    @BindView(R.id.floatingFavButton)
    FloatingActionButton floatingFavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);

        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent != null) {
            movieID = intent.getStringExtra(getResources().getString(R.string.KEY_MOVIE_ID));
            if (checkIfMovieExistsInFavorite()) {
                floatingFavButton.setTag(ADDED_TAG);
                floatingFavButton.setImageResource(R.drawable.btn_star_big_on);
            }
            movieTitle = intent.getStringExtra(getResources().getString(R.string.KEY_TITLE));
            movieReleaseDate = intent.getStringExtra(getResources().getString(R.string.KEY_RELEASE_DATE));
            moviePosterPath = intent.getStringExtra(getResources().getString(R.string.KEY_POSTER_PATH));
            moviePlotSynopsis = intent.getStringExtra(getResources().getString(R.string.KEY_PLOT));
            Double rate = 0.0;
            voteAverage = String.valueOf(intent.getDoubleExtra(getResources().getString(R.string.KEY_RATE), rate));
            voteAverage = voteAverage + " " + getResources().getText(R.string.vote_of_ten);
            String imagePosterLink = getResources().getText(R.string.primary_poster_path) + moviePosterPath;
            FULL_VIDEO_URL = BASE_URL + movieID + TRAILER_API_KEY;
            loadVideoData(FULL_VIDEO_URL);
            review.setOnClickListener(v -> {
                Intent intent1 = new Intent(context, ReviewActivity.class);
                intent1.putExtra(getResources().getString(R.string.KEY_MOVIE_ID), movieID);
                context.startActivity(intent1);

            });
            setTitle(null);
            title_tv.setText(movieTitle);
            plot_tv.setText(moviePlotSynopsis);
            vote_iv.setText(voteAverage);
            releaseDate_tv.setText(movieReleaseDate);
            Picasso.with(this).load(imagePosterLink).into(poster_iv, new Callback.EmptyCallback() {
                //make the progressbar disappear when the image is done loading
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.INVISIBLE);
                    poster_iv.setImageResource(R.drawable.ic_image_failed);
                }
            });
            floatingFavButton.setOnClickListener(v -> {

                FloatingActionButton fab = (FloatingActionButton) v;
                String fabTag = (String) fab.getTag();

                if (fabTag.equals(REMOVED_TAG)) {
                    floatingFavButton.setImageResource(R.drawable.btn_star_big_on);
                    fab.setTag(ADDED_TAG);
                    addMovieToFavorites();
                } else if (fabTag.equals(ADDED_TAG)) {
                    floatingFavButton.setImageResource(R.drawable.btn_star_big_off);
                    fab.setTag(REMOVED_TAG);
                    deleteMovieFromFavorites();
                }
            });
        }
    }

    private void loadVideoData(String url){

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.optJSONArray(getResources().getString(R.string.KEY_RESULTS));
                for (int i = 0; i < results.length(); i++){
                    if(i == 0){
                        JSONObject jo = results.optJSONObject(i);
                        Trailer trailer = new Trailer(jo.optString(getResources().getString(R.string.KEY_TRAILER_NAME)),jo.optString(getResources().getString(R.string.KEY_TRAILER_KEY)));
                        FirstTrailerKEY = trailer.getKey();
                    }
                    JSONObject jo = results.optJSONObject(i);
                    Trailer trailer = new Trailer(jo.optString(getResources().getString(R.string.KEY_TRAILER_NAME)),jo.optString(getResources().getString(R.string.KEY_TRAILER_KEY)));
                    trailerList.add(trailer);
                }
                if(trailerList.isEmpty()){
                    movieTrailer.setTextColor(getResources().getColor(red_button));
                    movieTrailer.setText(getResources().getText(R.string.no_trailers_error));
                }else {
                    movieTrailer.setText(getResources().getText(R.string.movie_trailer_tv));
                    trailersAdapter = new TrailersAdapter(trailerList, getApplicationContext(), this);
                    trailerRecyclerView.setAdapter(trailersAdapter);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this,
                getResources().getText(R.string.no_connectivity_error),
                Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Trailer trailer1 = trailerList.get(clickedItemIndex);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer1.getKey()));
        startActivity(appIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.share && FirstTrailerKEY.length() > 0) {
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType("text/plain")
                    .setChooserTitle("Share Movie 1st Trailer")
                    .setText("Hey, I recommend watching this movie:" + YouTubeBaseUrl + FirstTrailerKEY)
                    .startChooser();
        } else {
            item.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMovieToFavorites() {

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movieID);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, movieTitle);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, moviePosterPath);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_AVERAGE_RATE, voteAverage);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, moviePlotSynopsis);
        Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, cv);
    }

    private void deleteMovieFromFavorites() {

        String selection = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieID)};
        getContentResolver().delete(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, selection, selectionArgs);
    }

    private boolean checkIfMovieExistsInFavorite() {

        Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        assert cursor != null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
            movieId.add(id);
        }
        if (cursor.moveToFirst()) {
            if (movieId.contains(movieID)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }
}
