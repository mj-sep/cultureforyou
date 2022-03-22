package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageAdapter adapter;
    List<Image> Images;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ImageButton playButton;
    ImageButton setting_button;
    // ImageButton btn_profile;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // btn_profile = (ImageButton) findViewById(R.id.profile_button);
        setting_button = findViewById(R.id.setting_button);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("select_uil", uid);
        }

        ImageButton btn_profile = (ImageButton) findViewById(R.id.profile_button);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
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

        Images = new ArrayList<>();
        Images.add(new Image(R.drawable.main_1_active, "활기찬", ""));
        Images.add(new Image(R.drawable.main_2_strong, "강렬한", ""));
        Images.add(new Image(R.drawable.main_3_joyful, "즐거운", ""));
        Images.add(new Image(R.drawable.main_4_amazing, "놀라운", ""));
        Images.add(new Image(R.drawable.main_5_horror, "공포스러운", ""));
        Images.add(new Image(R.drawable.main_6_unpleasant, "불쾌한", ""));
        Images.add(new Image(R.drawable.main_7_anxious, "불안한", ""));
        Images.add(new Image(R.drawable.main_8_drowsy, "나른한", ""));
        Images.add(new Image(R.drawable.main_9_depressed, "우울한", ""));
        Images.add(new Image(R.drawable.main_10_static, "정적인", ""));
        Images.add(new Image(R.drawable.main_11_still, "잔잔한", ""));
        Images.add(new Image(R.drawable.main_12_comfort, "편안한", ""));
        Images.add(new Image(R.drawable.main_13_happy, "행복한", ""));
        Images.add(new Image(R.drawable.main_14_friendly, "친근한", ""));
        Images.add(new Image(R.drawable.main_15_mysterious, "신비로운", ""));
        Images.add(new Image(R.drawable.main_16_graceful, "우아한", ""));

        adapter = new ImageAdapter(Images, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        int dpValue = 90;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);

        playButton = findViewById(R.id.playButton);
/*        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                startActivity(intent);
            }


        });

 */


    }

}
