package ru.timrlm.testproject.ui.main;

import android.graphics.Bitmap;
import android.widget.Button;

import java.util.List;

import ru.timrlm.testproject.data.model.MyImage;
import ru.timrlm.testproject.ui.base.MvpView;

interface MainMvpView extends MvpView {
    void setImageBitmap(Bitmap bitmap);
    void setImages(List<MyImage> images);
    int addImage(int maxProgress);
    void updImageProgress(int pos, int progress);
    void setImage(int pos,Bitmap bitmap, String path);
    void rmvImage(int pos);
}
