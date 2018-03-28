package com.example.knightrider.popularmoviesstage1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.knightrider.popularmoviesstage1.R;
import com.example.knightrider.popularmoviesstage1.Models.Trailer;

import java.util.List;

/**
 * Created by KNIGHT RIDER on 3/18/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private final List<Trailer> trailers;
    private final Context context;
    private final ListTrailerClickListener mOnClickListener;

    public interface ListTrailerClickListener {

        void onListItemClick(int clickedItemIndex);
    }

    public TrailersAdapter(List<Trailer> trailers, Context context, ListTrailerClickListener listener){

        this.trailers = trailers;
        this.context = context;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_trailers_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.ViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {return trailers.size();}

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        final RelativeLayout relativeLayout;
        final TextView trailerName;
        final ImageButton trailerVideo;

        ViewHolder(View itemView) {

            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            trailerName = itemView.findViewById(R.id.trailerName);
            trailerVideo = itemView.findViewById(R.id.playTrailer);

        }

        void bind(int position) {

            final Trailer trailer = trailers.get(position);
            trailerName.setText(trailer.getName());
            trailerVideo.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }


}
