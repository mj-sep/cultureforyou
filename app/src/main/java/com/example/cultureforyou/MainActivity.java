package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageAdapter adapter;
    List<Image> Images;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ImageButton playButton;
    ImageButton setting_button;
    ImageButton btn_profile;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String uid = "";
    public static Context mContext;

    BottomNavigationView bottomNavigationView;
    private String TAG = "메인";

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_profile = (ImageButton) findViewById(R.id.profile_button);
        setting_button = findViewById(R.id.setting_button);
        firebaseAuth = FirebaseAuth.getInstance();


        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");


        //
        //프라그먼트
        fragment1 = new LikeFragment();
        fragment2 = new MainFragment();
        fragment3 = new PickFragment();

        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //초기 프래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, fragment2).commitAllowingStateLoss();

        // 바텀 네비게이션
        BottomNavigationView bottomNavigationView;
        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.tab2);

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


        /*
        // 프로필 이미지
        reference.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String profile_icon = snapshot1.child("profile_icon").getValue(String.class);

                    if (!profile_icon.equals("")) {
                        // 프로필 이미지
                        profile_icon = "https://drive.google.com/uc?export=view&id=" + profile_icon;
                        Glide.with(getApplicationContext()).load(profile_icon).transform(new CenterCrop(), new CircleCrop()).into(btn_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });


        // 프로필 버튼 클릭 시 -> 프로필 페이지 이동
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 클릭 시 ->
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "설정 페이지 이동 예정", Toast.LENGTH_SHORT).show();
            }
        });


         */

        /* Popup Activity 커스텀 다이얼로그
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

         */
/*
        playButton = findViewById(R.id.playButton);
       playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                startActivity(intent);
            }


        });


        Intent mainIntent = getIntent();
        int m_status = mainIntent.getIntExtra("m_status", 0);
        Log.d("processTest", String.valueOf(m_status));
        if(m_status == 1) {
            processIntent(mainIntent);
            Log.d("processTest1", "processTest1");
        }

 */


    }

    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }
    protected void processIntent(Intent intent) {
        if(intent != null){
            String m_title = intent.getStringExtra("m_title");
            String m_artist = intent.getStringExtra("m_artist");
            Log.d("processTest", m_title);
        }
    }

}
