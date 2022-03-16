package com.example.cultureforyou;

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
}
