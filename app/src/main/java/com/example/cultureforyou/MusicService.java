package com.example.cultureforyou;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;


// 메인 화면과 스트리밍 페이지에서의 음악 공유 상황
public class MusicService extends Service {

    private MediaPlayer player = null;
    private IBinder mBinder = new LocalBinder();
    String music ="";
    String pathUrl = "";
    int fulltime = 0; // 음악 전체 시간
    int pause_position = 0; // 정지 시 음악 현재 시간
    int currentposition = 0; // 현재 시간 반환해 줄 때
    String music_title = ""; // 현재 음악 제목
    String music_composer = ""; // 현재 음악 작곡가


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
        Log.i("isService", "onCreate() 호출");
        // Toast.makeText(getApplicationContext(),"Service Created", Toast.LENGTH_SHORT).show();
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
        else {
            Log.i("isService", "playmusicsrv is not null" + pause_position);
            player.seekTo(pause_position);
            player.start();
        }
    }

    // 음악 정지
    public void stopMusicService() {
        Log.i("isService", "stopmusicservice() 호출");

        pause_position = player.getCurrentPosition();

        if(player == null) Log.i("isService", "stopmusicsrv player null");
        else {
            Log.i("isService", "stopmusicsrv is not null " + pause_position);
            player.pause();
        }
    }

    // 음악 초기 설정 (URL)
    public int initService(Uri pathUrl) {
        Log.i("isService", "initService() 호출");

        player = MediaPlayer.create(this, pathUrl);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        fulltime = player.getDuration();
        pause_position = 0;

        return fulltime;
    }

    // 현재 초 반환
    public int onSecond(){
        Log.i("isService", "onSecond 호출 : " + player.getCurrentPosition());
        currentposition = player.getCurrentPosition();
        return currentposition;
    }

    // 유저가 Seekbar 움직일 때
    public void fromUserSeekBar(int progress) {
        Log.i("isService", "fromUserSeekbar 호출 : " + progress);
        player.seekTo(progress);
    }

    // 브로드 캐스트 보내기
    private void sendMessage(){
        Intent intent = new Intent("Broadcast");
        intent.putExtra("message", currentposition);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public boolean isPlayingCurrent(){
        Log.i("isService", "isPlayCurrent 호출");
        if(player != null) {
            if (player.isPlaying()) return true;
            else return false;
        } else return false;
    }


    // 알림 Notification
    public void initializeNotification(String title, String artist, String selectmood){


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ChangeAtoB.changeimg(selectmood));
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        int[] xy = new int[x*y];
        bitmap.getPixels(xy, 0, x,0,0, x, y);

        Intent notificationIntent = new Intent(this, CSVStreamingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{notificationIntent}, 0);

        /*
        Intent notiPlayIntent = new Intent(this, MusicService.class);
        notiPlayIntent.setAction(playMusicService());
        PendingIntent nPlayIntent = PendingIntent.getBroadcast(this, 0, notiPlayIntent, 0);
         */

        //This is the intent of PendingIntent
        Intent intentAction = new Intent(this, MusicService.class);

        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("action","stopmusic");

        PendingIntent pintent = PendingIntent.getBroadcast(this,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);


        Log.d("isService", "initializenotification()");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.drawable.artmotion); // 아이콘
        // builder.addAction(R.drawable.str_start, "Play", pendingIntent );
        builder.setContentText(artist); // 음악 아티스트
        builder.setContentTitle(title); // 음악 제목
        builder.setOngoing(true); // 사용자가 알림 못 지우게
        builder.setWhen(0); // 타임스탬프
        builder.setLargeIcon(bitmap);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.addAction(new NotificationCompat.Action(R.drawable.str_back, "Back", null));
        builder.addAction(new NotificationCompat.Action(R.drawable.str_start, "Start", pintent));
        builder.addAction(new NotificationCompat.Action(R.drawable.str_next, "Next", null));
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2));
        //builder.addAction(R.drawable.play, "Play", nPlayIntent);
        //builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle());




        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel("1", "포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
       }
        Notification notification = builder.build();
        startForeground(1, notification);
    }




}