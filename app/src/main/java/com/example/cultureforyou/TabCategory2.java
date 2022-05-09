package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabCategory2 extends Fragment {

    TextView eventnickname;
    RelativeLayout recomm_mozart;
    RelativeLayout recomm_haydn;
    RelativeLayout recomm_chai;
    RelativeLayout recomm_bach;
    RelativeLayout recomm_brahms;
    RelativeLayout recomm_handel;

    View.OnClickListener onClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_category2, container, false);

        eventnickname = rootView.findViewById(R.id.eventnickname);
        recomm_mozart = rootView.findViewById(R.id.recomm_mozart);
        recomm_haydn = rootView.findViewById(R.id.recomm_haydn);
        recomm_chai = rootView.findViewById(R.id.recomm_chai);
        recomm_bach = rootView.findViewById(R.id.recomm_bach);
        recomm_brahms = rootView.findViewById(R.id.recomm_brahms);
        recomm_handel = rootView.findViewById(R.id.recomm_handel);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seq = 10; // 순서번호
                switch (v.getId()) {
                    case R.id.recomm_mozart:
                        seq = 10;
                        break;
                    case R.id.recomm_bach:
                        seq = 11;
                        break;
                    case R.id.recomm_haydn:
                        seq = 12;
                        break;
                    case R.id.recomm_brahms:
                        seq = 13;
                        break;
                    case R.id.recomm_chai:
                        seq = 14;
                        break;
                    case R.id.recomm_handel:
                        seq = 15;
                        break;
                    default:
                        break;
                }
                Intent pickintent2 = new Intent(getActivity(), PickVer2Fragment.class);
                pickintent2.putExtra("pick_seq", seq);
                startActivity(pickintent2);



            }
        };

        recomm_mozart.setOnClickListener(onClickListener);
        recomm_bach.setOnClickListener(onClickListener);
        recomm_haydn.setOnClickListener(onClickListener);
        recomm_brahms.setOnClickListener(onClickListener);
        recomm_chai.setOnClickListener(onClickListener);
        recomm_handel.setOnClickListener(onClickListener);

        return rootView;
    }
}
