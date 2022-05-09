package com.example.cultureforyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TabCategory1 extends Fragment {

    TextView eventnickname;
    RelativeLayout recomm_rainy;
    RelativeLayout recomm_happy;
    RelativeLayout recomm_coffee;
    RelativeLayout recomm_alone;
    RelativeLayout recomm_morning;
    RelativeLayout recomm_walk;
    RelativeLayout recomm_dance;
    RelativeLayout recomm_horror;
    RelativeLayout recomm_sun;
    RelativeLayout recomm_swan;

    View.OnClickListener onClickListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_category1, container, false);

        eventnickname = rootView.findViewById(R.id.eventnickname);
        recomm_rainy = rootView.findViewById(R.id.recomm_rainy);
        recomm_happy = rootView.findViewById(R.id.recomm_happy);
        recomm_coffee = rootView.findViewById(R.id.recomm_coffee);
        recomm_alone = rootView.findViewById(R.id.recomm_alone);
        recomm_morning = rootView.findViewById(R.id.recomm_morning);
        recomm_walk = rootView.findViewById(R.id.recomm_walk);
        recomm_dance = rootView.findViewById(R.id.recomm_dance);
        recomm_horror = rootView.findViewById(R.id.recomm_horror);
        recomm_sun = rootView.findViewById(R.id.recomm_sun);
        recomm_swan = rootView.findViewById(R.id.recomm_swan);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seq = 0; // 순서번호
                switch (v.getId()) {
                    case R.id.recomm_rainy:
                        seq = 0;
                        break;
                    case R.id.recomm_happy:
                        seq = 1;
                        break;
                    case R.id.recomm_coffee:
                        seq = 2;
                        break;
                    case R.id.recomm_alone:
                        seq = 3;
                        break;
                    case R.id.recomm_morning:
                        seq = 4;
                        break;
                    case R.id.recomm_walk:
                        seq = 5;
                        break;
                    case R.id.recomm_dance:
                        seq = 6;
                        break;
                    case R.id.recomm_horror:
                        seq = 7;
                        break;
                    case R.id.recomm_sun:
                        seq = 8;
                        break;
                    case R.id.recomm_swan:
                        seq = 9;
                        break;
                    default:
                        break;
                }
                Intent pickintent1 = new Intent(getActivity(), PickVer1Fragment.class);
                pickintent1.putExtra("pick_seq", seq);
                startActivity(pickintent1);

                /*
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putInt("pick_seq", seq);//번들에 넘길 값 저장
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                PickVer1Fragment fragment2 = new PickVer1Fragment();//프래그먼트2 선언
                fragment2.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.frame, fragment2);
                transaction.commit();

                 */
            }
        };

        recomm_rainy.setOnClickListener(onClickListener);
        recomm_happy.setOnClickListener(onClickListener);
        recomm_coffee.setOnClickListener(onClickListener);
        recomm_alone.setOnClickListener(onClickListener);
        recomm_morning.setOnClickListener(onClickListener);
        recomm_walk.setOnClickListener(onClickListener);
        recomm_dance.setOnClickListener(onClickListener);
        recomm_horror.setOnClickListener(onClickListener);
        recomm_sun.setOnClickListener(onClickListener);
        recomm_swan.setOnClickListener(onClickListener);

        return rootView;
    }
}
