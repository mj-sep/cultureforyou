package com.example.cultureforyou;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private Intent playIntent;
    private boolean musicBound = false;

    ImageButton str_arrow;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streaming_tracklist);

        stradapter = new StrTrackAdapter();
        play_listview = findViewById(R.id.play_listview);
        str_arrow = findViewById(R.id.str_arrow);

        // onStart();

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

        str_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CSVStreamingActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void onNewIntent(Intent intent2) {
        onStart();
        /*
        isplayingnow = intent2.getIntExtra("isPlayingNow", 0);
        Log.d("Service: isPlayingg", String.valueOf(isplayingnow));
         */
        super.onNewIntent(intent2);
    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    public void onStop(){
        super.onStop();
        unbindService(conn);
        isService = false;
    }

    private String ChangeTimeFormat(String second) {
        int type = (int) Double.parseDouble(second);
        int m = type / 60;
        int s = type % 60;
        Log.d("type", String.valueOf(type));
        String presenttime = String.format("%02d:%02d", m, s);
        return presenttime;
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

}
