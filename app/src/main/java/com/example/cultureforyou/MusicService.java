package com.example.cultureforyou;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

// 메인 화면과 스트리밍 페이지에서의 음악 공유 상황
public class MusicService extends Service {

    private MediaPlayer player;
    private IBinder mBinder = new LocalBinder();
    String music ="";
    String pathUrl = "";
    int fulltime = 0; // 음악 전체 시간
    int pause_position = 0; // 정지 시 음악 현재 시간


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

        /*
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        int positon = intent.getIntExtra("position", 0);
        String urlPath = intent.getStringExtra("url");
        player = MediaPlayer.create(this, Uri.parse(urlPath));
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf(); // 노래가 끝나면 알아서 재생종료
            }
        });
        player.seekTo(position);
        player.start();

        /*
        Intent intent2 = new Intent(getApplicationContext(), MainFragment.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(player.isPlaying()) {
            intent2.putExtra("isPlayingNow", 1);
        } else intent2.putExtra("isPlayingNow", 0);
        startActivity(intent2);
         */
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("isService", "onDestroy() 호출");
        try {
            player.stop();
            player.release();
            player = null;
        } catch (Exception e) {}
        //player.getCurrentPosition();
        //player.pause();
        super.onDestroy();
    }

    public void playMusicService() {
        Log.i("isService", "playmusicservice() 호출");

        if(player == null) Log.i("isService", "playmusicsrv player null");
        else Log.i("isService", "playmusicsrv is not null" + pause_position);

        player.seekTo(pause_position);
        player.start();
    }

    public void stopMusicService() {
        Log.i("isService", "stopmusicservice() 호출");

        if(player == null) Log.i("isService", "stopmusicsrv player null");
        else Log.i("isService", "stopmusicsrv is not null " + pause_position);

        pause_position = player.getCurrentPosition();
        player.pause();
    }

    public void initService(String pathUrl) {
        Log.i("isService", "initService() 호출");

        player = MediaPlayer.create(this, Uri.parse(pathUrl));
        fulltime = player.getDuration();
    }
    private void processCommand(Intent intent, int flags, int startId) {

       /* String title = intent2.getStringExtra("m_title");
        String artist = intent2.getStringExtra("m_artist");
        // 메인 액티비티로 보낼 인텐트
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("m_status", 1);
        mainIntent.putExtra("m_title", title);
        mainIntent.putExtra("m_artist", artist);
        // startActivity(mainIntent);
        */
    }
}