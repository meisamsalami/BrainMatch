package com.meisam.gamestwo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SpeedMatchLauncher extends Fragment {

    private static final MatchName MATCH_NAME = MatchName.SPEED_MATCH;
    private Button startBtn, bestScoresBtn;
    private AnimatorSet animatorSet = new AnimatorSet();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_witch_one_launcher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        configureViews();
        sturtUpAnimation();
    }

    private void configureViews() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = MyPreferenceManager.getInstance(getActivity()).getCurrentUser();
                if (currentUser != null) {
                    openSpeedGame(currentUser.getName());
                } else {
                    GetNameDialog nameDialog = new GetNameDialog(getActivity(), new OnNameSelectedListener() {
                        @Override
                        public void onNameSelected(String playerName) {
                            User newUser=new User();
                            newUser.setName(playerName);
                            MyPreferenceManager.getInstance(getActivity()).putCurrentUser(newUser);
                            openSpeedGame(playerName);
                        }
                    });
                    nameDialog.show();
                }
            }
        });
        bestScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("witchMatch", MATCH_NAME.name());
                BestScoresFragment bestScoresFragment = new BestScoresFragment();
                bestScoresFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().add(R.id.fragment_best_score, bestScoresFragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    private void openSpeedGame(String playerName) {
        Bundle bundle = new Bundle();
        bundle.putString("playerName", playerName);
        SpeedMatchFragment speedMatchFragment = new SpeedMatchFragment();
        speedMatchFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_witch_one, speedMatchFragment)
                .addToBackStack(null).commit();
    }

    private void findViews(View view) {
        bestScoresBtn = (Button) view.findViewById(R.id.best_scores_btn);
        startBtn = (Button) view.findViewById(R.id.start_btn);
    }

    private void sturtUpAnimation() {
        startBtn.setTranslationX(-getResources().getDisplayMetrics().widthPixels / 2 - startBtn.getWidth() / 2 - 10);
        startBtn.setVisibility(View.VISIBLE);
        ObjectAnimator transStartBtnX = ObjectAnimator.ofFloat(startBtn, "translationX", 0);
        bestScoresBtn.setTranslationX(getResources().getDisplayMetrics().widthPixels / 2 + startBtn.getWidth() / 2 + 10);
        bestScoresBtn.setVisibility(View.VISIBLE);
        ObjectAnimator transBestBtnX = ObjectAnimator.ofFloat(bestScoresBtn, "translationX", 0);
        animatorSet.playTogether(transBestBtnX, transStartBtnX);
        animatorSet.setDuration(700);
        animatorSet.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        animatorSet.cancel();
    }
}
