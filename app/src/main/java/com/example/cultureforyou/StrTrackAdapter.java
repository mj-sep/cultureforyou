package com.example.cultureforyou;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StrTrackAdapter extends BaseAdapter {

    private ArrayList<ListDTO> listCustom = new ArrayList<>();

    @Override
    public int getCount() {
        return listCustom.size();
    }

    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlistview, null, false);

            holder = new CustomViewHolder();
            holder.minimood = (TextView) convertView.findViewById(R.id.minimoodtxt);
            holder.startsecond = (TextView) convertView.findViewById(R.id.startsecondlist);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        ListDTO dto = listCustom.get(position);

        if(dto.getCheck() == 1) {
            //dto.setCheck(0);
            holder.minimood.setText(dto.getMinimood());
            holder.minimood.setTextColor(Color.YELLOW);
            holder.startsecond.setText(dto.getStartsecond());
            holder.startsecond.setTextColor(Color.YELLOW);
        } else {
            holder.minimood.setText(dto.getMinimood());
            holder.minimood.setTextColor(Color.WHITE);
            holder.startsecond.setText(dto.getStartsecond());
            holder.startsecond.setTextColor(Color.WHITE);

        }
        return convertView;
    }

    class CustomViewHolder {
        TextView minimood;
        TextView startsecond;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(ListDTO dto) {
        listCustom.add(dto);
    }

}
