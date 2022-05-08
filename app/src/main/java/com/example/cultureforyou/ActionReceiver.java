package com.example.cultureforyou;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private Intent playIntent;
    private boolean musicBound = false;
    private static final int REQUEST_CODE = 200;

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MusicService.LocalBinder mb = (MusicService.LocalBinder) service;
            musicSrv = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            Log.i("isService", name + " 서비스 연결");
            isService = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Log.i("isService", name + " 서비스 연결 해제");
            // Toast.makeText(getA,"서비스 연결 해제", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        Log.d("isService", "onReceive");

        if(action.equals("action1")){
            musicSrv.playMusicService();
        }
        else if(action.equals("stopmusic")){
            musicSrv.stopMusicService();
        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
