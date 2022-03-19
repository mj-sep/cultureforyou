package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.Arrays;

public class ProfileEditActivity extends AppCompatActivity {

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
    GridView artist_gridview;
    ScrollView pf_ed_scrollview;

    Boolean switch_on = false;
    ArrayAdapter<CharSequence> adapter_month, adapter_day, adapter_emotion;
    String select_months = "";
    String select_day = "";
    String select_date = "";
    String select_emotion = "";


    String nickname;
    String profile_icon;
    Integer annivonoff;
    String anniv_mood, anniv_name, anniv_date;
    String[] fav_artist_img;
    String[] fav_artist_name;


    @Override
    protected void onCreate(Bundle savedInstanceState){
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


        // 인텐트 (get) - 닉네임, 프로필 이미지
        Intent intent2 = getIntent();
        nickname = intent2.getStringExtra("nickname");
        pf_edit_nickname.setText(nickname);
        pf_edit_nickname.setSelection(pf_edit_nickname.getText().length());
        profile_icon = intent2.getStringExtra("profile_icon");
        Glide.with(getApplicationContext()).load(profile_icon).transform(new CenterCrop(), new RoundedCorners(16)).into(pf_edit_image);

        // 인텐트 (get) - 기념일
        annivonoff = intent2.getIntExtra("anniv_onoff", 0);

        // 기념일 설정
        anniv_day();
        anniv_day_mood();

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
            spinner_day.setSelection(getIndex(spinner_day, array_date[1]));



            //tx_anniv_mood.setText(anniv_mood);
            pf_ed_anniv_name.setText(anniv_name);
            anniv_off_text.setVisibility(View.INVISIBLE);

            spinner_emotion.setSelection(getIndex(spinner_emotion, anniv_mood));

        } else {
            pf_ed_switch.setChecked(false);
            sw_anniv.setVisibility(View.INVISIBLE);
            anniv_off_text.setVisibility(View.VISIBLE);
        }

        btn_profile_Img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileImgEditActivity.class);
                startActivity(intent);
            }
        });



        // 인텐트 - 선호하는 아티스트
        fav_artist_img = intent2.getStringArrayExtra("fav_artist_img");
        Log.d("fav_artist_img", (Arrays.toString(fav_artist_img)));
        fav_artist_name = intent2.getStringArrayExtra("fav_artist_name");

        FavArtistAdapter favArtistAdapter = new FavArtistAdapter(ProfileEditActivity.this, fav_artist_name, fav_artist_img);
        artist_gridview.setAdapter(favArtistAdapter);
        pf_ed_scrollview.post(new Runnable() {
            @Override
            public void run() {
                pf_ed_scrollview.fullScroll(View.FOCUS_UP);
            }
        });


        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // 기념일 설정 - 월에 따른 일수 조정
    public void anniv_day () {
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
                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_29, android.R.layout.simple_spinner_dropdown_item);
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

                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_30, android.R.layout.simple_spinner_dropdown_item);
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

                    adapter_day = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.date_date_31, android.R.layout.simple_spinner_dropdown_item);
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
}
