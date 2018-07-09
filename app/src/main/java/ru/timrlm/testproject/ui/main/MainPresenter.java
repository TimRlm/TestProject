package ru.timrlm.testproject.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.data.DataManager;
import ru.timrlm.testproject.di.ActivityContext;
import ru.timrlm.testproject.di.ApplicationContext;
import ru.timrlm.testproject.di.ConfigPersistent;
import ru.timrlm.testproject.ui.base.BasePresenter;
import ru.timrlm.testproject.util.RxUtil;

@ConfigPersistent
class MainPresenter extends BasePresenter<MainMvpView> {
    private DataManager mDataManager;
    private Context mContext;
    private Disposable mDisposable;

    @Inject
    public MainPresenter(@ApplicationContext Context context, DataManager mDataManager) {
        this.mDataManager = mDataManager;
        mContext = context;
    }

    public void loadImg(String url){
        getMvpView().showProgressView();
        mDisposable = DataManager.getBitmapFromURL(url).subscribe(bitmap->{
            getMvpView().hideProgressView();
            getMvpView().setImageBitmap(bitmap);
        },t->{
            getMvpView().hideProgressView();
            getMvpView().setImageBitmap(null);
            getMvpView().showErrorView(mContext.getString(R.string.no_format));
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mDisposable);
    }
}
