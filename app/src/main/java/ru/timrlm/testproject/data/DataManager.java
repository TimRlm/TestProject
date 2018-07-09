package ru.timrlm.testproject.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.timrlm.testproject.data.local.PreferencesHelper;
import ru.timrlm.testproject.data.model.MyImage;
import ru.timrlm.testproject.di.ApplicationContext;
import ru.timrlm.testproject.util.RxEventBus;

import static android.content.ContentValues.TAG;

@Singleton
public class DataManager {
    private final PreferencesHelper mPreferencesHelper;
    private final RxEventBus mRxEventBus;
    private Context mContext;

    @Inject
    public DataManager(@ApplicationContext Context context, PreferencesHelper preferencesHelper, RxEventBus rxEventBus) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mRxEventBus = rxEventBus;
    }

    public Observable<Bitmap> getBitmapFromURL(String src) {
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

    public Observable<String> addBitmap(Bitmap bmp) {
        return Observable.create((ObservableEmitter<String> obs) ->{
            getFolder();
            Calendar calendar = Calendar.getInstance();
            String mImageName="MI_"+ calendar.getTimeInMillis() +".jpg";
            File file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "images" + File.separator + mImageName);
            FileOutputStream fOut  = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
            fOut.flush();
            fOut.close();
            obs.onNext(file.getAbsolutePath());
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Boolean> deleteBitmap(String path) {
        return Observable.create((ObservableEmitter<Boolean> obs) ->{
            File file = new File(path);
            file.delete();
            obs.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Single<List<MyImage>> getAllBitmaps(){
        return Observable.create((ObservableEmitter<MyImage> obs) -> {
            File folder = getFolder();
            if (folder.exists()) {
                File[] allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jpg");
                    }
                });
                for(File file : allFiles) obs.onNext(new MyImage(getBitmap(file.getAbsolutePath()),file.getAbsolutePath()));
            }
            obs.onComplete();
        }).toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Bitmap getBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math
                .abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);
        return output;
    }

    private  File getFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "images");
        if (!folder.exists()) { folder.mkdirs(); }
       return folder;
    }
}