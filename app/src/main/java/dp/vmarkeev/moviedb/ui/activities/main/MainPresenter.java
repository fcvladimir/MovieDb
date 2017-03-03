package dp.vmarkeev.moviedb.ui.activities.main;

import android.content.Context;

import dp.vmarkeev.moviedb.ui.activities.base.BasePresenter;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface MainPresenter extends BasePresenter {

    void getMovie(Context context, int page, int moviesType);
}
