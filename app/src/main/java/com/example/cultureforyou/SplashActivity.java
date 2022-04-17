package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    /*
    private final TimerTask spashScreenFinished = new TimerTask() {
        @Override
        public void run() {
            Intent splash = new Intent(SplashActivity.this, LoginActivity.class);
            // We set these flags so the user cannot return to the SplashScreen
            splash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(splash);
        }
    };

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        movemain(2);

        // We get the ImageView and set the background. (If possible do this in XML instead of code)
        final ImageView splashImageView = (ImageView) findViewById(R.id.splashview);
        final ImageView splashImageView2 = (ImageView) findViewById(R.id.splashview2);

        // We load the slide animation and apply it to the ImageView
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out);
        splashImageView.startAnimation(slide);
        splashImageView2.startAnimation(slide2);


        // We use a Timer to schedule a TimerTask for 0.5 seconds in the future!
        // Timer timer = new Timer();
        //timer.schedule(this.spashScreenFinished, 500);
    }

    private void movemain(int sec) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //new Intent(현재 context, 이동할 activity)
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);	//intent 에 명시된 액티비티로 이동

                finish();	//현재 액티비티 종료
            }
        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
    }

}