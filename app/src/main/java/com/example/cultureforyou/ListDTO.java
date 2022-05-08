package com.example.cultureforyou;

public class ListDTO {
    private String moodtxt;
    private String Mtitle;
    private String Mcomposer;
    private String mplaylistid;
    private int check;
    private int moodimg;

    public String getMplaylistid() {
        return mplaylistid;
    }

    public void setMplaylistid(String mplaylistid){ this.mplaylistid = mplaylistid; }

    public String getMoodtxt() { return moodtxt; }

    public void setMoodtxt(String moodtxt) {
        this.moodtxt = moodtxt;
    }

    public String getMtitle() {
        return Mtitle;
    }

    public void setMtitle(String Mtitle) { this.Mtitle = Mtitle; }

    public String getMcomposer() {return Mcomposer;}

    public void setMcomposer(String Mcomposer) { this.Mcomposer = Mcomposer; }

    public int getMoodimg() { return moodimg; }

    public void setMoodimg(int moodimg) {this.moodimg = moodimg;}
}
