package com.meisam.gamestwo;

import java.util.ArrayList;

public class RankList {
    private ArrayList<User> rankList=new ArrayList<User>();

    public RankList(ArrayList<User> rankList) {
        this.rankList = rankList;
    }

    public RankList() {
    }

    public void addToRankList(User user){
        for(int i=rankList.size();i>0;i--){
            if(rankList.get(i-1).equals(user)){
                if(rankList.get(i-1).getScore()>user.getScore()){
                    return;
                }else{
                    rankList.remove(i-1);
                }
            }
        }
        rankList.add(user);
    }

    public ArrayList<User> getRankList() {
        return rankList;
    }

    public void setRankList(ArrayList<User> rankList) {
        this.rankList = rankList;
    }
}
