package com.example.cultureforyou;

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
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class PopupActivity extends Dialog {
    private final Context context;
    private ImageButton feeling_intense_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 전체화면 모드 (상태바 제거)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.feeling_list_popup);

        // 버튼 및 뷰 정의
        feeling_intense_btn = findViewById(R.id.feeling_intense_btn);

        // 강렬함 버튼만 활성화된 상태 (12/12)
        feeling_intense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StreamingActivity.class);
                intent.putExtra("selectmood", "a1");
                intent.putExtra("streaming", "0" );
                context.startActivity(intent);
            }
        });
    }

    public PopupActivity(@NonNull Context context) {
        super(context);
        this.context = context;
    }
}
