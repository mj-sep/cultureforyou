package com.example.cultureforyou;

import android.animation.ArgbEvaluator;
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
import androidx.constraintlayout.motion.widget.MotionLayout;
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
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {

    ViewPager viewPager;
    ImageAdapter adapter;
    List<Image> Images;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ImageButton playButton;
    ImageButton setting_button;
    ImageButton btn_profile;
    ImageButton btn_main_start;
    TextView m_music_title;
    TextView m_music_artist;
    LinearLayout currentplayinfo;
    MotionLayout mainMotionlayout;


    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String uid = "";
    public static Context mContext;

    String selectmood = "";
    ArrayList<String> moodselect = new ArrayList<>(); // 무드값에 해당하는 플레이리스트ID 집합
    String moodselectid_result = ""; // 무드값에 해당하는 플레이리스트 중 랜덤으로 하나만 추출한 값(ID)
    ArrayList<String> select_playlist = new ArrayList<>(); // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트

    private View view;
    private String TAG = "프래그먼트";

    // 서비스
    private MusicService musicSrv;
    boolean isService = false;
    private Intent playIntent;
    private boolean musicBound = false;
    private static final int REQUEST_CODE = 200;

    int check = 0; // 음악 제목, 작곡가 체크
    boolean isPlaying = true; // 현재 재생 중 1, 아님 0


    int annivonoff = 0;
    String anniv_mood;
    String anniv_date;
    String anniv_name;
    String nickname;


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
        btn_main_start = view.findViewById(R.id.main_start);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // 초기 설정 - 스트리밍바 안 보이게
        currentplayinfo.setVisibility(View.GONE);

        Intent intent = getActivity().getIntent();
        int test = intent.getIntExtra("isPlayingNow", 100);
        Log.d("PlayingMainFragment", String.valueOf(test));

        // 현재 사용자 업데이트
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Log.d("select_uil", uid);
        }
        String id = user.getUid();


        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");



        if(isService) {
            m_music_title.setText(ChangeAtoB.setCurrentMusicTitle());
        }


        // 재생, 정지 버튼 클릭 시
        btn_main_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv.isPlayingCurrent()) {
                    Log.i("isService", "MainFragment stopmusicsrv");
                    musicSrv.stopMusicService();
                    btn_main_start.setImageResource(R.drawable.str_start);
                } else {
                    Log.i("isService", "MainFragment startmusicsrv");
                    musicSrv.playMusicService();
                    btn_main_start.setImageResource(R.drawable.str_stop);
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


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String today = sdf.format(date);
        Log.d("anniv_to", today);

        // 기념일 관련
        reference.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    annivonoff = snapshot1.child("anniv_onoff").getValue(Integer.class);
                    nickname = snapshot1.child("nickname").getValue(String.class);
                    if(annivonoff == 1) {
                        anniv_date = snapshot1.child("anniversary").getValue(String.class);
                        anniv_name = snapshot1.child("anniversary_name").getValue(String.class);
                        anniv_mood = snapshot1.child("anni_mood").getValue(String.class);

                        CastAnniv.getOnoff(annivonoff);
                        CastAnniv.getAnnivmood(anniv_mood);
                        CastAnniv.getAnnivdate(anniv_date);
                        CastAnniv.getAnnivname(anniv_name);
                        CastAnniv.getNickname(nickname);

                        Log.d("anniv_name", anniv_name);
                        Log.d("anniv_date", anniv_date);
                        Log.d("anniv_mood", anniv_mood);
                        Log.d("anniv_nickname", nickname);

                        if(CastAnniv.setgetOnoff() == 1 && CastAnniv.setAnnivdate().equals(today) && CastAnniv.setClose() == false){
                            // 기념일 팝업
                            AnnivDialogActivity anniv_dialog = new AnnivDialogActivity(getActivity(), R.style.Theme_Dialog);
                            anniv_dialog.show();
                        }
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
            }

        });

        Images = new ArrayList<>();
        Images.add(new Image("a0", R.drawable.main_1_active, "활기찬", ""));
        Images.add(new Image("a1", R.drawable.main_2_strong, "강렬한", ""));
        Images.add(new Image("a2", R.drawable.main_3_joyful, "즐거운", ""));
        Images.add(new Image("a3", R.drawable.main_4_amazing, "놀라운", ""));
        Images.add(new Image("a4", R.drawable.main_5_horror, "공포스러운", ""));
        Images.add(new Image("a5", R.drawable.main_6_unpleasant, "불쾌한", ""));
        Images.add(new Image("a6", R.drawable.main_7_anxious, "불안한", ""));
        Images.add(new Image("a7", R.drawable.main_8_drowsy, "나른한", ""));
        Images.add(new Image("a8", R.drawable.main_9_depressed, "우울한", ""));
        Images.add(new Image("a9", R.drawable.main_10_static, "정적인", ""));
        Images.add(new Image("a10", R.drawable.main_11_still, "잔잔한", ""));
        Images.add(new Image("a11", R.drawable.main_12_comfort, "편안한", ""));
        Images.add(new Image("a12", R.drawable.main_13_happy, "행복한", ""));
        Images.add(new Image("a13", R.drawable.main_14_friendly, "친근한", ""));
        Images.add(new Image("a14", R.drawable.main_15_mysterious, "신비로운", ""));
        Images.add(new Image("a15", R.drawable.main_16_graceful, "우아한", ""));

        adapter = new ImageAdapter(Images, getActivity());

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);

        int dpValue = 90;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 20);


        // 뷰페이저 좌우 이동에 따른 크기 조정
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = viewPager.getMeasuredWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight();
                int pageHeight = viewPager.getHeight();
                int paddingLeft = viewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;

                final float normalizedposition = Math.abs(Math.abs(transformPos) - 1);
                page.setAlpha(normalizedposition + 0.5f);
                float pos = Math.abs(1-Math.abs(transformPos));

                if (transformPos < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setTranslationY(0);
                    page.setScaleX(0.7f);
                    page.setScaleY(0.7f);
                } else if (transformPos <= 1) { // [-1,1]
                    if(pos < 0.71) {
                        page.setScaleX(0.7f);
                        page.setScaleY(0.7f);
                    } else {
                        page.setScaleX(pos);
                        page.setScaleY(pos);
                    }
                    Log.d("test", String.valueOf(pos));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setTranslationY(0);
                    page.setScaleX(0.7f);
                    page.setScaleY(0.7f);
                }


            }
        });


        // 재생 정보 띄우기
        TimerTask t0 = new TimerTask() {
            @Override
            public void run() {
                if(getActivity() == null)
                    return;

                String m_title = m_music_title.getText().toString();
                String cha_set_title = ChangeAtoB.setCurrentMusicTitle();

                if(cha_set_title != "Unknown" && cha_set_title != m_title){
                    check = 1;
                    Log.d("isService", "메인 페이지에서 제목 띄우기");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            currentplayinfo.setVisibility(View.VISIBLE);
                            m_music_title.setText(ChangeAtoB.setCurrentMusicTitle());
                            m_music_artist.setText(ChangeAtoB.setCurrentMusicComposer());

                        }
                    });
                }


                if(cha_set_title != "Unknown") {
                    if(isPlaying == musicSrv.isPlayingCurrent()) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                btn_main_start.setImageResource(R.drawable.str_stop);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                btn_main_start.setImageResource(R.drawable.str_start);
                            }
                        });
                    }
                }

            }
        };

        Timer timer = new Timer();
        timer.schedule(t0, 0, 1000);

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


    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public void getPlaylistData(String selectmood){
        try {
            /* 본데이터 Playlist.csv 링크
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
            https://drive.google.com/uc?export=view&id=1-5RiipcJZgjM20xdE3Ok1iHPVzy2q-Ns
             */
            // 샘플데이터 Playlist.csv 링크
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
            // 본 String pid = "1ULBLk0bYuSeBAbXtyGSmzBA3djOQpeI2lZkP_2YMFyo";

            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));

            CSVReader reader = new CSVReader(in);
            String[] nextline;
            Integer j = 0;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (!nextline[CSVStreamingActivity.Category.Playlist_Mood.number].equals(selectmood)) {
                    continue;
                }
                Log.d("nextline_csv", Arrays.toString(nextline));

                // 무드의 플레이리스트 ID 기록
                moodselect.add(nextline[CSVStreamingActivity.Category.Playlist_ID.number]);
            }

            // 플레이리스트 랜덤섞기
            Collections.shuffle(moodselect);
            Log.d("nextline_moodselect", String.valueOf(moodselect));
            // in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
