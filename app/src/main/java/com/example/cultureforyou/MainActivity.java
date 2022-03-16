package com.example.cultureforyou;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private List<Image> imageList;
    private ImageAdapter adapter;
    private Handler sliderHandler= new Handler();
    private ImageButton btn_profile;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_profile = findViewById(R.id.profile_button);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("select_uil", uid);
        }

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                String id = user.getUid();

                reference.orderByChild("uid").equalTo(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String nickname = snapshot1.child("nickname").getValue(String.class);
                            String profile_icon = snapshot1.child("profile_icon").getValue(String.class);

                            Log.d("select_id2", id);
                            Log.d("select_nickname", nickname);
                            Log.d("select_icon", profile_icon);


                            intent.putExtra("profile_icon", profile_icon);
                            intent.putExtra("unickname", nickname);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });

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
