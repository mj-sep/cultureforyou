package com.example.cultureforyou;

public class CastAnniv {

    private static String anniv_mood;
    private static String anniv_date;
    private static String anniv_name;
    private static String nickname;

    private static int annivonoff = 0;


    // 기념일 온오프
    public static void getOnoff (int onoff){
        annivonoff = onoff;
    }

    public static Integer setgetOnoff() {
        return annivonoff;
    }

    // 기념일 무드값
    public static void getAnnivmood (String mood){
        anniv_mood = mood;
    }

    public static String setAnnivmood() {
        return anniv_mood;
    }

    // 기념일 날짜값
    public static void getAnnivdate (String date){
        anniv_date = date;
    }

    public static String setAnnivdate() {
        return anniv_date;
    }

    // 기념일 명칭
    public static void getNickname (String nick){
        nickname = nick;
    }

    public static String setNickname() {
        return nickname;
    }

    // 닉네임
    public static void getAnnivname (String name){
        anniv_name = name;
    }

    public static String setAnnivname() {
        return anniv_name;
    }



}
