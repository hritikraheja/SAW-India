package com.example.saw_india.modalClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.saw_india.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.LinkedList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder>{

    LinkedList<SliderImage> imageUrls;

    public SliderAdapter(LinkedList<SliderImage> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.slider_layout, parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        Glide.with(viewHolder.imageView).load(imageUrls.get(position).getImageUrl()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    class SliderViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
