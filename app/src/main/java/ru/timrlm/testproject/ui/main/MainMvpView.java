package ru.timrlm.testproject.ui.main;

import android.graphics.Bitmap;

import ru.timrlm.testproject.ui.base.MvpView;

interface MainMvpView extends MvpView {
    void setImageBitmap(Bitmap bitmap);
}
