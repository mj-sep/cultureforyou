package com.example.cultureforyou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirstSettingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private ImageButton profile_img_edit_button;
    private EditText nickname;
    private EditText anni_name;
    private ImageButton btn_next;
    private ImageView profile_icon;
    private Switch sw;

    private Spinner spinner_month;
    private Spinner spinner_day;
    private Spinner spinner_emotion;
    private RelativeLayout sw_anniv;
    private FirebaseAuth firebaseAuth;


    Boolean switch_on = false;
    ArrayAdapter<CharSequence> adapter_month, adapter_day, adapter_emotion;
    String select_months = "";
    String select_day = "";
    String select_date = "";
    String select_emotion = "";
    String select_icon = "";
    String select_icon_gid = "";
    String unickname = "";
    String anniv_name = "";
    FirebaseDatabase database;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_profile_setup);

        profile_img_edit_button = findViewById(R.id.profile_img_edit_button);
        nickname = findViewById(R.id.nickname);
        anni_name = findViewById(R.id.anniv_name);
        btn_next = findViewById(R.id.btn_next);
        profile_icon = findViewById(R.id.profile_icon);
        sw = findViewById(R.id.sw);
        sw_anniv = findViewById(R.id.sw_anniv);
        spinner_month = findViewById(R.id.spinner_month);
        spinner_day = findViewById(R.id.spinner_date);
        spinner_emotion = findViewById(R.id.spinner_emotion);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();







        // 닉네임 설정
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(nickname.length() > 0) {
                    btn_next.setClickable(true);
                    btn_next.setImageResource(R.drawable.bottom_button);
                } else {
                    btn_next.setClickable(false);
                    btn_next.setImageResource(R.drawable.bottom_button_gray);
                }
            }
        });



        // spinner anniv date - adapter
        adapter_month = ArrayAdapter.createFromResource(this, R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_month);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 2월 선택 시 29일 (윤년)
                if(adapter_month.getItem(position).equals("2월")) {
                    // db에 mm-dd 타입으로 넣을 예정
                    select_months = "02";
                    adapter_day = ArrayAdapter.createFromResource(FirstSettingActivity.this, R.array.date_date_29, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_day.setAdapter(adapter_day);
                    spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            select_day = (String) adapter_day.getItem(position);
                            if (position < 9) select_day = "0" + select_day.substring(0,1);
                            else select_day = select_day.substring(0,2);
                            select_date = select_months + "-" + select_day;
                            Log.d("select_date", select_date);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            select_day = "";
                        }
                    });
                } // 4, 6, 9, 11월 선택 시 30일
                else if(adapter_month.getItem(position).equals("4월") || adapter_month.getItem(position).equals("6월")
                        || adapter_month.getItem(position).equals("9월") || adapter_month.getItem(position).equals("11월")) {
                    select_months = (String) adapter_month.getItem(position);

                    if(select_months.equals("11월")) {select_months = "11";}
                    else {select_months = "0" + select_months.substring(0, 1);}

                    adapter_day = ArrayAdapter.createFromResource(FirstSettingActivity.this, R.array.date_date_30, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_day.setAdapter(adapter_day);
                    spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            select_day = (String) adapter_day.getItem(position);
                            if (position < 9) select_day = "0" + select_day.substring(0,1);
                            else select_day = select_day.substring(0,2);
                            select_date = select_months + "-" + select_day;
                            Log.d("select_date", select_date);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            select_day = "";
                        }
                    });
                } // 그외 월 선택 시 31일까지
                else {
                    select_months = (String) adapter_month.getItem(position);
                    if(select_months.equals("10월") || select_months.equals("12월")) {select_months = select_months.substring(0, 2);}
                    else {select_months = "0" + select_months.substring(0, 1);}

                    adapter_day = ArrayAdapter.createFromResource(FirstSettingActivity.this, R.array.date_date_31, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_day.setAdapter(adapter_day);
                    spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            select_day = (String) adapter_day.getItem(position);
                            if (position < 9) select_day = "0" + select_day.substring(0,1);
                            else select_day = select_day.substring(0,2);
                            select_date = select_months + "-" + select_day;
                            Log.d("select_date", select_date);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            select_day = "";
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                select_months = "";
            }
        });


        // spinner anniv emotion - adapter
        adapter_emotion = ArrayAdapter.createFromResource(this, R.array.emotion, R.layout.spinner_setting);
        adapter_emotion.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_emotion.setAdapter(adapter_emotion);
        spinner_emotion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_emotion = (String) adapter_emotion.getItem(position);
                Log.d("select_emotion", select_emotion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 프로필 사진 설정 버튼 클릭 시
        profile_img_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileImgEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        // 스위치 (기념일 플레이리스트 추천받기) ON/OFF
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw.isChecked()) {
                    switch_on = true;
                    sw_anniv.setVisibility(View.VISIBLE);

                } else {
                    switch_on = false;
                    sw_anniv.setVisibility(View.INVISIBLE);

                }
            }
        });

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("select_uil", uid);
        }


        // 다음 버튼 클릭 시
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 닉네임, 기념일, 기념일무드 값 db에 업데이트
                unickname = nickname.getText().toString().trim();
                anniv_name = anni_name.getText().toString().trim();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String email = user.getEmail();
                String uid = user.getUid();
                String nickname = unickname;
                String anniversary_name = anniv_name;
                String anniversary = select_date;
                String anni_mood = select_emotion;
                String profile_icon = select_icon_gid;

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");

                // 스위치를 켰다면 기념일 정보도 DB에 삽입
                if(switch_on == true){
                    reference.child(uid).child("anniv_onoff").setValue(1);
                    reference.child(uid).child("anniversary_name").setValue(anniversary_name);
                    reference.child(uid).child("anniversary").setValue(anniversary);
                    reference.child(uid).child("anni_mood").setValue(anni_mood);
                }
                else reference.child(uid).child("anniv_onoff").setValue(0);

                reference.child(uid).child("profile_icon").setValue(profile_icon);
                reference.child(uid).child("nickname").setValue(nickname);

                // 선호하는 아티스트 페이지로 이동
                Intent intent2 = new Intent(getApplicationContext(), FavoriteArtistActivity.class);
                startActivity(intent2);
            }
        });

    }


    // 선택한 프로필 아이콘 받아서 imageView에 띄움
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                select_icon = data.getExtras().getString("select_icon");
                select_icon_gid = ChangeAtoB.icon_gdrive_id(select_icon);
                Log.d("VIEW", select_icon);
                Glide.with(getApplicationContext()).load("https://drive.google.com/uc?export=view&id=" + select_icon_gid).transform(new CenterCrop(), new RoundedCorners(16)).into(profile_icon);
                //profile_icon.setImageResource(icons.get(select_icon).intValue());
            }
        }

    }
}
