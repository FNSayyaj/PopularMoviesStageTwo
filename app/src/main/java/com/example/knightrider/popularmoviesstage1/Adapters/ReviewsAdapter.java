package com.example.knightrider.popularmoviesstage1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.knightrider.popularmoviesstage1.Models.Review;
import com.example.knightrider.popularmoviesstage1.R;
import java.util.List;

/**
 * Created by KNIGHT RIDER on 3/18/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final List<Review> reviews;
    private final Context context;


    public ReviewsAdapter(List<Review> reviews, Context context) {

        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_reviews_items, parent, false);

        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        final RelativeLayout relativeLayout;
        final TextView userName;
        final TextView userReview;
        final ImageView userImage;

        ViewHolder(View itemView) {

            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            userName = itemView.findViewById(R.id.userName);
            userReview = itemView.findViewById(R.id.userReview);
            userImage = itemView.findViewById(R.id.userImage);

        }

        void bind(int position) {

            final Review review = reviews.get(position);
            userName.setText(review.getAuthor());
            userReview.setText(review.getContent());
        }
    }
}


