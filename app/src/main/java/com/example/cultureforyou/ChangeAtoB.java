package com.example.cultureforyou;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ChangeAtoB {

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


    // 플레이리스트 csv 데이터 가공 -> 선택 무드값의 플레이리스트 중 랜덤으로 하나만 추출
    public static ArrayList getOnePlaylist(String moodselectid_result){
        ArrayList<String> select_playlist = new ArrayList<>();
        try {
            /* 본데이터
            URL stockURL = new URL("https://drive.google.com/uc?export=view&id=1GEoWHtpi65qwstI7H7bCwQsyzQqSvNhq");
             */
            String pid = "1-5RiipcJZgjM20xdE3Ok1iHPVzy2q-Ns";
            URL stockURLs = new URL("https://drive.google.com/uc?export=view&id=" + pid);
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURLs.openStream()));
            CSVReader reader2 = new CSVReader(in);
            String[] nextline;

            Integer j = 0;

            while ((nextline = reader2.readNext()) != null) {
                // 무드값이 동일한 플레이리스트만 추출
                if (nextline[Category.Playlist_ID.number].equals(moodselectid_result)) {
                    //Log.d("nextline_select", Arrays.toString(nextline));
                    for(int i=0; i<Category.values().length; i++) {
                        select_playlist.add(nextline[i+1]);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return select_playlist;
    }

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


}
