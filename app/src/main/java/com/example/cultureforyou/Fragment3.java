package com.example.cultureforyou;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class Fragment3 extends Fragment {

    private View view;
    private String TAG = "프래그먼트";

    TabLayout tabs;

    TabCategory1 tab_category1;
    TabCategory2 tab_category2;
    //Fragment3 tab_category3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_3, container, false);

        tab_category1 = new TabCategory1();
        tab_category2 = new TabCategory2();
        //tab_category3 = new Fragment3();

        getFragmentManager().beginTransaction().add(R.id.container, tab_category1).commit();

        tabs = view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("전체보기"));
        tabs.addTab(tabs.newTab().setText("상황별"));
        //tabs.addTab(tabs.newTab().setText("더보기"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0)
                    selected = tab_category1;
                else if(position == 1)
                    selected = tab_category2;
                //else if(position == 2)
                    //selected = tab_category3;
                getFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
