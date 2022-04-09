package com.example.cultureforyou;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// 메인 화면과 스트리밍 페이지에서의 음악 공유 상황
public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent2, int flags, int startId) {
        Log.d("processTest", "onStartCommand() 호출");

        if(intent2 == null) return Service.START_STICKY;
        else { // intent가 null이 아니면 processCommand 메시지 출력
            processCommand(intent2, flags, startId);
        }
        return super.onStartCommand(intent2, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent2) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void processCommand(Intent intent2, int flags, int startId) {
        String title = intent2.getStringExtra("m_title");
        String artist = intent2.getStringExtra("m_artist");


        // 메인 액티비티로 보낼 인텐트
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("m_status", 1);
        mainIntent.putExtra("m_title", title);
        mainIntent.putExtra("m_artist", artist);
        // startActivity(mainIntent);
    }
}
