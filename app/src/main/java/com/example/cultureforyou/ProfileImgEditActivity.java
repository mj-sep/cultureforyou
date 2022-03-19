package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileImgEditActivity extends AppCompatActivity {

    ImageButton btn_backward;
    ImageButton icon_painting_1, icon_painting_2, icon_painting_3;
    ImageButton icon_painting_4, icon_painting_5, icon_painting_6;
    ImageButton icon_painting_7, icon_painting_8, icon_painting_9;
    ImageButton icon_painting_10, icon_painting_11, icon_painting_12;
    ImageButton icon_painting_13, icon_painting_14, icon_painting_15;

    View.OnClickListener onClickListener;
    String select_icon ="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_icon_select);

        btn_backward = findViewById(R.id.backward_button);
        icon_painting_1 = findViewById(R.id.profile_icon_1);
        icon_painting_2 = findViewById(R.id.profile_icon_2);
        icon_painting_3 = findViewById(R.id.profile_icon_3);
        icon_painting_4 = findViewById(R.id.profile_icon_4);
        icon_painting_5 = findViewById(R.id.profile_icon_5);
        icon_painting_6 = findViewById(R.id.profile_icon_6);
        icon_painting_7 = findViewById(R.id.profile_icon_7);
        icon_painting_8 = findViewById(R.id.profile_icon_8);
        icon_painting_9 = findViewById(R.id.profile_icon_9);
        icon_painting_10 = findViewById(R.id.profile_icon_10);
        icon_painting_11 = findViewById(R.id.profile_icon_11);
        icon_painting_12 = findViewById(R.id.profile_icon_12);
        icon_painting_13 = findViewById(R.id.profile_icon_13);
        icon_painting_14 = findViewById(R.id.profile_icon_14);
        icon_painting_15 = findViewById(R.id.profile_icon_15);


        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.profile_icon_1:
                        select_icon = "icon_painting_1";
                        break;
                    case R.id.profile_icon_2:
                        select_icon = "icon_painting_2";
                        break;
                    case R.id.profile_icon_3:
                        select_icon = "icon_painting_3";
                        break;
                    case R.id.profile_icon_4:
                        select_icon = "icon_painting_4";
                        break;
                    case R.id.profile_icon_5:
                        select_icon = "icon_painting_5";
                        break;
                    case R.id.profile_icon_6:
                        select_icon = "icon_painting_6";
                        break;
                    case R.id.profile_icon_7:
                        select_icon = "icon_painting_7";
                        break;
                    case R.id.profile_icon_8:
                        select_icon = "icon_painting_8";
                        break;
                    case R.id.profile_icon_9:
                        select_icon = "icon_painting_9";
                        break;
                    case R.id.profile_icon_10:
                        select_icon = "icon_painting_10";
                        break;
                    case R.id.profile_icon_11:
                        select_icon = "icon_painting_11";
                        break;
                    case R.id.profile_icon_12:
                        select_icon = "icon_painting_12";
                        break;
                    case R.id.profile_icon_13:
                        select_icon = "icon_painting_13";
                        break;
                    case R.id.profile_icon_14:
                        select_icon = "icon_painting_14";
                        break;
                    case R.id.profile_icon_15:
                        select_icon = "icon_painting_15";
                        break;
                    default:
                        break;
                }


                Intent intent = new Intent(getApplicationContext(),FirstSettingActivity.class);
                Intent intent2 = new Intent(getApplicationContext(), ProfileEditActivity.class);
                intent.putExtra("select_icon",select_icon);
                intent2.putExtra("select_icon", select_icon);
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        icon_painting_1.setOnClickListener(onClickListener);
        icon_painting_2.setOnClickListener(onClickListener);
        icon_painting_3.setOnClickListener(onClickListener);
        icon_painting_4.setOnClickListener(onClickListener);
        icon_painting_5.setOnClickListener(onClickListener);
        icon_painting_6.setOnClickListener(onClickListener);
        icon_painting_7.setOnClickListener(onClickListener);
        icon_painting_8.setOnClickListener(onClickListener);
        icon_painting_9.setOnClickListener(onClickListener);
        icon_painting_10.setOnClickListener(onClickListener);
        icon_painting_11.setOnClickListener(onClickListener);
        icon_painting_12.setOnClickListener(onClickListener);
        icon_painting_13.setOnClickListener(onClickListener);
        icon_painting_14.setOnClickListener(onClickListener);
        icon_painting_15.setOnClickListener(onClickListener);

    }



}
