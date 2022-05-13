package com.example.cultureforyou;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Image {

    private String moodid;
    private int image;
    private String title;
    private String desc;

    public Image(String moodid, int image, String title, String desc) {
        this.moodid = moodid;
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoodid() {return moodid;}

    public void setMoodid(String moodid) {this.moodid = moodid;}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



}
