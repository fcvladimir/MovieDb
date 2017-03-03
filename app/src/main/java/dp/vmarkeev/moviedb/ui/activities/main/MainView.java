package dp.vmarkeev.moviedb.ui.activities.main;

import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseView;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface MainView extends BaseView {

    void onMovieResult(MovieModel model, boolean isLoading);

    void onLoadMoreListener(MovieModel model, boolean isLoading);

    void onFavoritesListener(MovieModel model);
}
