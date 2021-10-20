package com.example.testmoveanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.testmoveanimation.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private final int speedItem = 4000;
    private final int speedCreaterViews = 1000;

    private int widthDisplay;
    private int heightDisplay;

    private int widthItem = 150;
    private int heightItem = 150;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        getDisplaySize();
        initView();
    }

    private void initView() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        viewModel.getIsGame();
        binding.btnStart.setOnClickListener(v -> {
            startGame();
            hideBtnStart();
        });
    }

    private void showBtnStart() {
        binding.btnStart.setVisibility(View.VISIBLE);
    }

    private void hideBtnStart() {
        binding.btnStart.setVisibility(View.GONE);
    }

    private void startGame() {
        viewModel.isGame.setValue(true);
        clearAllItemInRoot();
        updateScore(0);
        updateTextScore();
        startGameTimer();
    }

    private void clearAllItemInRoot() {
        binding.rootLayout.removeAllViews();
    }

    private void updateScore(int i) {
        score = i;
    }

    private void updateTextScore() {
        binding.textScore.setText(String.valueOf(score));
    }

    private void startGameTimer() {
        new CountDownTimer(10000, speedCreaterViews) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.textView.setText(String.valueOf(millisUntilFinished / 1000));

                viewModel.getIsGame().observe(MainActivity.this, aBoolean -> {
                    if (aBoolean) {
                        createImg();
                    }
                });
            }

            @Override
            public void onFinish() {
                stopGame();
                showBtnStart();
            }
        }.start();
    }

    private void stopGame() {
        viewModel.isGame.setValue(false);
        clearAllItemInRoot();
    }

    private void createImg() {
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(widthItem, heightItem);
        params.setMarginStart(getMarginStart());

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.orc);
        imageView.setLayoutParams(params);
        binding.rootLayout.addView(imageView);

        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(imageView, "translationY", 0f, heightDisplay - heightItem);
        translateYAnimation.setRepeatCount(0);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(speedItem);
        set.playTogether(translateYAnimation);
        set.setInterpolator(new LinearInterpolator());
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.rootLayout.removeView(imageView);
            }
        });

        imageView.setOnClickListener(v -> {
            updateScore(++score);
            updateTextScore();
            binding.rootLayout.removeView(imageView);
            set.cancel();
        });
    }

    private int getMarginStart() {
        return new Random().nextInt(widthDisplay - widthItem);
    }

    private void getDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthDisplay = size.x;
        heightDisplay = size.y;
    }
}