package com.example.knightrider.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.knightrider.popularmoviesstage1.Adapters.ReviewsAdapter;
import com.example.knightrider.popularmoviesstage1.Models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ReviewActivity extends AppCompatActivity {

    String REVIEW_API_KEY = "/reviews?api_key=b36df342389b0e125bea096556b9a60a";
    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    String FULL_REVIEW_URL = "";
    List<Review> reviewList;
    ReviewsAdapter reviewsAdapter;
    RecyclerView reviewRecyclerView;
    TextView noReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setTitle("User Reviews");

        noReviews = findViewById(R.id.noReview);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewList = new ArrayList<>();




        Intent intent = getIntent();
        if (intent != null){
            String movieID = intent.getStringExtra(getResources().getString(R.string.KEY_MOVIE_ID));
            FULL_REVIEW_URL = BASE_URL + movieID + REVIEW_API_KEY;
            loadReviewData(FULL_REVIEW_URL);
        }

    }

    private void loadReviewData(String url){

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.optJSONArray(getResources().getString(R.string.KEY_RESULTS));
                for (int i = 0; i < results.length(); i++){
                    JSONObject jo = results.optJSONObject(i);
                    Review review = new Review(jo.optString(getResources().getString(R.string.KEY_REVIEW_AUTHOR)),jo.optString(getResources().getString(R.string.KEY_REVIEW_CONTENT)));
                    reviewList.add(review);
                }
                if(reviewList.isEmpty()){
                    noReviews.setText(getResources().getText(R.string.no_reviews_error));
                    noReviews.setVisibility(View.VISIBLE);
                }else {
                    reviewsAdapter = new ReviewsAdapter(reviewList, getApplicationContext());
                    reviewRecyclerView.setAdapter(reviewsAdapter);
                    noReviews.setVisibility(View.INVISIBLE);
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
}
