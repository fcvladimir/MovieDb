package dp.vmarkeev.moviedb.ui.activities.main;

import android.content.Context;

import java.util.ArrayList;

import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.api.NetworkApi;
import dp.vmarkeev.moviedb.api.RetrofitUtils;
import dp.vmarkeev.moviedb.db.DBHelper;
import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.models.movies.Results;
import dp.vmarkeev.moviedb.utils.LocaleHelper;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vmarkeev on 28.02.2017.
 */

class MainInteractorImpl implements MainInteractor {

    @Override
    public void getMovie(final Context context, final int page, int moviesType, final OnMovieListener onMovieListener, final OnErrorListener onErrorListener) {

        NetworkApi networkApi = RetrofitUtils.createApi(NetworkApi.class);
        String api_key = context.getString(R.string.api_key);
        String language = LocaleHelper.getLanguage(context);

        switch (moviesType) {
            case Consts.MovieConstant.POPULAR:
                networkApi.getPopular(api_key, language, page)
                        .retry(Consts.NetworkConstant.RETRY_COUNT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<MovieModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e.toString().contains(context.getString(R.string.err_no_internet))) {
                                    onErrorListener.onError("");
                                } else {
                                    onErrorListener.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(MovieModel model) {
                                if (model != null) {

                                    boolean isLoading = page != model.getTotal_pages();

                                    if (page == 1) {
                                        onMovieListener.onMovieResult(model, isLoading);
                                    } else {
                                        onMovieListener.onLoadMoreListener(model, isLoading);
                                    }
                                }
                            }
                        });
                break;
            case Consts.MovieConstant.TOP_RATED:
                networkApi.getTopRated(api_key, language, page)
                        .retry(Consts.NetworkConstant.RETRY_COUNT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<MovieModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e.toString().contains(context.getString(R.string.err_no_internet))) {
                                    onErrorListener.onError("");
                                } else {
                                    onErrorListener.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(MovieModel model) {
                                if (model != null) {

                                    boolean isLoading = page != model.getTotal_pages();

                                    if (page == 1) {
                                        onMovieListener.onMovieResult(model, isLoading);
                                    } else {
                                        onMovieListener.onLoadMoreListener(model, isLoading);
                                    }
                                }
                            }
                        });
                break;
            case Consts.MovieConstant.FAVORITES:
                DBHelper dbHelper = new DBHelper(context);
                ArrayList<Results> resultses = dbHelper.getAllPlaylist();
                MovieModel model = new MovieModel();
                model.setResults(resultses);
                onMovieListener.onFavoritesListener(model);
                break;
        }
    }
}
