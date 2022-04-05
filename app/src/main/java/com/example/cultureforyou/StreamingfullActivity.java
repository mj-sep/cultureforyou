package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StreamingfullActivity extends AppCompatActivity {

    // 파이어베이스
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;

    private TextView str_full_mood;
    private TextView str_full_arttitle;
    private TextView str_full_artartist;
    private PhotoView str_full_art;
    private ImageView str_full_blur;
    private ImageButton str_full_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_fullview_potrait);


        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();

        // 버튼 및 뷰 정의
        str_full_mood = findViewById(R.id.str_full_mood);
        str_full_arttitle = findViewById(R.id.str_full_arttitle);
        str_full_artartist = findViewById(R.id.str_full_artartist);
        str_full_art = findViewById(R.id.str_full_art);
        str_full_blur = findViewById(R.id.str_full_blur);
        str_full_back = findViewById(R.id.str_full_back);

        Intent intent = getIntent();
        String art_id = intent.getStringExtra("art_id");
        String art_title = intent.getStringExtra("art_title");
        String art_artist = intent.getStringExtra("art_artist");
        String art_drive = intent.getStringExtra("art_gdrive");
        String str_mood = intent.getStringExtra("str_mood");
        str_full_mood.setText(str_mood);

        Log.i("ValueARTID", art_id);

        // str_full_back 클릭 시, 뒤로 가기
        str_full_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        str_full_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_full_mood.getVisibility() == View.VISIBLE) {
                    str_full_mood.setVisibility(View.INVISIBLE);
                    str_full_arttitle.setVisibility(View.INVISIBLE);
                    str_full_artartist.setVisibility(View.INVISIBLE);
                } else {
                    str_full_mood.setVisibility(View.VISIBLE);
                    str_full_arttitle.setVisibility(View.VISIBLE);
                    str_full_artartist.setVisibility(View.VISIBLE);
                }
            }
        });

        str_full_arttitle.setText(art_title);
        str_full_arttitle.setSelected(true);
        str_full_artartist.setText(art_artist);

        // 명화 불러오기
        String url = "https://drive.google.com/uc?export=view&id=" + art_drive;
        Log.i("ValueURL", url);
        Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_full_art);
        // 명화 블러 배경
        Glide.with(getApplicationContext()).load(url).
                apply(bitmapTransform(new BlurTransformation(25,3))).into(str_full_blur);
    }


    /*
    public void playart(String art_id){
        DatabaseReference part = dref.child("Art");
        part.orderByChild("Art_ID").equalTo(art_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot asnap : snapshot.getChildren()) {
                    Log.i("ValueArtinfo", asnap.getValue().toString());
                    String artGdrive_ID = asnap.child("Gdrive_ID").getValue(String.class);
                    String art_title = asnap.child("Title").getValue(String.class);
                    String art_artist = asnap.child("Artist").getValue(String.class);

                    str_full_arttitle.setText(art_title);
                    str_full_arttitle.setSelected(true);
                    str_full_artartist.setText(art_artist);

                    // 명화 불러오기
                    String fileId = artGdrive_ID;
                    String url = "https://drive.google.com/uc?export=view&id=" + artGdrive_ID;
                    Log.i("ValueURL", url);
                    Glide.with(getApplicationContext()).load(url).thumbnail(0.6f).into(str_full_art);
                    // 명화 블러 배경
                    Glide.with(getApplicationContext()).load(url).
                            apply(bitmapTransform(new BlurTransformation(25,3))).into(str_full_blur);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

     */

    }




