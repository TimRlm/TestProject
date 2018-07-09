package ru.timrlm.testproject.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.data.model.MyImage;
import ru.timrlm.testproject.ui.base.BaseActivity;
import ru.timrlm.testproject.util.BitmapUtil;

public class MainActivity extends BaseActivity implements MainMvpView, ImagesAdapter.OnItemClickListener {
    @Inject MainPresenter mPresenter;
    @BindView(R.id.main_img) AppCompatImageView mImgView;
    @BindView(R.id.main_rv) RecyclerView mRv;
    private final int IMAGE_RESULT = 1;
    private final int GALLARY_RESULT = 2;
    private Bitmap mActualBitmap;
    private ImagesAdapter mAdapter;


    public void init() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.loadImages();
    }

    @Override
    public void setImages(List<MyImage> images) {
        mAdapter = new ImagesAdapter(images);
        mAdapter.setOnItemClickListener(this);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public int addImage(int maxProgress) {
        mAdapter.add(maxProgress);
        mRv.scrollToPosition(mAdapter.getItemCount() - 1);
        return mAdapter.getItemCount() - 1;
    }

    @Override
    public void updImageProgress(int pos, int progress) { mAdapter.upd(pos,progress); }

    @Override
    public void setImage(int pos,Bitmap bitmap, String path) { mAdapter.upd(pos,bitmap, path); }

    @Override
    public void rmvImage(int pos) { mAdapter.rmv(pos); }

    @Override
    public void onItemClick(int position) {
        MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter((d,index,item)->{
            if (index == 0){
                setImageBitmap(mAdapter.getItem(position).getBitmap());
            }else{
                mPresenter.remove((mAdapter.getItem(position).getPath()),position);
            }
            d.dismiss();
        });
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(R.string.choose)
                .icon(android.R.drawable.checkbox_on_background)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(R.string.delete)
                .icon(android.R.drawable.ic_delete)
                .build());
        new MaterialDialog.Builder(this)
                .adapter(adapter,null)
                .show();
    }

    @OnClick({R.id.main_rotate,R.id.main_invert,R.id.main_mirror})
    public void rotate(Button button) {
        if (mActualBitmap == null) return;
        if (ContextCompat.checkSelfPermission(MainActivity.this,      Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            return;
        }
        switch (button.getId()){
            case R.id.main_rotate:  mPresenter.rotate(mActualBitmap); break;
            case R.id.main_invert:  mPresenter.monochrome(mActualBitmap); break;
            case R.id.main_mirror:  mPresenter.mirrorBitmap(mActualBitmap); break;
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mPresenter.attachView(this);
        init();
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

    @Override
    public void setImageBitmap(Bitmap bitmap){
        mActualBitmap = bitmap;
        mImgView.setImageBitmap(bitmap);
        mImgView.setVisibility(View.VISIBLE);
        findViewById(R.id.choose_img).setVisibility(View.GONE);
    }
}
