package com.example.cultureforyou;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class PopupActivity extends Dialog {
    private final Context context;
    private ImageButton feeling_list_1;
    private ImageButton feeling_list_2;
    private ImageButton feeling_list_3;
    private ImageButton feeling_list_4;
    private ImageButton feeling_list_5;
    private ImageButton feeling_list_6;
    private ImageButton feeling_list_7;
    private ImageButton feeling_list_8;
    private ImageButton feeling_list_9;
    private ImageButton feeling_list_10;
    private ImageButton feeling_list_11;
    private ImageButton feeling_list_12;
    private ImageButton feeling_list_13;
    private ImageButton feeling_list_14;
    private ImageButton feeling_list_15;
    private ImageButton feeling_list_16;
    private ImageButton close_btn;

    View.OnClickListener onClickListener;

    String selectmood = "";
    String streaming="";



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 전체화면 모드 (상태바 제거)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.feeling_list_popup);

        // 버튼 및 뷰 정의
        feeling_list_1 = findViewById(R.id.feeling_list_1);
        feeling_list_2 = findViewById(R.id.feeling_list_2);
        feeling_list_3 = findViewById(R.id.feeling_list_3);
        feeling_list_4 = findViewById(R.id.feeling_list_4);
        feeling_list_5 = findViewById(R.id.feeling_list_5);
        //feeling_list_6 = findViewById(R.id.feeling_list_6);
        feeling_list_7 = findViewById(R.id.feeling_list_7);
        feeling_list_8 = findViewById(R.id.feeling_list_8);
        feeling_list_9 = findViewById(R.id.feeling_list_9);
        feeling_list_10 = findViewById(R.id.feeling_list_10);
        feeling_list_11 = findViewById(R.id.feeling_list_11);
        feeling_list_12 = findViewById(R.id.feeling_list_12);
        feeling_list_13 = findViewById(R.id.feeling_list_13);
        feeling_list_14 = findViewById(R.id.feeling_list_14);
        feeling_list_15 = findViewById(R.id.feeling_list_15);
        feeling_list_16 = findViewById(R.id.feeling_list_16);


        /* 강렬함 버튼만 활성화된 상태 (12/12)
        feeling_list_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StreamingActivity.class);
                intent.putExtra("selectmood", "a1");
                intent.putExtra("streaming", "0" );
                context.startActivity(intent);
            }
        });

         */



        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.feeling_list_1:
                        selectmood = "a0";
                        break;
                    case R.id.feeling_list_2:
                        selectmood = "a1";
                        break;
                    case R.id.feeling_list_3:
                        selectmood = "a2";
                        break;
                    case R.id.feeling_list_4:
                        selectmood = "a3";
                        break;
                    case R.id.feeling_list_5:
                        selectmood = "a4";
                        break;
                    case R.id.feeling_list_6:
                        selectmood = "a5";
                        break;
                    case R.id.feeling_list_7:
                        selectmood = "a6";
                        break;
                    case R.id.feeling_list_8:
                        selectmood = "a7";
                        break;
                    case R.id.feeling_list_9:
                        selectmood = "a8";
                        break;
                    case R.id.feeling_list_10:
                        selectmood = "a9";
                        break;
                    case R.id.feeling_list_11:
                        selectmood = "a10";
                        break;
                    case R.id.feeling_list_12:
                        selectmood = "a11";
                        break;
                    case R.id.feeling_list_13:
                        selectmood = "a12";
                        break;
                    case R.id.feeling_list_14:
                        selectmood = "a13";
                        break;
                    case R.id.feeling_list_15:
                        selectmood = "a14";
                        break;
                    case R.id.feeling_list_16:
                        selectmood = "a15";
                        break;
                    default:
                        selectmood = "";
                        break;
                }
                Intent intent = new Intent(context.getApplicationContext(), StreamingActivity.class);
                intent.putExtra("selectmood", selectmood);
                intent.putExtra("streaming", "0" );
                context.startActivity(intent);
            }
        };

        feeling_list_1.setOnClickListener(onClickListener);
        feeling_list_2.setOnClickListener(onClickListener);
        feeling_list_3.setOnClickListener(onClickListener);
        feeling_list_4.setOnClickListener(onClickListener);
        feeling_list_5.setOnClickListener(onClickListener);
//        feeling_list_6.setOnClickListener(onClickListener);
        feeling_list_7.setOnClickListener(onClickListener);
        feeling_list_8.setOnClickListener(onClickListener);
        feeling_list_9.setOnClickListener(onClickListener);
        feeling_list_10.setOnClickListener(onClickListener);
        feeling_list_11.setOnClickListener(onClickListener);
        feeling_list_12.setOnClickListener(onClickListener);
        feeling_list_13.setOnClickListener(onClickListener);
        feeling_list_14.setOnClickListener(onClickListener);
        feeling_list_15.setOnClickListener(onClickListener);
        feeling_list_16.setOnClickListener(onClickListener);


    }

    public PopupActivity(@NonNull Context context) {
        super(context);
        this.context = context;
    }
}
