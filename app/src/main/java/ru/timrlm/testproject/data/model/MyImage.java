package ru.timrlm.testproject.data.model;

import android.graphics.Bitmap;

public class MyImage {
    private String path;
    private Bitmap bitmap;
    private int progress;
    private int maxProgress;

    public MyImage(Bitmap bitmap, String path) {
        this.bitmap = bitmap;
        this.progress = 0;
        this.maxProgress = 0;
        this.path = path;
    }

    public MyImage(int maxProgress) {
        this.bitmap = null;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
