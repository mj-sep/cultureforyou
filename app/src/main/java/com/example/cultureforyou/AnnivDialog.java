package com.example.cultureforyou;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.Nullable;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class AnnivDialog extends DialogFragment {

    private TextView anniv_nottody; // 오늘 하루 그만 보기
    private TextView anniv_close; // 닫기
    private ImageView anniv_image;
    private TextView anniv_text; // 베토벤바이러스
    private TextView anniv_text2; // 생일
    private TextView anniv_text3; // 생일
    private ImageButton anniv_pl_btn;

    static AnnivDialog newInstance(){
        return new AnnivDialog();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullscreendialogforanniv);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anniv_dialog, container, false);

        anniv_nottody = view.findViewById(R.id.anniv_nottoday);
        anniv_close = view.findViewById(R.id.anniv_close);
        anniv_image = view.findViewById(R.id.anniv_image);
        anniv_text = view.findViewById(R.id.anniv_text);
        anniv_text2 = view.findViewById(R.id.anniv_text2);
        anniv_text3 = view.findViewById(R.id.anniv_text3);
        anniv_pl_btn = view.findViewById(R.id.anniv_pl_btn);

        anniv_text.setText(CastAnniv.setNickname());
        anniv_text2.setText(CastAnniv.setAnnivname());
        anniv_text3.setText(CastAnniv.setAnnivname());

        // 닫기 버튼 클릭 시
        anniv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
