package com.meisam.gamestwo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by meisam on 5/31/2018.
 */

public class BestScoresFragment extends Fragment {

    RecyclerView rankList;
    MatchName witchMatch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        readArguments();
        return inflater.inflate(R.layout.fragment_best_scores,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        configureItems();

    }

    private void readArguments(){
        witchMatch=MatchName.valueOf(getArguments().getString("witchMatch",null));
    }

    private void configureItems(){
        RankList items;
        if(witchMatch.equals(MatchName.WITCH_ONE)){
            items=MyPreferenceManager.getInstance(getActivity()).getWitchOneRankList();
        }else{
            items=MyPreferenceManager.getInstance(getActivity()).getSpeedRankList();
        }
        ViewAdapter adapter=new ViewAdapter(items.getRankList());
        rankList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankList.setAdapter(adapter);
    }

    private void findViews(View view){
        rankList=(RecyclerView)view.findViewById(R.id.rankList);
    }
}
