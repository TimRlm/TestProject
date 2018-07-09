package ru.timrlm.testproject.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.ui.base.BaseActivity;
import ru.timrlm.testproject.util.BitmapUtil;

public class MainActivity extends BaseActivity implements MainMvpView {
    @Inject MainPresenter mPresenter;
    @BindView(R.id.main_img) AppCompatImageView mImgView;
    @BindView(R.id.main_rv) RecyclerView mRv;
    private final int IMAGE_RESULT = 1;
    private final int GALLARY_RESULT = 2;
    private Bitmap mActualBitmap;

    @Override
    public void init() {

    }

    @OnClick(R.id.main_rotate)
    public void rotate() {
        mActualBitmap = BitmapUtil.rotate(mActualBitmap,90);
        mImgView.setImageBitmap(mActualBitmap);
    }

    @OnClick(R.id.main_invert)
    public void invert() {
        mActualBitmap = BitmapUtil.monochrome(mActualBitmap);
        mImgView.setImageBitmap(mActualBitmap);
    }

    @OnClick(R.id.main_mirror)
    public void mirror() {
        mActualBitmap = BitmapUtil.mirrorBitmap(mActualBitmap);
        mImgView.setImageBitmap(mActualBitmap);
    }

    @OnClick(R.id.main_img)
    public void fromUri() {
        new MaterialDialog.Builder(this)
                .title(R.string.input_uri)
                .input("", "", (dialog, input) -> mPresenter.loadImg(input.toString()))
                .show();
    }

    @OnClick(R.id.choose_img)
    public void choose() {
        MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter((d,index,item)->{
            if (index == 0){ chooseFromCamera(); }else{ chooseFromGalary(); }
            d.dismiss();
        });
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(R.string.choose_camera)
                .icon(android.R.drawable.ic_menu_camera)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(R.string.choose_gallery)
                .icon(android.R.drawable.ic_menu_gallery)
                .build());
        new MaterialDialog.Builder(this)
                .adapter(adapter,null)
                .show();
    }

    private void chooseFromCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_RESULT);
        }
    }
    private void chooseFromGalary(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLARY_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK) {
            setImageBitmap((Bitmap) data.getExtras().get("data"));
        }
        if (requestCode == GALLARY_RESULT && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try { setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage));
            } catch (IOException e) { showError(getString(R.string.no_image)); }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bitmap){
        mActualBitmap = bitmap;
        mImgView.setImageBitmap(bitmap);
        mImgView.setVisibility(View.VISIBLE);
        findViewById(R.id.choose_img).setVisibility(View.GONE);
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

    @Override
    public void showProgressView() { showProgress(); }

    @Override
    public void hideProgressView() { hideProgress(); }

    @Override
    public void showErrorView(String mes) { showError(mes); }
}
