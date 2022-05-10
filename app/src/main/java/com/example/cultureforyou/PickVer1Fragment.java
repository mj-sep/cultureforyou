package com.example.cultureforyou;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickVer1Fragment extends AppCompatActivity {

    private View view;
    private StrTrackAdapter pick1adapter; // 재생버튼 없앨 시 트랙리스트 리스트뷰 쓰기, 있으면 좋아요 리사이클러뷰
    private ImageView pick_image;
    private ImageView pick_back;
    private TextView pick_title;
    private ImageButton pick_play;
    private ListView pick_ver1_list;

    int seq = 0;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;


    ArrayList<String> picktitlelist = new ArrayList<>();
    ArrayList<String> pickartistlist = new ArrayList<>();
    ArrayList<String> pickmoodlist = new ArrayList<>();
    ArrayList<String> pickplidlist = new ArrayList<String>();

    String pplidlist = "";
    String ptitlelist = "";
    String partistlist = "";
    String pmoodlist = "";




    protected void onCreate(@Nullable Bundle savedInstaceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstaceState);
        setContentView(R.layout.pick_version1);

        pick1adapter = new StrTrackAdapter();
        pick_image = findViewById(R.id.pick_image);
        pick_back = findViewById(R.id.pick_back);
        pick_title = findViewById(R.id.pick_title);
        pick_play = findViewById(R.id.pick_play);
        pick_ver1_list = findViewById(R.id.pick_ver1_list);

        Intent pickintent1 = getIntent();
        seq = pickintent1.getIntExtra("pick_seq", 0);
        picktitlelist = (ArrayList<String>) pickintent1.getSerializableExtra("picktitlelist");
        pickmoodlist = (ArrayList<String>) pickintent1.getSerializableExtra("pickmoodlist");
        pickplidlist = (ArrayList<String>) pickintent1.getSerializableExtra("pickplidlist");
        pickartistlist = (ArrayList<String>) pickintent1.getSerializableExtra("pickartistlist");

        Log.d("pickartistlist", String.valueOf(pickartistlist));


        pick_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new Thread(()-> {

            // 인텐트에서 받아온 데이터로 리스트뷰에 띄우기
            for (int i = 0; i < pickplidlist.size(); i++) {
                ListDTO dto = new ListDTO();
                dto.setMoodtxt(ChangeAtoB.setMood(pickmoodlist.get(i)));
                dto.setMplaylistid(pickplidlist.get(i));
                dto.setMtitle(picktitlelist.get(i));
                dto.setMcomposer(pickartistlist.get(i));
                dto.setMoodimg(ChangeAtoB.changeimg(pickmoodlist.get(i)));

                pick1adapter.addItem(dto);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pick_ver1_list.setAdapter(pick1adapter);
                }
            });
        }).start();


        switch (seq) {
            case 0:
                pick_image.setImageResource(R.drawable.pick_rainy);
                pick_title.setText("비가 오는 날 듣기 좋은");
                break;
            case 1:
                pick_image.setImageResource(R.drawable.pick_happy);
                pick_title.setText("소소하지만 확실한 행복");
                break;
            case 2:
                pick_image.setImageResource(R.drawable.pick_coffee);
                pick_title.setText("커피향 가득한 카페에서");
                break;
            case 3:
                pick_image.setImageResource(R.drawable.pick_alone);
                pick_title.setText("혼자 있고 싶을 때");
                break;
            case 4:
                pick_image.setImageResource(R.drawable.pick_morning);
                pick_title.setText("상쾌한 아침을 만들어주는");
                break;
            case 5:
                pick_image.setImageResource(R.drawable.pick_walk);
                pick_title.setText("산책하면서 듣기 좋은");
                break;
            case 6:
                pick_image.setImageResource(R.drawable.pick_dance);
                pick_title.setText("신나는 무도회장 한가운데");
                break;
            case 7:
                pick_image.setImageResource(R.drawable.pick_horror);
                pick_title.setText("더운 여름, 무섭지!");
                break;
            case 8:
                pick_image.setImageResource(R.drawable.pick_sun);
                pick_title.setText("따스한 햇볕 아래");
                break;
            case 9:
                pick_image.setImageResource(R.drawable.pick_swan);
                pick_title.setText("호수 위 아름다운 백조처럼");
                break;
        }
    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        Log.d("isService", "PickVer1 : onStart");
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        Log.d("isService", "PickVer1 : onStop");
        unbindService(conn);
        isService = false;
    }

    public void onResume() {
        super.onResume();
        Log.d("isService", "PickVer1 : onResume");
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
