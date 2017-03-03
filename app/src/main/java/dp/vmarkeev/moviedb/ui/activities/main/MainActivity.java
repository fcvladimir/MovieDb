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

    // The minimum number of items remaining before we should loading more.
    private static final int VISIBLE_THRESHOLD = 5;
    private boolean mIsLoading = true;
    private int PAGE = 1;
    private int moviesType;

    private TextView noResults;
    private ImageView noConnection;

    private RecyclerView mRvMovies;
    private ProgressBar loading;
    private MovieAdapter adapter;
    private GridLayoutManager mLayoutManager;

    private MainPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLinearLayoutManager();
        adapter = new MovieAdapter(this);
        mRvMovies.setAdapter(adapter);
        moviesType = Consts.MovieConstant.POPULAR;
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
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        if (noResults != null) {
            noResults.setVisibility(View.GONE);
        }
        loading.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.action_show_popular:
                if (moviesType != Consts.MovieConstant.POPULAR) {
                    moviesType = Consts.MovieConstant.POPULAR;
                    PAGE = 1;
                    getMovies();
                    setMainTitle();
                }
                return true;
            case R.id.action_show_top_rated:
                if (moviesType != Consts.MovieConstant.TOP_RATED) {
                    moviesType = Consts.MovieConstant.TOP_RATED;
                    PAGE = 1;
                    getMovies();
                    setMainTitle();
                }
                return true;
            case R.id.action_show_favorites:
                if (moviesType != Consts.MovieConstant.FAVORITES) {
                    moviesType = Consts.MovieConstant.FAVORITES;
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
        loading = (ProgressBar) findViewById(android.R.id.empty);
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
        mPresenter.getMovie(context, PAGE, moviesType);
    }

    private void setMainTitle() {
        switch (moviesType) {
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
        loading.setVisibility(View.GONE);
        if (TextUtils.isEmpty(error)) {
            checkConnectivity();
        } else {
            setNoResultsVisibility(View.VISIBLE, error);
        }
    }

    private void checkConnectivity() {
        if (noConnection == null) {
            final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
            noConnection = (ImageView) stub.inflate();
            noConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMovies();
                }
            });
        } else {
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    private void setNoResultsVisibility(int visibility, String error) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                noResults.setOnClickListener(new View.OnClickListener() {
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
            noResults.setText(ssb);
        }
        if (noResults != null) {
            noResults.setVisibility(visibility);
        }
    }

    @Override
    public void onMovieResult(MovieModel model, boolean isLoading) {
        mIsLoading = isLoading;
        loading.setVisibility(View.GONE);
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        if (noResults != null) {
            noResults.setVisibility(View.GONE);
        }
        adapter.refresh(model.getResults());
    }

    @Override
    public void onLoadMoreListener(MovieModel model, boolean isLoading) {
        mIsLoading = isLoading;
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        adapter.addAndUpdate(model.getResults());
    }

    @Override
    public void onFavoritesListener(MovieModel model) {
        loading.setVisibility(View.GONE);
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        if (noResults != null) {
            noResults.setVisibility(View.GONE);
        }
        if (model.getResults().size() > 0) {
            adapter.refresh(model.getResults());
        } else {
            adapter.clearData();
            setNoResultsVisibility(View.VISIBLE, getString(R.string.err_no_favorite_movies));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
