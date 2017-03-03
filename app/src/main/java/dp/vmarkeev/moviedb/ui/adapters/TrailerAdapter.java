package dp.vmarkeev.moviedb.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.models.trailers.Results;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Results> mItems;
    private OnTrailerClickListener onTrailerClickListener;

    public TrailerAdapter(OnTrailerClickListener onTrailerClickListener) {
        mItems = new ArrayList<>();
        this.onTrailerClickListener = onTrailerClickListener;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.iv_movie_trailer.setText(mItems.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrailerClickListener.onTrailerClick(mItems.get(position).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getDataItemCount();
    }

    private int getDataItemCount() {
        return mItems.size();
    }

    public void refresh(List<Results> ordersList) {
        mItems = ordersList;
        notifyDataSetChanged();
    }

    public interface OnTrailerClickListener {
        void onTrailerClick(String position);
    }

    /**
     * Class to hold recycleView items.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView iv_movie_trailer;

        private ViewHolder(View itemView) {
            super(itemView);
            iv_movie_trailer = (TextView) itemView.findViewById(R.id.iv_movie_trailer);
        }
    }
}
