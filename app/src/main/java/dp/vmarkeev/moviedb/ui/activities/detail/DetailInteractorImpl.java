package dp.vmarkeev.moviedb.ui.activities.detail;

import android.content.Context;

import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.api.NetworkApi;
import dp.vmarkeev.moviedb.api.RetrofitUtils;
import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.utils.LocaleHelper;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vmarkeev on 28.02.2017.
 */

class DetailInteractorImpl implements DetailInteractor {

    @Override
    public void getDetails(final Context context, String id, final OnDetailsListener onDetailsListener, final OnDetailsErrorListener onDetailsErrorListener) {

        NetworkApi networkApi = RetrofitUtils.createApi(NetworkApi.class);
        String api_key = context.getString(R.string.api_key);
        String language = LocaleHelper.getLanguage(context);

        networkApi.getDetails(id, api_key, language)
                .retry(Consts.NetworkConstant.RETRY_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.toString().contains(context.getString(R.string.err_no_internet))) {
                            onDetailsErrorListener.onDetailsError("");
                        } else {
                            onDetailsErrorListener.onDetailsError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DetailModel model) {
                        if (model != null) {
                            onDetailsListener.onDetailsResult(model);
                        }
                    }
                });
    }

    @Override
    public void getTrailers(final Context context, String id, final OnTrailersListener onTrailersListener, final OnTrailersErrorListener onTrailersErrorListener) {

        NetworkApi networkApi = RetrofitUtils.createApi(NetworkApi.class);
        String api_key = context.getString(R.string.api_key);
        String language = LocaleHelper.getLanguage(context);

        networkApi.getTrailers(id, api_key, language)
                .retry(Consts.NetworkConstant.RETRY_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrailerModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.toString().contains(context.getString(R.string.err_no_internet))) {
                            onTrailersErrorListener.onTrailersError("");
                        } else {
                            onTrailersErrorListener.onTrailersError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(TrailerModel model) {
                        if (model != null) {
                            onTrailersListener.onTrailersResult(model);
                        }
                    }
                });
    }
}
