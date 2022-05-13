package com.example.cultureforyou;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChangeAtoB {


    private static String pick_title;

    private static String music_title = ""; // 현재 음악 제목
    private static String music_composer = ""; // 현재 음악 작곡가
    private static int music_play = 0; // 정지

    private static String selectmood = ""; // 플리 대표감성

    private static ArrayList<String> moodtracklist = new ArrayList<>(); // 같은 무드의 플레이리스트 정보
    private static ArrayList<String> moodtrackmusicid = new ArrayList<>();
    private static ArrayList<String> moodtracktitle = new ArrayList<>(); // 같은 무드의 플레이리스트 정보 - 음악 제목
    private static ArrayList<String> moodtrackcomposer = new ArrayList<>(); // 같은 무드의 플레이리스트 정보 - 작곡가명
    private static ArrayList<String> moodlist = new ArrayList<>(); // 대표감성 리스트



    private static ArrayList<String> getPlaylist = new ArrayList<>();

    public static String favorite_artist_name(int id){
        switch (id) {
            case 0:
                return "베토벤";
            case 1:
                return "드뷔시";
            case 2:
                return "슈베르트";
            case 3:
                return "바흐";
            case 4:
                return "헨델";
            case 5:
                return "모짜르트";
            case 6:
                return "차이코프스키";
            case 7:
                return "멘델스존";
            case 8:
                return "비발디";
            case 9:
                return "모네";
            case 10:
                return "반 고흐";
            case 11:
                return "고갱";
            case 12:
                return "세잔";
            case 13:
                return "피카소";
            case 14:
                return "다빈치";
            case 15:
                return "샤갈";
            case 16:
                return "르누아르";
            case 17:
                return "마그리트";
            default:
                return "";
        }
    }

    public static String icon_gdrive_id (String icon_id){
        switch (icon_id) {
            case "icon_painting_1":
                return "1PjGreqBBanIM_FUweNORd4yhv7qM-H-K";
            case "icon_painting_2":
                return "1pzvD_VjWlvSKtylx3-cwwdINDePT9x_c";
            case "icon_painting_3":
                return "1gmRRONvrDT6Bn2oferlOKbQo1-P_G8yL";
            case "icon_painting_4":
                return "1SHKCqTBrNxHB26psPlZqkeXbXFYO8Pl8";
            case "icon_painting_5":
                return "1B1c6iWGY8exrXYE7QM7PzwtPpKCXT0_Q";
            case "icon_painting_6":
                return "1YhYtlLo8-jk9up1ux3EO-LX247ESQvPg";
            case "icon_painting_7":
                return "1CD1GI2XEMOR2alZ4XrWxWy-D0GyCrHnq";
            case "icon_painting_8":
                return "1umjcBjkFRlxlCeR0l9dpv1e7ZmcFxMPd";
            case "icon_painting_9":
                return "1d15pRKnDq8peS89Rb47POC4qatorDuO6";
            case "icon_painting_10":
                return "1dcdlhRdvmgWZ6hdAAyBMhsB7MLgbnMUx";
            case "icon_painting_11":
                return "1EKpokr09VnCfEbzlkANa776PXZfiz51e";
            case "icon_painting_12":
                return "1OTcYfpU0T8JA7JRbzFsL6KzxtpfZcDlo";
            case "icon_painting_13":
                return "1AxrvMXKKF0OzbQHPYnjz0aJDPkXGTueM";
            case "icon_painting_14":
                return "119sfq5bqSHjK6aFb3v-EMsAeAK8A2adj";
            case "icon_painting_15":
                return "1E7D8kyUwBdu4m_-G2jXx-h1vplhZreQN";
            default:
                return "";
        }
    }

    public static String FA_gdrive_id(String icon_id){
        switch (icon_id) {
            case "0":
                return "1N4VyeBJOTnNXON9Jtvsb3EH4hIn4fyzE";
            case "1":
                return "1hqjeqviDzjr-7xmP5k-T5bPLenF3bxOH";
            case "2":
                return "1Q7By9XD2ifTafCBjXR0SykzSTE5OM_cV";
            case "3":
                return "1GGQzv2YerXDoc7DSZ47lEzdhQ03FaYT6";
            case "4":
                return "1hv0lQj1nCej5GkiXVb4RAJxJ98DNFgUo";
            case "5":
                return "1dwz0UHfr0PFQemiRjrwSuy1URFaIYaLh";
            case "6":
                return "1PciAt3ksoF7YR7VJaWS976-azULS93Eu";
            case "7":
                return "1GUKLdtgKV3J1i3GdZR2FNs44qVDV36ar";
            case "8":
                return "1eWTZh_Pi9-cQs-PNbaQ3YcCYAMCR59-4";
            case "9":
                return "1ZGe-27WdeE6uzftBEStQNc5MtJVqnaT7";
            case "10":
                return "1H5zvanEpAwBM4epqha9R0CO8cKgucw3E";
            case "11":
                return "1GSlrSDlp-cuTjH_LbIcB8JtS5Ikilf1C";
            case "12":
                return "1nWyk03CRp4ZKKBJDsYv1H9k8CjLYTexJ";
            case "13":
                return "11vredZxxLqF8DrgzQhHHDUkBgHHIP3ao";
            case "14":
                return "1n5VIZfn3mGTvL4qDAJ-qohl0xeTXSKwI";
            case "15":
                return "1Ta_bs-RTI17b5SO2E7xwHtTRvnF2_914";
            case "16":
                return "1VOSmV1RoqyD-upuYwktToowjPjzzfkqA";
            case "17":
                return "1eUAoI1UOktF6L1dy1g82na3O3Jtksu_0";
            default:
                return "";
        }
    }




    public static String setMood(String selectmood){
        switch (selectmood){
            case "a0": selectmood = "활기찬";
                break;
            case "a1" : selectmood = "강렬한";
                break;
            case "a2" : selectmood = "즐거운";
                break;
            case "a3" : selectmood = "놀라운";
                break;
            case "a4": selectmood = "공포스러운";
                break;
            case "a5": selectmood = "불쾌한";
                break;
            case "a6": selectmood = "불안한";
                break;
            case "a7": selectmood = "나른한";
                break;
            case "a8": selectmood = "우울한";
                break;
            case "a9": selectmood = "정적인";
                break;
            case "a10": selectmood = "잔잔한";
                break;
            case "a11": selectmood = "편안한";
                break;
            case "a12": selectmood = "행복한";
                break;
            case "a13": selectmood = "친근한";
                break;
            case "a14": selectmood = "신비로운";
                break;
            case "a15": selectmood = "우아한";
                break;
        }
        return selectmood;
    }

    // 무드값에 따른 이미지 변환
    public static int changeimg (String selectmood){
        int imgdrawable = 0;
        switch (selectmood) {
            case "a0": imgdrawable = R.drawable.main_1_active;
                break;
            case "a1" : imgdrawable = R.drawable.main_2_strong;
                break;
            case "a2" : imgdrawable = R.drawable.main_3_joyful;
                break;
            case "a3" : imgdrawable = R.drawable.main_4_amazing;
                break;
            case "a4": imgdrawable = R.drawable.main_5_horror;
                break;
            case "a5": imgdrawable = R.drawable.main_6_unpleasant;
                break;
            case "a6": imgdrawable = R.drawable.main_7_anxious;
                break;
            case "a7": imgdrawable = R.drawable.main_8_drowsy;
                break;
            case "a8": imgdrawable = R.drawable.main_9_depressed;
                break;
            case "a9": imgdrawable = R.drawable.main_10_static;
                break;
            case "a10": imgdrawable = R.drawable.main_11_still;
                break;
            case "a11": imgdrawable = R.drawable.main_12_comfort;
                break;
            case "a12": imgdrawable = R.drawable.main_13_happy;
                break;
            case "a13": imgdrawable = R.drawable.main_14_friendly;
                break;
            case "a14": imgdrawable = R.drawable.main_15_mysterious;
                break;
            case "a15": imgdrawable = R.drawable.main_16_graceful;
                break;
        }
        return imgdrawable;
    }

    // 플레이리스트 csv 데이터 가공 -> 플레이리스트 하나의 정보 뽑기
    public static ArrayList getOnePlaylist(String moodselectid_result){
        ArrayList<String> select_playlist = new ArrayList<>();
        try {
            /* 본데이터
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
             */
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));
            CSVReader reader2 = new CSVReader(in);
            String[] nextline;

            // Integer j = 0;
            while ((nextline = reader2.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[Category.Playlist_ID.number].equals(moodselectid_result)) {
                    //Log.d("nextline_select", Arrays.toString(nextline));
                    for(int i=0; i<Category.values().length; i++) {
                        select_playlist.add(nextline[i+1]);
                    }
                }
            }
            Log.d("nextline_Fileerpp", "atob finish");
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return select_playlist;
    }


    public static String getOnlyMusicID(String moodselectid_result){
        String onlymusicid = "";
        try {
            String pid = "1jABcrRx1HJqWkyMfhgrVTwAPwDXk88iAorr3AvpQGm8";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openConnection().getInputStream()));
            CSVReader reader2 = new CSVReader(in);
            String[] nextline;

            Integer j = 0;

            while ((nextline = reader2.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[Category.Playlist_ID.number].equals(moodselectid_result)) {
                    onlymusicid = nextline[3];
                }

            }
            Log.d("nextline_Fileerpp", "atob finish");
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlymusicid;
    }


    public static ArrayList<String> getMusicDT(String music_ID){
        ArrayList<String> musicinfor = new ArrayList<>();
        try {
            String pid = "1-2oAHqu7JaS1Ufvw7aZ-v7BZ4Bd8DzSZPMVIdIvXXF8";
            URL stockURL = new URL("https://docs.google.com/spreadsheets/d/" + pid + "/export?format=csv");

            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
            CSVReader reader = new CSVReader(in);
            String[] nextline2;

            Integer j = 0;

            while ((nextline2 = reader.readNext()) != null) {
                // 선택된 플레이리스트의 Music_ID와 동일한 음악 정보 추출
                if (nextline2[CSVStreamingActivity.Category_Music.Music_ID.number].equals(music_ID)) {
                    Log.d("nextline_music", Arrays.toString(nextline2));
                    musicinfor.add(nextline2[2]);
                    musicinfor.add(nextline2[3]);
                    break;
                }
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return musicinfor;
    }





    // 메인에서 쓸 현재 재생중인 음악 제목
    public static void getCurrentMusicTitle(String str_music_title){
        music_title = str_music_title;
    }

    public static String setCurrentMusicTitle() {
        if(music_title == "") {
            return "Unknown";
        } else return music_title;
    }

    // 메인에서 쓸 현재 재생중인 음악 작곡가
    public static void getCurrentMusicComposer(String str_music_composer){
        music_composer = str_music_composer;
    }

    public static String setCurrentMusicComposer() {
        return music_composer;
    }


    // 메인에서 쓸 현재 재생중 판별
    public static void getCurrentPlay(int status){
        if(status == 1) music_play = 1;
        else music_play = 0;
    }

    public static int setCurrentPlay() {
        return music_play;
    }

    // 재생목록 Dialog에서 사용 - musicid
    public static void getmoodtrackmusicid(ArrayList trackid) {
        moodtrackmusicid = trackid;
    }

    public ArrayList setMoodtrackmusicid(){
        return moodtrackmusicid;
    }

    // 재생목록 Dialog에서 사용 - musictitle
    public static void getmoodtracktitle(ArrayList tracktitle) { moodtracktitle = tracktitle; }

    public static ArrayList setMoodtracktitle(){
        return moodtracktitle;
    }

    // 재생목록 Dialog에서 사용 - musiccomposer
    public static void getmoodtrackcomposer(ArrayList trackcomposer) { moodtrackcomposer = trackcomposer; }

    public static ArrayList setMoodtrackcomposer(){
        return moodtrackcomposer;
    }

    // 재생목록 Dialog에서 사용 - musicklist
    public static void getmoodtracklist(ArrayList tracklist) {
        moodtracklist = tracklist;
    }

    public static ArrayList setMoodtracklist(){
        return moodtracklist;
    }

    // 재생목록 Dialog에서 사용 - selectmood
    public static void getSelectmood(String mood) {
        selectmood = mood;
    }

    public static String setSelectmood(){
        return selectmood;
    }

    // 재생목록 Dialog에서 사용 - 추천 플레이리스트에서 접속 시 추천 플리 제목
    public static void getPickTitle(String title) {pick_title = title;}

    public static String setPickTitle() {return pick_title;}

    // 재생목록 Dialog에서 사용 - 플리 대표감성 리스트
    public static void getMoodList(ArrayList csvmoodlist) {moodlist = csvmoodlist;}

    public static ArrayList setMoodList() {return moodlist;}

    public enum Category {
        Playlist_ID(0),
        MiniPlaylist_ID_list(2),
        Music_ID(3),
        PM_Value(4),
        Playlist_Mood(5);

        public final int number;

        Category(int number) {
            this.number = number;
        }
    }

    public enum Category_Music {
        Music_ID(1),
        Title(2),
        Composer(3),
        Length(10),
        Gdrive_ID(17);

        public final int number;

        Category_Music(int number) {
            this.number = number;
        }
    }

}
