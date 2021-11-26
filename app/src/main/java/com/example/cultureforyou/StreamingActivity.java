package com.example.cultureforyou;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Trace;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StreamingActivity extends MainActivity {
    private TextView str_mood;
    private TextView str_musictitle;
    private TextView str_musicartist;
    private TextView str_presentsecond;
    private TextView str_endsecond;
    private SeekBar str_seekbar;
    private ImageButton str_start;
    List MiniPlaylistIDlist = new ArrayList();


    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;
    //MediaPlayer mMediaplayer;

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
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);
        str_seekbar = findViewById(R.id.str_seekbar);

        Intent intent = getIntent();
        String selectmood = intent.getStringExtra("selectmood");
        str_mood.setText(selectmood);
        Startplaylist(selectmood);


    }

    // playlist
    public void Startplaylist(String mood) {
        DatabaseReference plist = dref.child("Playlist");
        DatabaseReference pmusic = dref.child("Music");
        DatabaseReference pminip = dref.child("MiniPlaylist");
        //List<MiniPlaylistID> MiniPlaylist_ID_list  = new ArrayList<>();

        // 문제 발생 !! 존나 느림... 거진 삼중루프라 코드가 더러움... -> 수정해야함
        // Playlist 스키마 -> ID와 음악 ID 가져오기
        plist.orderByChild("Playlist_ID").equalTo(7).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // MiniPlaylist_ID_list.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.i("Valueplaylistimfo", dataSnapshot.getValue().toString());
                    MiniPlaylistIDlist.clear();

                    // miniplaylist 값 끌어오기
                    MiniPlaylistIDlist.add(snapshot.child("MiniPlaylist_ID_list").getValue().toString());
                    Log.i("ValueMiniPlaylist", MiniPlaylistIDlist.toString());

                    // Playlist-Music_ID 추출
                    String music_ID = snapshot.child("Music_ID").getValue(String.class);
                    // 음악 재생 (Music_ID 이용)
                    playmusic(music_ID);


                    // Music-Title, Composer 추출
                   pmusic.orderByChild("Music_ID").equalTo(music_ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot msnap: snapshot.getChildren()){
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


                    //str_musictitle.setText(music_ID);
                    //StartMiniPlaylist();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    // 음악 재생
    public void playmusic(String music_ID){
        StorageReference smusic = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Music/" + music_ID +".mp3");
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
                            if(fromUser) player.seekTo(progress);

                            int m = progress / 1000 / 60;
                            int s = progress / 1000 % 60;
                            String presenttime = String.format("%02d:%02d", m, s);
                            str_presentsecond.setText(presenttime);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}
                    });
                    player.start();
                    // 쓰레드 생성
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (player.isPlaying()){ // 음악이 실행 중일 때
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
    /*private void StartMiniPlaylist(){
        String music_ID = str_musictitle.getText().toString();
        DatabaseReference pmusic = dref.child("Music");

        // Music 스키마 -> 음악 제목과 작곡가 가져오기
        pmusic.orderByChild("Music_ID").equalTo(music_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot msnap: snapshot.getChildren()){
                    Log.i("Valuemusicinfo", snapshot.getValue().toString());
                    String music_title = msnap.child("Title").getValue(String.class);
                    String music_artist = msnap.child("Composer").getValue(String.class);
                    str_musictitle.setText(music_title);
                    str_musicartist.setText(music_artist);

                    String music_filename = msnap.child("Filename").getValue(String.class);
                    StorageReference smusic = storage.getReferenceFromUrl("gs://cultureforyou-b4b12.appspot.com/Music/" + music_filename);
                    smusic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                MediaPlayer player = new MediaPlayer();
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                String url = uri.toString();
                                player.setDataSource(url);
                                player.prepare();
                                player.start();
                            } catch (IOException e) {
                                Log.i("ValueError", "error playing audio");
                                e.printStackTrace();
                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }*/

}
