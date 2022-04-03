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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
    ArrayList<ArrayList<String>> miniplaylist_info_sum = new ArrayList<ArrayList<String>>(); //
    ArrayList<String> miniplaylist_startsecond = new ArrayList<String>(); // 미니플레이리스트의 정보 모음 (2차원)
    ArrayList<String> art_id_list = new ArrayList<String>(); // 미니플레이리스트 내부의 명화 리스트

    String[] mood_extract = new String[16];

    String maxmood = "a0";
    String mood_ext_str = "";

    String music_title = "";
    String music_composer = "";
    String art_id_array ="";
    int pause_position = 0;
    int time = 0;
    int Mlist_id = 0;
    int timer_test = 0;

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
            Log.d("nextline_test", "플레이리스트 데이터 추출 및 재생");

            // 음악 데이터 추출 및 재생
            getMusicData(select_playlist.get(2));
            Log.d("nextline_test", "음악 데이터 추출 및 재생");

            // 미니플레이리스트 추출 및 재생
            getMiniPlaylist(select_playlist.get(1));
            Log.d("nextline_test", "미니플레이리스트 추출 및 재생");

        }).start();
    }


    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public String getPlaylistData(String selectmood){
        try {
            /* 본데이터 Playlist.csv 링크
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
             */
            // 샘플데이터 Playlist.csv 링크
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1-5RiipcJZgjM20xdE3Ok1iHPVzy2q-Ns");

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
            Log.i("nextline_moodsetid_re", moodselectid_result);

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
        miniplaylist_startsecond.clear();
        //miniplaylist_result.clear();

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
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1-1BOG2JFtsxM8nrb10AEOtvwgasSCJ5u");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline3;
            Log.d("nextline_minicsv", miniplaylist_id.get(Mlist_id));
            // id = 0;
            int j = 0;
            int k = 0; // miniplaylist_id.size() == k 이면 break

            // Start_MiniPlaylist(miniplaylist_id.get(Mlist_id));

            TimerTask t1 = new TimerTask() {
                @Override
                public void run() {
                    /*
                    if(time > Integer.valueOf(miniplaylist_startsecond.get(1))) {
                        Mlist_id++;
                    }
                    */
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    Log.i("ValueTime", String.valueOf(time));
                    /*
                    if(time > Integer.valueOf(miniplaylist_startsecond.get(1))){
                        timer_test = 1;
                    }

                     */

                }
            };

            TimerTask t2 = new TimerTask() {
                @Override
                public void run() {
                    if(timer_test == 1) {
                        t1.cancel();
                        timer_test = 0;
                        Mlist_id++;
                        Start_MiniPlaylist(miniplaylist_id.get(Mlist_id));
                    }
                }
            };


            Timer timer = new Timer();
            timer.schedule(t1, 0, 1000);
            //timer.schedule(t2, 0, 300);




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
                        for(int m = 1; m < mood_extract.length; m++) {
                            if (max <= Double.parseDouble(mood_extract[m]))
                                max = Double.parseDouble(mood_extract[m]);
                                maxmood = "a" + m;
                        }
                        // Log.d("nextline_maxmood", maxmood);
                        miniplaylist_info.add(maxmood);

                    } else {
                        miniplaylist_info.add(nextline3[category_miniplaylist[i]]);
                        // Log.d("nextline_info_test", String.valueOf(miniplaylist_info));
                        if(i==2) miniplaylist_startsecond.add(nextline3[category_miniplaylist[2]]);
                    }
                }
                // miniplaylist_info_sum.add(miniplaylist_info);
                Log.d("nextline_miniplaylist_info", k + " " + String.valueOf(miniplaylist_info));
                // Log.d("nextline_mini_result", String.valueOf(miniplaylist_info_sum));
            }

            Log.d("nextline_startsecond", String.valueOf(miniplaylist_startsecond));
            Log.d("nextline_mn", String.valueOf(miniplaylist_startsecond.get(1)));
            Start_MiniPlaylist(miniplaylist_id.get(Mlist_id));
            /*
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

             */


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 각각의 미니플레이리스트 재생
    public void Start_MiniPlaylist(String miniplaylist_id) {
        try {
            Log.d("nextline_startmini", miniplaylist_id);
            // 샘플데이터 코드
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1-1BOG2JFtsxM8nrb10AEOtvwgasSCJ5u");
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
            Log.d("nextline4_array", String.valueOf(art_id_array));
            art_id_array = art_id_array.substring(1, art_id_array.length()-1);
            String[] art_id_list_string = art_id_array.split(", ");
            for (int i = 0; i < art_id_list_string.length; i++) {
                art_id_list.add(art_id_list_string[i]);
            }
            Log.d("nextline4_art", String.valueOf(art_id_list));

            // 미술 데이터 접근
            URL stockURL2 = new URL ("https://drive.google.com/uc?export=view&id=19UtIOWJ18endBWtVnm8pL8C3bEVPAURM");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(stockURL2.openStream()));
            CSVReader reader2 = new CSVReader(in2);
            String[] nextline5;

            int[] category_art_SP = {
                    Category_Art_SP.Art_ID.number,
                    Category_Art_SP.Title.number,
                    Category_Art_SP.Artist.number,
                    Category_Art_SP.Gdrive_ID.number
            };

            String art_id_mini = art_id_list.get(0);
            // String art_id_mini_sub = art_id_mini.substring(1, art_id_mini.length()-1);

            while ((nextline5 = reader2.readNext()) != null) {
                // Log.d("nextline5_array", Arrays.toString(nextline5));
                // Log.d("nextline_ch", nextline4[Category_MiniPlaylist_SP.MiniPlaylist_ID.number]);
                if (nextline5[Category_Art_SP.Art_ID.number].equals(art_id_mini)) {
                    Log.d("nextline5", Arrays.toString(nextline5));
                    //art_id_array = nextline4[category_miniplaylist_sp[2]];

                    break;
                }

            }
                // miniplaylist_info_sum.add(miniplaylist_info);
                // Log.d("nextline_miniplaylist_info", k + " " + String.valueOf(miniplaylist_info));
                // Log.d("nextline_mini_result", String.valueOf(miniplaylist_info_sum));


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
        Art_ID (10),
        MiniPlaylist_ID (15);

        public final int number;

        Category_Miniplaylist(int number) {
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
        Gdrive_ID(14);

        public final int number;
        Category_Art_SP(int number) {this.number = number;}
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

