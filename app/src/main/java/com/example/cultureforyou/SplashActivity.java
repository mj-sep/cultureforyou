package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private final TimerTask spashScreenFinished = new TimerTask() {
        @Override
        public void run() {
            Intent splash = new Intent(SplashActivity.this, Splash2Activity.class);
            // We set these flags so the user cannot return to the SplashScreen
            splash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(splash);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // We get the ImageView and set the background. (If possible do this in XML instead of code)
        final ImageView splashImageView = (ImageView) findViewById(R.id.splashview);
        //splashImageView.setBackgroundResource(R.anim.splashanim);

        // We load the slide animation and apply it to the ImageView
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        splashImageView.startAnimation(slide);

        // We use a Timer to schedule a TimerTask for 5 seconds in the future!
        Timer timer = new Timer();
        timer.schedule(this.spashScreenFinished, 5000);
    }
}