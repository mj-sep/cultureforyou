package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    ImageButton btn_profile_Img_edit;
    ImageButton btn_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        btn_profile_Img_edit = findViewById(R.id.profile_img_edit_button);

        btn_profile_Img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileImgEditActivity.class);
                startActivity(intent);
            }
        });

        btn_backward = findViewById(R.id.backward_button);

        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
