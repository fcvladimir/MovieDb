package dp.vmarkeev.moviedb.ui.activities.detail;

import android.content.Context;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailInteractor {

    void getDetails(Context context, String id, OnDetailsListener onDetailsListener, OnDetailsErrorListener onDetailsErrorListener);

    void getTrailers(Context context, String id, OnTrailersListener onTrailersListener, OnTrailersErrorListener onTrailersErrorListener);

    interface OnDetailsListener {
        void onDetailsResult(DetailModel model);
    }

    interface OnTrailersListener {
        void onTrailersResult(TrailerModel model);
    }

    interface OnDetailsErrorListener {
        void onDetailsError(String error);
    }

    interface OnTrailersErrorListener {
        void onTrailersError(String error);
    }
}
