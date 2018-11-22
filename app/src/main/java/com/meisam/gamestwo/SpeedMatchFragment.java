package com.meisam.gamestwo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.Random;

public class SpeedMatchFragment extends Fragment {

    private static final long GAME_TIMER = 30000;
    private Context context;
    private static final int LEVELS = 5;
    private Button noneBtn, oneBtn, bothBtn;
    private TextView countDownTxt;
    private ImageView shapeBack;
    private int countDown = 3;
    private ImageView currentImage;
    private ImageView prevImage;
    private ImageView secondImage;
    private ImageView firstImage;
    private ImageView timerBack;
    private ImageView tick;
    private Shape firstShape = null;
    private Shape secondShape = null;
    private Shape currentShape = null;
    private Shape prevShape = null;
    private final long LEVEL_TIMER = 5000;
    private CountDownTimer countDownTimer;
    private CountDownTimer matchTimer;
    private ProgressBar progressLevel;
    private Integer point = 0;
    private String playerName;
    private Random random = new Random();
    private View buttonContainer;
    private DisplayMetrics metrics;
    private AnimatorSet animatorSet=new AnimatorSet();
    private AnimatorSet animatorSetStart=new AnimatorSet();
    private int progress = 0;

    class Shape {
        Collor collor;
        Clishe clishe;

        public Shape() {
        }

        public Shape(Collor collor, Clishe clishe) {
            this.collor = collor;
            this.clishe = clishe;
        }

        public Collor getCollor() {
            return collor;
        }

        public void setCollor(Collor collor) {
            this.collor = collor;
        }

        public Clishe getClishe() {
            return clishe;
        }

        public void setClishe(Clishe clishe) {
            this.clishe = clishe;
        }
    }

    enum Collor {
        ORANGE(R.color.red), GREEN(R.color.green), BLUE(R.color.blue);
        private final int code;

        Collor(int code) {
            this.code = code;
        }

        public int getCollorCode() {
            return this.code;
        }
    }

    enum Clishe {
        CIRCLE(R.drawable.speed_circle), TRIANGLE(R.drawable.ic_iconmonstr_triangle_1), SQUARE(R.drawable.square);
        private final int code;

        Clishe(int code) {
            this.code = code;
        }

        public int getClisheCode() {
            return this.code;
        }
    }

    private void readArguments() {
        playerName = getArguments().getString("playerName", null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        readArguments();
        context = this.getActivity();
        return inflater.inflate(R.layout.fragment_speed_match, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        configureViews();
        firstShape = generateShape();
        initShape(firstImage, firstShape);
        secondShape = generateShape();
        initShape(secondImage, secondShape);
        startUpAnimation();
    }

    private void findViews(View view) {
        noneBtn = (Button) view.findViewById(R.id.btn_none);
        oneBtn = (Button) view.findViewById(R.id.btn_one);
        bothBtn = (Button) view.findViewById(R.id.btn_both);
        countDownTxt = (TextView) view.findViewById(R.id.start_up_count_down);
        timerBack = (ImageView) view.findViewById(R.id.start_up_timer_circle);
        shapeBack = (ImageView) view.findViewById(R.id.shapeBack);
        firstImage = (ImageView) view.findViewById(R.id.shape);
        secondImage = (ImageView) view.findViewById(R.id.shape_second);
        tick = (ImageView) view.findViewById(R.id.tick);
        progressLevel = (ProgressBar) view.findViewById(R.id.progress_level);
        buttonContainer = view.findViewById(R.id.button_container);
    }

    private void initShape(ImageView imageView, Shape shape) {
        imageView.setImageResource(shape.getClishe().getClisheCode());
        imageView.setColorFilter(getResources().getColor(shape.getCollor().getCollorCode()));
    }

    public void setProgress(int progrs) {
        progress = progrs;
        progressLevel.setProgress(progrs);
    }

    private void configureViews() {
        metrics = getActivity().getResources().getDisplayMetrics();
        countDownTimer = new CountDownTimer(LEVEL_TIMER, 50) {

            @Override
            public void onTick(long l) {
                progress += 1;
                progressLevel.setProgress(progress);
            }

            @Override
            public void onFinish() {
                setProgress(0);
                tick.setImageResource(R.drawable.ic_cancel_black_24dp);
                animateTick(tick);
                this.cancel();
                animateLevel();
            }
        };
        noneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateLevelPoint(Choice.NONE);
                setProgress(0);
                countDownTimer.cancel();
                animateLevel();
            }
        });
        oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateLevelPoint(Choice.ONE);
                countDownTimer.cancel();
                setProgress(0);
                animateLevel();
            }
        });
        bothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateLevelPoint(Choice.BOTH);
                countDownTimer.cancel();
                setProgress(0);
                animateLevel();
            }
        });
        progressLevel.setProgress(0);
        matchTimer = new CountDownTimer(GAME_TIMER, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                updateHighScore();
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.speed_earn_text, point)).show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (getFragmentManager().getBackStackEntryCount() > 1) {
                            getFragmentManager().popBackStack();
                        }
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

        };


    }

    enum Choice {
        NONE, BOTH, ONE
    }

    private void evaluateLevelPoint(Choice choice) {
        bothBtn.setEnabled(false);
        noneBtn.setEnabled(false);
        oneBtn.setEnabled(false);
        Choice correct;
        if (currentShape.getClishe().getClisheCode() == prevShape.getClishe().getClisheCode()) {
            if (currentShape.getCollor().getCollorCode() == prevShape.getCollor().getCollorCode()) {
                correct = Choice.BOTH;
            } else {
                correct = Choice.ONE;
            }
        } else {
            if (currentShape.getCollor().getCollorCode() == prevShape.getCollor().getCollorCode()) {
                correct = Choice.ONE;
            } else {
                correct = Choice.NONE;
            }
        }
        if (correct.equals(choice)) {
            tick.setImageResource(R.drawable.ic_check_circle_black_24dp);
            point++;
            animateTick(tick);
            return;
        }
        tick.setImageResource(R.drawable.ic_cancel_black_24dp);
        animateTick(tick);
    }

    private void animateTick(View tick) {
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(tick, "alpha", 0, 1);
        ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(tick, "scaleX", 1.2f);
        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(tick, "scaleY", 1.2f);
        ObjectAnimator scaleXOut = ObjectAnimator.ofFloat(tick, "scaleX", 1f);
        ObjectAnimator scaleYOut = ObjectAnimator.ofFloat(tick, "scaleY", 1f);
        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(tick, "alpha", 1, 0);
        tick.setVisibility(View.VISIBLE);
        AnimatorSet animatorSetFadeIn = new AnimatorSet();
        animatorSetFadeIn.playTogether(alphaIn, scaleXIn, scaleYIn);
        animatorSetFadeIn.setDuration(300);
        alphaIn.setDuration(100);
        alphaOut.setDuration(200);
        animatorSetFadeIn.start();
        AnimatorSet animatorSetFadeOut = new AnimatorSet();
        animatorSetFadeOut.playTogether(alphaOut, scaleXOut, scaleYOut);
        animatorSetFadeOut.setStartDelay(300);
        animatorSetFadeOut.setDuration(300);
        animatorSetFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bothBtn.setEnabled(true);
                noneBtn.setEnabled(true);
                oneBtn.setEnabled(true);
            }
        });
        animatorSetFadeOut.start();
    }

    private void animateLevel() {
        countDownTimer.start();
        progressLevel.setProgress(0);
        secondShape = generateShape();
        initShape(prevImage, secondShape);
        prevShape = secondShape;
        animatorSet=new AnimatorSet();
        animatorSet.playTogether(animateShapeOut(currentImage),
                animateShapeIn(prevImage));
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ImageView temp = prevImage;
                prevImage = currentImage;
                currentImage = temp;
                Shape tempShape = prevShape;
                prevShape = currentShape;
                currentShape = tempShape;
                bothBtn.setEnabled(true);
                noneBtn.setEnabled(true);
                oneBtn.setEnabled(true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }
        });
        animatorSet.start();
    }


    private void startUpAnimation() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(countDownTxt, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleXAnimatorShape = ObjectAnimator.ofFloat(timerBack, "scaleX", 1f, 1.2f);
        scaleXAnimator.setDuration(1000);
        scaleXAnimatorShape.setDuration(1000);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(countDownTxt, "scaleY", 1f, 1.5f);
        ObjectAnimator scaleYAnimatorShape = ObjectAnimator.ofFloat(timerBack, "scaleY", 1f, 1.2f);
        scaleYAnimator.setDuration(1000);
        scaleYAnimatorShape.setDuration(1000);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(countDownTxt, "alpha", 1f, 0.8f);
        ObjectAnimator alphaAnimatorShape = ObjectAnimator.ofFloat(timerBack, "alpha", 1f, 0.8f);
        alphaAnimator.setDuration(1000);
        alphaAnimatorShape.setDuration(1000);
        animatorSetStart = new AnimatorSet();
        animatorSetStart.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSetStart.playTogether(scaleXAnimatorShape, scaleYAnimatorShape, alphaAnimatorShape);
        animatorSetStart.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (countDown == 0) {
                    startGame();
                    countDownTxt.setVisibility(View.GONE);
                    timerBack.setVisibility(View.GONE);
                } else {
                    startUpAnimation();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                countDownTxt.setText(String.valueOf(countDown));
                countDown--;
            }

        });
        animatorSetStart.start();
    }

    private void startGame() {
        currentImage = firstImage;
        prevImage = secondImage;
        currentShape = firstShape;
        prevShape = secondShape;
        animateLevel();
        matchTimer.start();
        countDownTimer.start();
    }

    private Animator animateShapeIn(View shapeIn) {
        shapeIn.setTranslationX(metrics.widthPixels + shapeIn.getWidth() / 2);
        shapeIn.setVisibility(View.VISIBLE);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(shapeIn, "translationX", 0);
        translateX.setDuration(300);
        translateX.setStartDelay(200);
        return translateX;
    }

    private Animator animateShapeOut(View shapeOut) {
        ObjectAnimator translateX = ObjectAnimator.ofFloat(shapeOut, "translationX", -metrics.widthPixels / 2 - shapeOut.getWidth() / 2 - 10);
        translateX.setDuration(300);
        return translateX;
    }

    private Shape generateShape() {
        int randColor = Math.abs(random.nextInt(2));
        int randClishe = Math.abs(random.nextInt(2));
        Shape shape = new Shape();
        switch (randClishe) {
            case 0:
                shape.setClishe(Clishe.CIRCLE);
                break;
            case 1:
                shape.setClishe(Clishe.TRIANGLE);
                break;
            case 2:
                shape.setClishe(Clishe.SQUARE);
                break;
        }
        switch (randColor) {
            case 0:
                shape.setCollor(Collor.GREEN);
                break;
            case 1:
                shape.setCollor(Collor.ORANGE);
                break;
            case 2:
                shape.setCollor(Collor.BLUE);
                break;
        }
        return shape;
    }

    private void updateHighScore() {
        RankList rankList = MyPreferenceManager.getInstance(getActivity()).getSpeedRankList();
        User newUser = new User(playerName, point);
        rankList.addToRankList(newUser);
        Collections.sort(rankList.getRankList(), (a, b) -> b.getScore() - a.getScore());
        MyPreferenceManager.getInstance(getActivity()).putSpeedRankList(rankList);
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        updateHighScore();
        matchTimer.cancel();
        animatorSet.removeAllListeners();
        animatorSetStart.removeAllListeners();
    }

}
