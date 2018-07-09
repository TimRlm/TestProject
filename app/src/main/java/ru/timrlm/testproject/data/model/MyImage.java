package ru.timrlm.testproject.data.model;

import android.graphics.Bitmap;

public class MyImage {
    private Bitmap bitmap;
    private int progress;
    private int maxProgress;

    public MyImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.progress = 0;
        this.maxProgress = 0;
    }

    public MyImage(Bitmap bitmap, int maxProgress) {
        this.bitmap = bitmap;
        this.progress = 0;
        this.maxProgress = maxProgress;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
