package com.example.knightrider.popularmoviesstage1.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.knightrider.popularmoviesstage1.Models.Movie;
import com.example.knightrider.popularmoviesstage1.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by KNIGHT RIDER on 3/10/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final  List<Movie> movies;
    private final Context context;
    private final ListMovieClickListener mOnClickListener;

    public interface ListMovieClickListener {

        void onListItemClick(int clickedItemIndex);

    }

    public MoviesAdapter(List<Movie> movies, Context context, ListMovieClickListener listener ){

        this.movies = movies;
        this.context = context;
        this.mOnClickListener = listener;

    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_movies_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, final int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView poster_path;
        final LinearLayout linearLayout;

        ViewHolder(View itemView) {

            super(itemView);
            poster_path = itemView.findViewById(R.id.poster_image);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {

            final Movie movie = movies.get(position);
            Picasso.with(context).load(context.getText(R.string.primary_poster_path) + movie.getPosterPath()).into(poster_path,
                    new Callback.EmptyCallback() {

                        @Override
                        public void onError() {

                            poster_path.setImageResource(R.drawable.ic_image_failed);
                        }
                    });
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void refreshEvents(List<Movie> movies) {

        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

}