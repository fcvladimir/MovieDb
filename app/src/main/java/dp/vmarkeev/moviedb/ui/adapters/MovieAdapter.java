package dp.vmarkeev.moviedb.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dp.vmarkeev.moviedb.Config;
import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.models.movies.Results;
import dp.vmarkeev.moviedb.ui.activities.detail.DetailActivity;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Results> mItems;
    private Activity mContext;

    public MovieAdapter(Activity context) {
        mItems = new ArrayList<>();
        mContext = context;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Picasso.with(mContext)
                .load(Config.IMAGE_URL + mItems.get(position).getPoster_path())
                .into(holder.mIvMoviePoster);

        holder.mIvMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Consts.IntentConstant.ID, mItems.get(position).getId());

                Pair<View, String> pair1 = Pair.create((View) holder.mIvMoviePoster, mContext.getString(R.string.transition_pare_1));

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mContext, pair1);
                mContext.startActivity(intent, options.toBundle());
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

    public void addAndUpdate(List<? extends Results> newItems) {
        add(newItems);
        notifyDataSetChanged();
    }

    private void add(List<? extends Results> newItems) {
        for (Results newItem : newItems) {
            add(newItem);
        }
    }

    private void add(Results item) {
        mItems.add(item);
    }

    public void clearData() {
        mItems.clear(); //clear list
        notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }

    /**
     * Class to hold recycleView items.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvMoviePoster;

        private ViewHolder(View itemView) {
            super(itemView);
            mIvMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
