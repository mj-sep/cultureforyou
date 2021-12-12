package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.stream.Stream;

public class PopupActivity extends AppCompatActivity {
    private ImageButton feeling_intense_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeling_list_activity);

        // 버튼 및 뷰 정의
        feeling_intense_btn = findViewById(R.id.feeling_intense_btn);

        // 강렬함 버튼만 활성화된 상태 (12/12)
        feeling_intense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                intent.putExtra("selectmood", "a1");
                startActivity(intent);
            }
        });
    }
}
