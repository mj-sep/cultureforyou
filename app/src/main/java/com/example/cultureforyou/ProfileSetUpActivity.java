package com.example.cultureforyou;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProfileSetUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_profile_setup);

        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_month);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_date);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_date, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        Spinner emotionSpinner = (Spinner)findViewById(R.id.spinner_emotion);
        ArrayAdapter emotionAdapter = ArrayAdapter.createFromResource(this,
                R.array.emotion, android.R.layout.simple_spinner_item);
        emotionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(emotionAdapter);


    }
}