package com.example.cultureforyou;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

// 메인 화면과 스트리밍 페이지에서의 음악 공유 상황
public class MusicService extends Service {

    private MediaPlayer player;


    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        player.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        player.setOnErrorListener((MediaPlayer.OnErrorListener) this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("processTest", "onStartCommand() 호출");
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
        player.seekTo(positon);
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
        Log.d("DestroyTest", "onDestroy() 호출");
        player.getCurrentPosition();
        player.pause();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
