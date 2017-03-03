package dp.vmarkeev.moviedb.ui.activities.detail;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseView;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailView extends BaseView {

    void onDetailsResult(DetailModel model);

    void onTrailersResult(TrailerModel model);

    void onTrailersError(String error);
}
