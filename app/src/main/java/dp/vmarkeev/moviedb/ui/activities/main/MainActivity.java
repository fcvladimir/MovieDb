package dp.vmarkeev.moviedb.ui.activities.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseActivity;
import dp.vmarkeev.moviedb.ui.adapters.MovieAdapter;
import dp.vmarkeev.moviedb.ui.views.ItemOffsetDecorationGrid;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class MainActivity extends BaseActivity implements MainView {

    // The minimum number of items remaining before we should mLoading more.
    private static final int VISIBLE_THRESHOLD = 5;
    private boolean mIsLoading = true;
    private int PAGE = 1;
    private int mMoviesType;

    private TextView mNoResults;
    private ImageView mNoConnection;

    private RecyclerView mRvMovies;
    private ProgressBar mLoading;
    private MovieAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private MainPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLinearLayoutManager();
        mAdapter = new MovieAdapter(this);
        mRvMovies.setAdapter(mAdapter);
        mMoviesType = Consts.MovieConstant.POPULAR;
        getMovies();
        setMainTitle();
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_main, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        if (mNoResults != null) {
            mNoResults.setVisibility(View.GONE);
        }
        mLoading.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.action_show_popular:
                if (mMoviesType != Consts.MovieConstant.POPULAR) {
                    mMoviesType = Consts.MovieConstant.POPULAR;
                    PAGE = 1;
                    getMovies();
                    setMainTitle();
                }
                return true;
            case R.id.action_show_top_rated:
                if (mMoviesType != Consts.MovieConstant.TOP_RATED) {
                    mMoviesType = Consts.MovieConstant.TOP_RATED;
                    PAGE = 1;
                    getMovies();
                    setMainTitle();
                }
                return true;
            case R.id.action_show_favorites:
                if (mMoviesType != Consts.MovieConstant.FAVORITES) {
                    mMoviesType = Consts.MovieConstant.FAVORITES;
                    getMovies();
                    setMainTitle();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initLinearLayoutManager() {
        int column = getResources().getInteger(R.integer.num_columns);
        mLayoutManager = new GridLayoutManager(context, column);
        mRvMovies.setLayoutManager(mLayoutManager);
        ItemOffsetDecorationGrid itemDecoration = new ItemOffsetDecorationGrid(context, R.dimen.padding_card_view);
        mRvMovies.addItemDecoration(itemDecoration);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void initViews() {
        mRvMovies = (RecyclerView) findViewById(R.id.rv_movie);
        mLoading = (ProgressBar) findViewById(android.R.id.empty);
    }

    @Override
    protected void initListeners() {
        mRvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 || !mIsLoading) return;
                final int visibleItemCount = recyclerView.getChildCount();
                final int totalItemCount = mLayoutManager.getItemCount();
                final int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    mIsLoading = false;
                    PAGE++;
                    getMovies();
                }
            }
        });
    }

    private void getMovies() {
        mPresenter.getMovie(context, PAGE, mMoviesType);
    }

    private void setMainTitle() {
        switch (mMoviesType) {
            case Consts.MovieConstant.POPULAR:
                setTitle(R.string.mn_title_popular_movies);
                break;
            case Consts.MovieConstant.TOP_RATED:
                setTitle(R.string.mn_title_top_rated_movies);
                break;
            case Consts.MovieConstant.FAVORITES:
                setTitle(R.string.mn_title_favorites_movies);
                break;
        }
    }

    @Override
    public void onError(String error) {
        mLoading.setVisibility(View.GONE);
        if (TextUtils.isEmpty(error)) {
            checkConnectivity();
        } else {
            setNoResultsVisibility(View.VISIBLE, error);
        }
    }

    private void checkConnectivity() {
        if (mNoConnection == null) {
            final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
            mNoConnection = (ImageView) stub.inflate();
            mNoConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMovies();
                }
            });
        } else {
            mNoConnection.setVisibility(View.VISIBLE);
        }
    }

    private void setNoResultsVisibility(int visibility, String error) {
        if (visibility == View.VISIBLE) {
            if (mNoResults == null) {
                mNoResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                mNoResults.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PAGE = 1;
                        getMovies();
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
    public void onMovieResult(MovieModel model, boolean isLoading) {
        mIsLoading = isLoading;
        mLoading.setVisibility(View.GONE);
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        if (mNoResults != null) {
            mNoResults.setVisibility(View.GONE);
        }
        mAdapter.refresh(model.getResults());
    }

    @Override
    public void onLoadMoreListener(MovieModel model, boolean isLoading) {
        mIsLoading = isLoading;
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        mAdapter.addAndUpdate(model.getResults());
    }

    @Override
    public void onFavoritesListener(MovieModel model) {
        mLoading.setVisibility(View.GONE);
        if (mNoConnection != null) {
            mNoConnection.setVisibility(View.GONE);
        }
        if (mNoResults != null) {
            mNoResults.setVisibility(View.GONE);
        }
        if (model.getResults().size() > 0) {
            mAdapter.refresh(model.getResults());
        } else {
            mAdapter.clearData();
            setNoResultsVisibility(View.VISIBLE, getString(R.string.err_no_favorite_movies));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
