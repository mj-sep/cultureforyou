package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class FavoriteArtistActivity extends AppCompatActivity {

    private ImageButton favorite_artist_1;
    private ImageButton favorite_artist_2;
    private ImageButton favorite_artist_3;
    private ImageButton favorite_artist_4;
    private ImageButton favorite_artist_5;
    private ImageButton favorite_artist_6;
    private ImageButton favorite_artist_7;
    private ImageButton favorite_artist_8;
    private ImageButton favorite_artist_9;
    private ImageButton favorite_artist_10;
    private ImageButton favorite_artist_11;
    private ImageButton favorite_artist_12;
    private ImageButton favorite_artist_13;
    private ImageButton favorite_artist_14;
    private ImageButton favorite_artist_15;
    private ImageButton favorite_artist_16;
    private ImageButton favorite_artist_17;
    private ImageButton favorite_artist_18;
    private ImageButton btn_next;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;


    View.OnClickListener onClickListener;
    int[] select_artist = new int[18];
    ArrayList<Integer> favorite_artist_num = new ArrayList<>();
    ArrayList<String> favorite_artist = new ArrayList<>();
    int select_check = 0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_artist);

        favorite_artist_1 = findViewById(R.id.favorite_artist_1);
        favorite_artist_2 = findViewById(R.id.favorite_artist_2);
        favorite_artist_3 = findViewById(R.id.favorite_artist_3);
        favorite_artist_4 = findViewById(R.id.favorite_artist_4);
        favorite_artist_5 = findViewById(R.id.favorite_artist_5);
        favorite_artist_6 = findViewById(R.id.favorite_artist_6);
        favorite_artist_7 = findViewById(R.id.favorite_artist_7);
        favorite_artist_8 = findViewById(R.id.favorite_artist_8);
        favorite_artist_9 = findViewById(R.id.favorite_artist_9);
        favorite_artist_10 = findViewById(R.id.favorite_artist_10);
        favorite_artist_11 = findViewById(R.id.favorite_artist_11);
        favorite_artist_12 = findViewById(R.id.favorite_artist_12);
        favorite_artist_13 = findViewById(R.id.favorite_artist_13);
        favorite_artist_14 = findViewById(R.id.favorite_artist_14);
        favorite_artist_15 = findViewById(R.id.favorite_artist_15);
        favorite_artist_16 = findViewById(R.id.favorite_artist_16);
        favorite_artist_17 = findViewById(R.id.favorite_artist_17);
        favorite_artist_18 = findViewById(R.id.favorite_artist_18);
        btn_next = findViewById(R.id.btn_next);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // 인텐트 (1-FavoriteArtistActivity, 2-ProfileEditActivity)
        Intent intent = getIntent();
        int fromActivity = intent.getIntExtra("FromActivity", 0);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_check = 0;

                    switch (v.getId()) {
                        case R.id.favorite_artist_1:
                            if (change_btn(0)) {
                                favorite_artist_1.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_1.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_2:
                            if (change_btn(1)) {
                                favorite_artist_2.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_2.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_3:
                            if (change_btn(2)) {
                                favorite_artist_3.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_3.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_4:
                            if (change_btn(3)) {
                                favorite_artist_4.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_4.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_5:
                            if (change_btn(4)) {
                                favorite_artist_5.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_5.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_6:
                            if (change_btn(5)) {
                                favorite_artist_6.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_6.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_7:
                            if (change_btn(6)) {
                                favorite_artist_7.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_7.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_8:
                            if (change_btn(7)) {
                                favorite_artist_8.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_8.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_9:
                            if (change_btn(8)) {
                                favorite_artist_9.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_9.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_10:
                            if (change_btn(9)) {
                                favorite_artist_10.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_10.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_11:
                            if (change_btn(10)) {
                                favorite_artist_11.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_11.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_12:
                            if (change_btn(11)) {
                                favorite_artist_12.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_12.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_13:
                            if (change_btn(12)) {
                                favorite_artist_13.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_13.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_14:
                            if (change_btn(13)) {
                                favorite_artist_14.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_14.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_15:
                            if (change_btn(14)) {
                                favorite_artist_15.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_15.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_16:
                            if (change_btn(15)) {
                                favorite_artist_16.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_16.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_17:
                            if (change_btn(16)) {
                                favorite_artist_17.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_17.setBackgroundResource(0);
                            break;
                        case R.id.favorite_artist_18:
                            if (change_btn(17)) {
                                favorite_artist_18.setBackgroundResource(R.drawable.favorite_artist_border);
                            } else favorite_artist_18.setBackgroundResource(0);
                            break;
                        default:
                            break;
                    }
                for(int i=0; i<18; i++){
                    if(select_artist[i] == 1) select_check++;
                }
                // 6명 이상 선택시 완료 버튼 비활성화
                if(select_check > 6) {
                    Toast.makeText(FavoriteArtistActivity.this, "최대 6명 선택 가능", Toast.LENGTH_SHORT).show();
                    btn_next.setImageResource(R.drawable.bottom_button_gray);
                } else btn_next.setImageResource(R.drawable.bottom_button);
            }
        };

        favorite_artist_1.setOnClickListener(onClickListener);
        favorite_artist_2.setOnClickListener(onClickListener);
        favorite_artist_3.setOnClickListener(onClickListener);
        favorite_artist_4.setOnClickListener(onClickListener);
        favorite_artist_5.setOnClickListener(onClickListener);
        favorite_artist_6.setOnClickListener(onClickListener);
        favorite_artist_7.setOnClickListener(onClickListener);
        favorite_artist_8.setOnClickListener(onClickListener);
        favorite_artist_9.setOnClickListener(onClickListener);
        favorite_artist_10.setOnClickListener(onClickListener);
        favorite_artist_11.setOnClickListener(onClickListener);
        favorite_artist_12.setOnClickListener(onClickListener);
        favorite_artist_13.setOnClickListener(onClickListener);
        favorite_artist_14.setOnClickListener(onClickListener);
        favorite_artist_15.setOnClickListener(onClickListener);
        favorite_artist_16.setOnClickListener(onClickListener);
        favorite_artist_17.setOnClickListener(onClickListener);
        favorite_artist_18.setOnClickListener(onClickListener);

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("select_uil", uid);
        }


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<18; i++){
                    if (select_artist[i] == 1) {
                        favorite_artist_num.add(i);
                        favorite_artist.add(ChangeAtoB.favorite_artist_name(i));
                    }
                }

                // 초기 설정 - 선호하는 아티스트일 때
                if(fromActivity == 1) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String uid = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");

                    // 스위치를 켰다면 기념일 정보도 DB에 삽입
                    reference.child(uid).child("favorite_artist_num").setValue(favorite_artist_num);
                    reference.child(uid).child("favorite_artist").setValue(favorite_artist);

                    // 메인 페이지로 이동
                    finish();
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);

                } // 프로필 수정 - 선호하는 아티스트 편집일 때
                else {
                    Intent intent3 = new Intent(getApplicationContext(), ProfileEditActivity.class);
                    intent3.putExtra("FAA", 1);
                    intent3.putExtra("FAA_artist_num", favorite_artist_num);
                    intent3.putExtra("FAA_artist_name",favorite_artist);
                    setResult(RESULT_OK, intent3);
                    finish();
                }

                Log.d("fav_fromActivity", String.valueOf(fromActivity));
                Log.d("fav_Artist_num", String.valueOf(favorite_artist_num));
            }

        });
    }

    public boolean change_btn (int id){
        if(select_artist[id] == 0) {
            select_artist[id] = 1;
            return true;
        } else {
            select_artist[id] = 0;
            return false;
        }
    }


}
