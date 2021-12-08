package com.example.cultureforyou;

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
import java.util.regex.Pattern;

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
    private ImageButton str_start;
    private TextView str_arttitle;
    private TextView str_artartist;
    List MiniPlaylistIDlist = new ArrayList();
    List MiniPlaylistArtlist = new ArrayList();


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
        str_arttitle = findViewById(R.id.str_arttitle);
        str_artartist = findViewById(R.id.str_artartist);

        Intent intent = getIntent();
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);
        String selectmood = intent.getStringExtra("selectmood");
        str_mood.setText(selectmood);
        Startplaylist(selectmood);


    }


    // playlist
    public void Startplaylist(String mood) {
        DatabaseReference plist = dref.child("Playlist");
        DatabaseReference pmusic = dref.child("Music");
        DatabaseReference pminip = dref.child("MiniPlaylist");

        // 문제 발생 !! 존나 느림... 거진 삼중루프라 코드가 더러움... -> 수정해야함
        // Playlist 스키마 -> ID와 음악 ID 가져오기
        plist.orderByChild("Playlist_ID").equalTo(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("Valueplaylistimfo", dataSnapshot.getValue().toString());
                    MiniPlaylistIDlist.clear();

                    // miniplaylist 값 끌어오기
                    for (DataSnapshot minipsnap : snapshot.child("MiniPlaylist_ID_list").getChildren()) {
                        MiniPlaylistIDlist.add(minipsnap.getValue(long.class));
                    }
                    Log.i("ValueMiniPlaylist", String.valueOf(MiniPlaylistIDlist));

                    // Playlist-Music_ID, Playlist_ID 추출
                    String music_ID = snapshot.child("Music_ID").getValue(String.class);
                    int playlist_ID = snapshot.child("Playlist_ID").getValue(Integer.class);

                    // 음악 재생 (Music_ID 이용), 미니플레이리스트 재생
                    playmusic(music_ID);
                    // StartMiniPlaylist(playlist_ID);

                    // Music-Title, Composer 추출
                    pmusic.orderByChild("Music_ID").equalTo(music_ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot msnap : snapshot.getChildren()) {
                                Log.i("Valuemusicinfo", snapshot.getValue().toString());
                                String music_title = msnap.child("Title").getValue(String.class);
                                String music_artist = msnap.child("Composer").getValue(String.class);

                                int m_length = (int) Double.parseDouble(msnap.child("Length").getValue(String.class));
                                int m_m = m_length / 60;
                                int m_s = m_length % 60;
                                String music_length = String.format("%02d:%02d", m_m, m_s);

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

                    MiniPlaylist(playlist_ID);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    // 음악 재생
    public void playmusic(String music_ID) {
        StorageReference smusic = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Music/" + music_ID + ".mp3");
        smusic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    MediaPlayer player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    String url = uri.toString();
                    player.setDataSource(url);
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
        });

    }

    // 미니플레이리스트 재생
    public void MiniPlaylist(int playlist_ID) {
        int minip_id = 2237;


        /* ArtID 담아놓기
        MiniPlaylistArtlist.clear();
        for(int i=0; i<MiniPlaylistIDlist.size();i++){
            minip_id = Integer.parseInt(String.valueOf(MiniPlaylistIDlist.get(i)));
            pminip.orderByChild("MiniPlaylist_ID").equalTo(minip_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot mnsnap : snapshot.getChildren()) {
                        for (DataSnapshot artsnap : mnsnap.child("Art_ID").getChildren()) {
                            MiniPlaylistArtlist.add(artsnap.getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }*/

        minip_id = Integer.parseInt(String.valueOf(MiniPlaylistIDlist.get(0)));
        StartMiniPlaylist(minip_id);
    }

    public void StartMiniPlaylist(int miniplaylistID) {
        DatabaseReference pminip = dref.child("MiniPlaylist");
        DatabaseReference part = dref.child("Art");

        pminip.orderByChild("MiniPlaylist_ID").equalTo(miniplaylistID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MiniPlaylistArtlist.clear();
                for (DataSnapshot mnsnap : snapshot.getChildren()) {
                    // Art_ID (2개 이상인 경우가 있으므로 arraylist) > 우선은 플레이리스트의 모든 미니플- 의 art는 다 넣어둠 -> 추후 수정 (타임스탬프 이용)
                    for (DataSnapshot artsnap : mnsnap.child("Art_ID").getChildren()) {
                        MiniPlaylistArtlist.add(artsnap.getValue(String.class));
                    }
                    Log.i("Valueminiartidlist", MiniPlaylistArtlist.toString());

                    String art_id = (String) MiniPlaylistArtlist.get(0);
                    Log.i("ValueArtID", art_id);

                    part.orderByChild("Art_ID").equalTo(art_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot asnap : snapshot.getChildren()) {
                                Log.i("ValueArtinfo", asnap.getValue().toString());
                                String Gdrive_ID = asnap.child("Gdrive_ID").getValue(String.class);
                                String art_title = asnap.child("Title").getValue(String.class);
                                String art_artist = asnap.child("Artist").getValue(String.class);

                                str_arttitle.setText(art_title);
                                str_artartist.setText(art_artist);

                                Log.i("Gdrive_IDValue", Gdrive_ID);

                                // 명화 불러오기 (1MB 이상 명화 다운 가능 -> 근데 개느림)
                                String fileId = Gdrive_ID;
                                String url = "https://drive.google.com/uc?export=view&id=" + Gdrive_ID;
                                Log.i("ValueURL", url);
                                Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_art);

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
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });


    }

}
