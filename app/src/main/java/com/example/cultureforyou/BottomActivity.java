package com.example.cultureforyou;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomActivity extends AppCompatActivity {

    LikeFragment likeFragment;
    MainFragment mainFragment;
    PickFragment pickFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity);

        likeFragment = new LikeFragment();
        mainFragment = new MainFragment();
        pickFragment = new PickFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, mainFragment).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "첫번째 탭", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, likeFragment).commit();
                        return true;

                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "두번째 탭", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, mainFragment).commit();
                        return true;

                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "세번째 탭", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, pickFragment).commit();
                        return true;
                }
                return false;
            }
        });



    }
}
