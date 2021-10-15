package com.example.testmoveanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testmoveanimation.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    float imageXPosition;
    float imageYPosition;
    int score = 0;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        final TextView positionTextView = findViewById(R.id.text_view);
        ImageView myimage = findViewById(R.id.img_orc);

        ObjectAnimator translateXAnimation= ObjectAnimator.ofFloat(myimage, "translationX", 0f, 0f);
        ObjectAnimator translateYAnimation= ObjectAnimator.ofFloat(myimage, "translationY", 0f, 800f);
//        translateXAnimation.setRepeatCount(ValueAnimator.INFINITE);
//        translateYAnimation.setRepeatCount(ValueAnimator.INFINITE);
        translateXAnimation.setRepeatCount(0);
        translateYAnimation.setRepeatCount(0);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(10000);
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        translateXAnimation.addUpdateListener(animation -> imageXPosition = (Float) animation.getAnimatedValue());

        translateYAnimation.addUpdateListener(animation -> {
            imageYPosition = (Float) animation.getAnimatedValue();
            @SuppressLint("DefaultLocale") String position = String.format("X:%d Y:%d", (int) imageXPosition, (int) imageYPosition);
            positionTextView.setText(position);
            if (imageYPosition == 800) {
                binding.textScore.setText("Complete");
            }
        });


        binding.imgOrc.setOnClickListener(v -> {
            score++;
            binding.textScore.setText(String.valueOf(score));
        });
    }
}