package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StreamingfullActivity extends AppCompatActivity {

    private TextView str_full_mood;
    private TextView str_full_arttitle;
    private TextView str_full_artartist;
    private TextView str_mini_mood;
    private PhotoView str_full_art;
    private ImageView str_full_blur;
    private ImageButton str_full_back;
    private ImageButton str_full_status;

    // 파이어베이스 스토리지
    FirebaseStorage storage;


    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;

    boolean music_status = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_fullview_potrait);

        // 버튼 및 뷰 정의
        str_full_mood = findViewById(R.id.str_full_mood);
        str_full_arttitle = findViewById(R.id.str_full_arttitle);
        str_full_artartist = findViewById(R.id.str_full_artartist);
        str_full_art = findViewById(R.id.str_full_art);
        str_full_blur = findViewById(R.id.str_full_blur);
        str_full_back = findViewById(R.id.str_full_back);
        str_mini_mood = findViewById(R.id.str_mini_mood);
        str_full_status = findViewById(R.id.str_full_status);

        Intent intent = getIntent();
        String art_id = intent.getStringExtra("art_id");
        String art_title = intent.getStringExtra("art_title");
        String art_artist = intent.getStringExtra("art_artist");
        String art_drive = intent.getStringExtra("art_gdrive");
        String str_mood = intent.getStringExtra("str_mood");
        String str_minimood = intent.getStringExtra("str_mini_mood");
        str_full_mood.setText(str_mood + "  |  ");
        str_mini_mood.setText(str_minimood);

        storage = FirebaseStorage.getInstance();

        Log.i("ValueARTID", art_id);

        // str_full_back 클릭 시, 뒤로 가기
        str_full_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_full = new Intent(getApplicationContext(), CSVStreamingActivity.class);
                setResult(RESULT_OK, intent_full);
                finish();
            }
        });

        str_full_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_full_mood.getVisibility() == View.VISIBLE) {
                    str_full_mood.setVisibility(View.INVISIBLE);
                    str_mini_mood.setVisibility(View.INVISIBLE);
                    str_full_arttitle.setVisibility(View.INVISIBLE);
                    str_full_artartist.setVisibility(View.INVISIBLE);
                    str_full_status.setVisibility(View.INVISIBLE);
                } else {
                    str_full_mood.setVisibility(View.VISIBLE);
                    str_mini_mood.setVisibility(View.VISIBLE);
                    str_full_arttitle.setVisibility(View.VISIBLE);
                    str_full_artartist.setVisibility(View.VISIBLE);
                    str_full_status.setVisibility(View.VISIBLE);

                }
            }
        });

        str_full_arttitle.setText(art_title);
        str_full_arttitle.setSelected(true);
        str_full_artartist.setText(art_artist);

        // 명화 불러오기 - 구글 드라이브용
        /*
        String url = "https://drive.google.com/uc?export=view&id=" + art_drive;
        Log.i("ValueURL", url);
        Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_full_art);
        // 명화 블러 배경
        Glide.with(getApplicationContext()).load(url).
                apply(bitmapTransform(new BlurTransformation(25,3))).into(str_full_blur);
         */

        StorageReference sart = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Art/" + art_drive);
        sart.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri url) {
                Glide.with(getApplicationContext()).load(url).thumbnail(0.1f).into(str_full_art);
                // 명화 블러 배경
                Glide.with(getApplicationContext()).load(url).override(100, 100).thumbnail(0.1f).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(str_full_blur);
                str_full_arttitle.setText(art_title);
                str_full_artartist.setText(art_artist);
            }
        });

        // 쓰레드 생성
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("isService isPlayingCurrent()-full", String.valueOf(musicSrv.isPlayingCurrent()));
                if (musicSrv.isPlayingCurrent()) { // 음악이 실행 중일 때 이미지 버튼 변경 (|| 표시로)
                    str_full_status.setImageResource(R.drawable.str_full_stop);
                } else {
                    // 음악이 실행 중이지 않다면 이미지 버튼 변경 (>표시로)
                    str_full_status.setImageResource(R.drawable.str_full_start);
                    Log.d("isService status-Streamingfull", String.valueOf(isService));
                }
            }
        }).start();


        // 재생/정지 버튼 클릭 시
        str_full_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv.isPlayingCurrent()) {
                    musicSrv.stopMusicService();
                    str_full_status.setImageResource(R.drawable.str_full_start);
                }
                else {
                    musicSrv.playMusicService();
                    str_full_status.setImageResource(R.drawable.str_full_stop);
                }
            }
        });
    }

    protected void onNewIntent(Intent intent2) {
        onStart();

        super.onNewIntent(intent2);
    }

    public void onStart() {
        super.onStart();
        Intent intent2 = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    public void onStop(){
        super.onStop();
        unbindService(conn);
        isService = false;
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




