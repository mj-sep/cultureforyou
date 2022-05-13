package com.example.cultureforyou;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class LikeFragment extends Fragment {

    private View view;
    private EditText like_searchview;
    private HorizontalScrollView horizontalscroll;
    private HorizontalScrollView horizontalscroll2;
    private AppCompatButton moodbtn0;
    private AppCompatButton moodbtn1;
    private AppCompatButton moodbtn2;
    private AppCompatButton moodbtn3;
    private AppCompatButton moodbtn4;
    private AppCompatButton moodbtn5;
    private AppCompatButton moodbtn6;
    private AppCompatButton moodbtn7;
    private AppCompatButton moodbtn8;
    private AppCompatButton moodbtn9;
    private AppCompatButton moodbtn10;
    private AppCompatButton moodbtn11;
    private AppCompatButton moodbtn12;
    private AppCompatButton moodbtn13;
    private AppCompatButton moodbtn14;
    private AppCompatButton moodbtn15;

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private static final int REQUEST_CODE = 200;
    private Intent playIntent;
    private boolean musicBound = false;


    private String TAG = "프래그먼트";
    String uid = "";

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    View.OnClickListener onClickListener;

    private RecyclerView mRecyclerView;
    private LikeRecyclerAdapter mRecyclerAdapter;
    private ArrayList<LikelistDTO> mlikelists = new ArrayList<>();
    private ArrayList<LikelistDTO> filteredlist = new ArrayList<>();
    private ArrayList<LikelistDTO> filteredmood = new ArrayList<>();
    private ArrayList<LikelistDTO> item = new ArrayList<>();

    ArrayList<String> select_playlist = new ArrayList<>(); // LikeRecyclerAdaper에서 받은 플레이리스트의 info
    ArrayList<String> moodselectlist = new ArrayList<>(); // 같은 감성값을 가진 플레이리스트 번호
    ArrayList<String> moodarray = new ArrayList<>();

    List<String> titlelist = new ArrayList<>();
    List<String> composerlist = new ArrayList<>();
    List<String> smoodlist = new ArrayList<>();
    List<String> plidlist = new ArrayList<>();
    List<String> moodTag = new ArrayList<>();

    // 무드 태그 선택 리스트
    List<String> moodchecklist = new ArrayList<>();
    int num = 0;
    int[] moodtagselect = new int[16];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.likefragment, container, false);

        like_searchview = view.findViewById(R.id.like_searchview);
        horizontalscroll = view.findViewById(R.id.horizontalscroll);
        moodbtn0 = view.findViewById(R.id.moodbtn0);
        moodbtn1 = view.findViewById(R.id.moodbtn1);
        moodbtn2 = view.findViewById(R.id.moodbtn2);
        moodbtn3 = view.findViewById(R.id.moodbtn3);
        moodbtn4 = view.findViewById(R.id.moodbtn4);
        moodbtn5 = view.findViewById(R.id.moodbtn5);
        moodbtn6 = view.findViewById(R.id.moodbtn6);
        moodbtn7 = view.findViewById(R.id.moodbtn7);
        moodbtn8 = view.findViewById(R.id.moodbtn8);
        moodbtn9 = view.findViewById(R.id.moodbtn9);
        moodbtn10 = view.findViewById(R.id.moodbtn10);
        moodbtn11 = view.findViewById(R.id.moodbtn11);
        moodbtn12 = view.findViewById(R.id.moodbtn12);
        moodbtn13 = view.findViewById(R.id.moodbtn13);
        moodbtn14 = view.findViewById(R.id.moodbtn14);
        moodbtn15 = view.findViewById(R.id.moodbtn15);


        // 무드 태그 스크롤바 숨기기
        horizontalscroll.setHorizontalScrollBarEnabled(false);


        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        Query ref = reference.orderByChild("uid").equalTo(uid);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerAdapter = new LikeRecyclerAdapter();

        mRecyclerView.setAdapter(mRecyclerAdapter);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));




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
                        mlikelists.add(new LikelistDTO(plidlist.get(i), ChangeAtoB.changeimg(smoodlist.get(i)), ChangeAtoB.setMood(smoodlist.get(i)),titlelist.get(i), composerlist.get(i)));
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


        // 좋아요 삭제
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        // 삭제할 아이템 담아두기
                        LikelistDTO deleteItem = mlikelists.get(pos);

                        String remove_id = mlikelists.get(pos).getPlaylistID();
                        Log.d("remove_id", remove_id);

                        // 삭제
                        mlikelists.remove(pos);
                        // mRecyclerAdapter.removeItem(pos);
                        // mRecyclerAdapter.notifyItemRemoved(pos);
                        mRecyclerAdapter.notifyDataSetChanged();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database.getReference("Users").child(uid).child("likelist");

                        reference1.orderByChild("plid").equalTo(remove_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()) {
                                    Log.d ("reference1", "파이어베이스 접근");
                                    //ds.getRef().removeValue();
                                    ds.getRef().setValue(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        });


                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftActionIcon(R.drawable.like_remove)
                        .addSwipeLeftLabel("삭제")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecyclerView);



        // 리스트의 플레이 버튼 클릭 시 해당 플레이리스트 재생
        mRecyclerAdapter.setOnItemClickListener(new LikeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String iddata, String mooddata) {

                new Thread(() -> {
                    select_playlist = ChangeAtoB.getOnePlaylist(iddata);
                    /*
                    getPlaylistData(mooddata, iddata);
                    moodarray.add(iddata);
                    for(int i=0; i<moodselectlist.size(); i++){
                        moodarray.add(moodselectlist.get(i));
                    }

                     */
                    Intent intent = new Intent(getActivity(), CSVStreamingActivity.class);
                    intent.putExtra("selectmood", mooddata);
                    intent.putExtra("select_playlist_popup", select_playlist);
                    intent.putExtra("selectplaylistid", iddata);
                    intent.putExtra("moodplaylist", moodarray); // 해당 무드(a7)의 플레이리스트 ID (29, 30)
                    intent.putExtra("streaming", "0" );
                    startActivity(intent);
                }).start();
            }
        });


        // 검색 입력 시
        like_searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = like_searchview.getText().toString();
                searchFilter(searchText);
            }
        });


        // 무드 태그 버튼
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodchecklist.clear();

                switch (v.getId()){
                    case R.id.moodbtn0:
                        if(moodtagselect[0] == 0) {
                            moodtagselect[0] = 1;
                            moodbtn0.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn0.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[0] = 0;
                            moodbtn0.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn0.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn1:
                        if(moodtagselect[1] == 0) {
                            moodtagselect[1] = 1;
                            moodbtn1.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn1.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[1] = 0;
                            moodbtn1.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn1.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn2:
                        if(moodtagselect[2] == 0) {
                            moodtagselect[2] = 1;
                            moodbtn2.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn2.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[2] = 0;
                            moodbtn2.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn2.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn3:
                        if(moodtagselect[3] == 0) {
                            moodtagselect[3] = 1;
                            moodbtn3.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn3.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[3] = 0;
                            moodbtn3.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn3.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn4:
                        if(moodtagselect[4] == 0) {
                            moodtagselect[4] = 1;
                            moodbtn4.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn4.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[4] = 0;
                            moodbtn4.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn4.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn5:
                        if(moodtagselect[5] == 0) {
                            moodtagselect[5] = 1;
                            moodbtn5.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn5.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[5] = 0;
                            moodbtn5.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn5.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn6:
                        if(moodtagselect[6] == 0) {
                            moodtagselect[6] = 1;
                            moodbtn6.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn6.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[6] = 0;
                            moodbtn6.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn6.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn7:
                        if(moodtagselect[7] == 0) {
                            moodtagselect[7] = 1;
                            moodbtn7.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn7.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[7] = 0;
                            moodbtn7.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn7.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn8:
                        if(moodtagselect[8] == 0) {
                            moodtagselect[8] = 1;
                            moodbtn8.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn8.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[8] = 0;
                            moodbtn8.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn8.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn9:
                        if(moodtagselect[9] == 0) {
                            moodtagselect[9] = 1;
                            moodbtn9.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn9.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[9] = 0;
                            moodbtn9.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn9.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn10:
                        if(moodtagselect[10] == 0) {
                            moodtagselect[10] = 1;
                            moodbtn10.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn10.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[10] = 0;
                            moodbtn10.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn10.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn11:
                        if(moodtagselect[11] == 0) {
                            moodtagselect[11] = 1;
                            moodbtn11.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn11.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[11] = 0;
                            moodbtn11.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn11.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn12:
                        if(moodtagselect[12] == 0) {
                            moodtagselect[12] = 1;
                            moodbtn12.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn12.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[12] = 0;
                            moodbtn12.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn12.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn13:
                        if(moodtagselect[13] == 0) {
                            moodtagselect[13] = 1;
                            moodbtn13.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn13.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[13] = 0;
                            moodbtn13.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn13.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn14:
                        if(moodtagselect[14] == 0) {
                            moodtagselect[14] = 1;
                            moodbtn14.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn14.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[14] = 0;
                            moodbtn14.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn14.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    case R.id.moodbtn15:
                        if(moodtagselect[15] == 0) {
                            moodtagselect[15] = 1;
                            moodbtn15.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeroundselect));
                            moodbtn15.setTextColor(Color.parseColor("#000627"));
                        }
                        else {
                            moodtagselect[15] = 0;
                            moodbtn15.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.likeround));
                            moodbtn15.setTextColor(Color.parseColor("#9ABBFF"));
                        }
                        break;
                    default:
                        break;
                }

                for(int i=0; i<moodtagselect.length; i++) {
                    if(moodtagselect[i]==1) moodchecklist.add("a" + i);
                }

                if(moodchecklist.size() != 0) {
                    moodFilter(moodchecklist);
                } else mRecyclerAdapter.setLikeList(mlikelists);
                Log.d("moodlistselect", String.valueOf(moodchecklist));
            }
        };

        moodbtn0.setOnClickListener(onClickListener);
        moodbtn1.setOnClickListener(onClickListener);
        moodbtn2.setOnClickListener(onClickListener);
        moodbtn3.setOnClickListener(onClickListener);
        moodbtn4.setOnClickListener(onClickListener);
        moodbtn5.setOnClickListener(onClickListener);
        moodbtn6.setOnClickListener(onClickListener);
        moodbtn7.setOnClickListener(onClickListener);
        moodbtn8.setOnClickListener(onClickListener);
        moodbtn9.setOnClickListener(onClickListener);
        moodbtn10.setOnClickListener(onClickListener);
        moodbtn11.setOnClickListener(onClickListener);
        moodbtn12.setOnClickListener(onClickListener);
        moodbtn13.setOnClickListener(onClickListener);
        moodbtn14.setOnClickListener(onClickListener);
        moodbtn15.setOnClickListener(onClickListener);

        return view;
    }



    public void onStart() {
        super.onStart();
        Intent intent2 = new Intent(getActivity(), MusicService.class);
        Log.d("isService", "onStart");
        getActivity().bindService(intent2, conn, Context.BIND_AUTO_CREATE);
    }

    public void onStop(){
        super.onStop();
        getActivity().unbindService(conn);
        isService = false;
    }


    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MusicService.LocalBinder mb = (MusicService.LocalBinder) service;
            musicSrv = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Log.i("isService", name + " 서비스 연결 해제");
            Toast.makeText(getActivity(),
                    "서비스 연결 해제",
                    Toast.LENGTH_LONG).show();
        }
    };

    // 검색
    public void searchFilter(String searchText) {
        filteredlist.clear();

        for (int i = 0; i < mlikelists.size(); i++) {
            if (mlikelists.get(i).getMusictitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredlist.add(mlikelists.get(i));
            }
        }
        mRecyclerAdapter.setLikeList(filteredlist);
    }


    // 무드값에 따른 좋아요 리스트 반환
    public void moodFilter(List<String> moodchecklist){
        filteredmood.clear();

        for (int i = 0; i < mlikelists.size(); i++) {
            for(int j=0; j<moodchecklist.size(); j++) {
                Log.d("mood", mlikelists.get(i).getLikemoodname());
                if (mlikelists.get(i).getLikemoodname().contains(ChangeAtoB.setMood(moodchecklist.get(j)))) {
                    Log.d("moodfor문", ChangeAtoB.setMood(moodchecklist.get(j)));
                    filteredmood.add(mlikelists.get(i));
                }
            }
        }
        mRecyclerAdapter.setLikeList(filteredmood);
    }


    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public void getPlaylistData(String selectmood, String iddata){
        try {
            // 샘플데이터 Playlist.csv 링크
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));

            CSVReader reader = new CSVReader(in);
            String[] nextline;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (!nextline[CSVStreamingActivity.Category.Playlist_Mood.number].equals(selectmood)) {
                    continue;
                }
                // 무드의 플레이리스트 ID 기록
                if(!nextline[CSVStreamingActivity.Category.Playlist_ID.number].equals(iddata)) {
                    moodselectlist.add(nextline[CSVStreamingActivity.Category.Playlist_ID.number]);
                }
            }

            // 플레이리스트 랜덤섞기
            Collections.shuffle(moodselectlist);
            Log.d("nextline_moodselect", String.valueOf(moodselectlist));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
