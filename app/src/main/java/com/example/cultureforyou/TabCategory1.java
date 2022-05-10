package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class TabCategory1 extends Fragment {

    TextView eventnickname;
    RelativeLayout recomm_rainy;
    RelativeLayout recomm_happy;
    RelativeLayout recomm_coffee;
    RelativeLayout recomm_alone;
    RelativeLayout recomm_morning;
    RelativeLayout recomm_walk;
    RelativeLayout recomm_dance;
    RelativeLayout recomm_horror;
    RelativeLayout recomm_sun;
    RelativeLayout recomm_swan;

    View.OnClickListener onClickListener;

    ArrayList<String> picktitlelist = new ArrayList<>();
    ArrayList<String> pickartistlist = new ArrayList<>();
    ArrayList<String> pickmoodlist = new ArrayList<>();
    ArrayList<String> pickplidlist = new ArrayList<String>();

    String pplidlist = "";
    String ptitlelist = "";
    String partistlist = "";
    String pmoodlist = "";
    int seq = 0;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_category1, container, false);

        eventnickname = rootView.findViewById(R.id.eventnickname);
        recomm_rainy = rootView.findViewById(R.id.recomm_rainy);
        recomm_happy = rootView.findViewById(R.id.recomm_happy);
        recomm_coffee = rootView.findViewById(R.id.recomm_coffee);
        recomm_alone = rootView.findViewById(R.id.recomm_alone);
        recomm_morning = rootView.findViewById(R.id.recomm_morning);
        recomm_walk = rootView.findViewById(R.id.recomm_walk);
        recomm_dance = rootView.findViewById(R.id.recomm_dance);
        recomm_horror = rootView.findViewById(R.id.recomm_horror);
        recomm_sun = rootView.findViewById(R.id.recomm_sun);
        recomm_swan = rootView.findViewById(R.id.recomm_swan);

        // 이미지 버튼 클릭 시
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seq = 0; // 순서번호
                switch (v.getId()) {
                    case R.id.recomm_rainy:
                        seq = 0;
                        break;
                    case R.id.recomm_happy:
                        seq = 1;
                        break;
                    case R.id.recomm_coffee:
                        seq = 2;
                        break;
                    case R.id.recomm_alone:
                        seq = 3;
                        break;
                    case R.id.recomm_morning:
                        seq = 4;
                        break;
                    case R.id.recomm_walk:
                        seq = 5;
                        break;
                    case R.id.recomm_dance:
                        seq = 6;
                        break;
                    case R.id.recomm_horror:
                        seq = 7;
                        break;
                    case R.id.recomm_sun:
                        seq = 8;
                        break;
                    case R.id.recomm_swan:
                        seq = 9;
                        break;
                    default:
                        break;
                }
                Intent pickintent1 = new Intent(getActivity(), PickVer1Fragment.class);
                int finalSeq = seq;
                new Thread(() -> {
                    // 드라이브에서 seq에 해당하는 정보 가져와서 분석 후 인텐트
                    // 인텐트 하고 분석하면 화면이 비어 있는 시간이 길어서... 조삼모사지만 사용자 편의상
                    getPickData(finalSeq);

                    pickintent1.putExtra("pick_seq", finalSeq);
                    pickintent1.putExtra("pickplidlist", pickplidlist);
                    pickintent1.putExtra("pickmoodlist", pickmoodlist);
                    pickintent1.putExtra("picktitlelist", picktitlelist);
                    pickintent1.putExtra("pickartistlist", pickartistlist);
                    startActivity(pickintent1);
                }).start();

            }
        };

        recomm_rainy.setOnClickListener(onClickListener);
        recomm_happy.setOnClickListener(onClickListener);
        recomm_coffee.setOnClickListener(onClickListener);
        recomm_alone.setOnClickListener(onClickListener);
        recomm_morning.setOnClickListener(onClickListener);
        recomm_walk.setOnClickListener(onClickListener);
        recomm_dance.setOnClickListener(onClickListener);
        recomm_horror.setOnClickListener(onClickListener);
        recomm_sun.setOnClickListener(onClickListener);
        recomm_swan.setOnClickListener(onClickListener);

        return rootView;
    }


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
                    pplidlist = nextline[PickVer1Fragment.Category_pick.Playlist_ID_list.number];
                    pplidlist = pplidlist.substring(1, pplidlist.length()-1);
                    pmoodlist = nextline[PickVer1Fragment.Category_pick.Playlist_Mood_list.number];
                    pmoodlist = pmoodlist.substring(1, pmoodlist.length()-1);
                    ptitlelist = nextline[PickVer1Fragment.Category_pick.Music_title_list.number];
                    ptitlelist = ptitlelist.substring(1, ptitlelist.length()-1);
                    partistlist = nextline[PickVer1Fragment.Category_pick.Music_artist_list.number];
                    partistlist = partistlist.substring(1, partistlist.length()-1);
                }

            }
            String[] playlist_id_array = pplidlist.split(", ");
            String[] mood_id_array = pmoodlist.split(", ");
            String[] title_list_array = ptitlelist.split(", ");
            String[] artist_list_array = partistlist.split(", ");

            for (int i = 0; i < playlist_id_array.length; i++) {
                pickplidlist.add(playlist_id_array[i]);
                pickmoodlist.add(mood_id_array[i].substring(1, mood_id_array[i].length()-1));
                picktitlelist.add(title_list_array[i].substring(1, title_list_array[i].length()-1));
                pickartistlist.add(artist_list_array[i].substring(1, artist_list_array[i].length()-1));
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
