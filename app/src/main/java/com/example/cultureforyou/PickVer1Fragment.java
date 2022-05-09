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

public class PickVer1Fragment extends AppCompatActivity {

    private View view;
    private ImageView pick_image;
    private ImageView pick_back;
    private TextView pick_title;
    private ImageButton pick_play;
    private RecyclerView pick_ver1_list;

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


    /*
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.pick_version1, container, false);

        pick_image = view.findViewById(R.id.pick_image);
        pick_title = view.findViewById(R.id.pick_title);
        pick_play = view.findViewById(R.id.pick_play);
        pick_ver1_list = view.findViewById(R.id.pick_ver1_list);


        return view;
    }

     */

    protected void onCreate(@Nullable Bundle savedInstaceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstaceState);
        setContentView(R.layout.pick_version1);

        pick_image = findViewById(R.id.pick_image);
        pick_back = findViewById(R.id.pick_back);
        pick_title = findViewById(R.id.pick_title);
        pick_play = findViewById(R.id.pick_play);
        pick_ver1_list = findViewById(R.id.pick_ver1_list);

        Intent pickintent1 = getIntent();
        seq = pickintent1.getIntExtra("pick_seq", 0);

        pick_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new Thread(()-> {
            Log.d("pickver11_seq", String.valueOf(seq));
            getPickData(seq);
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
                pick_title.setText("카페에서 틀고 싶은");
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


    public void getPickData(int seq){
        try {
            String pid = "1iS8GJc-vYba6TdNZ9XVw6iJ9lBqT0NrR";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[1].equals(String.valueOf(seq))) {
                    Log.d("nextline_pickver11_real", Arrays.toString(nextline));
                    pplidlist = nextline[Category_pick.Playlist_ID_list.number];
                    pplidlist = pplidlist.substring(1, pplidlist.length()-1);
                    pmoodlist = nextline[Category_pick.Playlist_Mood_list.number];
                    pmoodlist = pmoodlist.substring(1, pmoodlist.length()-1);
                    ptitlelist = nextline[Category_pick.Music_title_list.number];
                    ptitlelist = ptitlelist.substring(1, ptitlelist.length()-1);
                    partistlist = nextline[Category_pick.Music_artist_list.number];
                    partistlist = partistlist.substring(1, partistlist.length()-1);
                }

            }
            String[] playlist_id_array = pplidlist.split(", ");
            String[] mood_id_array = pmoodlist.split(", ");
            String[] title_list_array = ptitlelist.split(", ");
            String[] artist_list_array = partistlist.split(", ");

            for (int i = 0; i < playlist_id_array.length; i++) {
                pickplidlist.add(playlist_id_array[i]);
                pickmoodlist.add(mood_id_array[i]);
                picktitlelist.add(title_list_array[i]);
                pickartistlist.add(artist_list_array[i]);
            }
            Log.d("nextline_pickver11_plid", String.valueOf(pickplidlist));
            Log.d("nextline_pickver11_pmood", String.valueOf(pickmoodlist));
            Log.d("nextline_pickver11_ptitle", String.valueOf(picktitlelist));
            Log.d("nextline_pickver11_partist", String.valueOf(pickartistlist));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
