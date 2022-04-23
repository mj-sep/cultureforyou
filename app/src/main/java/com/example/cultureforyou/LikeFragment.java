package com.example.cultureforyou;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class LikeFragment extends Fragment {

    private View view;

    private String TAG = "프래그먼트";
    String uid = "";

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    private RecyclerView mRecyclerView;
    private LikeRecyclerAdapter mRecyclerAdapter;
    private ArrayList<LikelistDTO> mlikelists = new ArrayList<>();

    List<String> titlelist = new ArrayList<>();
    List<String> composerlist = new ArrayList<>();
    List<String> smoodlist = new ArrayList<>();
    List<String> plidlist = new ArrayList<>();
    List<String> moodTag = new ArrayList<>();

    int num = 0;

    //LikeRecyclerAdapter mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.likefragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

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
        Query ref = reference.orderByChild("uid").equalTo(uid);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerAdapter = new LikeRecyclerAdapter();

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));



        // 어댑터 데이터
        //mlikelists = new ArrayList<>();

        // firebase에서 데이터 가져오기
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("ondatachange", snapshot.getValue().toString());

                for(DataSnapshot ds : snapshot.getChildren()) {
                    //Log.i("dslist", ds.child("likelist").getValue().toString());
                    Map<String, Object> map = (Map<String, Object>) ds.child("likelist").getValue();
                    //Log.d(TAG, "Value is: " + map);

                    for(DataSnapshot ds2: ds.child("likelist").getChildren()){
                        String plid = ds2.child("plid").getValue(String.class);
                        plidlist.add(plid);
                        String selectmusictitle = ds2.child("title").getValue(String.class);
                        titlelist.add(selectmusictitle);
                        String selectmusicartist = ds2.child("composer").getValue(String.class);
                        composerlist.add(selectmusicartist);
                        String selectmood = ds2.child("mood").getValue(String.class);
                        smoodlist.add(selectmood);
                        Log.d("ds2 종료", "ds 2 종료");
                    }


                    mlikelists.clear();
                    for(int i = plidlist.size()-1; i>=0; i--){
                        mlikelists.add(new LikelistDTO(plidlist.get(i), changeimg(smoodlist.get(i)), ChangeAtoB.setMood(smoodlist.get(i)),titlelist.get(i), composerlist.get(i)));
                    }

                    plidlist.clear();
                    smoodlist.clear();
                    titlelist.clear();
                    composerlist.clear();
                    mRecyclerAdapter.setLikeList(mlikelists);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return view;
    }


    public int changeimg (String selectmood){
        int imgdrawable = 0;
        switch (selectmood) {
            case "a0": imgdrawable = R.drawable.main_1_active;
                break;
            case "a1" : imgdrawable = R.drawable.main_2_strong;
                break;
            case "a2" : imgdrawable = R.drawable.main_3_joyful;
                break;
            case "a3" : imgdrawable = R.drawable.main_4_amazing;
                break;
            case "a4": imgdrawable = R.drawable.main_5_horror;
                break;
            case "a5": imgdrawable = R.drawable.main_6_unpleasant;
                break;
            case "a6": imgdrawable = R.drawable.main_7_anxious;
                break;
            case "a7": imgdrawable = R.drawable.main_8_drowsy;
                break;
            case "a8": imgdrawable = R.drawable.main_9_depressed;
                break;
            case "a9": imgdrawable = R.drawable.main_10_static;
                break;
            case "a10": imgdrawable = R.drawable.main_11_still;
                break;
            case "a11": imgdrawable = R.drawable.main_12_comfort;
                break;
            case "a12": imgdrawable = R.drawable.main_13_happy;
                break;
            case "a13": imgdrawable = R.drawable.main_14_friendly;
                break;
            case "a14": imgdrawable = R.drawable.main_15_mysterious;
                break;
            case "a15": imgdrawable = R.drawable.main_16_graceful;
                break;
        }
        return imgdrawable;
    }

    private void setmoodlist() {
        moodTag.add("활기찬");
        moodTag.add("강렬한");
        moodTag.add("즐거운");
        moodTag.add("놀라운");
        moodTag.add("공포스러운");
        moodTag.add("불쾌한");
        moodTag.add("불안한");
        moodTag.add("나른한");
    }
}
