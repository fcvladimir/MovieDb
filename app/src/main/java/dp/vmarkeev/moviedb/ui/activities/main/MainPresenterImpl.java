package dp.vmarkeev.moviedb.ui.activities.main;

import android.content.Context;

import dp.vmarkeev.moviedb.models.movies.MovieModel;

/**
 * Created by vmarkeev on 28.02.2017.
 */

class MainPresenterImpl implements MainPresenter,
        MainInteractor.OnMovieListener,
        MainInteractor.OnErrorListener {

    private MainView mView;
    private MainInteractorImpl mInteractor;

    MainPresenterImpl(MainView view) {
        mView = view;
        mInteractor = new MainInteractorImpl();
    }

    @Override
    public void getMovie(Context context, int page, int moviesType) {
        mInteractor.getMovie(context, page, moviesType, this, this);
    }

    @Override
    public void onMovieResult(MovieModel model, boolean isLoading) {
        if (mView != null) {
            mView.onMovieResult(model, isLoading);
        }
    }

    @Override
    public void onLoadMoreListener(MovieModel model, boolean isLoading) {
        if (mView != null) {
            mView.onLoadMoreListener(model, isLoading);
        }
    }

    @Override
    public void onFavoritesListener(MovieModel model) {
        if (mView != null) {
            mView.onFavoritesListener(model);
        }
    }

    @Override
    public void onError(String error) {
        if (mView != null) {
            mView.onError(error);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
