package com.example.cultureforyou;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_1 = 202;
    private static final int REQUEST_CODE_2 = 201;
    private ImageButton btn_profile_Img_edit;
    private ImageButton btn_backward;
    private ImageView pf_edit_image;
    private EditText pf_edit_nickname;
    private EditText pf_ed_anniv_name;
    private Spinner spinner_month;
    private Spinner spinner_day;
    private Spinner spinner_emotion;
    private RelativeLayout sw_anniv;
    private TextView anniv_off_text;
    private Switch pf_ed_switch;
    private ImageButton pf_ed_nextbtn;
    private ImageButton pf_ed_favbtn;
    private FirebaseAuth firebaseAuth;
    GridView artist_gridview;
    ScrollView pf_ed_scrollview;

    Boolean switch_on = false;
    ArrayAdapter<CharSequence> adapter_month, adapter_day, adapter_emotion;
    String select_months = "";
    String select_day = "";
    String select_date = "";
    String select_emotion = "";
    Boolean fav_artist_change = false;
    Boolean profile_img_change = false;

    ArrayList<String> fav_img = new ArrayList<>();
    ArrayList<String> fav_name = new ArrayList<>();
    String nickname;
    String profile_icon;
    Integer annivonoff;
    String anniv_mood, anniv_name, anniv_date;
    String[] fav_artist_img;
    String[] fav_artist_name;
    String select_icon = "";
    String select_icon_gid = "";
    String uid="";
    FirebaseDatabase database;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        // 컴포넌트
        btn_profile_Img_edit = findViewById(R.id.profile_img_edit_button);
        pf_edit_image = findViewById(R.id.pf_edit_image);
        pf_edit_nickname = findViewById(R.id.pf_edit_nickname);
        pf_ed_anniv_name = findViewById(R.id.pf_ed_anniv_name);
        spinner_month = findViewById(R.id.spinner_month);
        spinner_day = findViewById(R.id.spinner_date);
        spinner_emotion = findViewById(R.id.spinner_emotion);
        sw_anniv = findViewById(R.id.sw_anniv);
        anniv_off_text = findViewById(R.id.anniv_off_text);
        pf_ed_switch = findViewById(R.id.pf_ed_switch);
        artist_gridview = findViewById(R.id.artist_gridview);
        pf_ed_scrollview = findViewById(R.id.pf_ed_scrollview);
        btn_backward = findViewById(R.id.backward_button);
        pf_ed_nextbtn = findViewById(R.id.pf_ed_nextbtn);
        pf_ed_favbtn = findViewById(R.id.pf_ed_favbtn);

        fav_img.clear();
        fav_name.clear();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }

        // 인텐트 (get) - 닉네임, 프로필 이미지
        Intent intent2 = getIntent();
        nickname = intent2.getStringExtra("nickname");
        pf_edit_nickname.setText(nickname);
        pf_edit_nickname.setSelection(pf_edit_nickname.getText().length());


        // 프로필 이미지 인텐트
        profile_icon = intent2.getStringExtra("profile_icon");
        Glide.with(getApplicationContext()).load(profile_img(profile_icon)).transform(new CenterCrop(), new RoundedCorners(16)).into(pf_edit_image);

        // 인텐트 (get) - 기념일
        annivonoff = intent2.getIntExtra("anniv_onoff", 0);

        // 기념일 설정
        anniv_day();
        anniv_day_mood();

        // 기념일 스위치 ON/OFF
        pf_ed_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pf_ed_switch.isChecked()) {
                    switch_on = true;
                    annivonoff = 1;
                    sw_anniv.setVisibility(View.VISIBLE);
                    anniv_off_text.setVisibility(View.INVISIBLE);

                } else {
                    switch_on = false;
                    annivonoff = 0;
                    sw_anniv.setVisibility(View.INVISIBLE);
                    anniv_off_text.setVisibility(View.VISIBLE);

                }
            }
        });

        if(annivonoff == 1){
            pf_ed_switch.setChecked(true);
            anniv_mood = intent2.getStringExtra("anniv_mood");
            anniv_name = intent2.getStringExtra("anniv_name");
            anniv_date = intent2.getStringExtra("anniv_date");

            String[] array_date = anniv_date.split("-");
            if (array_date[0].substring(0,1).equals("0")) {
                array_date[0] = array_date[0].substring(1) + "월";
            } else array_date[0] = array_date[0] + "월";

            if (array_date[1].substring(0,1).equals("0")) {
                array_date[1] = array_date[1].substring(1) + "일";
            } else array_date[1] = array_date[1] + "일";

            spinner_month.setSelection(getIndex(spinner_month, array_date[0]));


            pf_ed_anniv_name.setText(anniv_name);
            anniv_off_text.setVisibility(View.INVISIBLE);

            spinner_emotion.setSelection(getIndex(spinner_emotion, anniv_mood));
            spinner_day.setSelection(getIndex(spinner_day, array_date[1]));

        } else {
            pf_ed_switch.setChecked(false);
            sw_anniv.setVisibility(View.INVISIBLE);
            anniv_off_text.setVisibility(View.VISIBLE);
        }

        // 프로필 이미지 편집
        btn_profile_Img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileImgEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });


        // 인텐트 - 선호하는 아티스트
        int faa = intent2.getIntExtra("FAA", 0);
        if(faa==0) {
            fav_artist_img = intent2.getStringArrayExtra("fav_artist_img");
            Log.d("fav_artist_img", (Arrays.toString(fav_artist_img)));
            fav_artist_name = intent2.getStringArrayExtra("fav_artist_name");
        }

        FavArtistAdapter favArtistAdapter = new FavArtistAdapter(ProfileEditActivity.this, fav_artist_name, fav_artist_img);
        artist_gridview.setAdapter(favArtistAdapter);
        pf_ed_scrollview.post(

         new Runnable() {
            @Override
            public void run() {
                pf_ed_scrollview.fullScroll(View.FOCUS_UP);
            }
        });

        // 뒤로가기
        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 선호하는 아티스트 페이지로 이동
        pf_ed_favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), FavoriteArtistActivity.class);
                intent3.putExtra("FromActivity", 2);
                startActivityForResult(intent3, REQUEST_CODE_2);
            }
        });



        // 닉네임 설정 (공란이면 저장하기 버튼 비활성화)
        pf_edit_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pf_edit_nickname.length() > 0) {
                    pf_ed_nextbtn.setClickable(true);
                    pf_ed_nextbtn.setImageResource(R.drawable.profile_save);
                } else {
                    pf_ed_nextbtn.setClickable(false);
                    pf_ed_nextbtn.setImageResource(R.drawable.profile_save_gray);
                }
            }
        });


        // 저장하기 버튼 클릭
        pf_ed_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseUser user = firebaseAuth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");

                Log.d("profile_change", String.valueOf(profile_img_change));
                Log.d("fav_change", String.valueOf(fav_artist_change));

                nickname = pf_edit_nickname.getText().toString().trim();
                anniv_name = pf_ed_anniv_name.getText().toString().trim();
                anniv_mood = select_emotion;
                anniv_date = select_date;
                profile_icon = select_icon;

                Log.d("select_uid_2", uid);
                if(profile_img_change == true)
                    reference.child(uid).child("profile_icon").setValue(profile_icon);
                if(fav_artist_change == true) {
                    reference.child(uid).child("favorite_artist_num").setValue(fav_img);
                    reference.child(uid).child("favorite_artist").setValue(fav_name);
                }

                if(annivonoff == 1){
                    reference.child(uid).child("anniv_onoff").setValue(1);
                    reference.child(uid).child("anniversary_name").setValue(anniv_name);
                    reference.child(uid).child("anniversary").setValue(anniv_date);
                    reference.child(uid).child("anni_mood").setValue(anniv_mood);
                }
                else {
                    reference.child(uid).child("anniv_onoff").setValue(0);
                    reference.child(uid).child("anniversary_name").setValue("");
                    reference.child(uid).child("anniversary").setValue("");
                    reference.child(uid).child("anni_mood").setValue("");
                }

                reference.child(uid).child("nickname").setValue(nickname);
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // 선택한 프로필 아이콘 받아서 imageView에 띄움 & 선호하는 아티스트 변경 값 받아서 띄움
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == Activity.RESULT_OK) {
                profile_img_change = true;
                select_icon = data.getExtras().getString("select_icon");
                select_icon_gid = ChangeAtoB.icon_gdrive_id(select_icon);
                Log.d("VIEW", select_icon);
                Glide.with(getApplicationContext()).load(profile_img(select_icon)).transform(new CenterCrop(), new RoundedCorners(16)).into(pf_edit_image);
            }
        }

        if (requestCode == REQUEST_CODE_2) {
            if (resultCode == Activity.RESULT_OK) {
                fav_artist_change = true;
                // intent getExtra ArrayList - 선호하는 아티스트
                fav_img = (ArrayList<String>) data.getSerializableExtra("FAA_artist_num");
                Log.d("fav_pedit_num", String.valueOf(fav_img));
                Serializable s = data.getSerializableExtra("FAA_artist_num");
                fav_artist_img = new String[fav_img.size()];
                for(int i=0; i<fav_img.size(); i++) {
                    fav_artist_img[i] = (String.valueOf(fav_img.get(i)));
                    fav_artist_img[i] = ChangeAtoB.FA_gdrive_id(fav_artist_img[i]);
                }
                Log.d("select_fav4", (Arrays.toString(fav_artist_img)));

                fav_name = (ArrayList<String>) data.getSerializableExtra("FAA_artist_name");
                Serializable s2 = data.getSerializableExtra("FAA_artist_name");
                fav_artist_name = fav_name.toArray(new String[fav_name.size()]);
                for(int i=0; i<fav_name.size(); i++) {
                    fav_artist_name[i] = fav_name.get(i);
                }
                Log.d("fav_artist_img_faa", (Arrays.toString(fav_artist_img)));

                FavArtistAdapter favArtistAdapter = new FavArtistAdapter(ProfileEditActivity.this, fav_artist_name, fav_artist_img);
                artist_gridview.setAdapter(favArtistAdapter);
                pf_ed_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        pf_ed_scrollview.fullScroll(View.FOCUS_UP);
                    }
                });

            }
        }

    }



    // 기념일 설정 - 월에 따른 일수 조정
    public void anniv_day () {
        // spinner anniv date - adapter
        adapter_month = ArrayAdapter.createFromResource(this, R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_month.setAdapter(adapter_month);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 2월 선택 시 29일 (윤년)
                if(adapter_month.getItem(position).equals("2월")) {
                    // db에 mm-dd 타입으로 넣을 예정
                    select_months = "02";
                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_29, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_30, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_31, android.R.layout.simple_spinner_dropdown_item);
                    adapter_day.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
    }

    // 기념일 설정 - 무드
    public void anniv_day_mood () {
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
    }

    // 기념일 설정 - 무드 스피너 default 값
    private int getIndex(Spinner spinner, String item) {
        for(int i=0; i<spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                return i;
            }
        }
        return 0;
    }


    // 프로필 해쉬맵
    private int profile_img (String str) {
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

        return icons.get(str).intValue();
    }

    protected void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MusicService.class);
        Log.d("isService", "onStart");
        bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        Log.d("isService", "onStop");
        unbindService(conn);
        isService = false;
    }

    public void onResume() {
        super.onResume();
        Log.d("isService", "onResume");

    }

    protected void onNewIntent(Intent intent2) {
        super.onNewIntent(intent2);
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
