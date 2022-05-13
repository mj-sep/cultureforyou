package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PickVer2Fragment extends AppCompatActivity {

    private View view;
    private StrTrackAdapter pick2adapter; // 재생버튼 없앨 시 트랙리스트 리스트뷰 쓰기, 있으면 좋아요 리사이클러뷰
    private ImageView pick_blur;
    private ImageView pick_image;
    private ImageView pick_back;
    private TextView pick_title;
    private ImageButton pick_play;
    private ListView pick_ver2_list;

    int seq = 0;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;


    ArrayList<String> picktitlelist2 = new ArrayList<>();
    ArrayList<String> pickartistlist2 = new ArrayList<>();
    ArrayList<String> pickmoodlist2 = new ArrayList<>();
    ArrayList<String> pickplidlist2 = new ArrayList<String>();

    String pplidlist2 = "";
    String ptitlelist2 = "";
    String partistlist2 = "";
    String pmoodlist2 = "";

    ArrayList<String> select_playlist = new ArrayList<>(); // 첫 번째 플리의 정보


    public void onCreate(@Nullable Bundle savedInstaceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstaceState);
        setContentView(R.layout.pick_version2);

        pick2adapter = new StrTrackAdapter();
        pick_blur = findViewById(R.id.pick_blur);
        pick_image = findViewById(R.id.pick_image);
        pick_back = findViewById(R.id.pick_back);
        pick_title = findViewById(R.id.pick_title);
        pick_play = findViewById(R.id.pick_play);
        pick_ver2_list = findViewById(R.id.pick_ver2_list);

        Intent pickintent2 = getIntent();
        seq = pickintent2.getIntExtra("pick_seq", 10);
        picktitlelist2 = (ArrayList<String>) pickintent2.getSerializableExtra("picktitlelist");
        pickmoodlist2 = (ArrayList<String>) pickintent2.getSerializableExtra("pickmoodlist");
        pickplidlist2 = (ArrayList<String>) pickintent2.getSerializableExtra("pickplidlist");
        pickartistlist2 = (ArrayList<String>) pickintent2.getSerializableExtra("pickartistlist");

        String pickfirstid = pickplidlist2.get(0);

        new Thread(() -> {
            select_playlist = ChangeAtoB.getOnePlaylist(pickfirstid);
            Log.d("pick1select_playlist", String.valueOf(select_playlist));
        }).start();

        // 뒤로 가기 버튼 클릭 시 한 단계 뒤로
        pick_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 재생 버튼 클릭 시 스트리밍 페이지 (CSVStreamingActivity.java)로 이동
        pick_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CSVStreamingActivity.class);
                intent.putExtra("kind", 1);
                intent.putExtra("moodplaylist", pickplidlist2); // 해당 추천 플리의 플레이리스트 ID 목록
                intent.putExtra("selectmood", pickmoodlist2.get(0)); // 해당 추천 플리의 1번째 플리 대표감성
                intent.putExtra("select_playlist_popup", select_playlist); // 해당 추천 플리의 1번째 플리 정보
                intent.putExtra("selectplaylistid", pickplidlist2.get(0)); // 선택한 플레이리스트 ID (29)
                intent.putExtra("streaming", "0"); // 재생목록 앞뒤 버튼 막히게
                intent.putExtra("picktitle", pick_title.getText().toString()); // 플리 제목 (비가 오는 날 듣기 좋은)
                intent.putExtra("moodnamelist", pickmoodlist2); // 무드값 리스트
                startActivity(intent);
            }
        });

        new Thread(() -> {
            // 인텐트에서 받아온 데이터로 리스트뷰에 띄우기
            for (int i = 0; i < pickplidlist2.size(); i++) {
                ListDTO dto = new ListDTO();
                dto.setMoodtxt(ChangeAtoB.setMood(pickmoodlist2.get(i)));
                dto.setMplaylistid(pickplidlist2.get(i));
                dto.setMtitle(picktitlelist2.get(i));
                dto.setMcomposer(pickartistlist2.get(i));
                dto.setMoodimg(ChangeAtoB.changeimg(pickmoodlist2.get(i)));

                pick2adapter.addItem(dto);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pick_ver2_list.setAdapter(pick2adapter);
                }
            });

        }).start();



        switch (seq) {
            case 10:
                Glide.with(getApplicationContext()).load(R.drawable.blur_mozart).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_mozart);
                pick_title.setText("모차르트");
                break;
            case 11:
                Glide.with(getApplicationContext()).load(R.drawable.blur_bach).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_bach);
                pick_title.setText("바흐");
                break;
            case 12:
                Glide.with(getApplicationContext()).load(R.drawable.blur_haydn).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_haydn);
                pick_title.setText("하이든");
                break;
            case 13:
                Glide.with(getApplicationContext()).load(R.drawable.blur_brahms).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_brahms);
                pick_title.setText("브람스");
                break;
            case 14:
                Glide.with(getApplicationContext()).load(R.drawable.blur_chai).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_chai);
                pick_title.setText("차이코프스키");
                break;
            case 15:
                Glide.with(getApplicationContext()).load(R.drawable.blur_handel).
                        apply(bitmapTransform(new BlurTransformation(25, 3))).into(pick_blur);
                pick_image.setImageResource(R.drawable.pick_handel);
                pick_title.setText("헨델");
                break;
            default:
                break;
        }


    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        Log.d("isService", "PickVer2 : onStart");
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop() {
        super.onStop();
        Log.d("isService", "PickVer2 : onStop");
        unbindService(conn);
        isService = false;
    }

    public void onResume() {
        super.onResume();
        Log.d("isService", "PickVer2 : onResume");
    }


    protected void onNewIntent(Intent intent) {
        // processIntent(intent);
        super.onNewIntent(intent);
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




    public enum Category_pick {
        Pick_ID(1),
        Playlist_ID_list(2),
        Playlist_Mood_list(3),
        Pick_Title(4),
        Music_title_list(5),
        Music_artist_list(6);

        public final int number;

        Category_pick(int number) {
            this.number = number;
        }
    }

}
