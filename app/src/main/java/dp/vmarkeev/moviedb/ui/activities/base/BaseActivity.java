package dp.vmarkeev.moviedb.ui.activities.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dp.vmarkeev.moviedb.utils.L;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log(getClass().getSimpleName());

        context = this;
        initLayout();
        initPresenter();
        initViews();
        initListeners();
    }

    private void initLayout() {
        View v = getContentView();
        setContentView(v);
    }

    protected abstract View getContentView();

    protected abstract void initPresenter();

    protected abstract void initViews();

    protected abstract void initListeners();

    protected void log(Object o) {
        L.d(o);
    }
}
