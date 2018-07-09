package ru.timrlm.testproject.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.data.model.MyImage;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private List<MyImage> images;
    private Context mContext;

    public ImagesAdapter(List<MyImage> images) { this.images = images; }

    synchronized
    public void add(int max){
        images.add(new MyImage(null,max));
        notifyItemInserted(images.size()-1);
    }

    synchronized
    public void upd(int pos, int progress){
        images.get(pos).setProgress(progress);
        notifyItemChanged(pos);
    }

    synchronized
    public void upd(int pos, Bitmap bitmap){
        images.get(pos).setProgress(0);
        images.get(pos).setMaxProgress(0);
        images.get(pos).setBitmap(bitmap);
        notifyItemChanged(pos);
    }

    @Override
    public int getItemViewType(int position) { return position % 2; }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ImageViewHolder view = new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image,parent,false));
        view.setBackground(viewType == 0 ? R.color.colorPrimary : R.color.lightgray);
        return view;
    }

    @Override
    public int getItemCount() { return images.size(); }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        MyImage image = images.get(position);
        holder.setImage(image.getBitmap());
        if (image.getProgress() == image.getMaxProgress()) {
            holder.getProgressBar().setVisibility(View.GONE);
        }else{
            holder.getProgressBar().setVisibility(View.VISIBLE);
            holder.getProgressBar().setMax(image.getMaxProgress());
            holder.getProgressBar().setProgress(image.getProgress());
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        private View rootView;
        ImageViewHolder(View view){
            super(view);
            rootView = view;
        }

        public void setBackground(int color){ rootView.setBackgroundColor(mContext.getResources().getColor(color)); }

        public void setImage(Bitmap bitmap){ ((ImageView) rootView.findViewById(R.id.view_image_img)).setImageBitmap(bitmap); }

        public ProgressBar getProgressBar(){ return rootView.findViewById(R.id.view_image_progress); }
    }
}
