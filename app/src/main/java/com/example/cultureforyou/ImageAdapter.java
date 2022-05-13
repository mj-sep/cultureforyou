package com.example.cultureforyou;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ImageAdapter extends PagerAdapter {

    private List<Image> Images;
    private LayoutInflater layoutInflater;
    private Context context;

    String selectmood = "";
    ArrayList<String> moodselect = new ArrayList<>(); // 무드값에 해당하는 플레이리스트ID 집합
    String moodselectid_result = ""; // 무드값에 해당하는 플레이리스트 중 랜덤으로 하나만 추출한 값(ID)
    ArrayList<String> select_playlist = new ArrayList<>(); // 무드값 플레이리스트 중 랜덤으로 하나만 추출했던 ID의 플레이리스트

    public ImageAdapter(List<Image> images, FragmentActivity context) {
        Images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_container,container,false);

        TextView moodid;
        ImageView image;
        TextView title;
        ImageButton playButton;

        moodid = view.findViewById(R.id.moodid);
        image=view.findViewById(R.id.image);
        title=view.findViewById(R.id.title);
        playButton=view.findViewById(R.id.playButton);

        moodid.setText(Images.get(position).getMoodid());
        image.setImageResource(Images.get(position).getImage());
        title.setText(Images.get(position).getTitle());
        container.addView(view,0);

        // 메인 -> 스트리밍 바로 이동
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("getMood", moodid.getText().toString());
                new Thread(() -> {
                    selectmood = moodid.getText().toString();
                    Log.d("getMood", selectmood);

                    getPlaylistData(selectmood);
                    moodselectid_result = moodselect.get(0);
                    select_playlist = ChangeAtoB.getOnePlaylist(moodselectid_result);

                    Intent intent = new Intent(context, CSVStreamingActivity.class);
                    intent.putExtra("selectmood", selectmood); // 선택 감성 (a7)

                    // 선택한 플레이리스트 정보 [29, [669, 670, 671, 672, 673, 674, 675, 676, 677, 678, 679], T_6801, [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], a7]
                    intent.putExtra("select_playlist_popup", select_playlist);
                    intent.putExtra("selectplaylistid", moodselectid_result); // 선택한 플레이리스트 ID (29)
                    intent.putExtra("streaming", "0" );
                    intent.putExtra("moodplaylist", moodselect); // 해당 무드(a7)의 플레이리스트 ID (29, 30)
                    context.startActivity(intent);

                }).start();

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void getPlaylistData(String selectmood){
        try {
            // 샘플데이터 Playlist.csv 링크
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
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
