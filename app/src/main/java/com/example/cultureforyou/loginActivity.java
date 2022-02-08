package com.example.cultureforyou;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {

    private EditText putid;
    private EditText putpw;
    private Button btn_find_id_pw;
    private Button btn_register;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        putid = findViewById(R.id.putid);
        putpw = findViewById(R.id.putpw);
        btn_find_id_pw = findViewById(R.id.btn_find_id_pw);
        btn_register = findViewById(R.id.btn_register);


    }
}
