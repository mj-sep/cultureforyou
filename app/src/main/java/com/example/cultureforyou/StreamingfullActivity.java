package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
    private ImageView str_full_art;
    private ImageView str_full_blur;
    private ImageButton str_full_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_fullview);

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
        String str_mood = intent.getStringExtra("str_mood");
        str_full_mood.setText(str_mood);

        Log.i("ValueARTID", art_id);
        playart(art_id);

        // str_full_back 클릭 시, 뒤로 가기
        str_full_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

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
    }
    // 전체화면 모드 관련
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
