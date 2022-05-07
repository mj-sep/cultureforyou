package com.example.cultureforyou;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class PopupActivity extends Dialog {
    private final Context context;
    private ImageButton feeling_list_1;
    private ImageButton feeling_list_2;
    private ImageButton feeling_list_3;
    private ImageButton feeling_list_4;
    private ImageButton feeling_list_5;
    private ImageButton feeling_list_6;
    private ImageButton feeling_list_7;
    private ImageButton feeling_list_8;
    private ImageButton feeling_list_9;
    private ImageButton feeling_list_10;
    private ImageButton feeling_list_11;
    private ImageButton feeling_list_12;
    private ImageButton feeling_list_13;
    private ImageButton feeling_list_14;
    private ImageButton feeling_list_15;
    private ImageButton feeling_list_16;
    private ImageButton close_btn;

    View.OnClickListener onClickListener;

    String selectmood = "";
    String streaming="";
    ArrayList<String> moodselect = new ArrayList<>(); // 무드값에 해당하는 플레이리스트ID 집합
    String moodselectid_result = ""; // 무드값에 해당하는 플레이리스트 중 랜덤으로 하나만 추출한 값(ID)
    ArrayList<String> select_playlist = new ArrayList<>(); // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 전체화면 모드 (상태바 제거)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.feeling_list_popup);

        // 버튼 및 뷰 정의
        feeling_list_1 = findViewById(R.id.feeling_list_1);
        feeling_list_2 = findViewById(R.id.feeling_list_2);
        feeling_list_3 = findViewById(R.id.feeling_list_3);
        feeling_list_4 = findViewById(R.id.feeling_list_4);
        feeling_list_5 = findViewById(R.id.feeling_list_5);
        feeling_list_6 = findViewById(R.id.feeling_list_6);
        feeling_list_7 = findViewById(R.id.feeling_list_7);
        feeling_list_8 = findViewById(R.id.feeling_list_8);
        feeling_list_9 = findViewById(R.id.feeling_list_9);
        feeling_list_10 = findViewById(R.id.feeling_list_10);
        feeling_list_11 = findViewById(R.id.feeling_list_11);
        feeling_list_12 = findViewById(R.id.feeling_list_12);
        feeling_list_13 = findViewById(R.id.feeling_list_13);
        feeling_list_14 = findViewById(R.id.feeling_list_14);
        feeling_list_15 = findViewById(R.id.feeling_list_15);
        feeling_list_16 = findViewById(R.id.feeling_list_16);
        close_btn = findViewById(R.id.close_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.feeling_list_1:
                        selectmood = "a0";
                        break;
                    case R.id.feeling_list_2:
                        selectmood = "a1";
                        break;
                    case R.id.feeling_list_3:
                        selectmood = "a2";
                        break;
                    case R.id.feeling_list_4:
                        selectmood = "a3";
                        break;
                    case R.id.feeling_list_5:
                        selectmood = "a4";
                        break;
                    case R.id.feeling_list_6:
                        selectmood = "a5";
                        break;
                    case R.id.feeling_list_7:
                        selectmood = "a6";
                        break;
                    case R.id.feeling_list_8:
                        selectmood = "a7";
                        break;
                    case R.id.feeling_list_9:
                        selectmood = "a8";
                        break;
                    case R.id.feeling_list_10:
                        selectmood = "a9";
                        break;
                    case R.id.feeling_list_11:
                        selectmood = "a10";
                        break;
                    case R.id.feeling_list_12:
                        selectmood = "a11";
                        break;
                    case R.id.feeling_list_13:
                        selectmood = "a12";
                        break;
                    case R.id.feeling_list_14:
                        selectmood = "a13";
                        break;
                    case R.id.feeling_list_15:
                        selectmood = "a14";
                        break;
                    case R.id.feeling_list_16:
                        selectmood = "a15";
                        break;
                    default:
                        selectmood = "";
                        break;
                }

                new Thread(() -> {
                    dismiss();
                    getPlaylistData(selectmood);
                    moodselectid_result = moodselect.get(0);
                    select_playlist = ChangeAtoB.getOnePlaylist(moodselectid_result);
                    Log.i("nextline_moodsetid_re", moodselectid_result);
                    Log.d("nextline_playlist", String.valueOf(select_playlist));
                    Log.d("nextline_test", "플레이리스트 데이터 추출 및 재생");

                    Intent intent = new Intent(context.getApplicationContext(), CSVStreamingActivity.class);
                    intent.putExtra("selectmood", selectmood);
                    intent.putExtra("select_playlist_popup", select_playlist);
                    intent.putExtra("selectplaylistid", moodselectid_result);
                    intent.putExtra("streaming", "0" );
                    intent.putExtra("moodplaylist", moodselect);
                    context.startActivity(intent);
                }).start();

            }
        };

        feeling_list_1.setOnClickListener(onClickListener);
        feeling_list_2.setOnClickListener(onClickListener);
        feeling_list_3.setOnClickListener(onClickListener);
        feeling_list_4.setOnClickListener(onClickListener);
        feeling_list_5.setOnClickListener(onClickListener);
        feeling_list_6.setOnClickListener(onClickListener);
        feeling_list_7.setOnClickListener(onClickListener);
        feeling_list_8.setOnClickListener(onClickListener);
        feeling_list_9.setOnClickListener(onClickListener);
        feeling_list_10.setOnClickListener(onClickListener);
        feeling_list_11.setOnClickListener(onClickListener);
        feeling_list_12.setOnClickListener(onClickListener);
        feeling_list_13.setOnClickListener(onClickListener);
        feeling_list_14.setOnClickListener(onClickListener);
        feeling_list_15.setOnClickListener(onClickListener);
        feeling_list_16.setOnClickListener(onClickListener);

    }


    public PopupActivity(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public void getPlaylistData(String selectmood){
        try {
            /* 본데이터 Playlist.csv 링크
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
            https://drive.google.com/uc?export=view&id=1-5RiipcJZgjM20xdE3Ok1iHPVzy2q-Ns
             */
            // 샘플데이터 Playlist.csv 링크
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
            // 본 String pid = "1ULBLk0bYuSeBAbXtyGSmzBA3djOQpeI2lZkP_2YMFyo";

            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            //HttpsURLConnection urlConnection = (HttpsURLConnection) stockURL.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.connect();

            //System.out.println("ResponseCode: " + urlConnection.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));

            CSVReader reader = new CSVReader(in);
            String[] nextline;
            Integer j = 0;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (!nextline[CSVStreamingActivity.Category.Playlist_Mood.number].equals(selectmood)) {
                    continue;
                }
                Log.d("nextline_csv", Arrays.toString(nextline));

                // 무드의 플레이리스트 ID 기록
                moodselect.add(nextline[CSVStreamingActivity.Category.Playlist_ID.number]);
            }

            // 플레이리스트 랜덤섞기
            Collections.shuffle(moodselect);
            Log.d("nextline_moodselect", String.valueOf(moodselect));
            // in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
