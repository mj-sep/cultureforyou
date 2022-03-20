package com.example.cultureforyou;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private Handler sliderHandler= new Handler();
    ImageButton btn_profile;
    ImageButton btn_setting;

    BottomNavigationView bottomNavigationView;
    private String TAG = "메인";

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프라그먼트
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //초기 프래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, fragment2).commitAllowingStateLoss();

        // 바텀 네비게이션
        BottomNavigationView bottomNavigationView;
        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()){
                    case R.id.tab1:
                        Log.i(TAG, "좋아요 탭");
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container,fragment1).commitAllowingStateLoss();
                        return true;
                    case R.id.tab2:
                        Log.i(TAG, "홈 탭");
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container,fragment2).commitAllowingStateLoss();
                        return true;
                    case R.id.tab3:
                        Log.i(TAG, "추천 플레이리스트 탭");
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container,fragment3).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });


        btn_profile = findViewById(R.id.profile_button);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_setting = findViewById(R.id.setting_button);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });


        // Popup Activity 커스텀 다이얼로그
        ImageButton imageButton = (ImageButton) findViewById(R.id.feeling_list_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupActivity popup_feeling = new PopupActivity(MainActivity.this);
                popup_feeling.show();
            }
        });

        viewPager2=findViewById(R.id.ImageViewpager);
        imageList=new ArrayList<>();

        imageList.add(new Image(R.drawable.main_1_active));

        imageList.add(new Image(R.drawable.main_2_strong));
        imageList.add(new Image(R.drawable.main_3_joyful));
        imageList.add(new Image(R.drawable.main_4_amazing));
        imageList.add(new Image(R.drawable.main_5_horror));
        imageList.add(new Image(R.drawable.main_6_unpleasant));
        imageList.add(new Image(R.drawable.main_7_anxious));
        imageList.add(new Image(R.drawable.main_8_drowsy));
        imageList.add(new Image(R.drawable.main_9_depressed));
        imageList.add(new Image(R.drawable.main_10_static));
        imageList.add(new Image(R.drawable.main_11_still));
        imageList.add(new Image(R.drawable.main_12_comfort));
        imageList.add(new Image(R.drawable.main_13_happy));
        imageList.add(new Image(R.drawable.main_14_friendly));
        imageList.add(new Image(R.drawable.main_15_mysterious));
        imageList.add(new Image(R.drawable.main_16_graceful));

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
