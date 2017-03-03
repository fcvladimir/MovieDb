package dp.vmarkeev.moviedb.ui.activities.detail;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dp.vmarkeev.moviedb.Config;
import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.db.DBHelper;
import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseActivity;
import dp.vmarkeev.moviedb.ui.adapters.TrailerAdapter;
import dp.vmarkeev.moviedb.ui.views.DividerItemDecoration;
import dp.vmarkeev.moviedb.utils.DialogUtils;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class DetailActivity extends BaseActivity implements DetailView, View.OnClickListener, TrailerAdapter.OnTrailerClickListener {

    private String mId;
    private DetailModel mDetailModel;

    private ImageView mNoConnection;
    private TextView mNoResults;
    private ProgressBar mLoading;
    private ScrollView mSvMovieDetail;

    private TextView mTvMovieDetailName;
    private TextView mTvMovieDetailSynopsis;
    private TextView mTvMovieDetailYear;
    private TextView mTvMovieDetailDuration;
    private TextView mTvMovieDetailRating;
    private TextView mTvMovieDetailCheckTrailers;
    private ImageView mIvMovieDetailPoster;
    private RecyclerView mRvMovieDetailTrailer;

    private DetailPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showBackButton();
        getDataFromIntent();
        getDetail();
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_detail, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getDataFromIntent() {
        mId = getIntent().getStringExtra(Consts.IntentConstant.ID);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenterImpl(this);
    }

    @Override
    protected void initViews() {
        mLoading = (ProgressBar) findViewById(android.R.id.empty);
        mSvMovieDetail = (ScrollView) findViewById(R.id.sv_movie_detail);
        mTvMovieDetailName = (TextView) findViewById(R.id.tv_movie_detail_name);
        mTvMovieDetailSynopsis = (TextView) findViewById(R.id.tv_movie_detail_synopsis);
        mIvMovieDetailPoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        mTvMovieDetailYear = (TextView) findViewById(R.id.tv_movie_detail_year);
        mTvMovieDetailDuration = (TextView) findViewById(R.id.tv_movie_detail_duration);
        mTvMovieDetailRating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        mTvMovieDetailCheckTrailers = (TextView) findViewById(R.id.tv_movie_detail_check_trailers);
        mRvMovieDetailTrailer = (RecyclerView) findViewById(R.id.rv_movie_detail_trailer);
    }

    @Override
    protected void initListeners() {
        mTvMovieDetailCheckTrailers.setOnClickListener(this);
        findViewById(R.id.btn_movie_detail_mark_as_favorite).setOnClickListener(this);
    }

    private void getDetail() {
        mPresenter.getDetails(context, mId);
    }

    @Override
    public void onError(String error) {
        mLoading.setVisibility(View.GONE);
        if (mSvMovieDetail.getVisibility() == View.GONE) {
            if (TextUtils.isEmpty(error)) {
                checkConnectivity();
            } else {
                setNoResultsVisibility(View.VISIBLE, error);
            }
        } else {
            if (TextUtils.isEmpty(error)) {
                DialogUtils.show(context, R.string.err_no_internet_error);
            } else {
                DialogUtils.show(context, error);
            }
        }
    }

    private void checkConnectivity() {
        if (mNoConnection == null) {
            final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
            mNoConnection = (ImageView) stub.inflate();
            mNoConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetail();
                    mLoading.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDetailsResult(DetailModel model) {
        mSvMovieDetail.setVisibility(View.VISIBLE);
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        if (mNoResults != null) {
            mNoResults.setVisibility(View.GONE);
        }
        mLoading.setVisibility(View.GONE);
        fillViews(model);
    }

    @Override
    public void onTrailersResult(TrailerModel model) {
        mTvMovieDetailCheckTrailers.setVisibility(View.GONE);
        initLinearLayoutManager();
        TrailerAdapter trailerAdapter = new TrailerAdapter(this);
        mRvMovieDetailTrailer.setHasFixedSize(true);
        mRvMovieDetailTrailer.setAdapter(trailerAdapter);
        trailerAdapter.refresh(model.getResults());
    }

    public void initLinearLayoutManager() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRvMovieDetailTrailer.setLayoutManager(mLayoutManager);
        mRvMovieDetailTrailer.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onTrailersError(String error) {
        DialogUtils.show(context, error);
    }

    private void fillViews(DetailModel model) {
        mDetailModel = model;
        mTvMovieDetailName.setText(model.getTitle());
        mTvMovieDetailSynopsis.setText(model.getOverview());
        mTvMovieDetailYear.setText(model.getRelease_date());
        mTvMovieDetailDuration.setText(getString(R.string.dt_movie_detail_duration, model.getRuntime()));
        mTvMovieDetailRating.setText(getString(R.string.dt_movie_detail_vote, model.getVote_average()));

        Picasso.with(context)
                .load(Config.IMAGE_URL + model.getPoster_path())
                .into(mIvMovieDetailPoster);
    }

    private void setNoResultsVisibility(int visibility, String error) {
        if (visibility == View.VISIBLE) {
            if (mNoResults == null) {
                mNoResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                mNoResults.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDetail();
                        mLoading.setVisibility(View.VISIBLE);
                    }
                });
            }
            String message =
                    getString(R.string.no_results);
            if (error != null) {
                message = error;
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder(message);
            ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNoResults.setText(ssb);
        }
        if (mNoResults != null) {
            mNoResults.setVisibility(visibility);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_movie_detail_check_trailers:
                mPresenter.getTrailers(context, mId);
                break;
            case R.id.btn_movie_detail_mark_as_favorite:
                DBHelper dbHelper = new DBHelper(context);
                boolean isAdded = dbHelper.addMovie(mId, mDetailModel.getPoster_path());
                DialogUtils.show(context, isAdded ? R.string.dt_movie_detail_mark_as_favorite_success : R.string.dt_movie_detail_mark_as_favorite_failure);
                break;
        }
    }

    @Override
    public void onTrailerClick(String position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.YOUTUBE_URL + position)));
    }
}
