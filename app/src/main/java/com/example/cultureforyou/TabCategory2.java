package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class TabCategory2 extends Fragment {

    TextView eventnickname;
    RelativeLayout recomm_mozart;
    RelativeLayout recomm_haydn;
    RelativeLayout recomm_chai;
    RelativeLayout recomm_bach;
    RelativeLayout recomm_brahms;
    RelativeLayout recomm_handel;

    View.OnClickListener onClickListener;

    ArrayList<String> picktitlelist2 = new ArrayList<>();
    ArrayList<String> pickartistlist2 = new ArrayList<>();
    ArrayList<String> pickmoodlist2 = new ArrayList<>();
    ArrayList<String> pickplidlist2 = new ArrayList<String>();

    String pplidlist2 = "";
    String ptitlelist2 = "";
    String partistlist2 = "";
    String pmoodlist2 = "";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_category2, container, false);

        eventnickname = rootView.findViewById(R.id.eventnickname);
        recomm_mozart = rootView.findViewById(R.id.recomm_mozart);
        recomm_haydn = rootView.findViewById(R.id.recomm_haydn);
        recomm_chai = rootView.findViewById(R.id.recomm_chai);
        recomm_bach = rootView.findViewById(R.id.recomm_bach);
        recomm_brahms = rootView.findViewById(R.id.recomm_brahms);
        recomm_handel = rootView.findViewById(R.id.recomm_handel);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seq = 10; // 순서번호
                switch (v.getId()) {
                    case R.id.recomm_mozart:
                        seq = 10;
                        break;
                    case R.id.recomm_bach:
                        seq = 11;
                        break;
                    case R.id.recomm_haydn:
                        seq = 12;
                        break;
                    case R.id.recomm_brahms:
                        seq = 13;
                        break;
                    case R.id.recomm_chai:
                        seq = 14;
                        break;
                    case R.id.recomm_handel:
                        seq = 15;
                        break;
                    default:
                        break;
                }
                Intent pickintent2 = new Intent(getActivity(), PickVer2Fragment.class);
                int finalSeq = seq;
                new Thread(() -> {
                    // 드라이브에서 seq에 해당하는 정보 가져와서 분석 후 인텐트
                    // 인텐트 하고 분석하면 화면이 비어 있는 시간이 길어서... 조삼모사지만 사용자 편의상
                    getPickData(finalSeq);

                    pickintent2.putExtra("pick_seq", finalSeq);
                    pickintent2.putExtra("pickplidlist", pickplidlist2);
                    pickintent2.putExtra("pickmoodlist", pickmoodlist2);
                    pickintent2.putExtra("picktitlelist", picktitlelist2);
                    pickintent2.putExtra("pickartistlist", pickartistlist2);
                    startActivity(pickintent2);
                }).start();


            }
        };

        recomm_mozart.setOnClickListener(onClickListener);
        recomm_bach.setOnClickListener(onClickListener);
        recomm_haydn.setOnClickListener(onClickListener);
        recomm_brahms.setOnClickListener(onClickListener);
        recomm_chai.setOnClickListener(onClickListener);
        recomm_handel.setOnClickListener(onClickListener);

        return rootView;
    }

    public void getPickData(int seq) {
        try {
            String pid = "1iS8GJc-vYba6TdNZ9XVw6iJ9lBqT0NrR";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[1].equals(String.valueOf(seq))) {
                    Log.d("nextline_pickver22_real", Arrays.toString(nextline));
                    pplidlist2 = nextline[PickVer1Fragment.Category_pick.Playlist_ID_list.number];
                    pplidlist2 = pplidlist2.substring(1, pplidlist2.length() - 1);
                    pmoodlist2 = nextline[PickVer1Fragment.Category_pick.Playlist_Mood_list.number];
                    pmoodlist2 = pmoodlist2.substring(1, pmoodlist2.length() - 1);
                    ptitlelist2 = nextline[PickVer1Fragment.Category_pick.Music_title_list.number];
                    ptitlelist2 = ptitlelist2.substring(1, ptitlelist2.length() - 1);
                    partistlist2 = nextline[PickVer1Fragment.Category_pick.Music_artist_list.number];
                    partistlist2 = partistlist2.substring(1, partistlist2.length() - 1);
                }

            }
            String[] playlist_id_array = pplidlist2.split(", ");
            String[] mood_id_array = pmoodlist2.split(", ");
            String[] title_list_array = ptitlelist2.split(", ");
            String[] artist_list_array = partistlist2.split(", ");

            for (int i = 0; i < playlist_id_array.length; i++) {
                pickplidlist2.add(playlist_id_array[i]);
                pickmoodlist2.add(mood_id_array[i].substring(1, mood_id_array[i].length()-1));
                picktitlelist2.add(title_list_array[i].substring(1, title_list_array[i].length()-1));
                pickartistlist2.add(artist_list_array[i].substring(1, artist_list_array[i].length()-1));
            }
            Log.d("nextline_pickver22_plid", String.valueOf(pickplidlist2));
            Log.d("nextline_pickver22_pmood", String.valueOf(pickmoodlist2));
            Log.d("nextline_pickver22_ptitle", String.valueOf(picktitlelist2));
            Log.d("nextline_pickver22_partist", String.valueOf(pickartistlist2));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
