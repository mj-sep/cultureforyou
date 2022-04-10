package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    ViewPager viewPager;
    ImageAdapter adapter;
    List<Image> Images;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ImageButton playButton;
    ImageButton setting_button;
    ImageButton btn_profile;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String uid = "";
    public static Context mContext;

    private View view;
    private String TAG = "프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_2, container, false);

        btn_profile = view.findViewById(R.id.profile_button);
        setting_button = view.findViewById(R.id.setting_button);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        // 프로필 이미지
        reference.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String profile_icon = snapshot1.child("profile_icon").getValue(String.class);

                    if (!profile_icon.equals("")) {
                        // 프로필 이미지
                        profile_icon = "https://drive.google.com/uc?export=view&id=" + profile_icon;
                        Glide.with(mContext.getApplicationContext()).load(profile_icon).transform(new CenterCrop(), new CircleCrop()).into(btn_profile);
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
            }
        });


        // Popup Activity 커스텀 다이얼로그
        ImageButton imageButton = view.findViewById(R.id.feeling_list_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupActivity popup_feeling = new PopupActivity(getActivity());
                popup_feeling.show();
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
}