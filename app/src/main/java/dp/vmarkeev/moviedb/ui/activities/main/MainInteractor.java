package dp.vmarkeev.moviedb.ui.activities.main;

import android.content.Context;

import dp.vmarkeev.moviedb.models.movies.MovieModel;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface MainInteractor {

    void getMovie(Context context, int page, int moviesType, OnMovieListener onMovieListener, OnErrorListener onErrorListener);

    interface OnMovieListener {
        void onMovieResult(MovieModel model, boolean isLoading);

        void onLoadMoreListener(MovieModel model, boolean isLoading);

        void onFavoritesListener(MovieModel model);
    }

    interface OnErrorListener {
        void onError(String error);
    }
}
