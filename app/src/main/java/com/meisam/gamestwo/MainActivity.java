package com.meisam.gamestwo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private Button witchOneBtn,speedMatchBtn,exitBtn;
    private WitchOnFragmentLauncher witchOneFragmentLauncher=new WitchOnFragmentLauncher();
    private SpeedMatchLauncher speedMatchFragmentLauncher=new SpeedMatchLauncher();
    private TextView greeting,selectGame;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        findViews();
        getCurrentUser();
        configure();
    }

    private void setViewsVisibility(int visibility){
        witchOneBtn.setVisibility(visibility);
        speedMatchBtn.setVisibility(visibility);
        exitBtn.setVisibility(visibility);
        greeting.setVisibility(visibility);
        selectGame.setVisibility(visibility);
    }

    private void getCurrentUser() {
        User currentUser=MyPreferenceManager.getInstance(MainActivity.this).getCurrentUser();
        if(currentUser!=null){
            greeting.setText(getString(R.string.greeting,currentUser.getName()));
            startUpAnimation();
        }else {
            GetNameDialog dialog = new GetNameDialog(MainActivity.this, new OnNameSelectedListener() {
                @Override
                public void onNameSelected(String playerName) {
                    User newUser=new User();
                    newUser.setName(playerName);
                    MyPreferenceManager.getInstance(MainActivity.this).putCurrentUser(newUser);
                    greeting.setText(getString(R.string.greeting,playerName));
                    startUpAnimation();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    MainActivity.this.finish();
                }
            });
            dialog.show();
        }
    }


    public void findViews(){
        witchOneBtn=findViewById(R.id.witch_one_btn);
        speedMatchBtn=findViewById(R.id.speed_match_btn);
        greeting=findViewById(R.id.greeting);
        exitBtn=findViewById(R.id.exit_btn);
        selectGame=findViewById(R.id.select_game);
    }

    public void configure(){

        witchOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_witch_one_launcher,witchOneFragmentLauncher)
                        .addToBackStack(null)
                        .commit();
            }
        });
        speedMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .add(R.id.speed_match_frame,speedMatchFragmentLauncher)
                        .addToBackStack(null)
                        .commit();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreferenceManager.getInstance(MainActivity.this).clearCurrentUser();
                MainActivity.this.finish();
            }
        });
    }

    private void startUpAnimation(){
        setViewsVisibility(VISIBLE);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        int width=metrics.widthPixels;
        int height=metrics.heightPixels;
        greeting.setTranslationY(-height-greeting.getHeight());
        selectGame.setTranslationY(-height-selectGame.getHeight());
        witchOneBtn.setTranslationY(-height-witchOneBtn.getHeight());
        speedMatchBtn.setTranslationY(-height-speedMatchBtn.getHeight());
        exitBtn.setTranslationY(-height-exitBtn.getHeight());
        ObjectAnimator translationYGreeting=ObjectAnimator.ofFloat(greeting,"translationY",-height-greeting.getHeight(),0);
        ObjectAnimator translationYSelect=ObjectAnimator.ofFloat(selectGame,"translationY",-height-selectGame.getHeight(),0);
        ObjectAnimator translationYWitchOne=ObjectAnimator.ofFloat(witchOneBtn,"translationY",-height-witchOneBtn.getHeight(),0);
        ObjectAnimator translationYSpeed=ObjectAnimator.ofFloat(speedMatchBtn,"translationY",-height-speedMatchBtn.getHeight(),0);
        ObjectAnimator translationYExit=ObjectAnimator.ofFloat(exitBtn,"translationY",-height-exitBtn.getHeight(),0);
        animatorSet=new AnimatorSet();
        animatorSet.playTogether(translationYGreeting,translationYSelect,translationYWitchOne,translationYSpeed,translationYExit);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
}