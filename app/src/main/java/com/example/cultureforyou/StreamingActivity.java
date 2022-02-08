package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StreamingActivity extends MainActivity {

    // 구글 드라이브 API
    private static final String TAG = "StreamingActivity";

    private TextView str_mood;
    private TextView str_musictitle;
    private TextView str_musicartist;
    private TextView str_presentsecond;
    private TextView str_endsecond;
    private SeekBar str_seekbar;
    private ImageView str_art;
    private ImageView str_blur;
    private ImageButton str_next;
    private ImageButton str_back;
    private ImageButton str_start;
    private ImageButton str_heart;
    private TextView str_arttitle;
    private TextView str_artartist;
    List MiniPlaylistIDlist = new ArrayList();
    List MiniPlaylistArtlist = new ArrayList();
    List MoodPlaylistIDList = new ArrayList();
    static int counter = 0;
    int time = 0;
    long mini = 0;
    int mini_id=0;
    int timer_test = 0;
    int pause_position = 0;
    int moodrandomplay;
    long mood_random_num;

    double start_second = 0.0;
    double end_second = 0.0;
    String minip_art = "";

    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streamingpage);

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();

        // 버튼 및 뷰 정의
        str_mood = findViewById(R.id.str_full_mood);
        str_musictitle = findViewById(R.id.str_musictitle);
        str_musicartist = findViewById(R.id.str_musicartist);
        str_start = findViewById(R.id.str_start);
        str_next = findViewById(R.id.str_next);
        str_back = findViewById(R.id.str_back);
        str_heart = findViewById(R.id.str_heart);
        str_seekbar = findViewById(R.id.str_seekbar);
        str_art = findViewById(R.id.str_full_art);
        str_blur = findViewById(R.id.str_full_blur);
        str_arttitle = findViewById(R.id.str_full_arttitle);
        str_artartist = findViewById(R.id.str_full_artartist);

        Intent intent = getIntent();
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);
        String selectmood = intent.getStringExtra("selectmood");
        String str_button_true = intent.getStringExtra("streaming");


        // 플레이리스트 재생 페이지에서는 앞뒤버튼 비활성화
        if(str_button_true.equals("0")){
            Log.i("VALUEBUTTON", str_button_true);
            str_next.setImageResource(R.drawable.str_next_disabled);
            str_next.setEnabled(false);
            str_back.setImageResource(R.drawable.str_back_disabled);
            str_back.setEnabled(false);
        }

        str_mood.setText(setMood(selectmood));
        DatabaseReference plist = dref.child("Playlist");

        // intent 받은 무드값의 플레이리스트 중 1개 랜덤 추출
        plist.orderByChild("Playlist_Mood").equalTo(selectmood).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MoodPlaylistIDList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MoodPlaylistIDList.add(dataSnapshot.child("Playlist_ID").getValue(long.class));
                }
                moodrandomplay = (int) (Math.random() * MoodPlaylistIDList.size());
                mood_random_num = (long) MoodPlaylistIDList.get(moodrandomplay);
                Log.i("MoodValue", String.valueOf(mood_random_num));
                Startplaylist(mood_random_num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });


        // 좋아요 버튼 클릭시
        str_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_heart.setImageResource(R.drawable.str_heart_fill);
            }
        });

        // 이미지뷰 클릭 시 전체화면 보기 전환
        str_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StreamingfullActivity.class);
                intent.putExtra("art_id", minip_art);
                intent.putExtra("str_mood", setMood(selectmood));
                startActivity(intent);
            }
        });
    }


    // playlist
    public void Startplaylist(long mood_random_num) {
        DatabaseReference plist = dref.child("Playlist");
        DatabaseReference pmusic = dref.child("Music");


        // Playlist 스키마 -> ID와 음악 ID 가져오기
        plist.orderByChild("Playlist_ID").equalTo(mood_random_num).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("Valueplaylistinfo", dataSnapshot.getValue().toString());
                    MiniPlaylistIDlist.clear();


                    // miniplaylist 값 끌어오기
                    for (DataSnapshot minipsnap : snapshot.child("MiniPlaylist_ID_list").getChildren()) {
                        MiniPlaylistIDlist.add(minipsnap.getValue(long.class));
                    }
                    Log.i("ValueMiniPlaylist", String.valueOf(MiniPlaylistIDlist));

                    // Playlist-Music_ID, Playlist_ID 추출
                    String music_ID = snapshot.child("Music_ID").getValue(String.class);
                    int playlist_ID = snapshot.child("Playlist_ID").getValue(Integer.class);



                    // Music-Title, Composer 추출
                    pmusic.orderByChild("Music_ID").equalTo(music_ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot msnap : snapshot.getChildren()) {
                                Log.i("Valuemusicinfo", snapshot.getValue().toString());
                                String music_title = msnap.child("Title").getValue(String.class);
                                String music_artist = msnap.child("Composer").getValue(String.class);
                                String music_gdrive = msnap.child("Gdrive_ID").getValue(String.class);

                                int m_length = (int) Double.parseDouble(msnap.child("Length").getValue(String.class));
                                int m_m = m_length / 60;
                                int m_s = m_length % 60;
                                String music_length = String.format("%02d:%02d", m_m, m_s);

                                playmusic(music_gdrive);
                                str_musictitle.setText(music_title);
                                str_musictitle.setSelected(true);
                                str_musicartist.setText(music_artist);
                                str_endsecond.setText(music_length);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                    // 플레이리스트 재생
                    Playlist(playlist_ID);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }


    // 음악 재생 - gdrive_ID 사용
    public void playmusic(String gdrive_ID) {
        String m_url = "https://drive.google.com/uc?export=view&id=" + gdrive_ID;

        try {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // 재생,일시정지
            str_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(player.isPlaying()) {
                        player.pause();
                        str_start.setImageResource(R.drawable.str_start);
                        pause_position = player.getCurrentPosition();
                    }
                    else {
                        player.seekTo(pause_position);
                        player.start();
                        str_start.setImageResource(R.drawable.str_stop);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (player.isPlaying()) {
                                    try {
                                        // 1초마다 Seekbar 위치 변경
                                        Thread.sleep(1000);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    // 현재 재생중인 위치를 가져와 시크바에 적용
                                    str_seekbar.setProgress(player.getCurrentPosition());
                                }
                            }
                        }).start();
                    }
                }
            });

            player.setDataSource(m_url);
            player.prepare();
            // 음악 길이 -> Seekbar 최대값에 적용
            str_seekbar.setMax(player.getDuration());
            str_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // 사용자가 Seekbar 움직이면 음악 재생 위치도 변경
                    if (fromUser) player.seekTo(progress);
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
            player.start();

            // 쓰레드 생성
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (player.isPlaying()) { // 음악이 실행 중일 때
                        str_start.setImageResource(R.drawable.str_stop);
                        try {
                            // 1초마다 Seekbar 위치 변경
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 현재 재생중인 위치를 가져와 시크바에 적용
                        str_seekbar.setProgress(player.getCurrentPosition());
                    }
                }
            }).start();



        } catch (IOException e) {
            Log.i("ValueError", "error playing audio");
            e.printStackTrace();
        }

    }


    public void Playlist(int playlistID){
        DatabaseReference pminiplay = dref.child("MiniPlaylist");
        Log.i("VALUEMARK", String.valueOf(timer_test));


        // delay
        if (mini_id < MiniPlaylistIDlist.size()){
            mini = (long) MiniPlaylistIDlist.get(mini_id);
            Log.i("VALUEMINI", String.valueOf(mini));
            pminiplay.orderByChild("MiniPlaylist_ID").equalTo(mini).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot minisnap : dataSnapshot.getChildren()){
                        start_second = minisnap.child("Start_Second").getValue(Double.class);
                        end_second = minisnap.child("End_Second").getValue(Double.class);
                        int end_second_con = (int) end_second-3;
                        Log.i("ValueValueVVV", String.valueOf(start_second));
                        Log.i("ValueValueEND", String.valueOf(end_second));
                        Log.i("ValueFinalMini", String.valueOf(mini));

                        StartMiniPlaylist_Pre(mini, start_second, end_second);


                        TimerTask t1 = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                Log.i("ValueTime", String.valueOf(time));
                                if(time > end_second_con){
                                    timer_test = 1;
                                }
                            }
                        };

                        TimerTask t2 = new TimerTask() {
                            @Override
                            public void run() {
                                if(timer_test == 1) {
                                    t1.cancel();
                                    timer_test = 0;
                                    mini_id = mini_id + 1;
                                    Playlist(playlistID);
                                }
                            }
                        };

                        Timer timer = new Timer();
                        timer.schedule(t1, 0, 1000);
                        timer.schedule(t2, 0, 300);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });



        }


    }



    public void StartMiniPlaylist_Pre(long minip_id, double start_second, double end_second){
        DatabaseReference pminip = dref.child("MiniPlaylist");
        DatabaseReference part = dref.child("Art");
        MiniPlaylistArtlist.clear();

        pminip.orderByChild("MiniPlaylist_ID").equalTo(minip_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){

                    for(DataSnapshot artsnapshot: snap.child("Art_ID").getChildren()){
                        MiniPlaylistArtlist.add(artsnapshot.getValue(String.class));
                    }
                    Log.i("ValueARTlist", String.valueOf(MiniPlaylistArtlist));
                    int subtract_second = (int)(end_second - start_second);
                    int middle_second = (int) (start_second + (subtract_second/MiniPlaylistArtlist.size()));
                    minip_art = (String) MiniPlaylistArtlist.get(0);

                    Log.i("ValueStringMINIPID", minip_art);



                    part.orderByChild("Art_ID").equalTo(minip_art).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot asnap : snapshot.getChildren()) {
                                Log.i("ValueArtinfo", asnap.getValue().toString());
                                String artGdrive_ID = asnap.child("Gdrive_ID").getValue(String.class);
                                String art_title = asnap.child("Title").getValue(String.class);
                                String art_artist = asnap.child("Artist").getValue(String.class);

                                str_arttitle.setText(art_title);
                                str_arttitle.setSelected(true);
                                str_artartist.setText(art_artist);

                                // 명화 불러오기
                                String fileId = artGdrive_ID;
                                String url = "https://drive.google.com/uc?export=view&id=" + artGdrive_ID;
                                Log.i("ValueURL", url);
                                Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_art);
                                // 명화 블러 배경
                                Glide.with(getApplicationContext()).load(url).
                                        apply(bitmapTransform(new BlurTransformation(25,3))).into(str_blur);


                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }



    public String setMood(String selectmood){
        switch (selectmood){
            case "a0": selectmood = "활기찬";
                break;
            case "a1" : selectmood = "강렬한";
                break;
            case "a2" : selectmood = "즐거운";
                break;
            case "a3" : selectmood = "놀라운";
                break;
            case "a4": selectmood = "공포스러운";
                break;
            case "a5": selectmood = "불쾌한";
                break;
            case "a6": selectmood = "불안한";
                break;
            case "a7": selectmood = "나른한";
                break;
            case "a8": selectmood = "우울한";
                break;
            case "a9": selectmood = "정적인";
                break;
            case "a10": selectmood = "잔잔한";
                break;
            case "a11": selectmood = "편안한";
                break;
            case "a12": selectmood = "행복한";
                break;
            case "a13": selectmood = "친근한";
                break;
            case "a14": selectmood = "신비로운";
                break;
            case "a15": selectmood = "우아한";
                break;
        }
        return selectmood;
    }

}
