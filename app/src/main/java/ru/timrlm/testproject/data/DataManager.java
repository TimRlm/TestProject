package ru.timrlm.testproject.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.timrlm.testproject.data.local.PreferencesHelper;
import ru.timrlm.testproject.util.RxEventBus;

@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final RxEventBus mRxEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, RxEventBus rxEventBus) {
        mPreferencesHelper = preferencesHelper;
        mRxEventBus = rxEventBus;
    }

    public static Observable<Bitmap> getBitmapFromURL(String src) {
        return Observable.create((ObservableEmitter<Bitmap> obs) ->{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            obs.onNext(myBitmap);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}