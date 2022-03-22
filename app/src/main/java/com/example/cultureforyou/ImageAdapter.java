package com.example.cultureforyou;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class ImageAdapter extends PagerAdapter {

    private List<Image> Images;
    private LayoutInflater layoutInflater;
    private Context context;

    public ImageAdapter(List<Image> images, Context context) {
        Images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_container,container,false);

        ImageView image;
        TextView title;
        ImageButton playButton;

        image=view.findViewById(R.id.image);
        title=view.findViewById(R.id.title);
        playButton=view.findViewById(R.id.playButton);

        image.setImageResource(Images.get(position).getImage());
        title.setText(Images.get(position).getTitle());
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
