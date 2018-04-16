package com.example.user.mathmaniac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private TextView numA;
    private TextView numB;
    private TextView result;
    private TextView score;
    private TextView highScore;
    private ImageView btnYes;
    private ImageView btnNo;
    private ProgressBar time;
    private LinearLayout gameOver;
    private RelativeLayout mainLayout;
    private View activityPlay;
    private ImageButton continuePlay, menu;
    private TextView resultYourScore, resultHighScore;
    MediaPlayer playSuccess, playGameOver;
    CountDownTimer timer;
    boolean isLose;
    boolean isHighScore;
    int vNumA, vNumB, vResult, vScore;
    int pivot;
    int vHighScore;
    static int stepToAds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5618073725839874/6879592142");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        stepToAds++;
        addControls();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get high score
        SharedPreferences pref = getSharedPreferences("highScore", MODE_PRIVATE);
        vHighScore = pref.getInt("highScore", 0);
        highScore.setText(String.valueOf(vHighScore));

        playSuccess = MediaPlayer.create(getApplicationContext(), R.raw.success);
        playGameOver = MediaPlayer.create(getApplicationContext(), R.raw.game_over);
    }

    private void addEvents() {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLose) {
                    if (vNumA + vNumB == vResult) {
                        playSuccess.start();
                        if (vScore != 0) timer.cancel();
                        vScore += 1;
                        if (vScore > Integer.parseInt(highScore.getText().toString())) {
                            vHighScore = vScore;
                            highScore.setText(String.valueOf(vScore));
                            isHighScore = true;
                        }
                        score.setText(String.valueOf(vScore));
                        gameActivity();
                    } else {
                        lose(isHighScore);
                    }
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLose) {
                    if (vNumA + vNumB != vResult) {
                        playSuccess.start();
                        if (vScore != 0) timer.cancel();
                        vScore += 1;
                        if (vScore > Integer.parseInt(highScore.getText().toString())){
                            vHighScore = vScore;
                            highScore.setText(String.valueOf(vScore));
                            isHighScore = true;
                        }
                        score.setText(String.valueOf(vScore));
                        gameActivity();
                    } else{
                        lose(isHighScore);
                    }
                }
            }
        });

        continuePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        numA = (TextView) findViewById(R.id.numA);
        numB = (TextView) findViewById(R.id.numB);
        result = (TextView) findViewById(R.id.result);
        score = (TextView) findViewById(R.id.score);
        highScore = (TextView) findViewById(R.id.highScore);
        btnYes = (ImageView) findViewById(R.id.btnYes);
        btnNo = (ImageView) findViewById(R.id.btnNo);
        activityPlay = findViewById(R.id.activity_play);
        gameOver = (LinearLayout) findViewById(R.id.gameOver);
        continuePlay = (ImageButton) findViewById(R.id.continuePlay);
        menu = (ImageButton) findViewById(R.id.menu);
        resultYourScore = (TextView) findViewById(R.id.resultYourScore);
        resultHighScore = (TextView) findViewById(R.id.resultHighScore);
        time = (ProgressBar) findViewById(R.id.time);
        time.setProgress(100);
        vScore = 0;
        isLose = false;
        isHighScore = false;
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        gameActivity();
    }

    private void gameActivity() {
        Random random = new Random();
        if(vScore <= 10){
            vNumA = random.nextInt(7) + 1;
            vNumB = random.nextInt(7) + 1;
        }
        else{
            vNumA = random.nextInt(15) + 1;
            vNumB = random.nextInt(15) + 1;
        }
        pivot = random.nextInt(2);
        if (pivot == 0) {
            pivot = random.nextInt(2);
            if (pivot == 0){
                vResult = vNumA + vNumB + random.nextInt(5) + 1;
            }
            else {
                do{
                    vResult = vNumA + vNumB - random.nextInt(5) - 1;
                } while (vResult <= 0);
            }
        }
        else vResult = vNumA + vNumB;
        numA.setText(String.valueOf(vNumA));
        numB.setText(String.valueOf(vNumB));
        result.setText(String.valueOf(vResult));
        if (vScore != 0) {
            mainLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_right_to_left));
            timer = new CountDownTimer(1000, 10) {
                @Override
                public void onTick(long l) {
                    double percent = l / 1000.0 * 100;
                    time.setProgress((int) percent);
                }

                @Override
                public void onFinish() {
                    lose(true, isHighScore);
                }
            }.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("highScore", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("highScore", vHighScore);
        editor.apply();
        playSuccess.release();
        playGameOver.release();
    }

    private void lose(boolean isTimeOut, final boolean isHighScore){
        playGameOver.start();
        isLose = true;
        if (!isTimeOut){
            if (vScore != 0) timer.cancel();
        }
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        activityPlay.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gameOver.setVisibility(View.VISIBLE);
                gameOver.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_above));
                resultYourScore.setText(String.valueOf(vScore));
                resultHighScore.setText(String.valueOf(vHighScore));
                if (isHighScore) resultHighScore.setTextColor(Color.YELLOW);
                if (mInterstitialAd.isLoaded()){
                    if (vScore >= 10){
                        if (stepToAds >= 4){
                            mInterstitialAd.show();
                            stepToAds = 0;
                        }
                    }
                    else{
                        if (stepToAds >= 10){
                            mInterstitialAd.show();
                            stepToAds = 0;
                        }
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void lose(boolean isHighScore){
        lose(false, isHighScore);
    }

    @Override
    public void onBackPressed() {
        if (isLose) super.onBackPressed();
        //else do nothing
    }
}
