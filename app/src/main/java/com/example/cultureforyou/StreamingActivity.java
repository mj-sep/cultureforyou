package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StreamingActivity extends MainActivity {

    // 구글 드라이브 API
    private static final String TAG = "StreamingActivity";
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    private DriveServiceHelper mDriveServiceHelper;

    private TextView str_mood;
    private TextView str_musictitle;
    private TextView str_musicartist;
    private TextView str_presentsecond;
    private TextView str_endsecond;
    private SeekBar str_seekbar;
    private ImageView str_art;
    private ImageView str_blur;
    private ImageButton str_start;
    private TextView str_arttitle;
    private TextView str_artartist;
    List MiniPlaylistIDlist = new ArrayList();
    List MiniPlaylistArtlist = new ArrayList();
    List MiniPlaylistArtTest = new ArrayList();
    static int counter = 0;
    int time = 0;
    long mini = 0;
    int mini_id=0;

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
        str_mood = findViewById(R.id.str_mood);
        str_musictitle = findViewById(R.id.str_musictitle);
        str_musicartist = findViewById(R.id.str_musicartist);
        str_start = findViewById(R.id.str_start);
        str_seekbar = findViewById(R.id.str_seekbar);
        str_art = findViewById(R.id.str_art);
        str_blur = findViewById(R.id.str_blur);
        str_arttitle = findViewById(R.id.str_arttitle);
        str_artartist = findViewById(R.id.str_artartist);

        Intent intent = getIntent();
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);
        String selectmood = intent.getStringExtra("selectmood");
        str_mood.setText(setMood(selectmood));
        Startplaylist(selectmood);


    }


    // playlist
    public void Startplaylist(String mood) {
        DatabaseReference plist = dref.child("Playlist");
        DatabaseReference pmusic = dref.child("Music");
        DatabaseReference pminiplay = dref.child("MiniPlaylist");

        // 문제 발생 !! 존나 느림... 거진 삼중루프라 코드가 더러움... -> 수정해야함
        // Playlist 스키마 -> ID와 음악 ID 가져오기
        plist.orderByChild("Playlist_ID").equalTo(10).addValueEventListener(new ValueEventListener() {
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


                    /*
                    // 미니플레이리스트의 Art_ID 끌어오기 (중요)
                    for(int i=0; i < MiniPlaylistIDlist.size(); i++) {
                        long mini_id = (long) MiniPlaylistIDlist.get(0);

                        pminiplay.orderByChild("MiniPlaylist_ID").equalTo(mini_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.i("ValueEQUALTO", String.valueOf(mini_id));
                                for(DataSnapshot minisnap : snapshot.child("Art_ID").getChildren()){
                                    Log.i("ValueSnapshot", (String) minisnap.getValue());

                                    MiniPlaylistArtlist.add(minisnap.getValue(long.class));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        });
                    }
                    Log.i("ValueMiniArtlist", String.valueOf(MiniPlaylistArtlist));
                     */


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
                                str_musicartist.setText(music_artist);
                                str_endsecond.setText(music_length);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                    //미니플레이리스트 재생, playlist_ID -> miniPlaylist_ID로 바꿔야함
                    //StartMiniPlaylist(playlist_ID);
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

        /*
        MiniPlaylistArtTest.clear();
        MiniPlaylistArtTest.add("Kaggle_31711"); // 298
        MiniPlaylistArtTest.add("Kaggle_69595"); // 299
        MiniPlaylistArtTest.add("Kaggle_48809");
        MiniPlaylistArtTest.add("Kaggle_66565"); // 300
        MiniPlaylistArtTest.add("Kaggle_24036"); // 301
        MiniPlaylistArtTest.add("Kaggle_63513");
        MiniPlaylistArtTest.add("Kaggle_41945"); // 302



        Log.i("SizeValue", String.valueOf(MiniPlaylistArtTest.size()));
        // Log.i("ValueMiniartTEST", String.valueOf(MiniPlaylistArtTest));



        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                String mini_art_id = (String) MiniPlaylistArtTest.get(counter);
                Log.i("TimerValue", String.valueOf(mini_art_id));
                StartMiniPlaylist(mini_art_id);
                counter++;
            }
        };

        // 10초마다 명화 변경
        Timer timer = new Timer();
        timer.schedule(tt, 0, 10000);
        // StartMiniPlaylist(mini_id);
        */



        // 타임스탬프 종료 시간을 받고 그 시간이 지나면 다음 미니 플레이리스트를 재생하도록 -> 더 수정해야 함
        Log.i("ValueTime", String.valueOf(time));
        if (mini_id < MiniPlaylistIDlist.size()){
            mini = (long) MiniPlaylistIDlist.get(mini_id);
            Log.i("VALUEMINI", String.valueOf(mini));
            pminiplay.orderByChild("MiniPlaylist_ID").equalTo(mini).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot minisnap : dataSnapshot.getChildren()){
                        start_second = minisnap.child("Start_Second").getValue(Double.class);
                        end_second = minisnap.child("End_Second").getValue(Double.class);
                        Log.i("ValueValueVVV", String.valueOf(start_second));
                        Log.i("ValueValueEND", String.valueOf(end_second));
                        Log.i("ValueFinalMini", String.valueOf(mini));
                        StartMiniPlaylist_Pre(mini, start_second, end_second);
                        if(time > end_second) {
                            mini_id = mini_id+1;
                            Playlist(playlistID);
                        }
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
                    if(MiniPlaylistArtlist.size() < 2){
                        minip_art = (String) MiniPlaylistArtlist.get(0);
                        Log.i("ValueStringMINIPID", minip_art);
                    }

                    part.orderByChild("Art_ID").equalTo(minip_art).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot asnap : snapshot.getChildren()) {
                                Log.i("ValueArtinfo", asnap.getValue().toString());
                                String artGdrive_ID = asnap.child("Gdrive_ID").getValue(String.class);
                                String art_title = asnap.child("Title").getValue(String.class);
                                String art_artist = asnap.child("Artist").getValue(String.class);

                                str_arttitle.setText(art_title);
                                str_artartist.setText(art_artist);

                                Log.i("Gdrive_IDValue", artGdrive_ID);

                                // 명화 불러오기
                                String fileId = artGdrive_ID;
                                String url = "https://drive.google.com/uc?export=view&id=" + artGdrive_ID;
                                Log.i("ValueURL", url);
                                Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_art);
                                // 명화 블러 배경
                                Glide.with(getApplicationContext()).load(url).
                                        apply(bitmapTransform(new BlurTransformation(22))).into(str_blur);
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



    public void StartMiniPlaylist(String mini_art_id) {
        DatabaseReference pminip = dref.child("MiniPlaylist");
        DatabaseReference part = dref.child("Art");

        part.orderByChild("Art_ID").equalTo(mini_art_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot asnap : snapshot.getChildren()) {
                    Log.i("ValueArtinfo", asnap.getValue().toString());
                    String artGdrive_ID = asnap.child("Gdrive_ID").getValue(String.class);
                    String art_title = asnap.child("Title").getValue(String.class);
                    String art_artist = asnap.child("Artist").getValue(String.class);

                    str_arttitle.setText(art_title);
                    str_artartist.setText(art_artist);

                    Log.i("Gdrive_IDValue", artGdrive_ID);

                    // 명화 불러오기
                    String fileId = artGdrive_ID;
                    String url = "https://drive.google.com/uc?export=view&id=" + artGdrive_ID;
                    Log.i("ValueURL", url);
                    Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_art);
                    // 명화 블러 배경
                    Glide.with(getApplicationContext()).load(url).
                            apply(bitmapTransform(new BlurTransformation(22))).into(str_blur);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
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
