package com.example.cultureforyou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton edit_button;
    private ImageButton backward_button;
    private TextView pf_nickname;
    private ImageView profile_icon_image;
    private FirebaseAuth firebaseAuth;
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
        profile_icon_image = findViewById(R.id.profile_icon);
        artist_gridView = findViewById(R.id.artist_gridview);
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
