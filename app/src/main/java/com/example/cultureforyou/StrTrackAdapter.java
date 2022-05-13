package com.example.cultureforyou;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
            holder.moodtxt = (TextView) convertView.findViewById(R.id.moodtxt);
            holder.Mtitle = (TextView) convertView.findViewById(R.id.Mtitle);
            holder.Mcomposer = (TextView) convertView.findViewById(R.id.Mcomposer);
            holder.mplaylistid = (TextView) convertView.findViewById(R.id.mplaylistid);
            holder.moodimg = (ImageView) convertView.findViewById(R.id.moodimg);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        ListDTO dto = listCustom.get(position);
        holder.moodtxt.setText(dto.getMoodtxt());
        holder.Mtitle.setText(dto.getMtitle());
        holder.Mcomposer.setText(dto.getMcomposer());
        holder.mplaylistid.setText(dto.getMplaylistid());
        holder.moodimg.setImageResource(dto.getMoodimg());

        return convertView;
    }

    class CustomViewHolder {
        TextView moodtxt;
        TextView Mtitle;
        TextView Mcomposer;
        TextView mplaylistid;
        ImageView moodimg;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(ListDTO dto) {
        listCustom.add(dto);
    }

}
