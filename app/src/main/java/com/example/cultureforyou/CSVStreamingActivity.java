package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CSVStreamingActivity extends AppCompatActivity {

    private TextView str_mood;
    private TextView str_musictitle;
    private TextView str_musicartist;
    private TextView str_musictitle_invi;
    private TextView str_musicartist_invi;
    private TextView str_presentsecond;
    private TextView str_endsecond;
    private SeekBar str_seekbar;
    private ImageView str_art;
    private ImageView str_blur;
    private ImageView str_arrow;
    private ImageButton str_next;
    private ImageButton str_back;
    private ImageButton str_start;
    private ImageButton str_heart;
    private ImageButton str_tracklist;
    private TextView str_arttitle;
    private TextView str_artartist;
    private TextView str_mini_mood;
    MotionLayout streamingmotion;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;

    /*
    ArrayList<String> moodselect = new ArrayList<>(); // 무드값에 해당하는 플레이리스트ID 집합
    String moodselectid_result = ""; // 무드값에 해당하는 플레이리스트 중 랜덤으로 하나만 추출한 값(ID)
     */
    // String[] select_playlist; // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트
    ArrayList<String> select_playlist = new ArrayList<String>(); // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트
    ArrayList<String> music_info = new ArrayList<>(); // 음악 정보
    ArrayList<String> miniplaylist_id = new ArrayList<>(); // 미니플레이리스트 ID 집합 (플레이리스트 내부)
    ArrayList<String> miniplaylist_info = new ArrayList<>(); // 미니플레이리스트 1개의 정보
    ArrayList<ArrayList<String>> miniplaylist_info_sum = new ArrayList<ArrayList<String>>(); //
    ArrayList<String> miniplaylist_minimood = new ArrayList<>(); // 미니플레이리스트 대표감성
    ArrayList<String> miniplaylist_startsecond = new ArrayList<String>(); // 미니플레이리스트의 시작 초
    ArrayList<String> art_id_list = new ArrayList<String>(); // 미니플레이리스트 내부의 명화 리스트
    ArrayList<String> art_mt_leng = new ArrayList<>(); // 미니플레이리스트의 길이 모음
    ArrayList<String> art_info = new ArrayList<>(); // 명화 정보
    ArrayList<String> moodtracklist = new ArrayList<>(); // 같은 무드의 플레이리스트 정보
    ArrayList<String> moodtrackmusicid = new ArrayList<>();
    ArrayList<String> moodtracktitle = new ArrayList<>(); // 같은 무드의 플레이리스트 정보 - 음악 제목
    ArrayList<String> moodtrackcomposer = new ArrayList<>(); // 같은 무드의 플레이리스트 정보 - 작곡가명

    String[] mood_extract = new String[16];

    String maxmood = "a0";
    String mood_ext_str = "";

    String art_id_array ="";
    String art_id_mini = "";
    String art_title = "";
    String art_artist = "";
    String art_drive = "";
    String music_title = "";
    String music_composer = "";
    String uid = "";
    int like_exist = 0; // 초기 상태 0 , 좋아요 처리 되어 있다면 1
    int isplayingnow = 1; // 현재 재생중인지 - 정지 0, 재생중 1
    int duration = 0; // 음악 길이

    String selectplaylistid = "";

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    int pause_position = 0;
    int time = 0;
    int timer_test = 0;
    int check = 0;
    int arttitme = 0;
    int pos = 0;
    int pos_t1 = 0;
    int mini_num = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streamingpage);

        // 버튼 및 뷰 정의
        str_mood = findViewById(R.id.str_full_mood);
        str_mini_mood = findViewById(R.id.str_mini_mood);
        str_musictitle = findViewById(R.id.str_musictitle);
        str_musicartist = findViewById(R.id.str_musicartist);
        str_start = findViewById(R.id.str_start);
        str_next = findViewById(R.id.str_next);
        str_back = findViewById(R.id.str_back);
        str_heart = findViewById(R.id.str_heart);
        str_tracklist = findViewById(R.id.str_tracklist);
        str_seekbar = findViewById(R.id.str_seekbar);
        str_art = findViewById(R.id.str_full_art);
        str_blur = findViewById(R.id.str_full_blur);
        str_arrow = findViewById(R.id.str_arrow);
        str_arttitle = findViewById(R.id.str_full_arttitle);
        str_artartist = findViewById(R.id.str_full_artartist);
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);
        str_musictitle_invi = findViewById(R.id.str_musictitle_invi);
        str_musicartist_invi = findViewById(R.id.str_musicartist_invi);
        streamingmotion = findViewById(R.id.streamingmotion);


        Intent intent = getIntent();
        String selectmood = intent.getStringExtra("selectmood");
        String str_button_true = intent.getStringExtra("streaming");
        selectplaylistid = intent.getStringExtra("selectplaylistid");
        moodtracklist = (ArrayList<String>) intent.getSerializableExtra("moodplaylist");
        ArrayList<String> select_playlist = (ArrayList<String>) intent.getSerializableExtra("select_playlist_popup");

        firebaseAuth = FirebaseAuth.getInstance();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        // 파이어베이스 정의
        DatabaseReference references = database.getReference("Users").child(uid).child("likelist");


        // 좋아요 (하트) 초기 상태 - 좋아요 체크했던 플레이리스트라면 하트 채워서 보여주기
        Log.d("snapshot_1", selectplaylistid);
        references.orderByChild("plid").equalTo(selectplaylistid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    if(ds.exists()) {
                        Log.d("snapshot_heart", "check");
                        like_exist = 1;
                        str_heart.setImageResource(R.drawable.str_heart_fill);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("select_playlist", "배열: " + select_playlist);
        // 플레이리스트 재생 페이지에서는 앞뒤버튼 비활성화
        if(str_button_true.equals("0")){
            Log.i("VALUEBUTTON", str_button_true);
            str_next.setImageResource(R.drawable.str_next_disabled);
            str_next.setEnabled(false);
            str_back.setImageResource(R.drawable.str_back_disabled);
            str_back.setEnabled(false);
        }


        // 플레이리스트 무드 텍스트
        str_mood.setText(ChangeAtoB.setMood(selectmood));


        // 뒤로 가기(이전) 클릭 시
        str_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent musicIntent = new Intent(getApplicationContext(), MainFragment.class);
                //setResult(RESULT_OK, musicIntent);
                // finish();
                // onBackPressed();
                onBackPressed();
            }
        });

        // 좋아요 버튼 클릭 시
        str_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference1 = database.getReference("Users").child(uid).child("likelist");

                // 만약 좋아요 되어 있지 않은 상태라면
                if(like_exist == 0) {
                    str_heart.setImageResource(R.drawable.str_heart_fill);
                    like_exist = 1;
                    Log.d("like_exist_notlike", String.valueOf(like_exist));

                    // DB에 업데이트 (Arraylist : 0-플레이리스트ID, 1-대표감성, 2-음악제목, 3-작곡가명, 4-시각)
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    // 해쉬맵 테이블 > 파이어베이스 DB에 저장 (Users > Likelist)
                    HashMap<Object, String> likeplaylist = new HashMap<>();
                    likeplaylist.put("plid", selectplaylistid);
                    likeplaylist.put("mood", selectmood);
                    likeplaylist.put("title", music_title);
                    likeplaylist.put("composer", music_composer);
                    likeplaylist.put("currentclock", getTime());

                    Log.d("hashmap_like", String.valueOf(likeplaylist));
                    reference1.push().setValue(likeplaylist);

                } // 만약 ]좋아요 되어 있던 상태라면
                else if (like_exist == 1) {
                    like_exist = 0;
                    str_heart.setImageResource(R.drawable.str_heart);
                    Log.d("like_exist_like", String.valueOf(like_exist));

                    reference1.orderByChild("plid").equalTo(selectplaylistid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren()) {
                                //ds.getRef().removeValue();
                                ds.getRef().setValue(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                }

            }
        });


        // 재생, 정지 버튼 클릭 시
        str_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("isService", "start.setOnClick");

                if(isplayingnow == 1) { // 재생중인 상태였다면
                    musicSrv.stopMusicService();
                    Log.i("isService", "now stop");
                    isplayingnow = 0;
                    str_start.setImageResource(R.drawable.str_start);
                } else {
                    isplayingnow = 1;
                    musicSrv.playMusicService();
                    Log.i("isService", "now start");
                    str_start.setImageResource(R.drawable.str_stop);
                    // 쓰레드 생성
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isService) { // 음악이 실행 중일 때
                                try {
                                    // 1초마다 Seekbar 위치 변경
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // 현재 재생중인 위치를 가져와 시크바에 적용
                                str_seekbar.setProgress(musicSrv.onSecond());
                            }
                        }
                    }).start();
                }
            }
        });

        // 리스트 버튼 클릭 시
        str_tracklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), StrTracklistActivity.class);
                intent1.putExtra("moodtracktitle", moodtracktitle);
                intent1.putExtra("moodtrackcomposer", moodtrackcomposer);
                intent1.putExtra("pos", pos);
                startActivityForResult(intent1, REQUEST_CODE);
            }
        });


        // 이미지뷰 클릭 시 전체화면 보기 전환
        str_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_full = new Intent(getApplicationContext(), StreamingfullActivity.class);
                intent_full.putExtra("art_id", art_id_mini);
                intent_full.putExtra("art_title", art_title);
                intent_full.putExtra("art_artist", art_artist);
                intent_full.putExtra("art_gdrive", art_drive);
                intent_full.putExtra("str_mood", ChangeAtoB.setMood(selectmood));
                intent_full.putExtra("str_mini_mood", ChangeAtoB.setMood(miniplaylist_minimood.get(pos_t1)));
                startActivityForResult(intent_full, REQUEST_CODE);
            }
        });

        // 멀티스레드로 반응시간 단축
        Thread thread2 = new Thread(() -> {
            // 음악 데이터 추출 및 재생
            getMusicData(select_playlist.get(2));
            try {
                playmusic(select_playlist.get(2));
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* 서비스로 보낼 데이터
            Intent intent2 = new Intent(getApplicationContext(),MusicService.class);
            intent2.putExtra("m_title", music_title);
            intent2.putExtra("m_artist", music_composer);
            startService(intent2);
             */
            Log.d("nextline_test", "음악 데이터 추출 및 재생");
        });


        Thread thread3 = new Thread(() -> {
            // 미니플레이리스트 추출 및 재생
            getMiniPlaylist(select_playlist.get(1));
            Log.d("nextline_test", "미니플레이리스트 추출 및 재생");
        });

        Thread thread4 = new Thread(()-> {
            // 재생목록 보기를 위한 플레이리스트 작업
            getTrackList(moodtracklist);
        });

        thread3.start();
        thread2.start();
        thread4.start();
    }


    public void getTrackList(ArrayList<String> moodtracklist){
        // 플레이리스트 돌면서 해당 플레이리스트의 Music_ID 뽑고
        for(int i=0; i<moodtracklist.size(); i++) {
            moodtrackmusicid.add(ChangeAtoB.getOnlyMusicID(moodtracklist.get(i)));
        }
        Log.d("moodmusicid", String.valueOf(moodtrackmusicid));

        // Music 돌면서 해당 음악 정보들 뽑고
        for(int i=0; i<moodtracklist.size(); i++){
            ArrayList<String> musicarray = ChangeAtoB.getMusicDT(moodtrackmusicid.get(i));
            moodtracktitle.add(musicarray.get(0));
            moodtrackcomposer.add(musicarray.get(1));
        }
        Log.d("moodmusictitle", String.valueOf(moodtracktitle));
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
        /*
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
        new IntentFilter("Broadcast"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isService) { // 음악이 실행 중일 때
                    try {
                        // 1초마다 Seekbar 위치 변경
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 현재 재생중인 위치를 가져와 시크바에 적용
                    str_seekbar.setProgress(musicSrv.onSecond());
                }
            }
        }).start();

         */
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d("isService", "mMessageReceiver");
            setSeekbar();
        }
    };

    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    // 음악 데이터
    public void getMusicData(String Music_ID) {
        music_info.clear();
        try {
            String pid = "1-2oAHqu7JaS1Ufvw7aZ-v7BZ4Bd8DzSZPMVIdIvXXF8";
            // 본 String pid = "1htYxxmzZhdCbLikj8IOc39Qr1zDuZYn2uEyPm7M5SXc";
            // URL stockURL = new URL("https://lh3.google.com/u/0/d/" + pid);
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline2;

            while ((nextline2 = reader.readNext()) != null) {
                // 선택된 플레이리스트의 Music_ID와 동일한 음악 정보 추출
                if (nextline2[Category_Music.Music_ID.number].equals(Music_ID)) {
                    Log.d("nextline_music", Arrays.toString(nextline2));
                    break;
                }
            }

            int[] category_music = {
                    Category_Music.Music_ID.number,
                    Category_Music.Title.number,
                    Category_Music.Composer.number,
                    Category_Music.Length.number,
                    Category_Music.Gdrive_ID.number
            };

            for(int i = 0; i< category_music.length; i++) {
                music_info.add(nextline2[category_music[i]]);
            }

            Log.d("nextline_music_info", String.valueOf(music_info));
            in.close();

            // 음악 정보 텍스트뷰에 띄움
            // playmusic(music_info.get(4));
            music_title = music_info.get(1);
            music_composer = music_info.get(2);
            String music_length = music_info.get(3);

            musicSrv.getCurrentMusicTitle(music_title);
            musicSrv.getCurrentMusicComposer(music_composer);
            musicSrv.initializeNotification(music_title, music_composer);

            // 음악 길이 mm:ss 단위로 변경
            int m_length = (int) Double.parseDouble(music_length);
            int m_m = m_length / 60;
            int m_s = m_length % 60;
            String music_length_cast = String.format("%02d:%02d", m_m, m_s);

            // Only the original thread that created a view hierarchy can touch its views. < 에러 해결 위한 코드
            runOnUiThread(new Runnable() {
                public void run() {
                    str_musictitle.setText(music_title);
                    //str_musictitle_invi.setText(music_title);
                    //str_musictitle_invi.setVisibility(View.INVISIBLE);
                    //str_musicartist_invi.setVisibility(View.INVISIBLE);
                    //str_musicartist_invi.setText(music_composer);
                    str_musictitle.setSelected(true);
                    str_musicartist.setText(music_composer);
                    str_endsecond.setText(music_length_cast);
                }
            });

            // 음악 재생
            //playmusic(nextline2[Category_Music.Gdrive_ID.number]);

            Log.d("nextline_music_title", music_title);
            Log.d("nextline_music_composer", music_composer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 미니플레이리스트
    public void getMiniPlaylist(String miniplaylist) {

        // miniplaylist_id ArrayList에 id값 담기
        miniplaylist_id.clear();
        miniplaylist_startsecond.clear();
        art_mt_leng.clear();

        String mini = miniplaylist;
        mini = mini.substring(1, mini.length()-1);
        String[] mini_id_array = mini.split(", ");
        for (int i = 0; i < mini_id_array.length; i++) {
            miniplaylist_id.add(mini_id_array[i]);
        }

        Log.d("nextline_mini", String.valueOf(miniplaylist_id));

        try {
            /* 본데이터 MiniPlaylist.csv 링크
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1HK38JL41YaDo9_MQA5Cnvs8YykDP14qS");
            */
            String pid = "19A3_1gJVd1swTCJE3L6TkdGjtd6yYrJ_05_QoZ_ZtIc";
            // 본 String pid = "1TRpj08nO36pdIc0GA6sMeycpJTz2NU4TyxAd6uF5Zbk";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline3;
            Log.d("nextline_minicsv", miniplaylist_id.get(mini_num));
            int j = 0;
            int k = 0; // miniplaylist_id.size() == k 이면 break
            // MiniPlaylist(miniplaylist_id.get(0));


            /* 본데이터 접근용
            int[] category_miniplaylist = {
                    Category_Miniplaylist.MiniPlaylist_ID.number,
                    Category_Miniplaylist.MT_Value.number,
                    Category_Miniplaylist.Start_Second.number,
                    Category_Miniplaylist.End_Second.number,
                    Category_Miniplaylist.MT_Length.number,
                    Category_Miniplaylist.Art_ID.number
            };
             */

            int[] category_miniplaylist = {
                    Category_MiniPlaylist_SP.MiniPlaylist_ID.number,
                    Category_MiniPlaylist_SP.MT_Value.number,
                    Category_MiniPlaylist_SP.Start_Second.number,
                    Category_MiniPlaylist_SP.End_Second.number,
                    Category_MiniPlaylist_SP.MT_Length.number,
                    Category_MiniPlaylist_SP.Art_ID.number
            };

            while ((nextline3 = reader.readNext()) != null) {

                // miniplaylist_id.size() == k 이면 break
                if(k == miniplaylist_id.size()) break;

                // 미니플레이리스트의 데이터 추출 (플레이리스트의 Music_ID값이 같은 것 중에 MT_ID가 같은 값들)
                if (!nextline3[Category_MiniPlaylist_SP.MiniPlaylist_ID.number].equals(miniplaylist_id.get(j))) {
                    continue;
                }

                j++; k++;
                miniplaylist_info.clear();
                for (int i = 0; i < category_miniplaylist.length; i++) {
                    if(i == 1) {
                        mood_ext_str = nextline3[category_miniplaylist[1]].substring(1, nextline3[category_miniplaylist[1]].length()-1);

                        // 미니플레이리스트 대표감성 추출
                        mood_extract = mood_ext_str.split(", ");
                        // Log.d("nextline_mood", Arrays.toString(mood_extract));
                        double max = Double.parseDouble(mood_extract[0]);
                        int max_mood = 0;
                        for(int m = 1; m < mood_extract.length; m++) {
                            if (max < Double.parseDouble(mood_extract[m])) {
                                max = Double.parseDouble(mood_extract[m]);
                                max_mood = m;
                            }
                            maxmood = "a" + max_mood;
                        }
                        // Log.d("nextline_maxmood", maxmood);
                        miniplaylist_info.add(maxmood);
                        miniplaylist_minimood.add(maxmood);


                    } else {
                        miniplaylist_info.add(nextline3[category_miniplaylist[i]]);
                        // Log.d("nextline_info_test", String.valueOf(miniplaylist_info));
                        if(i==2) miniplaylist_startsecond.add(nextline3[category_miniplaylist[2]]);
                        if(i==4) art_mt_leng.add(nextline3[category_miniplaylist[4]]);
                    }
                    miniplaylist_info_sum.add(miniplaylist_info);
                }
                // miniplaylist_info_sum.add(miniplaylist_info);
                Log.d("nextline_miniplaylist_info", k + " " + String.valueOf(miniplaylist_info));
                // Log.d("nextline_mini_result", String.valueOf(miniplaylist_info_sum));
            }

            Log.d("nextline777_startsecond", String.valueOf(miniplaylist_startsecond));
            Log.d("nextline_artmtlist", String.valueOf(art_mt_leng));


            in.close();


            MiniPlaylist(miniplaylist_id.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void MiniPlaylist(String miniplaylistID) {
        Log.i("nextline6_mark", String.valueOf(timer_test));
        if (pos_t1 < miniplaylist_id.size()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    str_mini_mood.setText(ChangeAtoB.setMood(miniplaylist_minimood.get(pos_t1)));
                }
            });
            Start_MiniPlaylist(miniplaylist_id.get(pos_t1), art_mt_leng.get(pos_t1));
            // Start_MiniPlaylist(miniplaylist_id.get(Mlist_id));


            TimerTask t0 = new TimerTask() {
                @Override
                public void run() {
                    for(int i = miniplaylist_startsecond.size() - 1; i >= 0; i--) {
                        int minisecondsize = (int) Double.parseDouble(miniplaylist_startsecond.get(i));
                        if(time > (int) Double.parseDouble(miniplaylist_startsecond.get(i)) - 2){
                            pos = i;
                            timer_test = 1;
                            Log.d("nextline22222_pos", String.valueOf(pos));
                            Log.i("nextline777_ValueTime", String.valueOf(time));

                            // check = 1;
                            break;
                        }
                        Log.d("nextline_minisecondsize", i + " " + String.valueOf(minisecondsize));
                    }
                }
            };

            TimerTask t1 = new TimerTask() {
                @Override
                public void run() {
                    if(timer_test == 1) {
                        timer_test = 0;
                        if(pos_t1 != pos) {
                            t0.cancel();
                            pos_t1 = pos;
                            Log.d("pos_t1", "다름 " + String.valueOf(pos_t1));
                            MiniPlaylist(miniplaylist_id.get(pos_t1));
                        }
                        Log.d("pos_t1", "같음 " + String.valueOf(pos_t1));

                    }
                }
            };


            Timer timer = new Timer();
            timer.schedule(t0, 0, 1000);
            timer.schedule(t1, 0, 1000);

        }
    }

    // 각각의 미니플레이리스트 재생
    public void Start_MiniPlaylist(String miniplaylist_id, String artlength) {
        art_id_list.clear();
        art_info.clear();

        try {

            // 미술 데이터 접근
            String pid = "1BBkLsEhY23g6840neKZvEtSeIzMAO72NYF0IwJi3ky8";
            // 본 String pid = "1yBfhDnod5lHpuWW_FJvtNaJkKzcAnGp60tlbP3EgG24";
            URL stockURL2 = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(stockURL2.openStream()));
            CSVReader reader2 = new CSVReader(in2);
            String[] nextline5;

            Log.d("nextline_startmini", miniplaylist_id);
            String minipid = "19A3_1gJVd1swTCJE3L6TkdGjtd6yYrJ_05_QoZ_ZtIc";
            // 본 String minipid = "1TRpj08nO36pdIc0GA6sMeycpJTz2NU4TyxAd6uF5Zbk";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + minipid + "/export?format=csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline4;

            int[] category_miniplaylist_sp = {
                    Category_MiniPlaylist_SP.MiniPlaylist_ID.number,
                    Category_MiniPlaylist_SP.MT_Value.number,
                    Category_MiniPlaylist_SP.Art_ID.number
            };

            while ((nextline4 = reader.readNext()) != null) {
                // Log.d("nextline_array", Arrays.toString(nextline4));
                // Log.d("nextline_ch", nextline4[Category_MiniPlaylist_SP.MiniPlaylist_ID.number]);
                if (nextline4[Category_MiniPlaylist_SP.MiniPlaylist_ID.number].equals(miniplaylist_id)) {
                    Log.d("nextline4_mini", Arrays.toString(nextline4));
                    art_id_array = nextline4[category_miniplaylist_sp[2]];
                    break;
                }

            }
            in.close();


            art_id_array = art_id_array.substring(1, art_id_array.length()-1);
            String[] art_id_list_string = art_id_array.split(", ");
            for (int i = 0; i < art_id_list_string.length; i++) {
                art_id_list.add(art_id_list_string[i]);
            }
            Log.d("nextline4_art", String.valueOf(art_id_list));


            int[] category_art_SP = {
                    Category_Art_SP.Art_ID.number,
                    Category_Art_SP.Title.number,
                    Category_Art_SP.Artist.number,
                    Category_Art_SP.Gdrive_ID.number,
                    Category_Art_SP.Filename.number
            };

            art_id_mini = art_id_list.get(0);
            String art_id_mini_sub = art_id_mini.substring(1, art_id_mini.length()-1);

            // 미니 플레이리스트의 명화 리스트를 초로 나눠서 돌리기
            Log.d("nextline4_array", String.valueOf(art_id_list.size()));

            double artlength_cast = Double.parseDouble(artlength);
            Log.d("nextline4_artlength_cast", String.valueOf(artlength_cast));

            if(art_id_list.size() > 1) {
                int passartsec = (int) artlength_cast / art_id_list.size();
                Log.d("nextline4_sec", String.valueOf(passartsec));

                /*
                TimerTask art_timer = new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("nextline_timertask", art_timer);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(art_timer, 0, passartsec);
                 */
            }

            while ((nextline5 = reader2.readNext()) != null) {
                if (nextline5[Category_Art_SP.Art_ID.number].equals(art_id_mini_sub)) {
                    Log.d("nextline5", Arrays.toString(nextline5));
                    break;
                }
            }

            for(int i = 0; i< category_art_SP.length; i++) {
                art_info.add(nextline5[category_art_SP[i]]);
            }

            Log.d("nextline5_art_data", String.valueOf(art_info));
            in2.close();

            // 미술(명화) 정보 텍스트뷰에 띄움
            art_title = art_info.get(1);
            art_artist = art_info.get(2);
            // 드라이브 접근 시
            // art_drive = art_info.get(3);
            // 파이어베이스 스토리지 접근 시
            art_drive = art_info.get(4);


            runOnUiThread(new Runnable() {
                public void run() {

                    // String url = "https://drive.google.com/uc?export=view&id=" + art_drive;
                    // String url = "http://docs.google.com/uc?export=open&id=" + art_drive;
                    //String url = "https://lh3.google.com/u/0/d/" + art_drive;

                    // String url = "https://docs.google.com/uc?export=open&id=" + art_drive;
                    StorageReference sart = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Art/" + art_drive);
                    sart.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri url) {
                            Glide.with(getApplicationContext()).load(url).thumbnail(0.1f).into(str_art);
                            // 명화 블러 배경
                            Glide.with(getApplicationContext()).load(url).override(100, 100).thumbnail(0.1f).
                                    apply(bitmapTransform(new BlurTransformation(25,3))).into(str_blur);
                            str_arttitle.setText(art_title);
                            str_artartist.setText(art_artist);
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Category {
        Playlist_ID(0),
        MiniPlaylist_ID_list(2),
        Music_ID(3),
        PM_Value(4),
        Playlist_Mood(5);

        public final int number;

        Category(int number) {
            this.number = number;
        }
    }

    public enum Category_Music {
        Music_ID(1),
        Title(2),
        Composer(3),
        Length(10),
        Gdrive_ID(17);

        public final int number;

        Category_Music(int number) {
            this.number = number;
        }
    }

    public enum Category_MiniPlaylist_SP {
        MiniPlaylist_ID(1),
        MT_Value(5),
        Start_Second(7),
        End_Second(8),
        MT_Length(9),
        Art_ID(11);

        public final int number;
        Category_MiniPlaylist_SP(int number) {this.number = number;}
    }

    public enum Category_Art_SP {
        Art_ID(1),
        Title(2),
        Artist(3),
        Filename(12),
        Gdrive_ID(14);

        public final int number;
        Category_Art_SP(int number) {this.number = number;}
    }

    protected void onNewIntent(Intent intent2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isService) { // 음악이 실행 중일 때
                    try {
                        // 1초마다 Seekbar 위치 변경
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 현재 재생중인 위치를 가져와 시크바에 적용
                    str_seekbar.setProgress(musicSrv.onSecond());
                }
            }
        }).start();
        super.onNewIntent(intent2);
    }

    // 음악 재생 - gdrive_ID 사용
    public void playmusic(String gdrive_ID) throws IOException {

        //MediaPlayer player = new MediaPlayer();
        StorageReference smusic = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Music/" + gdrive_ID +".mp3");
        smusic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri m_url) {
                //String m_url = "https://docs.google.com/uc?export=open&id=" + gdrive_ID;
                Log.i("isService", String.valueOf(isService));

                if(!isService) {
                    Log.i("isService", "서비스 중이 아닙니다. 데이터 받을 수 없음.");
                }

                // 음악 재생
                duration = musicSrv.initService(m_url);

                // musicSrv.stopMusicService();
                SeekbarSetting(duration);

                Log.d("isService", String.valueOf(duration));
                musicSrv.playMusicService();
                str_start.setImageResource(R.drawable.str_stop);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("isService", "isService setResult");
                //SeekbarSetting(duration);
                Log.d("isService duration", String.valueOf(duration));
                if(musicSrv.isPlayingCurrent()) {
                    str_start.setImageResource(R.drawable.str_stop);
                }
                else str_start.setImageResource(R.drawable.str_start);
                // SeekbarSetting(duration);
            }
            else Log.d("isService", "notsetResult");
        }

    }

    public void SeekbarSetting(int duration) {
        str_seekbar.setMax(duration);
        int second = musicSrv.onSecond();
        Log.d("isService second", String.valueOf(second));

        str_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("isService", "setSeekbar");
                // 사용자가 Seekbar 움직이면 음악 재생 위치도 변경
                //if (fromUser) player.seekTo(progress);
                if (fromUser) musicSrv.fromUserSeekBar(progress);
                int m = progress / 1000 / 60;
                int s = progress / 1000 % 60;
                time = progress / 1000;
                String presenttime = String.format("%02d:%02d", m, s);
                str_presentsecond.setText(presenttime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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
                Log.d("isService isPlayingCurrent()", String.valueOf(musicSrv.isPlayingCurrent()));
                while (musicSrv.isPlayingCurrent()) { // 음악이 실행 중일 때
                    try {
                        // 1초마다 Seekbar 위치 변경
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 현재 재생중인 위치를 가져와 시크바에 적용
                    str_seekbar.setProgress(musicSrv.onSecond());
                    if(duration == musicSrv.onSecond()) Log.d("isService", "MUSIC END");
                }
                Log.d("isService status-STREAMING", String.valueOf(isService));
                runOnUiThread(new Runnable() {
                    public void run() {
                        str_start.setImageResource(R.drawable.str_start);
                    }
                });
                musicSrv.stopSelf();
            }
        }).start();
    }


    public void setSeekbar() {

    }

    protected void onDestroy(){
        super.onDestroy();
        if(isService){
            unbindService(conn);
            isService=false;
        }
    }


    private String getTime () {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }
}