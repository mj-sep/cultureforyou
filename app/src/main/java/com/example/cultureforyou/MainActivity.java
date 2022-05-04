package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;


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

    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        Log.d("isService", "MainAcitivy : onStart");
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        Log.d("isService", "MainAcitivy : onStop");
        unbindService(conn);
        isService = false;
    }

    public void onResume() {
        super.onResume();
        Log.d("isService", "MainAcitivy : onResume");
        /*
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
        new IntentFilter("Broadcast"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isService) { // 음악이 실행 중일 때
                    try {
                        // 1초마다 Seekbar 위치 변경
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 현재 재생중인 위치를 가져와 시크바에 적용
                    str_seekbar.setProgress(musicSrv.onSecond());
                }
            }
        }).start();

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
            Log.d("isService", "processTest" + m_title);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MusicService.LocalBinder mb = (MusicService.LocalBinder) service;
            musicSrv = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Log.i("isService", name + " 서비스 연결 해제");
            Toast.makeText(getApplicationContext(),
                    "서비스 연결 해제",
                    Toast.LENGTH_LONG).show();
        }
    };


}
