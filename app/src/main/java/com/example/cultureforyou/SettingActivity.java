package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    ImageButton btn_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exampleeee);

        //btn_backward = findViewById(R.id.backward_button);
        /*
        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

         */

    }

}
