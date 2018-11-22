package com.meisam.gamestwo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by meisam on 5/31/2018.
 */

public class WitchOneFragment extends Fragment{

    private Button leftBtn, rightBtn, equalBtn;
    private TextView scoreTxt, remainingTimeTxt, countDownTxt;
    private final Long GAME_TIME = 20000L;
    private final long RANDOM_SEED = 100;
    private Integer point = 0;
    private int countDown=3;
    private CountDownTimer countDownTimer;
    private ConstraintLayout gameContainer;
    private String playerName;
    private int displayHeight;
    private int displayWidth;
    private AnimatorSet animatorSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        readArguments();
        return inflater.inflate(R.layout.fragment_witch_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        configure();
        startUpAnimation();
    }

    private void readArguments(){
        playerName=getArguments().getString("playerName",null);
    }

    private void startUpAnimation() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(countDownTxt, "scaleX", 1f, 3f);
        scaleXAnimator.setDuration(1000);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(countDownTxt, "scaleY", 1f, 3f);
        scaleYAnimator.setDuration(1000);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(countDownTxt, "alpha", 1f, 0f);
        alphaAnimator.setDuration(1000);
        animatorSet=new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (countDown == 0) {
                    animateViewsIn();
                    startGame();
                }else {
                    startUpAnimation();
                }
            }
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                countDownTxt.setText(String.valueOf(countDown));
                countDown--;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        animatorSet.start();
    }

    private void animateViewsIn(){
        AnimatorSet animatorSetIn=new AnimatorSet();
        animatorSetIn.playTogether(animateLeftButtonIn(),animateRightButtonIn(),animateEqualButtonIn(),animatePointTextIn(),animateRemainingTimeIn());
        animatorSetIn.setDuration(500);
        animatorSetIn.start();
    }

    private void animateViewsOut(){
        AnimatorSet animatorSetOut=new AnimatorSet();
        animatorSetOut.playTogether(animateLeftButtonOut(),animateRightButtonOut(),animateEqualButtonOut(),animatePointTextOut(),animateRemainingTimeOut());
        animatorSetOut.setDuration(700);
        animatorSetOut.start();
    }

    private ObjectAnimator animateLeftButtonOut(){
        ObjectAnimator traslationX=ObjectAnimator.ofFloat(leftBtn,"translationX",-displayWidth/2-leftBtn.getWidth()/2-10);
        return traslationX;
    }
    private ObjectAnimator animateLeftButtonIn(){
        leftBtn.setTranslationX(-displayWidth/2-leftBtn.getWidth()/2-10);
        leftBtn.setVisibility(View.VISIBLE);
        ObjectAnimator traslationX=ObjectAnimator.ofFloat(leftBtn,"translationX",0);
        return traslationX;
    }
    private ObjectAnimator animateRightButtonIn(){
        rightBtn.setTranslationX(displayWidth+rightBtn.getWidth()/2+10);
        rightBtn.setVisibility(View.VISIBLE);
        ObjectAnimator traslationX=ObjectAnimator.ofFloat(rightBtn,"translationX",0);
        return traslationX;
    }
    private ObjectAnimator animateRightButtonOut(){
        ObjectAnimator traslationX=ObjectAnimator.ofFloat(rightBtn,"translationX",displayWidth/2+rightBtn.getWidth()/2+10);
        return traslationX;
    }

    private ObjectAnimator animateEqualButtonIn(){
        equalBtn.setTranslationY(displayHeight+equalBtn.getHeight()/2+10);
        equalBtn.setVisibility(View.VISIBLE);
        ObjectAnimator translationY=ObjectAnimator.ofFloat(equalBtn,"translationY",0);
        return translationY;
    }

    private ObjectAnimator animateEqualButtonOut(){
        ObjectAnimator translationY=ObjectAnimator.ofFloat(equalBtn,"translationY",displayHeight+equalBtn.getHeight()/2+10);
        return translationY;
    }

    private ObjectAnimator animatePointTextIn(){
        ObjectAnimator alpha=ObjectAnimator.ofFloat(scoreTxt,"alpha",0,1f);
        scoreTxt.setVisibility(View.VISIBLE);
        return alpha;
    }

    private AnimatorSet animatePointTextOut(){
        float scale=displayWidth/scoreTxt.getWidth();
        ObjectAnimator scaleX=ObjectAnimator.ofFloat(scoreTxt,"scaleX",scale);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(scoreTxt,"scaleY",scale);
        ObjectAnimator translationY=ObjectAnimator.ofFloat(scoreTxt,"translationY",displayHeight/(scale*2));
        AnimatorSet animatorSetPoint=new AnimatorSet();
        animatorSetPoint.playTogether(scaleX,scaleY,translationY);
        return animatorSetPoint;
    }

    private ObjectAnimator animateRemainingTimeIn(){
        ObjectAnimator alpha=ObjectAnimator.ofFloat(remainingTimeTxt,"alpha",0,1f);
        remainingTimeTxt.setVisibility(View.VISIBLE);
        return alpha;
    }

    private ObjectAnimator animateRemainingTimeOut(){
        ObjectAnimator alpha=ObjectAnimator.ofFloat(remainingTimeTxt,"alpha",1,0);
        return alpha;
    }
    private void startGame() {
        gameContainer.setVisibility(View.VISIBLE);
        countDownTimer.start();
        refreshRemaining(GAME_TIME);
        generateOneLevel();
    }

    private void generateOneLevel() {
        Random rand = new Random();
        leftBtn.setText(String.valueOf(rand.nextInt((int) RANDOM_SEED)));
        rightBtn.setText(String.valueOf(rand.nextInt((int) RANDOM_SEED)));
    }

    private void updateHighScore(){
        RankList rankList=MyPreferenceManager.getInstance(getActivity()).getWitchOneRankList();
        User newUser=new User(playerName,point);
        rankList.addToRankList(newUser);
        Collections.sort(rankList.getRankList(),(a,b) -> b.getScore()-a.getScore());
        MyPreferenceManager.getInstance(getActivity()).putWitchOneRankList(rankList);
    }

    private void configure(){

        countDownTimer = new CountDownTimer(GAME_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                refreshScore();
                refreshRemaining(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                refreshScore();
                refreshRemaining(0);
                remainingTimeTxt.setText(getString(R.string.finish_text));
                leftBtn.setClickable(false);
                rightBtn.setClickable(false);
                equalBtn.setClickable(false);
                updateHighScore();
                animateViewsOut();
            }
        };
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int left = Integer.valueOf(leftBtn.getText().toString());
                int right = Integer.valueOf(rightBtn.getText().toString());
                if (left > right) {
                    point++;
                    refreshScore();
                }
                generateOneLevel();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int left = Integer.valueOf(leftBtn.getText().toString());
                int right = Integer.valueOf(rightBtn.getText().toString());
                if (left < right) {
                    point++;
                    refreshScore();
                }
                generateOneLevel();
            }
        });
        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int left = Integer.valueOf(leftBtn.getText().toString());
                int right = Integer.valueOf(rightBtn.getText().toString());
                if (left == right) {
                    point++;
                    refreshScore();
                }
                generateOneLevel();
            }
        });
        scoreTxt.setText(String.format(getString(R.string.your_point),0));
        remainingTimeTxt.setText(String.format(getString(R.string.remain_time),GAME_TIME/1000));
        displayHeight=getResources().getDisplayMetrics().heightPixels;
        displayWidth=getResources().getDisplayMetrics().widthPixels;
    }

    public void refreshScore() {
        scoreTxt.setText(String.format(getString(R.string.your_point), point));
    }

    public void refreshRemaining(long millisUntilFinished){
        remainingTimeTxt.setText(getString(R.string.remain_time, (int) millisUntilFinished / 1000));
    }

    private void findView(View view) {
        leftBtn = (Button) view.findViewById(R.id.left_number_btn);
        rightBtn = (Button) view.findViewById(R.id.right_number_btn);
        equalBtn = (Button) view.findViewById(R.id.equal_btn);
        scoreTxt = (TextView) view.findViewById(R.id.point_show_txt);
        remainingTimeTxt = (TextView) view.findViewById(R.id.remaining_time_txt);
        countDownTxt = (TextView) view.findViewById(R.id.count_down_txt);
        gameContainer=(ConstraintLayout)view.findViewById(R.id.witch_one_container);
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        animatorSet.removeAllListeners();
    }
}
