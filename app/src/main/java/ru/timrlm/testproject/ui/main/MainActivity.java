package ru.timrlm.testproject.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {
    @Inject MainPresenter mPresenter;
    @BindView(R.id.main_img) AppCompatImageView mImgView;
    @BindView(R.id.main_rv) RecyclerView mRv;
    private final int IMAGE_RESULT = 1;
    private final int GALLARY_RESULT = 2;

    @Override
    public void init() {

    }

    @OnClick(R.id.main_rotate)
    public void rotate() {
    }

    @OnClick(R.id.main_invert)
    public void invert() {
    }

    @OnClick(R.id.main_mirror)
    public void mirror() {
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
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImgView.setImageBitmap(imageBitmap);
            findViewById(R.id.choose_img).setVisibility(View.GONE);
        }
        if (requestCode == GALLARY_RESULT && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                mImgView.setImageBitmap(bitmap);
                findViewById(R.id.choose_img).setVisibility(View.GONE);
            } catch (IOException e) { showError(getString(R.string.no_image)); }
        }
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
}
