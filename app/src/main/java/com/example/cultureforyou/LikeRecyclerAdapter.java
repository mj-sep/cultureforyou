package com.example.cultureforyou;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LikeRecyclerAdapter extends RecyclerView.Adapter<LikeRecyclerAdapter.ViewHolder> {

    private ArrayList<LikelistDTO> mlikelist = new ArrayList<LikelistDTO>();

    @NonNull
    @Override
    public LikeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likeitem_recycerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(mlikelist.get(position));
    }

    public void setLikeList(ArrayList<LikelistDTO> list) {
        this.mlikelist = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mlikelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView selectmoodimg;
        TextView selectmusictitle;
        TextView selectmusicartist;
        TextView moodtxt;
        TextView selectplistid;

        public ViewHolder(View itemView) {
            super(itemView);

            selectmoodimg = (ImageView) itemView.findViewById(R.id.likemoodimg);
            selectmusictitle = (TextView) itemView.findViewById(R.id.likemusictitle);
            selectmusicartist = (TextView) itemView.findViewById(R.id.likemusicartist);
            moodtxt = (TextView) itemView.findViewById(R.id.moodtxt);
            selectplistid = (TextView) itemView.findViewById(R.id.selectplistid);

            }

        void onBind(LikelistDTO item) {
            selectmoodimg.setImageResource(item.getImgID());

            selectmusictitle.setText(item.getMusictitle());
            selectmusictitle.setSingleLine(true);
            selectmusictitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            selectmusictitle.setSelected(true);

            selectmusicartist.setText(item.getMusitartist());
            selectmusicartist.setSingleLine(true);
            selectmusicartist.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            selectmusicartist.setSelected(true);

            moodtxt.setText(item.getLikemoodname());
            selectplistid.setText(item.getPlaylistID());
        }

       }


    }

