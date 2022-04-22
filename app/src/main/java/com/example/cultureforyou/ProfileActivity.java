package com.example.cultureforyou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
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
import java.util.HashMap;

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
    private Switch pf_ed_switch;
    FirebaseDatabase database;
    GridView artist_gridView;
    String profile_icon;
    String nickname;
    Integer annivonoff;
    String anniv_mood;
    String anniv_name;
    String anniv_date;
    String[] fav_artist_img;
    String[] fav_artist_name;

    ArrayList fav_img = new ArrayList<>();
    ArrayList fav_name = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        edit_button = findViewById(R.id.profile_edit_button);
        backward_button = findViewById(R.id.backward_button);
        pf_nickname = findViewById(R.id.pf_nickname);
        tx_anniv_date = findViewById(R.id.anniv_date);
        tx_anniv_name = findViewById(R.id.pf_ed_anniv_name);
        tx_anniv_mood = findViewById(R.id.anniv_mood);
        profile_icon_image = findViewById(R.id.profile_icon);
        sw_anniv = findViewById(R.id.sw_anniv);
        anniv_off_text = findViewById(R.id.anniv_off_text);
        artist_gridView = findViewById(R.id.artist_gridview);
        pf_scrollView = findViewById(R.id.pf_scrollview);
        pf_ed_switch = findViewById(R.id.pf_ed_switch);
        firebaseAuth = FirebaseAuth.getInstance();
        fav_img.clear();
        fav_name.clear();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("select_uil", uid);
        }
        String id = user.getUid();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.orderByChild("uid").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    nickname = snapshot1.child("nickname").getValue(String.class);
                    profile_icon = snapshot1.child("profile_icon").getValue(String.class);
                    annivonoff = snapshot1.child("anniv_onoff").getValue(Integer.class);

                    if(annivonoff == 1) {
                        anniv_date = snapshot1.child("anniversary").getValue(String.class);
                        anniv_name = snapshot1.child("anniversary_name").getValue(String.class);
                        anniv_mood = snapshot1.child("anni_mood").getValue(String.class);
                        Log.d("fav_onoff", "on");
                    }

                    for(DataSnapshot favsnapshot: snapshot1.child("favorite_artist_num").getChildren()){
                        fav_img.add(favsnapshot.getValue(long.class));
                    }
                    for(DataSnapshot favsnapshot2: snapshot1.child("favorite_artist").getChildren()){
                        fav_name.add(favsnapshot2.getValue(String.class));
                    }

                    Log.d("select_fav", String.valueOf(fav_img));
                    Log.d("select_fav2", String.valueOf(fav_name));

                    // 프로필 이미지
                    // profile_icon = "https://drive.google.com/uc?export=view&id=" + profile_icon;

                    HashMap<String, Integer> icons = new HashMap<String, Integer>();
                    icons.put("icon_painting_1", Integer.valueOf(R.drawable.icon_painting_1));
                    icons.put("icon_painting_2", Integer.valueOf(R.drawable.icon_painting_2));
                    icons.put("icon_painting_3", Integer.valueOf(R.drawable.icon_painting_3));
                    icons.put("icon_painting_4", Integer.valueOf(R.drawable.icon_painting_4));
                    icons.put("icon_painting_5", Integer.valueOf(R.drawable.icon_painting_5));
                    icons.put("icon_painting_6", Integer.valueOf(R.drawable.icon_painting_6));
                    icons.put("icon_painting_7", Integer.valueOf(R.drawable.icon_painting_7));
                    icons.put("icon_painting_8", Integer.valueOf(R.drawable.icon_painting_8));
                    icons.put("icon_painting_9", Integer.valueOf(R.drawable.icon_painting_9));
                    icons.put("icon_painting_10", Integer.valueOf(R.drawable.icon_painting_10));
                    icons.put("icon_painting_11", Integer.valueOf(R.drawable.icon_painting_11));
                    icons.put("icon_painting_12", Integer.valueOf(R.drawable.icon_painting_12));
                    icons.put("icon_painting_13", Integer.valueOf(R.drawable.icon_painting_13));
                    icons.put("icon_painting_14", Integer.valueOf(R.drawable.icon_painting_14));
                    icons.put("icon_painting_15", Integer.valueOf(R.drawable.icon_painting_15));

                    Glide.with(getApplicationContext()).load(icons.get(profile_icon).intValue()).transform(new CenterCrop(), new RoundedCorners(16)).into(profile_icon_image);
                    // profile_icon_image.setImageResource(icons.get(select_icon).intValue());

                    // 기념일 설정
                    if(annivonoff == 1){
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
                    fav_artist_img = new String[fav_img.size()];
                    for(int i=0; i<fav_img.size(); i++) {
                        fav_artist_img[i] = (String.valueOf(fav_img.get(i)));
                        fav_artist_img[i] = ChangeAtoB.FA_gdrive_id(fav_artist_img[i]);
                    }
                    Log.d("select_fav4", (Arrays.toString(fav_artist_img)));

                    fav_artist_name = new String[fav_name.size()];
                    for(int i=0; i<fav_name.size(); i++) {
                        fav_artist_name[i] = (String) fav_name.get(i);
                    }

                    // 선호하는 아티스트 어댑터
                    FavArtistAdapter favArtistAdapter = new FavArtistAdapter(ProfileActivity.this, fav_artist_name, fav_artist_img);
                    artist_gridView.setAdapter(favArtistAdapter);
                    pf_scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            pf_scrollView.scrollTo(0,0);
                        }
                    });


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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });




        // 프로필 수정 버튼 클릭 시
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(annivonoff == 1) {
                    to_profile_edit_on(anniv_date, anniv_name, anniv_mood);
                } else {
                    to_profile_edit_off();
                }
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

    public void to_profile_edit_on(String anniv_date, String anniv_name, String anniv_mood) {
        finish();
        Intent intent2 = new Intent(getApplicationContext(),ProfileEditActivity.class);
        intent2.putExtra("profile_icon", profile_icon);
        intent2.putExtra("nickname", nickname);
        intent2.putExtra("anniv_onoff", 1);
        intent2.putExtra("anniv_date", anniv_date);
        intent2.putExtra("anniv_name", anniv_name);
        intent2.putExtra("anniv_mood", anniv_mood);
        intent2.putExtra("fav_artist_img", fav_artist_img);
        intent2.putExtra("fav_artist_name", fav_artist_name);
        startActivity(intent2);
    }

    public void to_profile_edit_off() {
        finish();
        Intent intent2 = new Intent(getApplicationContext(),ProfileEditActivity.class);
        intent2.putExtra("profile_icon", profile_icon);
        intent2.putExtra("nickname", nickname);
        intent2.putExtra("fav_artist_img", fav_artist_img);
        intent2.putExtra("fav_artist_name", fav_artist_name);
        startActivity(intent2);
    }

}
