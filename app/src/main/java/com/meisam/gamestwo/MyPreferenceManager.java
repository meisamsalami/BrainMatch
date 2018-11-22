package com.meisam.gamestwo;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MyPreferenceManager {

    private static MyPreferenceManager instance = null;
    private SharedPreferences mySharedPrefrences = null;
    private SharedPreferences.Editor editor = null;

    public static MyPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new MyPreferenceManager(context);
        }
        return instance;
    }

    private MyPreferenceManager(Context context) {
        mySharedPrefrences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        editor = mySharedPrefrences.edit();
    }

    public int getHighScore() {
        return mySharedPrefrences.getInt("high_score", 0);
    }

    public void putHighScore(int highScore) {
        editor.putInt("high_score", highScore);
        editor.apply();
    }

    public void putWitchOneRankList(RankList rankList) {
        Gson gson = new Gson();
        String gsonRankList = gson.toJson(rankList, RankList.class);
        editor.putString(RankListEnum.WICTH_ONE_RANK_LIST.name(), gsonRankList);
        editor.apply();
    }

    public RankList getWitchOneRankList() {
        String gsonRankList = mySharedPrefrences.getString(RankListEnum.WICTH_ONE_RANK_LIST.name(), null);
        if (gsonRankList == null) {
            return new RankList();
        }
        Gson gson = new Gson();
        return gson.fromJson(gsonRankList, RankList.class);
    }

    public void putSpeedRankList(RankList rankList) {
        Gson gson = new Gson();
        String gsonRankList = gson.toJson(rankList, RankList.class);
        editor.putString(RankListEnum.SPEED_MATCH_RANK_LIST.name(), gsonRankList);
        editor.apply();
    }

    public RankList getSpeedRankList() {
        String gsonRankList = mySharedPrefrences.getString(RankListEnum.SPEED_MATCH_RANK_LIST.name(), null);
        if (gsonRankList == null) {
            return new RankList();
        }
        Gson gson = new Gson();
        return gson.fromJson(gsonRankList, RankList.class);
    }

    public User getCurrentUser() {
        String gsonUser =mySharedPrefrences.getString("current_user",null);
        if(gsonUser==null){
            return null;
        }
        Gson gson=new Gson();
        return gson.fromJson(gsonUser,User.class);
    }

    public void putCurrentUser(User user){
        Gson gson=new Gson();
        String gsonUser=gson.toJson(user,User.class);
        editor.putString("current_user",gsonUser);
        editor.apply();
    }

    public void clearCurrentUser(){
        editor.remove("current_user");
        editor.apply();
    }
}
