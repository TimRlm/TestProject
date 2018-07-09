package ru.timrlm.testproject.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.data.DataManager;
import ru.timrlm.testproject.di.ActivityContext;
import ru.timrlm.testproject.di.ApplicationContext;
import ru.timrlm.testproject.di.ConfigPersistent;
import ru.timrlm.testproject.ui.base.BasePresenter;
import ru.timrlm.testproject.util.BitmapUtil;
import ru.timrlm.testproject.util.RxUtil;

@ConfigPersistent
class MainPresenter extends BasePresenter<MainMvpView> {
    private DataManager mDataManager;
    private Context mContext;
    private Disposable mDisposable;
    private List<Disposable> mDisposables = new ArrayList<>();

    @Inject
    public MainPresenter(@ApplicationContext Context context, DataManager mDataManager) {
        this.mDataManager = mDataManager;
        mContext = context;
    }

    public void loadImages(){
        getMvpView().setImages(new ArrayList<>());
    }

    public void mirrorBitmap(Bitmap bmp){ doIt(bmp,0); }

    public void monochrome(Bitmap bmp){doIt(bmp,1);}

    public void rotate(Bitmap bmp) {doIt(bmp,2);}

    //type = 0 - mirrorBitmap
    //type = 1 - monochrome
    //type = 2 - rotate
    private void doIt(Bitmap bmp, int type){
        int time = new Random().nextInt(25) + 5;
        int pos = getMvpView().addImage(time);
        mDisposables.add(Observable.interval(1, TimeUnit.SECONDS)
            .takeWhile(i -> i < time )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(i -> {
                if (i + 1 != time) {
                    getMvpView().updImageProgress(pos,i.intValue()+1);
                }else{
                    switch (type){
                        case 0: getMvpView().setImage(pos,BitmapUtil.mirrorBitmap(bmp)); break;
                        case 1: getMvpView().setImage(pos,BitmapUtil.monochrome(bmp)); break;
                        case 2: getMvpView().setImage(pos,BitmapUtil.rotate(bmp,90)); break;
                    }
                }
            },Throwable::printStackTrace));
    }

    public void loadImg(String url){
        getMvpView().showProgressView();
        mDisposable = mDataManager.getBitmapFromURL(url).subscribe(bitmap->{
            getMvpView().hideProgressView();
            getMvpView().setImageBitmap(bitmap);
        },t->{
            getMvpView().hideProgressView();
            getMvpView().setImageBitmap(null);
            getMvpView().showErrorView(t.getMessage());
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mDisposable);
        for (Disposable d : mDisposables) RxUtil.dispose(d);
    }
}
