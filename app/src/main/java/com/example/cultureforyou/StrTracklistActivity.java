package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StrTracklistActivity extends DialogFragment {
    // private final Context context;
    private StrTrackAdapter stradapter;
    private ListView play_listview;
    private TextView trackmood;
    private ImageView imageView3;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private Intent playIntent;
    private boolean musicBound = false;

    ImageButton str_arrow;


    /*
    public StrTracklistActivity(@NonNull Context context) {
        super(context);
        this.context = context;
    }

     */

    static StrTracklistActivity newInstance(){
        return new StrTracklistActivity();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //
        */
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullscreendialog);
        // setContentView(R.layout.streaming_tracklist);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.streaming_tracklist, container, false);

        stradapter = new StrTrackAdapter();
        play_listview = view.findViewById(R.id.play_listview);
        str_arrow = view.findViewById(R.id.str_arrow);
        trackmood = view.findViewById(R.id.trackmood);
        imageView3 = view.findViewById(R.id.imageView3);
        //imageView2 = findViewById(R.id.imageView2);

        //tracklayout.getBackground().setAlpha(51);

        // onStart();

        /*Intent listintent = getIntent();
        String selectmood = listintent.getStringExtra("selectmood");
        ArrayList<String> moodtracklist = (ArrayList<String>) listintent.getSerializableExtra("moodtracklist");
        ArrayList<String> moodtracktitle = (ArrayList<String>) listintent.getSerializableExtra("moodtracktitle");
        ArrayList<String> moodtrackcomposer = (ArrayList<String>) listintent.getSerializableExtra("moodtrackcomposer");
        int pos = listintent.getIntExtra("pos", 0); */

        String selectmood = ChangeAtoB.setSelectmood();
        ArrayList<String> moodtracklist = ChangeAtoB.setMoodtracklist();
        ArrayList<String> moodtracktitle = ChangeAtoB.setMoodtracktitle();
        ArrayList<String> moodtrackcomposer = ChangeAtoB.setMoodtrackcomposer();

        Glide.with(this).load(R.drawable.transparents).apply(bitmapTransform(new BlurTransformation(25,3))).into(imageView3);


        // imageView3.setAlpha(51);
        trackmood.setText(ChangeAtoB.setMood(selectmood));

        // 인텐트에서 받아온 데이터로 리스트뷰에 띄우기
        for (int i = 0; i < moodtracklist.size(); i++) {
            ListDTO dto = new ListDTO();
            dto.setMoodtxt(ChangeAtoB.setMood(selectmood));
            dto.setMplaylistid(moodtracklist.get(i));
            dto.setMtitle(moodtracktitle.get(i));
            dto.setMcomposer(moodtrackcomposer.get(i));
            dto.setMoodimg(ChangeAtoB.changeimg(selectmood));

            stradapter.addItem(dto);
        }


        play_listview.setAdapter(stradapter);

        str_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


}

