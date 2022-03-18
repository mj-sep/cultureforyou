package com.example.cultureforyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

// 프로필 설정 - 선호하는 아티스트 어댑터
public class FavArtistAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    String[] arrArtistName;
    String[] arrArtistImage;

    public FavArtistAdapter(Context context, String[] arrArtistName, String[] arrArtistImage) {
        this.context = context;
        this.arrArtistImage = arrArtistImage;
        this.arrArtistName = arrArtistName;
    }

    public int getCount(){
        return arrArtistName.length;
    }

    public Object getItem(int position) {
        return arrArtistName[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view==null){
            view = inflater.inflate(R.layout.fav_artist_layout, null);
        }

        ImageView favorite_artist_image = view.findViewById(R.id.favorite_artist_image);
        TextView favorite_artist_name = view.findViewById(R.id.favorite_artist_name);


        Glide.with(context.getApplicationContext())
                .load("https://drive.google.com/uc?export=view&id=" + arrArtistImage[position])
                .into(favorite_artist_image);
        //favorite_artist_image.setImageResource(Integer.parseInt(arrArtistImage[position]));
        favorite_artist_name.setText(arrArtistName[position]);

        return view;
    }
}
