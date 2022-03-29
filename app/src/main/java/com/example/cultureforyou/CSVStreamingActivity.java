package com.example.cultureforyou;

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

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

public class CSVStreamingActivity extends AppCompatActivity {

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

    ArrayList<String> moodselect = new ArrayList<>(); // 무드값에 해당하는 플레이리스트ID 집합
    String moodselectid_result = ""; // 무드값에 해당하는 플레이리스트 중 랜덤으로 하나만 추출한 값(ID)
    ArrayList<String> select_playlist = new ArrayList<>(); // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트
    ArrayList<String> music_info = new ArrayList<>(); // 음악 정보
    ArrayList<String> miniplaylist_id = new ArrayList<>(); // 미니플레이리스트 ID 집합 (플레이리스트 내부)
    ArrayList<String> miniplaylist_info = new ArrayList<>(); // 미니플레이리스트 1개의 정보
    ArrayList<ArrayList<String>> miniplaylist_result = new ArrayList<ArrayList<String>>(); // 미니플레이리스트의 정보 모음 (2차원)

    String music_title = "";
    String music_composer = "";
    int pause_position = 0;
    int time = 0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streamingpage);

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
        str_presentsecond = findViewById(R.id.str_presentsecond);
        str_endsecond = findViewById(R.id.str_endsecond);

        Intent intent = getIntent();
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

        // 플레이리스트 무드 텍스트
        str_mood.setText(ChangeAtoB.setMood(selectmood));


        new Thread(() -> {
            moodselect.clear();
            select_playlist = ChangeAtoB.getOnePlaylist(getPlaylistData(selectmood));
            Log.d("nextline_playlist", String.valueOf(select_playlist));

            // 음악 데이터 추출 및 재생
            getMusicData(select_playlist.get(2));

            // 미니플레이리스트 추출 및 재생
            getMiniPlaylist(select_playlist.get(1));

        }).start();
    }


    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public String getPlaylistData(String selectmood){
        try {
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline;
            Integer j = 0;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (!nextline[Category.Playlist_Mood.number].equals(selectmood)) {
                    continue;
                }
                Log.d("nextline_csv", Arrays.toString(nextline));

                // 무드의 플레이리스트 ID 기록
                moodselect.add(nextline[Category.Playlist_ID.number]);
            }

            int moodselectid = (int) (Math.random() * moodselect.size());
            moodselectid_result = moodselect.get(moodselectid);
            Log.d("nextline_moodselect", String.valueOf(moodselect));
            Log.i("nextline_moodselectid_re", moodselectid_result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return moodselectid_result;
    }


    // 음악 데이터
    public void getMusicData(String Music_ID) {
        music_info.clear();
        try {
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1oAJMB1I-IBdCBWDazYOcDLkU1t7kOCQS");
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

            Log.d("nextline_music_data", String.valueOf(music_info));

            // 음악 정보 텍스트뷰에 띄움
            String music_title = music_info.get(1);
            String music_composer = music_info.get(2);
            String music_length = music_info.get(3);
            playmusic(music_info.get(4));

            // 음악 길이 mm:ss 단위로 변경
            int m_length = (int) Double.parseDouble(music_length);
            int m_m = m_length / 60;
            int m_s = m_length % 60;
            String music_length_cast = String.format("%02d:%02d", m_m, m_s);

            // Only the original thread that created a view hierarchy can touch its views. < 에러 해결 위한 코드
            runOnUiThread(new Runnable() {
                public void run() {
                    str_musictitle.setText(music_title);
                    str_musictitle.setSelected(true);
                    str_musicartist.setText(music_composer);
                    str_endsecond.setText(music_length_cast);
                }
            });

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
        miniplaylist_result.clear();


        String mini = miniplaylist;
        mini = mini.substring(1, mini.length()-1);
        String[] mini_id_array = mini.split(", ");
        for (int i = 0; i < mini_id_array.length; i++) {
            miniplaylist_id.add(mini_id_array[i]);
        }
        Log.d("nextline_mini", String.valueOf(miniplaylist_id));

        try {
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1HK38JL41YaDo9_MQA5Cnvs8YykDP14qS");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline3;
            int j = 0;
            int k = 0; // miniplaylist_id.size() == k 이면 break

            int[] category_miniplaylist = {
                    Category_Miniplaylist.MiniPlaylist_ID.number,
                    Category_Miniplaylist.MT_Value.number,
                    Category_Miniplaylist.Start_Second.number,
                    Category_Miniplaylist.End_Second.number,
                    Category_Miniplaylist.MT_Length.number,
                    Category_Miniplaylist.AM_ID.number
            };

            while ((nextline3 = reader.readNext()) != null) {
                miniplaylist_info.clear();
                // miniplaylist_id.size() == k 이면 break
                if(k == miniplaylist_id.size()) break;

                // 미니플레이리스트의 데이터 추출 (플레이리스트의 Music_ID값이 같은 것 중에 MT_ID가 같은 값들)
                if (!nextline3[Category_Miniplaylist.MiniPlaylist_ID.number].equals(miniplaylist_id.get(j))) {
                    continue;
                }

                j++; k++;

                for (int i = 0; i < category_miniplaylist.length; i++) {
                    miniplaylist_info.add(nextline3[category_miniplaylist[i]]);
                    // Log.d("nextline_info_test", String.valueOf(miniplaylist_info));
                }
                miniplaylist_result.add(miniplaylist_info);


                Log.d("nextline_miniplaylist_info", k + String.valueOf(miniplaylist_info));
                Log.d("nextline_mini_result", String.valueOf(miniplaylist_result));

                /*
                j++;
                Log.d("nextline_minip", Arrays.toString(nextline3));

                 */
            }



/*
            for(int h=0; h < miniplaylist_id.size(); h++) {
                for (int i = 0; i < category_miniplaylist.length; i++) {
                    miniplaylist_info.add(nextline3[category_miniplaylist[i]]);
                }
                miniplaylist_result.add(miniplaylist_info);
                Log.d("nextline_miniplaylist_info", String.valueOf(miniplaylist_info));
            }

 */

            /*
            // 음악 정보 텍스트뷰에 띄움
            String music_title = music_info.get(1);
            String music_composer = music_info.get(2);
            String music_length = music_info.get(3);
            playmusic(music_info.get(4));

            // 음악 길이 mm:ss 단위로 변경
            int m_length = (int) Double.parseDouble(music_length);
            int m_m = m_length / 60;
            int m_s = m_length % 60;
            String music_length_cast = String.format("%02d:%02d", m_m, m_s);

            // Only the original thread that created a view hierarchy can touch its views. < 에러 해결 위한 코드
            runOnUiThread(new Runnable() {
                public void run() {
                    str_musictitle.setText(music_title);
                    str_musictitle.setSelected(true);
                    str_musicartist.setText(music_composer);
                    str_endsecond.setText(music_length_cast);
                }
            });

            Log.d("nextline_music_title", music_title);
            Log.d("nextline_music_composer", music_composer);


             */

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

    public enum Category_Miniplaylist {
        MT_Value(4),
        Start_Second(6),
        End_Second(7),
        MT_Length(8),
        AM_ID (9),
        MiniPlaylist_ID (15);

        public final int number;

        Category_Miniplaylist(int number) {
            this.number = number;
        }
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
                        runOnUiThread(new Runnable() {
                            public void run() {
                                str_start.setImageResource(R.drawable.str_stop);
                            }
                        });

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
}

