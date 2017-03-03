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

    private String id;
    private DetailModel detailModel;

    private ImageView noConnection;
    private TextView noResults;
    private ProgressBar loading;
    private ScrollView sv_movie_detail;

    private TextView tv_movie_detail_name;
    private TextView tv_movie_detail_synopsis;
    private TextView tv_movie_detail_year;
    private TextView tv_movie_detail_duration;
    private TextView tv_movie_detail_rating;
    private TextView tv_movie_detail_check_trailers;
    private ImageView iv_movie_detail_poster;
    private RecyclerView rv_movie_detail_trailer;

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
        id = getIntent().getStringExtra(Consts.IntentConstant.ID);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenterImpl(this);
    }

    @Override
    protected void initViews() {
        loading = (ProgressBar) findViewById(android.R.id.empty);
        sv_movie_detail = (ScrollView) findViewById(R.id.sv_movie_detail);
        tv_movie_detail_name = (TextView) findViewById(R.id.tv_movie_detail_name);
        tv_movie_detail_synopsis = (TextView) findViewById(R.id.tv_movie_detail_synopsis);
        iv_movie_detail_poster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        tv_movie_detail_year = (TextView) findViewById(R.id.tv_movie_detail_year);
        tv_movie_detail_duration = (TextView) findViewById(R.id.tv_movie_detail_duration);
        tv_movie_detail_rating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        tv_movie_detail_check_trailers = (TextView) findViewById(R.id.tv_movie_detail_check_trailers);
        rv_movie_detail_trailer = (RecyclerView) findViewById(R.id.rv_movie_detail_trailer);
    }

    @Override
    protected void initListeners() {
        tv_movie_detail_check_trailers.setOnClickListener(this);
        findViewById(R.id.btn_movie_detail_mark_as_favorite).setOnClickListener(this);
    }

    private void getDetail() {
        mPresenter.getDetails(context, id);
    }

    @Override
    public void onError(String error) {
        loading.setVisibility(View.GONE);
        if (sv_movie_detail.getVisibility() == View.GONE) {
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
        if (noConnection == null) {
            final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
            noConnection = (ImageView) stub.inflate();
            noConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetail();
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDetailsResult(DetailModel model) {
        sv_movie_detail.setVisibility(View.VISIBLE);
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        if (noResults != null) {
            noResults.setVisibility(View.GONE);
        }
        loading.setVisibility(View.GONE);
        fillViews(model);
    }

    @Override
    public void onTrailersResult(TrailerModel model) {
        tv_movie_detail_check_trailers.setVisibility(View.GONE);
        initLinearLayoutManager();
        TrailerAdapter trailerAdapter = new TrailerAdapter(this);
        rv_movie_detail_trailer.setHasFixedSize(true);
        rv_movie_detail_trailer.setAdapter(trailerAdapter);
        trailerAdapter.refresh(model.getResults());
    }

    public void initLinearLayoutManager() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv_movie_detail_trailer.setLayoutManager(mLayoutManager);
        rv_movie_detail_trailer.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onTrailersError(String error) {
        DialogUtils.show(context, error);
    }

    private void fillViews(DetailModel model) {
        detailModel = model;
        tv_movie_detail_name.setText(model.getTitle());
        tv_movie_detail_synopsis.setText(model.getOverview());
        tv_movie_detail_year.setText(model.getRelease_date());
        tv_movie_detail_duration.setText(getString(R.string.dt_movie_detail_duration, model.getRuntime()));
        tv_movie_detail_rating.setText(getString(R.string.dt_movie_detail_vote, model.getVote_average()));

        Picasso.with(context)
                .load(Config.IMAGE_URL + model.getPoster_path())
                .into(iv_movie_detail_poster);
    }

    private void setNoResultsVisibility(int visibility, String error) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                noResults.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDetail();
                        loading.setVisibility(View.VISIBLE);
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
            noResults.setText(ssb);
        }
        if (noResults != null) {
            noResults.setVisibility(visibility);
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
                mPresenter.getTrailers(context, id);
                break;
            case R.id.btn_movie_detail_mark_as_favorite:
                DBHelper dbHelper = new DBHelper(context);
                boolean isAdded = dbHelper.addMovie(id, detailModel.getPoster_path());
                DialogUtils.show(context, isAdded ? R.string.dt_movie_detail_mark_as_favorite_success : R.string.dt_movie_detail_mark_as_favorite_failure);
                break;
        }
    }

    @Override
    public void onTrailerClick(String position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.YOUTUBE_URL + position)));
    }
}
