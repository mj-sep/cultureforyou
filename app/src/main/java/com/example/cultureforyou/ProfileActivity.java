package com.example.cultureforyou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton edit_button;
    private ImageButton backward_button;
    private TextView pf_nickname;
    private TextView tx_anniv_date;
    private TextView tx_anniv_name;
    private TextView tx_anniv_mood;
    private ImageView profile_icon_image;
    private FirebaseAuth firebaseAuth;
    private ScrollView pf_scrollView;
    private RelativeLayout sw_anniv;
    private TextView anniv_off_text;
    FirebaseDatabase database;
    GridView artist_gridView;

    ArrayList<String> fav_img = new ArrayList<>();
    ArrayList<String> fav_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        edit_button = findViewById(R.id.edit_button);
        backward_button = findViewById(R.id.backward_button);
        pf_nickname = findViewById(R.id.pf_nickname);
        tx_anniv_date = findViewById(R.id.anniv_date);
        tx_anniv_name = findViewById(R.id.anniv_name);
        tx_anniv_mood = findViewById(R.id.anniv_mood);
        profile_icon_image = findViewById(R.id.profile_icon);
        sw_anniv = findViewById(R.id.sw_anniv);
        anniv_off_text = findViewById(R.id.anniv_off_text);
        artist_gridView = findViewById(R.id.artist_gridview);
        pf_scrollView = findViewById(R.id.pf_scrollview);
        firebaseAuth = FirebaseAuth.getInstance();
        fav_img.clear();
        fav_name.clear();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // intent getExtra
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("unickname");
        String profile_icon = "https://drive.google.com/uc?export=view&id=" + intent.getStringExtra("profile_icon");
        Glide.with(getApplicationContext()).load(profile_icon).transform(new CenterCrop(), new RoundedCorners(16)).into(profile_icon_image);

        String anniv_onoff = intent.getStringExtra("anniv_onoff");


        // 기념일 설정
        if(anniv_onoff == "1"){
            String anniv_mood = intent.getStringExtra("anniv_mood");
            String anniv_name = intent.getStringExtra("anniv_name");
            String anniv_date = intent.getStringExtra("anniv_date");
            String[] array_date = anniv_date.split("-");
            tx_anniv_date.setText(array_date[0] + "월 " + array_date[1] + "일");
            tx_anniv_mood.setText(anniv_mood);
            tx_anniv_name.setText(anniv_name);
            anniv_off_text.setVisibility(View.INVISIBLE);
        } else {
            sw_anniv.setVisibility(View.INVISIBLE);
            anniv_off_text.setVisibility(View.VISIBLE);
        }

        // intent getExtra ArrayList - 선호하는 아티스트
        fav_img = (ArrayList<String>) intent.getSerializableExtra("fav_artist_img");
        Serializable s = intent.getSerializableExtra("fav_artist_img");
        String[] fav_artist_img = new String[fav_img.size()];
        for(int i=0; i<fav_img.size(); i++) {
            fav_artist_img[i] = (String.valueOf(fav_img.get(i)));
            fav_artist_img[i] = ChangeAtoB.FA_gdrive_id(fav_artist_img[i]);
        }
        Log.d("select_fav4", (Arrays.toString(fav_artist_img)));

        fav_name = (ArrayList<String>) intent.getSerializableExtra("fav_artist_name");
        Serializable s2 = intent.getSerializableExtra("fav_artist_name");
        String[] fav_artist_name = fav_name.toArray(new String[fav_name.size()]);
        for(int i=0; i<fav_name.size(); i++) {
            fav_artist_name[i] = fav_name.get(i);
        }

        // 선호하는 아티스트 어댑터
        FavArtistAdapter favArtistAdapter = new FavArtistAdapter(ProfileActivity.this, fav_artist_name, fav_artist_img);
        artist_gridView.setAdapter(favArtistAdapter);
        pf_scrollView.post(new Runnable() {
            @Override
            public void run() {
                pf_scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            pf_nickname.setText(nickname);

        }


        // 프로필 수정 버튼 클릭 시
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로 가기 버튼 클릭 시
        backward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
