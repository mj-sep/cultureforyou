package com.example.cultureforyou;

import static android.content.ContentValues.TAG;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class AnnivDialogActivity extends Dialog {

    private final Context context;

    private TextView anniv_nottody; // 오늘 하루 그만 보기
    private TextView anniv_close; // 닫기
    private ImageView anniv_image;
    private TextView anniv_text; // 베토벤바이러스
    private TextView anniv_text2; // 생일
    private TextView anniv_text3; // 생일
    private ImageButton anniv_pl_btn;

    ArrayList<String> picktitlelist = new ArrayList<>();
    ArrayList<String> pickartistlist = new ArrayList<>();
    ArrayList<String> pickmoodlist = new ArrayList<>();
    ArrayList<String> pickplidlist = new ArrayList<String>();

    String pplidlist = "";
    String ptitlelist = "";
    String partistlist = "";
    String pmoodlist = "";

    View.OnClickListener mOnClickListener;

    public AnnivDialogActivity(@NonNull Context context, int fullscreendialogforanniv) {
        super(context);
        this.context = context;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.fullscreendialogforanniv);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.anniv_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        anniv_nottody = findViewById(R.id.anniv_nottoday);
        anniv_close = findViewById(R.id.anniv_close);
        anniv_image = findViewById(R.id.anniv_image);
        anniv_text = findViewById(R.id.anniv_text);
        anniv_text2 = findViewById(R.id.anniv_text2);
        anniv_text3 = findViewById(R.id.anniv_text3);
        anniv_pl_btn = findViewById(R.id.anniv_pl_btn);

        anniv_text.setText(CastAnniv.setNickname());
        anniv_text2.setText(CastAnniv.setAnnivname());
        anniv_text3.setText(CastAnniv.setAnnivname());

        // 닫기 버튼 클릭 시
        anniv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CastAnniv.getClose(true);
                dismiss();
            }
        });

        anniv_nottody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 플레이리스트 들으러 가기 버튼 클릭 시
        anniv_pl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent annivintent = new Intent(context.getApplicationContext(), PickVer1Fragment.class);

                new Thread(() -> {
                    // 드라이브에서 seq에 해당하는 정보 가져와서 분석 후 인텐트
                    getPickData(17);
                    annivintent.putExtra("pick_seq", 17);
                    annivintent.putExtra("pickplidlist", pickplidlist);
                    annivintent.putExtra("pickmoodlist", pickmoodlist);
                    annivintent.putExtra("picktitlelist", picktitlelist);
                    annivintent.putExtra("pickartistlist", pickartistlist);
                    context.startActivity(annivintent);
                }).start();

            }


        });
    }


    public void getPickData(int seq){

        pickplidlist.clear();
        pickmoodlist.clear();
        picktitlelist.clear();
        pickartistlist.clear();

        try {
            String pid = "1iS8GJc-vYba6TdNZ9XVw6iJ9lBqT0NrR";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline;

            while ((nextline = reader.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[1].equals(String.valueOf(seq))) {
                    Log.d("nextline_pickver11_real", Arrays.toString(nextline));
                    pplidlist = nextline[PickVer1Fragment.Category_pick.Playlist_ID_list.number];
                    pplidlist = pplidlist.substring(1, pplidlist.length()-1);
                    pmoodlist = nextline[PickVer1Fragment.Category_pick.Playlist_Mood_list.number];
                    pmoodlist = pmoodlist.substring(1, pmoodlist.length()-1);
                    ptitlelist = nextline[PickVer1Fragment.Category_pick.Music_title_list.number];
                    ptitlelist = ptitlelist.substring(1, ptitlelist.length()-1);
                    partistlist = nextline[PickVer1Fragment.Category_pick.Music_artist_list.number];
                    partistlist = partistlist.substring(1, partistlist.length()-1);
                }

            }
            String[] playlist_id_array = pplidlist.split(", ");
            String[] mood_id_array = pmoodlist.split(", ");
            String[] title_list_array = ptitlelist.split(", ");
            String[] artist_list_array = partistlist.split(", ");

            for (int i = 0; i < playlist_id_array.length; i++) {
                pickplidlist.add(playlist_id_array[i]);
                pickmoodlist.add(mood_id_array[i].substring(1, mood_id_array[i].length()-1));
                picktitlelist.add(title_list_array[i].substring(1, title_list_array[i].length()-1));
                pickartistlist.add(artist_list_array[i].substring(1, artist_list_array[i].length()-1));
            }
            Log.d("nextline_pickver11_plid", String.valueOf(pickplidlist));
            Log.d("nextline_pickver11_pmood", String.valueOf(pickmoodlist));
            Log.d("nextline_pickver11_ptitle", String.valueOf(picktitlelist));
            Log.d("nextline_pickver11_partist", String.valueOf(pickartistlist));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
