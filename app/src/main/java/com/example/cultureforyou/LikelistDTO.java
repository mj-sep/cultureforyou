package com.example.cultureforyou;

public class LikelistDTO {
    String playlistID;
    int imgID;
    String likemoodname;
    String musictitle;
    String musitartist;

    public LikelistDTO(String playlistID, int imgID, String likemoodname, String musictitle, String musitartist) {
        this.playlistID = playlistID;
        this.imgID = imgID;
        this.likemoodname = likemoodname;
        this.musictitle = musictitle;
        this.musitartist = musitartist;
    }

    public String getPlaylistID(){
        return playlistID;
    }

    public int getImgID(){
        return imgID;
    }

    public String getLikemoodname() {
        return likemoodname;
    }

    public String getMusictitle() {
        return musictitle;
    }

    public String getMusitartist() {
        return musitartist;
    }

    public void setPlaylistID(String playlistID){
        this.playlistID = playlistID;
    }

    public void setImgID(int imgID){
        this.imgID = imgID;
    }

    public void setLikemoodname(String likemoodname) {
        this.likemoodname = likemoodname;
    }

    public void setMusictitle(String musictitle) {
        this.musictitle = musictitle;
    }

    public void setMusitartist(String musitartist) {
        this.musitartist = musitartist;
    }

}
