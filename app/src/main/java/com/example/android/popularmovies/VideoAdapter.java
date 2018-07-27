package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private static final boolean shouldAttachToParentImmediately = false;
    private Video[] mVideos;
    public interface VideoAdapterOnClickHandler {
        void onListItemClick(Video video);
    }
    private VideoAdapterOnClickHandler mClickHandler;

    // data is passed into the constructor
    VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.video_item, parent, shouldAttachToParentImmediately);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder holder, int position) {
        Video video = mVideos[position];
        String name = video.getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mVideos == null) {
            return 0;
        }
        return mVideos.length;
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView name;
        public final ImageView movieIcon;

        public VideoAdapterViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.video_name);
            movieIcon = (ImageView) itemView.findViewById(R.id.movie_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onListItemClick(onItemClick(v, getAdapterPosition()));
        }

        // Convenience method for getting data click position
        public Video getItem(int id) {
            return mVideos[id];
        }

        public Video onItemClick(View v, int position) {
            return getItem(position);
        }
    }

    /**
     * This method is used to set the videos on a VideoAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new VideoAdapter to display it.
     *
     * @param videos The new videos data to be displayed.
     */
    public void setReviewData(Video[] videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }
}
