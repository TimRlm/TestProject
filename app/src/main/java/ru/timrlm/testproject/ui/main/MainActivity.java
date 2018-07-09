package ru.timrlm.testproject.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import ru.timrlm.testproject.R;
import ru.timrlm.testproject.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {
    @Inject MainPresenter mPresenter;

    @Override
    public void init() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public int setlayout() { return R.layout.activity_main; }
}
