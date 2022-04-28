package com.example.cultureforyou;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LikeRecyclerAdapter extends RecyclerView.Adapter<LikeRecyclerAdapter.ViewHolder> {

    private ArrayList<LikelistDTO> mlikelist = new ArrayList<LikelistDTO>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position, String iddata, String mooddata) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }



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
        ImageView like_playbtn;

        public ViewHolder(View itemView) {
            super(itemView);

            selectmoodimg = (ImageView) itemView.findViewById(R.id.likemoodimg);
            selectmusictitle = (TextView) itemView.findViewById(R.id.likemusictitle);
            selectmusicartist = (TextView) itemView.findViewById(R.id.likemusicartist);
            moodtxt = (TextView) itemView.findViewById(R.id.moodtxt);
            selectplistid = (TextView) itemView.findViewById(R.id.selectplistid);
            like_playbtn = (ImageView) itemView.findViewById(R.id.like_playbtn);

            // 플레이 버튼 클릭 시
            like_playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            String iddata = "";
                            String mooddata = "";
                            iddata = selectplistid.getText().toString();
                            mooddata = moodtxt.getText().toString();
                            mListener.onItemClick(v, pos, iddata, mooddata);
                        }
                    }
                }
            });



            }

        void onBind(LikelistDTO item) {
            selectmoodimg.setImageResource(item.getImgID());
            selectmusictitle.setText(item.getMusictitle());
            selectmusicartist.setText(item.getMusitartist());
            moodtxt.setText(item.getLikemoodname());
            selectplistid.setText(item.getPlaylistID());
        }

       }


    }



