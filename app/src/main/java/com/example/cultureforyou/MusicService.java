package com.example.cultureforyou;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


// 메인 화면과 스트리밍 페이지에서의 음악 공유 상황
public class MusicService extends Service {

    private MediaPlayer player;
    private IBinder mBinder = new LocalBinder();
    String music ="";
    String pathUrl = "";
    int fulltime = 0; // 음악 전체 시간
    int pause_position = 0; // 정지 시 음악 현재 시간
    int currentposition = 0; // 현재 시간 반환해 줄 때



    class LocalBinder extends Binder {
        MusicService getService() { return MusicService.this;}
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Service Created", Toast.LENGTH_SHORT).show();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("isService", "onStartCommand() 호출");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("isService", "onDestroy() 호출");
        /*
        try {
            player.stop();
            player.release();
            player = null;
        } catch (Exception e) {}

         */
        super.onDestroy();
    }

    // 음악 시작
    public void playMusicService() {
        Log.i("isService", "playmusicservice() 호출");

        if(player == null) Log.i("isService", "playmusicsrv player null");
        else Log.i("isService", "playmusicsrv is not null" + pause_position);

        player.seekTo(pause_position);
        player.start();
    }

    // 음악 정지
    public void stopMusicService() {
        Log.i("isService", "stopmusicservice() 호출");

        pause_position = player.getCurrentPosition();

        if(player == null) Log.i("isService", "stopmusicsrv player null");
        else Log.i("isService", "stopmusicsrv is not null " + pause_position);

        player.pause();
    }

    // 음악 초기 설정 (URL)
    public int initService(Uri pathUrl) {
        Log.i("isService", "initService() 호출");

        player = MediaPlayer.create(this, pathUrl);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        fulltime = player.getDuration();
        return fulltime;
    }

    // 현재 초 반환
    public int onSecond(){
        Log.i("isService", "onSecond : " + player.getCurrentPosition());
        currentposition = player.getCurrentPosition();
        return currentposition;
    }

    // 유저가 Seekbar 움직일 때
    public void fromUserSeekBar(int progress) {
        Log.i("isService", "fromUserSeekbar" + progress);
        player.seekTo(progress);
    }

    // 브로드 캐스트 보내기
    private void sendMessage(){
        Intent intent = new Intent("Broadcast");
        intent.putExtra("message", currentposition);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public boolean isPlayingCurrent(){
        if(player.isPlaying()) return true;
        else return false;
    }
}