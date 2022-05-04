package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {

    ViewPager viewPager;
    ImageAdapter adapter;
    List<Image> Images;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ImageButton playButton;
    ImageButton setting_button;
    ImageButton btn_profile;
    TextView m_music_title;
    TextView m_music_artist;
    LinearLayout currentplayinfo;


    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String uid = "";
    public static Context mContext;

    private View view;
    private String TAG = "프래그먼트";

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private Intent playIntent;
    private boolean musicBound = false;
    private static final int REQUEST_CODE = 200;

    // 이거 옮겼음 오류나면 이거 다시 되돌려
    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MusicService.LocalBinder mb = (MusicService.LocalBinder) service;
            musicSrv = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            Log.i("isService", name + " 서비스 연결");
            isService = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Log.i("isService", name + " 서비스 연결 해제");
            Toast.makeText(getActivity(),"서비스 연결 해제", Toast.LENGTH_LONG).show();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.mainfragment, container, false);

        btn_profile = view.findViewById(R.id.profile_button);
        setting_button = view.findViewById(R.id.setting_button);
        currentplayinfo = view.findViewById(R.id.currentplayinfo);
        m_music_title = view.findViewById(R.id.m_music_title);
        m_music_artist = view.findViewById(R.id.m_music_artist);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        Intent intent = getActivity().getIntent();
        int test = intent.getIntExtra("isPlayingNow", 100);
        Log.d("PlayingMainFragment", String.valueOf(test));

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        // currentplayinfo.setVisibility(View.INVISIBLE);
        if(isService) {
            m_music_title.setText(musicSrv.setCurrentMusicTitle());
        }

        m_music_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv.isPlayingCurrent()) {
                    Log.i("isService", "MainFragment stopmusicsrv");
                    musicSrv.stopMusicService();
                } else {
                    Log.i("isService", "MainFragment startmusicsrv");
                    musicSrv.playMusicService();
                }
            }
        });

        // 프로필 이미지
        reference.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String profile_icon = snapshot1.child("profile_icon").getValue(String.class);

                    if (!profile_icon.equals("")) {
                        // 프로필 이미지

                        HashMap<String, Integer> icons = new HashMap<String, Integer>();
                        icons.put("icon_painting_1", Integer.valueOf(R.drawable.icon_painting_1));
                        icons.put("icon_painting_2", Integer.valueOf(R.drawable.icon_painting_2));
                        icons.put("icon_painting_3", Integer.valueOf(R.drawable.icon_painting_3));
                        icons.put("icon_painting_4", Integer.valueOf(R.drawable.icon_painting_4));
                        icons.put("icon_painting_5", Integer.valueOf(R.drawable.icon_painting_5));
                        icons.put("icon_painting_6", Integer.valueOf(R.drawable.icon_painting_6));
                        icons.put("icon_painting_7", Integer.valueOf(R.drawable.icon_painting_7));
                        icons.put("icon_painting_8", Integer.valueOf(R.drawable.icon_painting_8));
                        icons.put("icon_painting_9", Integer.valueOf(R.drawable.icon_painting_9));
                        icons.put("icon_painting_10", Integer.valueOf(R.drawable.icon_painting_10));
                        icons.put("icon_painting_11", Integer.valueOf(R.drawable.icon_painting_11));
                        icons.put("icon_painting_12", Integer.valueOf(R.drawable.icon_painting_12));
                        icons.put("icon_painting_13", Integer.valueOf(R.drawable.icon_painting_13));
                        icons.put("icon_painting_14", Integer.valueOf(R.drawable.icon_painting_14));
                        icons.put("icon_painting_15", Integer.valueOf(R.drawable.icon_painting_15));

                        Log.d("profile_image", profile_icon);
                        Log.d("profile_hash", String.valueOf(icons.get(profile_icon).intValue()));
                        //profile_icon = "https://drive.google.com/uc?export=view&id=" + profile_icon;
                        if(getContext()!=null)
                            Glide.with(MainFragment.this).load(icons.get(profile_icon).intValue()).transform(new CenterCrop(), new CircleCrop()).into(btn_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });


        // 프로필 버튼 클릭 시 -> 프로필 페이지 이동
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 클릭 시 ->
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(getActivity(), SettingActivity.class);
                startActivity(settingintent);
            }
        });


        // Popup Activity 커스텀 다이얼로그
        ImageButton imageButton = view.findViewById(R.id.feeling_list_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupActivity popup_feeling = new PopupActivity(getActivity());
                popup_feeling.show();
                //Intent musicintent = new Intent(getActivity(), CSVStreamingActivity.class);
                //startActivityForResult(musicintent, REQUEST_CODE);
            }

        });

        Images = new ArrayList<>();
        Images.add(new Image(R.drawable.main_1_active, "활기찬", ""));
        Images.add(new Image(R.drawable.main_2_strong, "강렬한", ""));
        Images.add(new Image(R.drawable.main_3_joyful, "즐거운", ""));
        Images.add(new Image(R.drawable.main_4_amazing, "놀라운", ""));
        Images.add(new Image(R.drawable.main_5_horror, "공포스러운", ""));
        Images.add(new Image(R.drawable.main_6_unpleasant, "불쾌한", ""));
        Images.add(new Image(R.drawable.main_7_anxious, "불안한", ""));
        Images.add(new Image(R.drawable.main_8_drowsy, "나른한", ""));
        Images.add(new Image(R.drawable.main_9_depressed, "우울한", ""));
        Images.add(new Image(R.drawable.main_10_static, "정적인", ""));
        Images.add(new Image(R.drawable.main_11_still, "잔잔한", ""));
        Images.add(new Image(R.drawable.main_12_comfort, "편안한", ""));
        Images.add(new Image(R.drawable.main_13_happy, "행복한", ""));
        Images.add(new Image(R.drawable.main_14_friendly, "친근한", ""));
        Images.add(new Image(R.drawable.main_15_mysterious, "신비로운", ""));
        Images.add(new Image(R.drawable.main_16_graceful, "우아한", ""));

        adapter = new ImageAdapter(Images, getActivity());

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        int dpValue = 90;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);

        playButton = view.findViewById(R.id.playButton);
        return view;
    }



    public void onStart() {
        super.onStart();
        Intent intent2 = new Intent(getActivity(), MusicService.class);
        Log.d("isService", "MainFragment : onStart");
        getActivity().bindService(intent2, conn, Context.BIND_AUTO_CREATE);
        // isService = true;
        //Log.i("isService", "테스트테스트테스트 현재 재생중인지 " + isService);

    }

    public void onStop(){
        super.onStop();
        Log.d("isService", "MainFragment : onStop");
        getActivity().unbindService(conn);
        isService = false;
    }



    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("isService", "MainFragment setResult");
                // currentplayinfo.setVisibility(View.VISIBLE);
                // SeekbarSetting(duration);
            }
            else Log.d("isService", "notsetResult");
        }

    }

     */



}
