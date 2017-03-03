package dp.vmarkeev.moviedb.ui.activities.detail;

import android.content.Context;

import dp.vmarkeev.moviedb.ui.activities.base.BasePresenter;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailPresenter extends BasePresenter {

    void getDetails(Context context, String id);

    void getTrailers(Context context, String id);
}
