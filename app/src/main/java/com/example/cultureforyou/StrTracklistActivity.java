package com.example.cultureforyou;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class StrTracklistActivity extends AppCompatActivity {

    private StrTrackAdapter stradapter;
    private ListView play_listview;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streaming_tracklist);

        stradapter = new StrTrackAdapter();
        play_listview = findViewById(R.id.play_listview);

        Intent listintent = getIntent();
        ArrayList<String> minimoodlist = (ArrayList<String>) listintent.getSerializableExtra("minimoodlist");
        ArrayList<String> startsecondlist = (ArrayList<String>) listintent.getSerializableExtra("startsecondlist");
        int pos = listintent.getIntExtra("pos", 0);
        Log.d("minimood", String.valueOf(minimoodlist));

        // 인텐트에서 받아온 데이터로 리스트뷰에 띄우기
        for (int i = 0; i < minimoodlist.size(); i++) {
            ListDTO dto = new ListDTO();
            if(i == pos) {
                dto.setCheck(1);
                dto.setMinimood(ChangeAtoB.setMood(minimoodlist.get(i)));
                //dto.setStartsecond("" + i);
                dto.setStartsecond(ChangeTimeFormat(startsecondlist.get(i)));
            } else {
                dto.setCheck(0);
                dto.setMinimood(ChangeAtoB.setMood(minimoodlist.get(i)));
                dto.setStartsecond(ChangeTimeFormat(startsecondlist.get(i)));
            }
            stradapter.addItem(dto);
        }
        play_listview.setAdapter(stradapter);

    }


    private String ChangeTimeFormat(String second) {
        int type = (int) Double.parseDouble(second);
        int m = type / 60;
        int s = type % 60;
        Log.d("type", String.valueOf(type));
        String presenttime = String.format("%02d:%02d", m, s);
        return presenttime;
    }
}
