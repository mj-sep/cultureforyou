package com.example.cultureforyou;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private Handler sliderHandler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2=findViewById(R.id.viewpager2);
        imageList=new ArrayList<>();

        imageList.add(new Image(R.drawable.active1));
        imageList.add(new Image(R.drawable.strong2));
        imageList.add(new Image(R.drawable.joyful3));
        imageList.add(new Image(R.drawable.amazing4));
        imageList.add(new Image(R.drawable.horror5));
        imageList.add(new Image(R.drawable.unpleasant6));
        imageList.add(new Image(R.drawable.anxious7));
        imageList.add(new Image(R.drawable.drowsy8));
        imageList.add(new Image(R.drawable.depressed9));
        imageList.add(new Image(R.drawable.static10));
        imageList.add(new Image(R.drawable.still11_1));
        imageList.add(new Image(R.drawable.comfort12));
        imageList.add(new Image(R.drawable.happy13));
        imageList.add(new Image(R.drawable.friendly14));
        imageList.add(new Image(R.drawable.mysterious15));
        imageList.add(new Image(R.drawable.graceful16_1));

        adapter=new ImageAdapter(imageList,viewPager2);
        viewPager2.setAdapter(adapter);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer=new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.14f);
            }
        });

        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,2000);
            }
        });

    }
    private Runnable sliderRunnable= new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }
}