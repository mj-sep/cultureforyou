package com.example.cultureforyou;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    ImageButton btn_backward;
    Button advice;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        btn_backward = findViewById(R.id.backward_button);
        advice = findViewById(R.id.advice);

        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        Log.d("isService", "onStart");
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        Log.d("isService", "onStop");
        unbindService(conn);
        isService = false;
    }

    public void onResume() {
        super.onResume();
        Log.d("isService", "onResume");

    }

    protected void onNewIntent(Intent intent2) {
        super.onNewIntent(intent2);
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
